package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.recipe.SlashBladeUpRecipeRegister;
import com.til.recasting.common.register.sa.instance.FanaticalDanceSA;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.recipe.SlashBladeRecipeRegister;
import com.til.recasting.common.register.slash_blade.se.instance.CooperateWithSE;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

@VoluntarilyRegister
public class CoolMintSlashBladeRegister extends SlashBladeRegister {


    @VoluntarilyAssignment
    protected FanaticalDanceSA fanaticalDanceSA;

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.slashBladeState.setColorCode(0xC7F3CB);
        slashBladePack.slashBladeState.setBaseAttackModifier(7f);
        slashBladePack.slashBladeState.setSlashArtsKey(fanaticalDanceSA.getSlashArts().getName());
    }

    @VoluntarilyRegister
    public static class CoolMintSlashBladeRecipeRegister extends SlashBladeRecipeRegister {

        @VoluntarilyAssignment
        protected CoolMintSlashBladeRegister coolMintSlashBladeRegister;

        @VoluntarilyAssignment
        protected CooperateWithSE cooperateWithSE;

        @VoluntarilyAssignment
        protected BlueCloudSlashBladeRegister blueCloudSlashBladeRegister;

        @VoluntarilyAssignment
        protected BlackSlashBladeRegister blackSlashBladeRegister;

        @Override
        protected SlashBladeUpRecipeRegister.SlashBladeUpPack defaultConfigSlashBladeUpPack() {

            SlashBladePack blueCloudSlashBlade = blueCloudSlashBladeRegister.getSlashBladePack();
            blueCloudSlashBlade.slashBladeState.setKillCount(2500);
            blueCloudSlashBlade.slashBladeState.setRefine(300);
            blueCloudSlashBlade.ise.getPack(cooperateWithSE).setLevel(5);
            EnchantmentHelper.setEnchantments(MapUtil.of(
                            Enchantments.SMITE, 5,
                            Enchantments.BANE_OF_ARTHROPODS, 5,
                            Enchantments.SHARPNESS, 5,
                            Enchantments.EFFICIENCY, 5,
                            Enchantments.PIERCING, 1,
                            Enchantments.FIRE_ASPECT, 1),
                    blueCloudSlashBlade.itemStack);

            SlashBladePack blackSlashBlade = blackSlashBladeRegister.getSlashBladePack();
            blackSlashBlade.slashBladeState.setKillCount(1500);
            blackSlashBlade.slashBladeState.setRefine(150);
            EnchantmentHelper.setEnchantments(MapUtil.of(
                            Enchantments.PROTECTION, 4,
                            Enchantments.FIRE_PROTECTION, 4,
                            Enchantments.BLAST_PROTECTION, 4,
                            Enchantments.PROJECTILE_PROTECTION, 4),
                    blackSlashBlade.itemStack);


            return new SlashBladeUpRecipeRegister.SlashBladeUpPack(
                    ListUtil.of(
                            "  A",
                            " A ",
                            "VB "
                    ),
                    MapUtil.of("A", new IRecipeInItemPack.OfItemSE(cooperateWithSE, 4, true),
                            "B", new IRecipeInItemPack.OfSlashBlade(blackSlashBlade.itemStack),
                            "V", new IRecipeInItemPack.OfSlashBlade(blueCloudSlashBlade.itemStack)),
                    "V",
                    new IResultPack.OfSlashBladeRegister(coolMintSlashBladeRegister)
            );
        }
    }
}
