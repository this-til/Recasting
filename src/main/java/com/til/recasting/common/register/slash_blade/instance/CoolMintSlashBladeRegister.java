package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.capability.time_run.TimerCell;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.capability.capabilitys.TimeRunCapabilityRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.instance.CooperateWithSE;
import com.til.recasting.common.register.slash_blade.se.instance.CrossChopSE;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;

import java.awt.*;

@VoluntarilyRegister
public class CoolMintSlashBladeRegister extends SlashBladeRegister {


    @VoluntarilyAssignment
    protected CoolMintSlashBladeSA fanaticalDanceSA;

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(0xC7F3CB));
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
        slashBladePack.getSlashBladeState().setSlashArtsKey(fanaticalDanceSA.getSlashArts().getName());
        slashBladePack.getSlashBladeStateSupplement().setDurable(16);

    }

    @VoluntarilyRegister
    public static class CoolMintSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

        @VoluntarilyAssignment
        protected CoolMintSlashBladeRegister coolMintSlashBladeRegister;

        @VoluntarilyAssignment
        protected CooperateWithSE cooperateWithSE;

        @VoluntarilyAssignment
        protected CrossChopSE crossChopSE;

        @VoluntarilyAssignment
        protected BlueCloudSlashBladeRegister blueCloudSlashBladeRegister;

        @VoluntarilyAssignment
        protected BlackSlashBladeRegister blackSlashBladeRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {

            SlashBladePack blueCloudSlashBlade = blueCloudSlashBladeRegister.getSlashBladePack();
            blueCloudSlashBlade.getSlashBladeState().setKillCount(2500);
            blueCloudSlashBlade.getSlashBladeState().setRefine(300);
            blueCloudSlashBlade.getIse().getPack(crossChopSE).setLevel(1);
            blueCloudSlashBlade.getIse().getPack(cooperateWithSE).setLevel(1);
            EnchantmentHelper.setEnchantments(MapUtil.of(
                            Enchantments.SMITE, 2,
                            Enchantments.BANE_OF_ARTHROPODS, 2,
                            Enchantments.SHARPNESS, 2,
                            Enchantments.EFFICIENCY, 2,
                            Enchantments.PIERCING, 1,
                            Enchantments.FIRE_ASPECT, 1),
                    blueCloudSlashBlade.getItemStack());

            SlashBladePack blackSlashBlade = blackSlashBladeRegister.getSlashBladePack();
            blackSlashBlade.getSlashBladeState().setKillCount(1500);
            blackSlashBlade.getSlashBladeState().setRefine(150);
            EnchantmentHelper.setEnchantments(MapUtil.of(
                            Enchantments.PROTECTION, 1,
                            Enchantments.FIRE_PROTECTION, 1,
                            Enchantments.BLAST_PROTECTION, 1,
                            Enchantments.PROJECTILE_PROTECTION, 1),
                    blackSlashBlade.getItemStack());


            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "  A",
                            " VB",
                            "C  "
                    ),
                    MapUtil.of("A", new IRecipeInItemPack.OfItemSE(cooperateWithSE, 2),
                            "C", new IRecipeInItemPack.OfItemSE(crossChopSE, 2),
                            "B", new IRecipeInItemPack.OfSlashBlade(blackSlashBlade.getItemStack()),
                            "V", new IRecipeInItemPack.OfSlashBlade(blueCloudSlashBlade.getItemStack())),
                    "V",
                    new IResultPack.OfSlashBladeRegister(coolMintSlashBladeRegister)
            );
        }
    }

    /***
     * 乱舞
     */
    @VoluntarilyRegister
    public static class CoolMintSlashBladeSA extends SA_Register {

        @VoluntarilyAssignment
        protected TimeRunCapabilityRegister timeRunCapabilityRegister;


        @ConfigField
        protected int attackNumber;

        @ConfigField
        protected int attackDeviation;

        @ConfigField
        protected float hit;

        @ConfigField
        protected int delay;

        @ConfigField
        protected int offset;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            int number = attackNumber + slashBladeEntityPack.getEntity().getRNG().nextInt(attackDeviation);
            for (int i = 0; i < number; i++) {
                int _delay = delay * i;
                slashBladeEntityPack.getTimeRun().addTimerCell(new TimerCell(() -> {
                    AttackManager.doSlash(
                            slashBladeEntityPack.getEntity(),
                            slashBladeEntityPack.getEntity().getRNG().nextInt(360),
                            new Vector3d(slashBladeEntityPack.getEntity().getRNG().nextFloat() - 0.5f, slashBladeEntityPack.getEntity().getRNG().nextFloat() - 0.5f, 0).scale(offset),
                            false,
                            true,
                            hit
                    );
                }, _delay, 0));
            }
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attackNumber = 15;
            attackDeviation = 3;
            hit = 0.4f;
            delay = 1;
            offset = 3;
        }
    }
}
