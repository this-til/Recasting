package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.register.back_type.JudgementCutBackTypeRegister;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.sa.instance.DefaultSA;
import com.til.recasting.common.register.util.JudgementCutManage;
import com.til.recasting.common.register.world.item.SoulItemRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import java.awt.*;
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
        slashBladePack.getSlashBladeStateSupplement().setAttackDistance(1.5f);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(0xFF001E));
    }

    protected abstract int getState();

    public static abstract class VoidSword_SA extends SA_Register {

        @VoluntarilyAssignment
        protected JudgementCutBackTypeRegister.JudgementCutTickBackTypeRegister judgementCutTickBackTypeRegister;

        @VoluntarilyAssignment
        protected DefaultEntityPredicateRegister defaultEntityPredicateRegister;

        @ConfigField
        protected float attack;

        @ConfigField
        protected int life;

        @ConfigField
        protected float size;

        @ConfigField
        protected float range;

        @ConfigField
        protected float power;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            JudgementCutManage.doJudgementCut(slashBladeEntityPack.getEntity(), attack, life, null, null, judgementCutEntity -> {
                judgementCutEntity.setSize(size);
                judgementCutEntity.getBackRunPack().addRunBack(judgementCutTickBackTypeRegister, judgementCutEntity1 -> {
                    List<Entity> entityList = judgementCutEntity1.world.getEntitiesInAABBexcluding(judgementCutEntity, new Pos(judgementCutEntity1.getPositionVec()).axisAlignedBB(range), entity -> defaultEntityPredicateRegister.canTarget(entity, slashBladeEntityPack.getEntity()));
                    for (Entity entity : entityList) {
                        Vector3d direction = judgementCutEntity1.getPositionVec().subtract(entity.getPositionVec());
                        double length = direction.length();
                        if (length > range) {
                            continue;
                        }
                        double lengthRatio = length / range;
                        double strength = (1 - lengthRatio) * (1 - lengthRatio);
                        double _power = power * range;

                        Vector3d move = entity.getMotion();
                        entity.setMotion(move.add(
                                (direction.getX() / length) * strength * _power,
                                (direction.getY() / length) * strength * _power,
                                (direction.getZ() / length) * strength * _power));
                    }
                });
            });

        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attack = 0.05f;
            life = 20;
            size = 4;
            range = 32;
            power = 0.05f;
        }
    }

    @VoluntarilyRegister
    public static class VoidSword_1_SlashBladeRegister extends VoidSwordSlashBladeRegister {

        @VoluntarilyAssignment
        protected VoidSword_1_SA voidSwordSlashBlade_1_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(8f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(12);
            slashBladePack.setSA(voidSwordSlashBlade_1_sa);
        }


        @Override
        protected int getState() {
            return 1;
        }

        @VoluntarilyRegister
        public static class VoidSword_1_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
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
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack blackSlash = blackSlashBladeRegister.getSlashBladePack();
                blackSlash.getSlashBladeState().setKillCount(1200);
                blackSlash.getSlashBladeState().setRefine(145);

                SlashBladePack blueCloudSlashBlade = blueCloudSlashBladeRegister.getSlashBladePack();
                blueCloudSlashBlade.getSlashBladeState().setKillCount(4500);
                blueCloudSlashBlade.getSlashBladeState().setRefine(350);

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(ListUtil.of("ACA", "BVB", "ACA"), MapUtil.of("A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem())), "B", new IRecipeInItemPack.OfSlashBlade(blackSlash.getItemStack()), "C", new IRecipeInItemPack.OfItemSA(defaultSA), "V", new IRecipeInItemPack.OfSlashBlade(blueCloudSlashBlade.getItemStack())), "V", new IResultPack.OfSlashBladeRegister(voidSword_1_slashBladeRegister));
            }
        }

        @VoluntarilyRegister
        public static class VoidSword_1_SA extends VoidSword_SA {
        }
    }

    @VoluntarilyRegister
    public static class VoidSword_2_SlashBladeRegister extends VoidSwordSlashBladeRegister {

        @VoluntarilyAssignment
        protected VoidSword_2_SA voidSwordSlashBlade_2_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(48);
            slashBladePack.setSA(voidSwordSlashBlade_2_sa);
        }

        @Override
        protected int getState() {
            return 2;
        }

        @VoluntarilyRegister
        public static class VoidSword_2_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected VoidSword_1_SlashBladeRegister voidSword_1_slashBladeRegister;

            @VoluntarilyAssignment
            protected VoidSword_2_SlashBladeRegister voidSword_2_slashBladeRegister;

            @VoluntarilyAssignment
            protected SoulItemRegister.SoulCubeItemRegister soulCubeItemRegister;

            @VoluntarilyAssignment
            protected BlueCloudSlashBladeRegister blueCloudSlashBladeRegister;

            @VoluntarilyAssignment
            protected UmbrellaSlashBladeRegister.UmbrellaSlash_1_BladeRegister.MultipleDimensionalChoppingSA multipleDimensionalChoppingSA;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack blueCloudSlashBlade = blueCloudSlashBladeRegister.getSlashBladePack();
                blueCloudSlashBlade.getSlashBladeState().setKillCount(4200);
                blueCloudSlashBlade.getSlashBladeState().setRefine(245);

                SlashBladePack voidSword_1_slashBlade = voidSword_1_slashBladeRegister.getSlashBladePack();
                voidSword_1_slashBlade.getSlashBladeState().setKillCount(10000);
                voidSword_1_slashBlade.getSlashBladeState().setRefine(1350);

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                "ACA",
                                "BVB",
                                "ACA"
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem())),
                                "B", new IRecipeInItemPack.OfSlashBlade(blueCloudSlashBlade.getItemStack()),
                                "C", new IRecipeInItemPack.OfItemSA(multipleDimensionalChoppingSA),
                                "V", new IRecipeInItemPack.OfSlashBlade(voidSword_1_slashBlade.getItemStack())),
                        "V", new IResultPack.OfSlashBladeRegister(voidSword_2_slashBladeRegister));
            }
        }

        @VoluntarilyRegister
        public static class VoidSword_2_SA extends VoidSword_SA {
            @Override
            public void defaultConfig() {
                super.defaultConfig();
                life = 40;
                size = 6;
                range = 48;
                power = 0.1f;
            }
        }

    }

    @VoluntarilyRegister
    public static class VoidSword_3_SlashBladeRegister extends VoidSwordSlashBladeRegister {
        @VoluntarilyAssignment
        protected VoidSword_3_SA voidSwordSlashBlade_3_sa;


        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(8f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(1024);
            slashBladePack.setSA(voidSwordSlashBlade_3_sa);
        }

        @Override
        protected int getState() {
            return 3;
        }

        @VoluntarilyRegister
        public static class VoidSword_3_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected VoidSword_2_SlashBladeRegister voidSword_2_slashBladeRegister;

            @VoluntarilyAssignment
            protected VoidSword_3_SlashBladeRegister voidSword_3_slashBladeRegister;


            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack voidSword_2_slashBlade = voidSword_2_slashBladeRegister.getSlashBladePack();
                voidSword_2_slashBlade.getSlashBladeState().setKillCount(30000);
                voidSword_2_slashBlade.getSlashBladeState().setRefine(2500);


                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(ListUtil.of("AAA", "AVA", "AAA"), MapUtil.of("A", new IRecipeInItemPack.OfEntity(EntityType.WITHER), "V", new IRecipeInItemPack.OfSlashBlade(voidSword_2_slashBlade.getItemStack())), "V", new IResultPack.OfSlashBladeRegister(voidSword_3_slashBladeRegister));

            }
        }

        @VoluntarilyRegister
        public static class VoidSword_3_SA extends VoidSword_SA {
            @Override
            public void defaultConfig() {
                super.defaultConfig();
                life = 80;
                size = 8;
                range = 64;
                power = 0.15f;
            }
        }
    }

}
