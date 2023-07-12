package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.recipe.SlashBladeUpRecipeRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.se.instance.OverloadSE;
import com.til.recasting.common.register.slash_blade.recipe.SlashBladeRecipeRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class StarBladeSlashBladeRegister extends SlashBladeRegister {
    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(StarBladeSlashBladeRegister.class), "state" + getState(), "model.obj"));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(StarBladeSlashBladeRegister.class), "state" + getState(), "texture.png"));

        summondSwordModel = new ResourceLocation(getName().getNamespace(), String.join("/", "summond_sword", "star_blade", "model.obj"));
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.slashBladeState.setColorCode(0xE7E5E6);
    }

    protected abstract int getState();

    @VoluntarilyRegister
    public static class StarBlade_1_SlashBladeRegister extends StarBladeSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(4f);
            slashBladePack.iSlashBladeStateSupplement.setAttackDistance(0.75f);
        }

        @Override
        protected int getState() {
            return 1;
        }

        @VoluntarilyRegister
        public static class StarBlade_1_SlashBladeRecipeRegister extends SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected StarBlade_1_SlashBladeRegister starBlade_1_slashBladeRegister;

            @VoluntarilyAssignment
            protected OverloadSE overloadSE;

            @VoluntarilyAssignment
            protected BlackSlashBladeRegister blackSlashBladeRegister;

            @Override
            protected SlashBladeUpRecipeRegister.SlashBladeUpPack defaultConfigSlashBladeUpPack() {

                SlashBladePack blackSlashBlade = blackSlashBladeRegister.getSlashBladePack();
                blackSlashBlade.slashBladeState.setKillCount(250);
                blackSlashBlade.slashBladeState.setRefine(35);
                EnchantmentHelper.setEnchantments(MapUtil.of(Enchantments.PROTECTION, 3, Enchantments.INFINITY, 1), blackSlashBlade.itemStack);

                return new SlashBladeUpRecipeRegister.SlashBladeUpPack(
                        ListUtil.of(
                                " AB",
                                "AVA",
                                "BA "
                        ),
                        MapUtil.of("A", new IRecipeInItemPack.OfItemSE(overloadSE, 1, false),
                                "B", new IRecipeInItemPack.OfEntity(EntityType.PHANTOM),
                                "V", new IRecipeInItemPack.OfSlashBlade(blackSlashBlade.itemStack)),
                        "V",
                        new IResultPack.OfSlashBladeRegister(starBlade_1_slashBladeRegister)
                );
            }
        }

    }

    @VoluntarilyRegister
    public static class StarBlade_2_SlashBladeRegister extends StarBladeSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setColorCode(0xE7E5E6);
            slashBladePack.slashBladeState.setBaseAttackModifier(5f);
        }

        @Override
        protected int getState() {
            return 2;
        }

    }

    @VoluntarilyRegister
    public static class StarBlade_3_SlashBladeRegister extends StarBladeSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(6f);
            slashBladePack.iSlashBladeStateSupplement.setAttackDistance(1.5f);

        }

        @Override
        protected int getState() {
            return 3;
        }

    }

    @VoluntarilyRegister
    public static class StarBlade_4_SlashBladeRegister extends StarBladeSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(7f);
            slashBladePack.iSlashBladeStateSupplement.setAttackDistance(2f);
        }

        @Override
        protected int getState() {
            return 4;
        }
    }
}
