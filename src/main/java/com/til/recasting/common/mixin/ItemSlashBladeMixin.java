package com.til.recasting.common.mixin;

import com.google.common.collect.Multimap;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.util.StringUtil;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.register.slash_blade.AllSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.AllSARegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author til
 */
@Mixin(value = {ItemSlashBlade.class}, remap = false)
public abstract class ItemSlashBladeMixin {


    @Inject(method = "getAttributeModifiers",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/util/LazyOptional;ifPresent(Lnet/minecraftforge/common/util/NonNullConsumer;)V",
                    opcode = 1
            ))
    protected void getAttributeModifiers(EquipmentSlotType slot, ItemStack stack, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> callbackInfoReturnable) {

    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setDamage(ItemStack stack, int damage) {
        int half = getHalfMaxdamage();
        if (damage == half) {
            return;
        }
        SlashBladePack slashBladePack = new SlashBladePack(stack);

        //anti shrink damageItem
        if (damage > stack.getMaxDamage()) {
            stack.setCount(2);
        }

        float amount = (damage - half) / (float) half / 2;
        amount = amount / slashBladePack.iSlashBladeStateSupplement.getDurable();
        slashBladePack.slashBladeState.setDamage(  slashBladePack.slashBladeState.getDamage() + amount);
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent((s) -> {


            s.setDamage(s.getDamage() + amount);
        });
    }

    @Shadow
    abstract int getHalfMaxdamage();


    /**
     * @author
     * @reason
     */
    @Overwrite
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return Math.min(amount, getHalfMaxdamage(stack) / 2);
    }

    @Inject(method = "addInformation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/util/LazyOptional;ifPresent(Lnet/minecraftforge/common/util/NonNullConsumer;)V",
                    opcode = 1
            ))
    protected void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, CallbackInfo callbackInfo) {
        SlashBladePack slashBladePack = new SlashBladePack(stack);
        if (!slashBladePack.isEffective()) {
            return;
        }
        tooltip.add(new TranslationTextComponent("key:%s", slashBladePack.slashBladeState.getTranslationKey()));
        tooltip.add(new StringTextComponent(""));


        SA_Register sa_register = GlowingFireGlow.getInstance().getWorldComponent(AllSARegister.class).getSA_Register(slashBladePack.slashBladeState.getSlashArts());
        if (sa_register != null) {
            tooltip.add(new TranslationTextComponent("SA:%s",
                    new TranslationTextComponent(StringUtil.formatLang(sa_register.getName()))));
            /*if (detail) {
                tooltip.add(new TranslationTextComponent("  %s",
                        new TranslationTextComponent(StringUtil.formatLang(sa_register.getName().getNamespace(), sa_register.getName().getPath(), StringFinal.INTRODUCE))));
            }*/
            tooltip.add(new StringTextComponent(""));
        }

        boolean hasSE = false;
        for (Map.Entry<SE_Register, ISE.SE_Pack> se_registerSE_packEntry : slashBladePack.ise.getAllSE().entrySet()) {
            if (se_registerSE_packEntry.getValue().isEmpty()) {
                continue;
            }
            if (!hasSE) {
                tooltip.add(new StringTextComponent("SE:"));
                hasSE = true;
            }
            tooltip.add(
                    new TranslationTextComponent("%s    level:%s/%s",
                            new TranslationTextComponent(StringUtil.formatLang(se_registerSE_packEntry.getKey().getName())),
                            se_registerSE_packEntry.getValue().getLevel(),
                            se_registerSE_packEntry.getKey().getMaxLevel()
                    ));
            /*if (detail) {
                tooltip.add(new TranslationTextComponent("  %s",
                        new TranslationTextComponent(StringUtil.formatLang(
                                se_registerSE_packEntry.getKey().getName().getNamespace(),
                                se_registerSE_packEntry.getKey().getName().getPath(),
                                StringFinal.INTRODUCE))));
            }*/

        }
        /*if (!detail) {
            tooltip.add(new StringTextComponent(""));
            tooltip.add(new TranslationTextComponent(StringUtil.formatLang("information", StringFinal.INTRODUCE),
                    new TranslationTextComponent(informationClientKeyRegister.getKeyMapping().getKey().getTranslationKey())));
            tooltip.add(new StringTextComponent(""));
        }*/

    }

    @Inject(method = "fillItemGroup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/NonNullList;addAll(Ljava/util/Collection;)Z",
                    opcode = 1
            ))
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items, CallbackInfo callbackInfo) {
        List<SlashBladeRegister> slashBladeRegisterList = new ArrayList<>();
        for (SlashBladeRegister slashBladeRegister : GlowingFireGlow.getInstance().getWorldComponent(AllSlashBladeRegister.class).forAll()) {
            if (!slashBladeRegister.displayItem()) {
                continue;
            }
            slashBladeRegisterList.add(slashBladeRegister);
        }
        slashBladeRegisterList = slashBladeRegisterList.stream().sorted(Comparator.comparing(RegisterBasics::getName)).collect(Collectors.toList());
        for (SlashBladeRegister slashBladeRegister : slashBladeRegisterList) {
            items.add(slashBladeRegister.getSlashBladePack().itemStack);
        }
    }

    /**
     * @author
     * @reason
     */
    /*@Overwrite
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (group != SlashBlade.SLASHBLADE) {
            return;
        }
        RecipeManager rm = DistExecutor.runForDist(() -> ItemSlashBlade::getClientRM, () -> ItemSlashBlade::getServerRM);

        if (rm == null) return;

        Set<ResourceLocation> keys = rm.getKeys()
                .filter((loc) -> loc.getNamespace().equals(SlashBlade.modid)
                                 && (!(
                        loc.getPath().startsWith("material")
                        || loc.getPath().startsWith("bladestand")
                        || loc.getPath().startsWith("simple_slashblade"))))
                .collect(Collectors.toSet());

        List<ItemStack> allItems = keys.stream()
                .map(key -> rm.getRecipe(key)
                        .map(r -> {
                            ItemStack stack = r.getRecipeOutput().copy();
                            stack.readShareTag(stack.getShareTag());
                            SlashBladePack slashBladePack = new SlashBladePack(stack);
                            if (slashBladePack.isEffective()) {
                                slashBladePack.iSlashBladeStateSupplement.setSlashBladeName(r.getId());
                            }
                            return stack;
                        })
                        .orElse(ItemStack.EMPTY))
                .sorted(Comparator.comparing(s -> ((ItemStack) s).getTranslationKey()).reversed())
                .collect(Collectors.toList());

        items.addAll(allItems);
    }*/

}
