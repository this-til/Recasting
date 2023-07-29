package com.til.recasting.common.mixin;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.util.StringUtil;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.AllSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.AllSA_Register;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author til
 */
@Mixin(value = {ItemSlashBlade.class}, remap = false)
public abstract class ItemSlashBladeMixin {

    @Accessor
    public static UUID getATTACK_DAMAGE_AMPLIFIER() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    public static UUID getPLAYER_REACH_AMPLIFIER(){
        throw new UnsupportedOperationException();
    }

    /*@Inject(method = "getAttributeModifiers",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/util/LazyOptional;ifPresent(Lnet/minecraftforge/common/util/NonNullConsumer;)V",
                    opcode = 1
            ))*/

    /**
     * @author
     * @reason
     */
  /*  @Overwrite
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {

        SlashBladePack slashBladePack = new SlashBladePack(stack);
        if (!slashBladePack.isEffective(SlashBladePack.EffectiveType.canUse)) {
            return ImmutableMultimap.of();
        }
        if (slot != EquipmentSlotType.MAINHAND) {
            return ImmutableMultimap.of();
        }

        Multimap<Attribute, AttributeModifier> result = ArrayListMultimap.create();

        result.put(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        getATTACK_DAMAGE_AMPLIFIER(),
                        "Weapon modifier",
                        slashBladePack.slashBladeState.getBaseAttackModifier(),
                        AttributeModifier.Operation.ADDITION));

        result.put(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        getATTACK_DAMAGE_AMPLIFIER(),
                        "Weapon amplifier",
                        slashBladePack.slashBladeState.getAttackAmplifier(),
                        AttributeModifier.Operation.ADDITION
                ));

        result.put(ForgeMod.REACH_DISTANCE.get(),
                new AttributeModifier(
                        getPLAYER_REACH_AMPLIFIER(),
                        "Reach amplifer",
                        1.5f,
                        AttributeModifier.Operation.ADDITION
                ));

        *//*Multimap<Attribute, AttributeModifier> def = super.getAttributeModifiers(slot,stack);
        Multimap<Attribute, AttributeModifier> result = ArrayListMultimap.create();

        result.putAll(Attributes.ATTACK_DAMAGE, def.get(Attributes.ATTACK_DAMAGE));
        result.putAll(Attributes.ATTACK_SPEED, def.get(Attributes.ATTACK_SPEED));

        if (slot == EquipmentSlotType.MAINHAND) {
            LazyOptional<ISlashBladeState> state = stack.getCapability(BLADESTATE);
            state.ifPresent(s -> {
                float baseAttackModifier = s.getBaseAttackModifier();
                AttributeModifier base = new AttributeModifier(ATTACK_DAMAGE_MODIFIER,
                        "Weapon modifier",
                        (double) baseAttackModifier,
                        AttributeModifier.Operation.ADDITION);
                result.remove(Attributes.ATTACK_DAMAGE,base);
                result.put(Attributes.ATTACK_DAMAGE,base);

                float rankAttackAmplifier = s.getAttackAmplifier();
                result.put(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(ATTACK_DAMAGE_AMPLIFIER,
                                "Weapon amplifier",

                                (double) rankAttackAmplifier,
                                AttributeModifier.Operation.ADDITION));



                result.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(PLAYER_REACH_AMPLIFIER,
                        "Reach amplifer",
                        s.isBroken() ? 0 : 1.5, AttributeModifier.Operation.ADDITION));

            });
        }*//*

        return result;
    }
*/

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
        slashBladePack.slashBladeState.setDamage(slashBladePack.slashBladeState.getDamage() + amount);
    }

    @Shadow
    abstract int getHalfMaxdamage();

    @Inject(method = "addInformation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/util/LazyOptional;ifPresent(Lnet/minecraftforge/common/util/NonNullConsumer;)V",
                    opcode = 1
            ))
    protected void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, CallbackInfo callbackInfo) {
        SlashBladePack slashBladePack = new SlashBladePack(stack);
        if (!slashBladePack.isEffective(SlashBladePack.EffectiveType.isSlashBlade)) {
            return;
        }
        tooltip.add(new TranslationTextComponent("key:%s", slashBladePack.slashBladeState.getTranslationKey()));
        tooltip.add(new StringTextComponent(""));


        SA_Register sa_register = GlowingFireGlow.getInstance().getWorldComponent(AllSA_Register.class).getSA_Register(slashBladePack.slashBladeState.getSlashArts());
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

        List<SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister> slashBladeRecipeRegisterList = new ArrayList<>();
        for (SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister slashBladeRecipeRegister : GlowingFireGlow.getInstance().getWorldComponent(SlashBladeRecipeSerializerRegister.AllSlashBladeRecipeRegister.class).forAll()) {
            slashBladeRecipeRegisterList.add(slashBladeRecipeRegister);
        }
        slashBladeRecipeRegisterList = slashBladeRecipeRegisterList.stream().sorted(Comparator.comparing(RegisterBasics::getName)).collect(Collectors.toList());
        for (SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister slashBladeRecipeRegister : slashBladeRecipeRegisterList) {
            for (Map.Entry<String, IRecipeInItemPack> stringIRecipeInItemPackEntry : slashBladeRecipeRegister.getSlashBladeRecipeRecipePack().key.entrySet()) {
                if (stringIRecipeInItemPackEntry.getValue() instanceof IRecipeInItemPack.OfSlashBlade) {
                    items.add(((IRecipeInItemPack.OfSlashBlade) stringIRecipeInItemPackEntry.getValue()).getSlashBladePack().itemStack);
                }
            }
            if (slashBladeRecipeRegister.getSlashBladeRecipeRecipePack().result instanceof IResultPack.OfItemStack) {
                items.add(slashBladeRecipeRegister.getSlashBladeRecipeRecipePack().result.getOutItemStack());
            }
        }
    }

    @Inject(method = "onItemRightClick", at = @At("HEAD"), cancellable = true)
    public void onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn, CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        SlashBladePack slashBladePack = new SlashBladePack(itemstack);
        if (!slashBladePack.isEffective(SlashBladePack.EffectiveType.canUse)) {
            cir.setReturnValue(new ActionResult<>(ActionResultType.PASS, itemstack));
            cir.cancel();
        }
    }

    @Inject(method = "onLeftClickEntity", at = @At("HEAD"), cancellable = true)
    public void onLeftClickEntity(ItemStack itemstack, PlayerEntity playerIn, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        SlashBladePack slashBladePack = new SlashBladePack(itemstack);
        if (!slashBladePack.isEffective(SlashBladePack.EffectiveType.canUse)) {
            cir.setReturnValue(false);
            cir.cancel();
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
