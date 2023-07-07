package com.til.recasting.common.mixin;

import com.google.common.collect.Multimap;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.util.StringUtil;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.register.sa.AllSARegister;
import com.til.recasting.common.register.sa.SA_Register;
import com.til.recasting.common.register.se.SE_Register;
import com.til.recasting.common.register.slash_blade.AllSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.DistExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author til
 */
@Mixin(value = ItemSlashBlade.class, remap = false)
public class ItemSlashBladeMixin {


    @Inject(method = "getAttributeModifiers",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/util/LazyOptional;ifPresent(Lnet/minecraftforge/common/util/NonNullConsumer;)V",
                    opcode = 1
            ))
    protected void getAttributeModifiers(EquipmentSlotType slot, ItemStack stack, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> callbackInfoReturnable) {

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

        //tooltip.add(new TranslationTextComponent("name:%s", slashBladePack.iSlashBladeStateSupplement.getSlashBladeName()));
        //tooltip.add(new StringTextComponent(""));

        SA_Register sa_register = GlowingFireGlow.getInstance().getWorldComponent(AllSARegister.class).getSA_Register(slashBladePack.slashBladeState.getSlashArts());
        if (sa_register != null) {
            tooltip.add(new TranslationTextComponent("SA:%s",
                    new TranslationTextComponent(StringUtil.formatLang(sa_register.getName()))));
         /*   tooltip.add(new TranslationTextComponent("  %s",
                    new TranslationTextComponent(StringUtil.formatLang(sa_register.getName().getNamespace(), sa_register.getName().getPath(), StringFinal.INTRODUCE))));*/
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
          /*  tooltip.add(new TranslationTextComponent("  %s",
                    new TranslationTextComponent(StringUtil.formatLang(
                            se_registerSE_packEntry.getKey().getName().getNamespace(),
                            se_registerSE_packEntry.getKey().getName().getPath(),
                            StringFinal.INTRODUCE))));*/
        }

    }

    @Inject(method = "fillItemGroup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/NonNullList;addAll(Ljava/util/Collection;)Z",
                    opcode = 1
            ))
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items, CallbackInfo callbackInfo) {
        for (SlashBladeRegister slashBladeRegister : GlowingFireGlow.getInstance().getWorldComponent(AllSlashBladeRegister.class).forAll()) {
            items.add(slashBladeRegister.getDefaultItemStack());
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
