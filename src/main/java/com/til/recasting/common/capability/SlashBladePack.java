package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.register.capability.ISlashBladeStateSupplement_CapabilityRegister;
import com.til.recasting.common.register.capability.SE_CapabilityRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

@StaticVoluntarilyAssignment
public class SlashBladePack {


    @VoluntarilyAssignment
    protected static SE_CapabilityRegister se_capabilityRegister;

    @VoluntarilyAssignment
    protected static ISlashBladeStateSupplement_CapabilityRegister iSlashBladeStateSupplement_capabilityRegister;

    public final ItemStack itemStack;
    public final ISlashBladeState slashBladeState;
    public final ISlashBladeStateSupplement iSlashBladeStateSupplement;
    public final ISE ise;


    public SlashBladePack(ItemStack itemStack) {
        this.itemStack = itemStack;
        slashBladeState = itemStack.getCapability(ItemSlashBlade.BLADESTATE).orElse(null);
        iSlashBladeStateSupplement = itemStack.getCapability(iSlashBladeStateSupplement_capabilityRegister.getCapability()).orElse(null);
        ise = itemStack.getCapability(se_capabilityRegister.getCapability()).orElse(null);
    }

    public boolean isEffective(EffectiveType effectiveType) {
        boolean isSlashBlade = slashBladeState != null && ise != null && iSlashBladeStateSupplement != null;
        switch (effectiveType) {
            case isSlashBlade:
                return isSlashBlade;
            case canUse:
                return isSlashBlade && !slashBladeState.isBroken();
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
        if (!slashBladePack.slashBladeState.getTranslationKey().equals(slashBladeState.getTranslationKey())) {
            return false;
        }
        if (slashBladePack.slashBladeState.getKillCount() < slashBladeState.getKillCount()) {
            return false;
        }
        if (slashBladePack.slashBladeState.getRefine() < slashBladeState.getRefine()) {
            return false;
        }
        Map<Enchantment, Integer> mainEnchantmentMap = EnchantmentHelper.getEnchantments(itemStack);
        Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(itemStack);
        for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : mainEnchantmentMap.entrySet()) {
            if (!enchantmentMap.containsKey(enchantmentIntegerEntry.getKey()) || enchantmentMap.get(enchantmentIntegerEntry.getKey()) < enchantmentIntegerEntry.getValue()) {
                return false;
            }
        }
        Map<SE_Register, ISE.SE_Pack> mainSeMap = ise.getAllSE();
        for (Map.Entry<SE_Register, ISE.SE_Pack> se_registerSE_packEntry : mainSeMap.entrySet()) {
            if (se_registerSE_packEntry.getValue().isEmpty()) {
                continue;
            }
            if (!slashBladePack.ise.hasSE(se_registerSE_packEntry.getKey())) {
                return false;
            }
            if (slashBladePack.ise.getPack(se_registerSE_packEntry.getKey()).getLevel() < se_registerSE_packEntry.getValue().getLevel()) {
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
        ItemStack outItemStack = itemStack.copy();
        SlashBladePack outSlashBladePack = new SlashBladePack(outItemStack);
        outSlashBladePack.slashBladeState.setKillCount(slashBladePack.slashBladeState.getKillCount());
        outSlashBladePack.slashBladeState.setRefine(slashBladePack.slashBladeState.getRefine() / 2);
        for (Map.Entry<SE_Register, ISE.SE_Pack> se_registerSE_packEntry : slashBladePack.ise.getAllSE().entrySet()) {
            if (se_registerSE_packEntry.getValue().isEmpty()) {
                continue;
            }
            ISE.SE_Pack se_pack = outSlashBladePack.ise.getPack(se_registerSE_packEntry.getKey());
            se_pack.setLevel(se_pack.getLevel() - 1);
        }
        Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(slashBladePack.itemStack);
        Map<Enchantment, Integer> outEnchantmentMap = new HashMap<>(enchantmentMap);
        for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : enchantmentMap.entrySet()) {
            outEnchantmentMap.put(enchantmentIntegerEntry.getKey(), enchantmentIntegerEntry.getValue() - 1);
        }
        EnchantmentHelper.setEnchantments(outEnchantmentMap, outItemStack);
        return outSlashBladePack;
    }

    public void setSA(SA_Register sa_register) {
        slashBladeState.setSlashArtsKey(sa_register.getSlashArts().getName());
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
