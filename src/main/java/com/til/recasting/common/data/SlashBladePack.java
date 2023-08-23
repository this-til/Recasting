package com.til.recasting.common.data;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.capability.ISlashBladeStateSupplement;
import com.til.recasting.common.register.capability.ISlashBladeStateSupplementCapabilityRegister;
import com.til.recasting.common.register.capability.SE_CapabilityRegister;
import com.til.recasting.common.register.slash_blade.sa.AllSA_Register;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.SlashArts;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

@StaticVoluntarilyAssignment
public class SlashBladePack {


    @VoluntarilyAssignment
    protected static SE_CapabilityRegister se_capabilityRegister;

    @VoluntarilyAssignment
    protected static ISlashBladeStateSupplementCapabilityRegister iSlashBladeStateSupplement_capabilityRegister;

    @VoluntarilyAssignment
    protected static AllSA_Register allSARegister;

    protected ItemStack itemStack;
    protected ISlashBladeState slashBladeState;
    protected ISlashBladeStateSupplement iSlashBladeStateSupplement;
    protected ISE ise;
    protected boolean basicEffective;

    public SlashBladePack(ItemStack itemStack) {
        this.itemStack = itemStack;
        try {
            slashBladeState = itemStack.getCapability(ItemSlashBlade.BLADESTATE).orElse(null);
            iSlashBladeStateSupplement = itemStack.getCapability(iSlashBladeStateSupplement_capabilityRegister.getCapability()).orElse(null);
            ise = itemStack.getCapability(se_capabilityRegister.getCapability()).orElse(null);
            basicEffective = getSlashBladeState() != null && getIse() != null && getSlashBladeStateSupplement() != null;
        } catch (RuntimeException runtimeException) {
            basicEffective = false;
        }


    }

    public boolean isEffective(EffectiveType effectiveType) {
        boolean isSlashBlade = basicEffective;
        switch (effectiveType) {
            case isSlashBlade:
                return isSlashBlade;
            case canUse:
                return isSlashBlade && !getSlashBladeState().isBroken();
            default:
                return false;
        }
    }

    /***
     * 判定给定刀的状态能继承自己
     * 用做和成判定
     */
    public boolean isExtends(SlashBladePack slashBladePack) {
        if (!slashBladePack.isEffective(EffectiveType.isSlashBlade)) {
            return false;
        }
        if (!slashBladePack.getSlashBladeState().getTranslationKey().equals(getSlashBladeState().getTranslationKey())) {
            return false;
        }
        if (slashBladePack.getSlashBladeState().getKillCount() < getSlashBladeState().getKillCount()) {
            return false;
        }
        if (slashBladePack.getSlashBladeState().getRefine() < getSlashBladeState().getRefine()) {
            return false;
        }
        Map<Enchantment, Integer> mainEnchantmentMap = EnchantmentHelper.getEnchantments(getItemStack());
        Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(getItemStack());
        for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : mainEnchantmentMap.entrySet()) {
            if (!enchantmentMap.containsKey(enchantmentIntegerEntry.getKey())) {
                return false;
            }
            if (enchantmentMap.get(enchantmentIntegerEntry.getKey()) < Math.min(enchantmentIntegerEntry.getValue(), enchantmentIntegerEntry.getKey().getMaxLevel())) {
                return false;
            }
        }
        Map<SE_Register, ISE.SE_Pack> mainSeMap = getIse().getAllSE();
        for (Map.Entry<SE_Register, ISE.SE_Pack> se_registerSE_packEntry : mainSeMap.entrySet()) {
            if (se_registerSE_packEntry.getValue().isEmpty()) {
                continue;
            }
            if (!slashBladePack.getIse().hasSE(se_registerSE_packEntry.getKey())) {
                return false;
            }
            if (slashBladePack.getIse().getPack(se_registerSE_packEntry.getKey()).getLevel() < Math.min(se_registerSE_packEntry.getValue().getLevel(), se_registerSE_packEntry.getKey().getMaxLevel())) {
                return false;
            }
        }
        return true;
    }

    /***
     * 以自身为模板
     * 获取合成结果
     * @param slashBladePack 输入刀
     * @return 合成的结果
     */
    public SlashBladePack getRecipeResult(SlashBladePack slashBladePack) {
        ItemStack outItemStack = getItemStack().copy();
        SlashBladePack outSlashBladePack = new SlashBladePack(outItemStack);
        outSlashBladePack.getSlashBladeState().setKillCount(slashBladePack.getSlashBladeState().getKillCount());
        outSlashBladePack.getSlashBladeState().setRefine(slashBladePack.getSlashBladeState().getRefine() / 2);

        for (Map.Entry<SE_Register, ISE.SE_Pack> entry : slashBladePack.getIse().getAllSE().entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            ISE.SE_Pack outPack = outSlashBladePack.getIse().getPack(entry.getKey());
            outPack.setLevel(Math.max(outPack.getLevel(), entry.getValue().getLevel() - 1));
        }

        Map<Enchantment, Integer> outEnchantmentMap = EnchantmentHelper.getEnchantments(outItemStack);
        Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(slashBladePack.getItemStack());
        for (Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {
            outEnchantmentMap.put(entry.getKey(), Math.max(outEnchantmentMap.get(entry.getKey()), entry.getValue() - 1));
        }
        EnchantmentHelper.setEnchantments(outEnchantmentMap, outItemStack);
        return outSlashBladePack;
    }

    public void setSA(SA_Register sa_register) {
        getSlashBladeState().setSlashArtsKey(sa_register.getSlashArts().getName());
    }

    public SA_Register getSA() {
        SlashArts sa = getSlashBladeState().getSlashArts();
        return allSARegister.getSA_Register(sa);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ISlashBladeState getSlashBladeState() {
        return slashBladeState;
    }

    public ISlashBladeStateSupplement getSlashBladeStateSupplement() {
        return iSlashBladeStateSupplement;
    }

    public ISE getIse() {
        return ise;
    }

    public enum EffectiveType {
        /***
         * 表明是一把刀
         */
        isSlashBlade,
        /***
         * 表明刀可以使用
         */
        canUse,
    }

}
