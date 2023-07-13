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
import com.til.recasting.common.register.slash_blade.recipe.SlashBladeRecipeRegister;
import com.til.recasting.common.register.slash_blade.sa.instance.InfiniteDimensionalChoppingSA;
import com.til.recasting.common.register.slash_blade.sa.instance.MultipleDimensionalChoppingSA;
import com.til.recasting.common.register.slash_blade.se.instance.OverloadSE;
import com.til.recasting.common.register.slash_blade.se.instance.StormSE;
import com.til.recasting.common.register.slash_blade.se.instance.StormVariantSE;
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
                blackSlashBlade.ise.getPack(overloadSE).setLevel(1);
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

        @VoluntarilyAssignment
        protected MultipleDimensionalChoppingSA multipleDimensionalChoppingSA;


        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setColorCode(0xE7E5E6);
            slashBladePack.slashBladeState.setBaseAttackModifier(5f);
            slashBladePack.setSA(multipleDimensionalChoppingSA);
        }

        @Override
        protected int getState() {
            return 2;
        }

        @VoluntarilyRegister
        public static class StarBlade_2_SlashBladeRecipeRegister extends SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected StarBlade_2_SlashBladeRegister starBlade_2_slashBladeRegister;

            @VoluntarilyAssignment
            protected StarBlade_1_SlashBladeRegister starBlade_1_slashBladeRegister;

            @VoluntarilyAssignment
            protected OverloadSE overloadSE;

            @VoluntarilyAssignment
            protected StormSE stormSE;

            @VoluntarilyAssignment
            protected StormVariantSE stormVariantSE;


            @Override
            protected SlashBladeUpRecipeRegister.SlashBladeUpPack defaultConfigSlashBladeUpPack() {
                SlashBladePack starBlade_1 = starBlade_1_slashBladeRegister.getSlashBladePack();
                starBlade_1.slashBladeState.setKillCount(1000);
                starBlade_1.slashBladeState.setRefine(75);
                starBlade_1.ise.getPack(overloadSE).setLevel(2);
                starBlade_1.ise.getPack(stormSE).setLevel(1);
                starBlade_1.ise.getPack(stormVariantSE).setLevel(1);
                EnchantmentHelper.setEnchantments(MapUtil.of(
                        Enchantments.PROTECTION, 4,
                        Enchantments.EFFICIENCY, 3,
                        Enchantments.INFINITY, 1), starBlade_1.itemStack);

                return new SlashBladeUpRecipeRegister.SlashBladeUpPack(
                        ListUtil.of(
                                "  A",
                                " V ",
                                "B  "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemSE(stormSE, 1, true),
                                "B", new IRecipeInItemPack.OfItemSE(stormVariantSE, 1, true),
                                "V", new IRecipeInItemPack.OfSlashBlade(starBlade_1.itemStack)),
                        "V",
                        new IResultPack.OfSlashBladeRegister(starBlade_2_slashBladeRegister)
                );
            }
        }

    }

    @VoluntarilyRegister
    public static class StarBlade_3_SlashBladeRegister extends StarBladeSlashBladeRegister {

        @VoluntarilyAssignment
        protected MultipleDimensionalChoppingSA multipleDimensionalChoppingSA;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(6f);
            slashBladePack.iSlashBladeStateSupplement.setAttackDistance(1.5f);
            slashBladePack.setSA(multipleDimensionalChoppingSA);
        }

        @Override
        protected int getState() {
            return 3;
        }

        @VoluntarilyRegister
        public static class StarBlade_3_SlashBladeRecipeRegister extends SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected StarBlade_3_SlashBladeRegister starBlade_3_slashBladeRegister;

            @VoluntarilyAssignment
            protected StarBlade_2_SlashBladeRegister starBlade_2_slashBladeRegister;

            @VoluntarilyAssignment
            protected OverloadSE overloadSE;

            @VoluntarilyAssignment
            protected StormSE stormSE;

            @VoluntarilyAssignment
            protected StormVariantSE stormVariantSE;


            @Override
            protected SlashBladeUpRecipeRegister.SlashBladeUpPack defaultConfigSlashBladeUpPack() {
                SlashBladePack starBlade_2 = starBlade_2_slashBladeRegister.getSlashBladePack();
                starBlade_2.slashBladeState.setKillCount(2000);
                starBlade_2.slashBladeState.setRefine(275);
                starBlade_2.ise.getPack(overloadSE).setLevel(3);
                starBlade_2.ise.getPack(stormSE).setLevel(2);
                starBlade_2.ise.getPack(stormVariantSE).setLevel(2);
                EnchantmentHelper.setEnchantments(MapUtil.of(
                        Enchantments.PROTECTION, 4,
                        Enchantments.EFFICIENCY, 3,
                        Enchantments.INFINITY, 1), starBlade_2.itemStack);

                return new SlashBladeUpRecipeRegister.SlashBladeUpPack(
                        ListUtil.of(
                                "ABC",
                                "DVD",
                                "CBA"
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemSE(overloadSE, 2, true),
                                "B", new IRecipeInItemPack.OfItemSE(stormSE, 2, true),
                                "C", new IRecipeInItemPack.OfItemSE(stormVariantSE, 2, true),
                                "D", new IRecipeInItemPack.OfEntity(EntityType.WITCH),
                                "V", new IRecipeInItemPack.OfSlashBlade(starBlade_2.itemStack)
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(starBlade_3_slashBladeRegister)
                );
            }
        }


    }

    @VoluntarilyRegister
    public static class StarBlade_4_SlashBladeRegister extends StarBladeSlashBladeRegister {
        @VoluntarilyAssignment
        protected InfiniteDimensionalChoppingSA infiniteDimensionalChoppingSA;


        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(7f);
            slashBladePack.iSlashBladeStateSupplement.setAttackDistance(2f);
            slashBladePack.setSA(infiniteDimensionalChoppingSA);
        }

        @Override
        protected int getState() {
            return 4;
        }

        @VoluntarilyRegister
        public static class StarBlade_4_SlashBladeRecipeRegister extends SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected StarBlade_3_SlashBladeRegister starBlade_3_slashBladeRegister;

            @VoluntarilyAssignment
            protected StarBlade_4_SlashBladeRegister starBlade_4_slashBladeRegister;

            @VoluntarilyAssignment
            protected BlackSlashBladeRegister blackSlashBladeRegister;

            @VoluntarilyAssignment
            protected OverloadSE overloadSE;

            @VoluntarilyAssignment
            protected StormSE stormSE;

            @VoluntarilyAssignment
            protected StormVariantSE stormVariantSE;

            @VoluntarilyAssignment
            protected CoolMintSlashBladeRegister coolMintSlashBladeRegister;

            @Override
            protected SlashBladeUpRecipeRegister.SlashBladeUpPack defaultConfigSlashBladeUpPack() {

                SlashBladePack blackSlash = blackSlashBladeRegister.getSlashBladePack();
                blackSlash.slashBladeState.setKillCount(1000);
                blackSlash.slashBladeState.setRefine(125);
                blackSlash.ise.getPack(overloadSE).setLevel(3);
                blackSlash.ise.getPack(stormSE).setLevel(3);
                blackSlash.ise.getPack(stormVariantSE).setLevel(3);

                SlashBladePack coolMintSlashBlade = coolMintSlashBladeRegister.getSlashBladePack();
                coolMintSlashBlade.slashBladeState.setKillCount(2000);
                coolMintSlashBlade.slashBladeState.setRefine(250);
                coolMintSlashBlade.ise.getPack(overloadSE).setLevel(4);
                coolMintSlashBlade.ise.getPack(stormSE).setLevel(4);
                coolMintSlashBlade.ise.getPack(stormVariantSE).setLevel(4);

                SlashBladePack starBlade_3 = starBlade_3_slashBladeRegister.getSlashBladePack();
                starBlade_3.slashBladeState.setKillCount(6000);
                starBlade_3.slashBladeState.setRefine(650);
                starBlade_3.ise.getPack(overloadSE).setLevel(5);
                starBlade_3.ise.getPack(stormSE).setLevel(5);
                starBlade_3.ise.getPack(stormVariantSE).setLevel(5);
                EnchantmentHelper.setEnchantments(MapUtil.of(
                        Enchantments.PROTECTION, 4,
                        Enchantments.EFFICIENCY, 3,
                        Enchantments.INFINITY, 1), starBlade_3.itemStack);

                return new SlashBladeUpRecipeRegister.SlashBladeUpPack(
                        ListUtil.of(
                                "  A",
                                " A ",
                                "VB "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfSlashBlade(blackSlash.itemStack),
                                "B", new IRecipeInItemPack.OfSlashBlade(coolMintSlashBlade.itemStack),
                                "V", new IRecipeInItemPack.OfSlashBlade(starBlade_3.itemStack)
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(starBlade_4_slashBladeRegister)
                );
            }
        }

    }
}
