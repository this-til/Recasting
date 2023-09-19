package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.capability.time_run.TimerCell;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
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
import com.til.recasting.common.register.world.item.SoulItemRegister;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.awt.*;

@VoluntarilyRegister
public class BlueCloudSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected BlueCloudLightSA blueCloudLightSA;

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(6f);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(0xA7C683));
        slashBladePack.getSlashBladeStateSupplement().setDurable(4);
        slashBladePack.setSA(blueCloudLightSA);
    }

    @VoluntarilyRegister
    public static class BlueCloudSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

        @VoluntarilyAssignment
        protected BlueCloudSlashBladeRegister blueCloudSlashBladeRecipeRegister;

        @VoluntarilyAssignment
        protected BaGuaSlashBladeRegister baGuaSlashBladeRegister;

        @VoluntarilyAssignment
        protected CooperateWithSE cooperateWithSE;

        @VoluntarilyAssignment
        protected SoulItemRegister.SoulCubeItemRegister soulCubeItemRegister;

        @VoluntarilyAssignment
        protected SoulItemRegister.SoulCubeChangeItemRegister soulCubeChangeItemRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {

            SlashBladePack baGuaSlashBlade = baGuaSlashBladeRegister.getSlashBladePack();
            baGuaSlashBlade.getSlashBladeState().setKillCount(1000);
            baGuaSlashBlade.getSlashBladeState().setRefine(150);

            EnchantmentHelper.setEnchantments(MapUtil.of(
                            Enchantments.SMITE, 1,
                            Enchantments.BANE_OF_ARTHROPODS, 1,
                            Enchantments.SHARPNESS, 2,
                            Enchantments.EFFICIENCY, 2),
                    baGuaSlashBlade.getItemStack());

            baGuaSlashBlade.getIse().getPack(cooperateWithSE).setLevel(2);


            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "ABA",
                            "AVA",
                            "ACA"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfItemSE(cooperateWithSE, 0.5f),
                            "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem())),
                            "C", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeChangeItemRegister.getItem())),
                            "V", new IRecipeInItemPack.OfSlashBlade(baGuaSlashBlade.getItemStack())),
                    "V",
                    new IResultPack.OfSlashBladeRegister(blueCloudSlashBladeRecipeRegister)
            );
        }
    }

    @VoluntarilyRegister
    public static class BlueCloudLightSA extends SA_Register {

        @ConfigField
        protected int attackNumber;

        @ConfigField
        protected float hit;

        @ConfigField
        protected int delay;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            float r = 360f / attackNumber;
            for (int i = 0; i < attackNumber; i++) {
                int _delay = delay * i;
                int finalI = i;
                slashBladeEntityPack.getTimeRun().addTimerCell(new TimerCell(() -> {
                    AttackManager.doSlash(
                            slashBladeEntityPack.getEntity(),
                            r * finalI,
                            false,
                            false,
                            hit
                    );
                }, _delay, 0));
            }
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attackNumber = 8;
            hit = 0.3f;
            delay = 3;
        }

    }
}
