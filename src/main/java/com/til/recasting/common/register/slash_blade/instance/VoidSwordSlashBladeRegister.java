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
import com.til.recasting.common.register.slash_blade.sa.instance.DefaultSA;
import com.til.recasting.common.register.slash_blade.sa.instance.MultipleDimensionalChoppingSA;
import com.til.recasting.common.register.world.item.SoulItemRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class VoidSwordSlashBladeRegister extends SlashBladeRegister {

    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(VoidSwordSlashBladeRegister.class), "model.obj"));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(VoidSwordSlashBladeRegister.class), "state" + getState(), "texture.png"));
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.iSlashBladeStateSupplement.setAttackDistance(1.5f);
    }

    protected abstract int getState();

    @VoluntarilyRegister
    public static class VoidSword_1_SlashBladeRegister extends VoidSwordSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(8f);
            slashBladePack.slashBladeState.setColorCode(0xD25968);
        }


        @Override
        protected int getState() {
            return 1;
        }

        @VoluntarilyRegister
        public static class VoidSword_1_SlashBladeRecipeRegister extends SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected VoidSword_1_SlashBladeRegister voidSword_1_slashBladeRegister;

            @VoluntarilyAssignment
            protected BlackSlashBladeRegister blackSlashBladeRegister;

            @VoluntarilyAssignment
            protected SoulItemRegister.SoulCubeItemRegister soulCubeItemRegister;

            @VoluntarilyAssignment
            protected BlueCloudSlashBladeRegister blueCloudSlashBladeRegister;

            @VoluntarilyAssignment
            protected DefaultSA defaultSA;

            @Override
            protected SlashBladeUpRecipeRegister.SlashBladeUpPack defaultConfigSlashBladeUpPack() {
                SlashBladePack blackSlash = blackSlashBladeRegister.getSlashBladePack();
                blackSlash.slashBladeState.setKillCount(1200);
                blackSlash.slashBladeState.setRefine(145);

                SlashBladePack blueCloudSlashBlade = blueCloudSlashBladeRegister.getSlashBladePack();
                blueCloudSlashBlade.slashBladeState.setKillCount(4500);
                blueCloudSlashBlade.slashBladeState.setRefine(350);

                return new SlashBladeUpRecipeRegister.SlashBladeUpPack(
                        ListUtil.of(
                                "ACA",
                                "BVB",
                                "ACA"
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem())),
                                "B", new IRecipeInItemPack.OfSlashBlade(blackSlash.itemStack),
                                "C", new IRecipeInItemPack.OfItemSA(defaultSA),
                                "V", new IRecipeInItemPack.OfSlashBlade(blueCloudSlashBlade.itemStack)),
                        "V",
                        new IResultPack.OfSlashBladeRegister(voidSword_1_slashBladeRegister)
                );
            }
        }
    }

    @VoluntarilyRegister
    public static class VoidSword_2_SlashBladeRegister extends VoidSwordSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(7f);
            slashBladePack.slashBladeState.setColorCode(0xD25968);
        }

        @Override
        protected int getState() {
            return 2;
        }

        @VoluntarilyRegister
        public static class VoidSword_2_SlashBladeRecipeRegister extends SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected VoidSword_1_SlashBladeRegister voidSword_1_slashBladeRegister;

            @VoluntarilyAssignment
            protected VoidSword_2_SlashBladeRegister voidSword_2_slashBladeRegister;

            @VoluntarilyAssignment
            protected SoulItemRegister.SoulCubeItemRegister soulCubeItemRegister;

            @VoluntarilyAssignment
            protected BlueCloudSlashBladeRegister blueCloudSlashBladeRegister;

            @VoluntarilyAssignment
            protected MultipleDimensionalChoppingSA multipleDimensionalChoppingSA;

            @Override
            protected SlashBladeUpRecipeRegister.SlashBladeUpPack defaultConfigSlashBladeUpPack() {
                SlashBladePack blueCloudSlashBlade = blueCloudSlashBladeRegister.getSlashBladePack();
                blueCloudSlashBlade.slashBladeState.setKillCount(4200);
                blueCloudSlashBlade.slashBladeState.setRefine(245);

                SlashBladePack voidSword_1_slashBlade = voidSword_1_slashBladeRegister.getSlashBladePack();
                voidSword_1_slashBlade.slashBladeState.setKillCount(10000);
                voidSword_1_slashBlade.slashBladeState.setRefine(1350);

                return new SlashBladeUpRecipeRegister.SlashBladeUpPack(
                        ListUtil.of(
                                "ACA",
                                "BVB",
                                "ACA"
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem())),
                                "B", new IRecipeInItemPack.OfSlashBlade(blueCloudSlashBlade.itemStack),
                                "C", new IRecipeInItemPack.OfItemSA(multipleDimensionalChoppingSA),
                                "V", new IRecipeInItemPack.OfSlashBlade(voidSword_1_slashBlade.itemStack)),
                        "V",
                        new IResultPack.OfSlashBladeRegister(voidSword_2_slashBladeRegister)
                );
            }
        }


    }

    @VoluntarilyRegister
    public static class VoidSword_3_SlashBladeRegister extends VoidSwordSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setBaseAttackModifier(8f);
            slashBladePack.slashBladeState.setColorCode(0xD25968);
        }

        @Override
        protected int getState() {
            return 3;
        }

        @VoluntarilyRegister
        public static class VoidSword_3_SlashBladeRecipeRegister extends SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected VoidSword_2_SlashBladeRegister voidSword_2_slashBladeRegister;

            @VoluntarilyAssignment
            protected VoidSword_3_SlashBladeRegister voidSword_3_slashBladeRegister;


            @Override
            protected SlashBladeUpRecipeRegister.SlashBladeUpPack defaultConfigSlashBladeUpPack() {
                SlashBladePack voidSword_2_slashBlade = voidSword_2_slashBladeRegister.getSlashBladePack();
                voidSword_2_slashBlade.slashBladeState.setKillCount(30000);
                voidSword_2_slashBlade.slashBladeState.setRefine(2500);


                return new SlashBladeUpRecipeRegister.SlashBladeUpPack(
                        ListUtil.of(
                                "AAA",
                                "AVA",
                                "AAA"
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfEntity(EntityType.WITHER),
                                "V", new IRecipeInItemPack.OfSlashBlade(voidSword_2_slashBlade.itemStack)),
                        "V",
                        new IResultPack.OfSlashBladeRegister(voidSword_3_slashBladeRegister)
                );

            }
        }
    }

}
