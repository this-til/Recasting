package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.RandomUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.instance.BacktrackSE;
import com.til.recasting.common.register.slash_blade.se.instance.DivinitySE;
import com.til.recasting.common.register.slash_blade.se.instance.GrowSE;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import java.awt.*;
import java.util.Random;

@VoluntarilyRegister
public class DragonSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected SwordRainSA swordRainSA;

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(6f);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(255, 255, 0));
        slashBladePack.getSlashBladeStateSupplement().setDurable(22);
        slashBladePack.setSA(swordRainSA);
    }

    @VoluntarilyRegister
    public static class DragonSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

        @VoluntarilyAssignment
        protected DragonSlashBladeRegister dragonSlashBladeRegister;

        @VoluntarilyAssignment
        protected DragonScaleSlashBladeRegister dragonScaleSlashBladeRegister;

        @VoluntarilyAssignment
        protected BlackSlashBladeRegister blackSlashBladeRegister;

        @VoluntarilyAssignment
        protected BacktrackSE backtrackSE;

        @VoluntarilyAssignment
        protected GrowSE growSE;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {

            SlashBladePack dragonScaleSlashBlade = dragonScaleSlashBladeRegister.getSlashBladePack();
            dragonScaleSlashBlade.getSlashBladeState().setKillCount(3750);
            dragonScaleSlashBlade.getSlashBladeState().setRefine(225);
            dragonScaleSlashBlade.getIse().getPack(backtrackSE).setLevel(2);
            dragonScaleSlashBlade.getIse().getPack(growSE).setLevel(2);

            EnchantmentHelper.setEnchantments(
                    MapUtil.of(
                            Enchantments.PROTECTION, 5,
                            Enchantments.SHARPNESS, 5,
                            Enchantments.INFINITY, 1,
                            Enchantments.LOOTING, 3
                    ), dragonScaleSlashBlade.getItemStack());

            SlashBladePack blackSlashBlade = blackSlashBladeRegister.getSlashBladePack();
            blackSlashBlade.getSlashBladeState().setKillCount(1000);
            blackSlashBlade.getSlashBladeState().setRefine(100);
            blackSlashBlade.getIse().getPack(backtrackSE).setLevel(1);
            blackSlashBlade.getIse().getPack(growSE).setLevel(1);

            EnchantmentHelper.setEnchantments(
                    MapUtil.of(
                            Enchantments.EFFICIENCY, 4,
                            Enchantments.LOYALTY, 1
                    ), blackSlashBlade.getItemStack());

            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "ECA",
                            "DVD",
                            "BCF"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.DRAGON_EGG)),
                            "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.DRAGON_HEAD)),
                            "C", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.DRAGON_BREATH)),
                            "D", new IRecipeInItemPack.OfSlashBlade(blackSlashBlade.getItemStack()),
                            "E", new IRecipeInItemPack.OfItemSE(backtrackSE, 2),
                            "F", new IRecipeInItemPack.OfItemSE(growSE, 2),
                            "V", new IRecipeInItemPack.OfSlashBlade(dragonScaleSlashBlade.getItemStack())
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(dragonSlashBladeRegister)
            );
        }
    }

    @VoluntarilyRegister
    public static class DragonLambdaSlashBladeRegister extends SlashBladeRegister {
        @VoluntarilyAssignment
        protected SwordRainSA.VertexSwordRainSA swordRainSA;

        @VoluntarilyAssignment
        public DragonSlashBladeRegister dragonSlashBladeRegister;

        @Override
        protected void init() {
            super.init();
            model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, dragonSlashBladeRegister.getName().getPath(), "model.obj"));
            texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, dragonSlashBladeRegister.getName().getPath(), "texture.png"));
        }

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(6f);
            slashBladePack.getSlashBladeState().setEffectColor(new Color(255, 255, 0));
            slashBladePack.getSlashBladeStateSupplement().setDurable(22);
            slashBladePack.setSA(swordRainSA);
        }


        @VoluntarilyRegister
        public static class DragonLambdaRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected DragonSlashBladeRegister dragonSlashBladeRegister;

            @VoluntarilyAssignment
            protected DragonLambdaSlashBladeRegister dragonLambdaSlashBladeRegister;

            @VoluntarilyAssignment
            protected DivinitySE divinitySE;

            @VoluntarilyAssignment
            protected StarBladeSlashBladeRegister.StarBlade_4_SlashBladeRegister.StarBlade_4_SA starBlade_4_sa;

            @VoluntarilyAssignment
            protected ColorWingSlashBladeRegister.HeavenTwelveHitSA heavenTwelveHitSA;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack dragonSlashBlade = dragonSlashBladeRegister.getSlashBladePack();
                dragonSlashBlade.getSlashBladeState().setKillCount(10000);
                dragonSlashBlade.getSlashBladeState().setRefine(500);

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                " CA",
                                " V ",
                                "AB "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemSE(divinitySE, 5),
                                "B", new IRecipeInItemPack.OfItemSA(starBlade_4_sa),
                                "C", new IRecipeInItemPack.OfItemSA(heavenTwelveHitSA),
                                "V", new IRecipeInItemPack.OfSlashBlade(dragonSlashBlade.getItemStack())
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(dragonLambdaSlashBladeRegister)
                );
            }
        }
    }

    /***
     * 剑雨
     */
    @VoluntarilyRegister
    public static class SwordRainSA extends SA_Register {

        @VoluntarilyAssignment
        protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

        @ConfigField
        protected float attack;

        @ConfigField
        protected int attackNumber;

        @ConfigField
        protected float range;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            Vector3d pos = slashBladeEntityPack.getEntity().getPositionVec().add(0, range / 2, 0);
            Random random = slashBladeEntityPack.getEntity().getRNG();
            for (int i = 0; i < attackNumber; i++) {
                SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
                slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
                Vector3d _pos = pos.add(RandomUtil.nextVector3dInCircles(random, range));
                summondSwordEntity.setPosition(_pos.getX(), _pos.getY(), _pos.getZ());
                summondSwordEntity.setSize(0.6f);
                summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                summondSwordEntity.setDamage(attack);
                summondSwordEntity.setStartDelay(random.nextInt(60));
                slashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
            }
        }


        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attack = 0.05f;
            attackNumber = 150;
            range = 5;
        }

        @VoluntarilyRegister
        public static class VertexSwordRainSA extends SwordRainSA {
            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                Vector3d pos = slashBladeEntityPack.getEntity().getPositionVec().add(0, range / 2, 0);
                Random random = slashBladeEntityPack.getEntity().getRNG();
                Vector3d attackPos = slashBladeEntityPack.getAttackPos();
                for (int i = 0; i < attackNumber; i++) {
                    SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
                    slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
                    Vector3d _pos = pos.add(RandomUtil.nextVector3dInCircles(random, range));
                    summondSwordEntity.setPosition(_pos.getX(), _pos.getY(), _pos.getZ());
                    summondSwordEntity.setSize(0.6f);
                    summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                    summondSwordEntity.setDamage(attack);
                    summondSwordEntity.setStartDelay(random.nextInt(60));
                    summondSwordEntity.lookAt(attackPos, false);
                    slashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
                }
            }
        }
    }
}
