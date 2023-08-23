package com.til.recasting.common.register.slash_blade.instance.special;

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
import com.til.recasting.common.entity.PlanetEntity;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.entity_type.PlanetEntityTypeRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.instance.StarBladeSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.instance.DivinitySE;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import java.awt.*;

@VoluntarilyRegister
public class HTOD_SlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected HTOD_SlashBladeSA htod_slashBladeSA;

    protected ResourceLocation saModel;

    @Override
    protected void init() {
        super.init();
        summondSwordModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SUMMOND_SWORD, getName().getPath(), "model.obj"));
        saModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SPECIAL, getName().getPath(), "model.obj"));
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
        slashBladePack.getSlashBladeState().setColorCode(new Color(246, 67, 67, 255).getRGB());
        slashBladePack.getSlashBladeStateSupplement().setDurable(26);
        slashBladePack.setSA(htod_slashBladeSA);
    }


    @VoluntarilyRegister
    public static class HTOD_SlashBladeSA extends SA_Register {
        @VoluntarilyAssignment
        protected HTOD_SlashBladeRegister htod_slashBladeRegister;

        @VoluntarilyAssignment
        protected PlanetEntityTypeRegister planetEntityTypeRegister;

        @VoluntarilyAssignment
        protected DefaultEntityPredicateRegister defaultEntityPredicateRegister;

        @ConfigField
        protected float attack;

        @ConfigField
        protected float explosionAttack;

        @ConfigField
        protected float size;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            PlanetEntity driveEntity = new PlanetEntity(
                    planetEntityTypeRegister.getEntityType(),
                    slashBladeEntityPack.getEntity().world,
                    slashBladeEntityPack.getEntity()
            );
            driveEntity.setModel(htod_slashBladeRegister.saModel);
            //driveEntity.setModel(SummondSwordEntity.DEFAULT_MODEL_NAME);
            driveEntity.setTexture(SummondSwordEntity.DEFAULT_TEXTURE_NAME);
            driveEntity.setDamage(attack);
            driveEntity.setExplosionDamage(explosionAttack);
            driveEntity.setRoll(slashBladeEntityPack.getEntity().getRNG().nextInt(360));
            driveEntity.setSize(size / 4f);
            driveEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
            driveEntity.setPosition(
                    slashBladeEntityPack.getEntity().getPosX(),
                    slashBladeEntityPack.getEntity().getPosY() + 45,
                    slashBladeEntityPack.getEntity().getPosZ()
            );
            driveEntity.setSeep(1);
            driveEntity.lookAt(slashBladeEntityPack.getAttackPos(), false);
            slashBladeEntityPack.getEntity().world.addEntity(driveEntity);
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attack = 1.35f;
            explosionAttack = 9f;
            size = 12;
        }
    }

    @VoluntarilyRegister
    public static class HTOD_SlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {


        @VoluntarilyAssignment
        protected StarBladeSlashBladeRegister.StarBlade_4_SlashBladeRegister.StarBlade_4_LambdaSlashBladeRegister starBlade_4_lambdaSlashBladeRegister;

        @VoluntarilyAssignment
        protected HTOD_SlashBladeRegister htod_slashBladeRegister;

        @VoluntarilyAssignment
        protected DivinitySE divinitySE;


        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {

            SlashBladePack starBlade_4_lambda = starBlade_4_lambdaSlashBladeRegister.getSlashBladePack();
            starBlade_4_lambda.getSlashBladeState().setKillCount(10000);
            starBlade_4_lambda.getSlashBladeState().setRefine(3000);

            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "A A",
                            "AVA",
                            "A A"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfItemSE(divinitySE, 7.5f),
                            "V", new IRecipeInItemPack.OfSlashBlade(starBlade_4_lambda.getItemStack())),
                    "V",
                    new IResultPack.OfSlashBladeRegister(htod_slashBladeRegister)
            );
        }
    }

    @VoluntarilyRegister
    public static class HTOD_LambdaSlashBladeRegister extends HTOD_SlashBladeRegister {
        @VoluntarilyAssignment
        protected HTOD_SlashBladeRegister htod_slashBladeRegister;

        @VoluntarilyAssignment
        public HTOD_LambdaSlashBladeSA hotd_lambdaSlashBladeSA;

        @Override
        protected void init() {
            super.init();
            model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, htod_slashBladeRegister.getName().getPath(), "model.obj"));
            texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, htod_slashBladeRegister.getName().getPath(), "texture.png"));

            summondSwordModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SUMMOND_SWORD, htod_slashBladeRegister.getName().getPath(), "model.obj"));
        }

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.setSA(hotd_lambdaSlashBladeSA);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(8f);
        }

        @VoluntarilyRegister
        public static class HTOD_LambdaSlashBladeSA extends HTOD_SlashBladeSA {
            @ConfigField
            protected int append;

            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                super.trigger(slashBladeEntityPack);
                for (int i = 0; i < append; i++) {
                    PlanetEntity driveEntity = new PlanetEntity(
                            planetEntityTypeRegister.getEntityType(),
                            slashBladeEntityPack.getEntity().world,
                            slashBladeEntityPack.getEntity()
                    );
                    driveEntity.setModel(htod_slashBladeRegister.saModel);
                    //driveEntity.setModel(SummondSwordEntity.DEFAULT_MODEL_NAME);
                    driveEntity.setTexture(SummondSwordEntity.DEFAULT_TEXTURE_NAME);
                    driveEntity.setDamage(attack);
                    driveEntity.setExplosionDamage(explosionAttack);
                    driveEntity.setRoll(slashBladeEntityPack.getEntity().getRNG().nextInt(360));
                    driveEntity.setSize(size / 4f);
                    driveEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                    Vector3d pos = slashBladeEntityPack.getEntity().getPositionVec()
                            .add(0, 45, 0)
                            .add(RandomUtil.nextVector3dInCircles(slashBladeEntityPack.getEntity().getRNG(), 25));
                    driveEntity.setPosition(
                            pos.getX(),
                            pos.getY(),
                            pos.getZ()
                    );
                    driveEntity.setSeep(1);
                    driveEntity.lookAt(slashBladeEntityPack.getAttackPos(), false);
                    slashBladeEntityPack.getEntity().world.addEntity(driveEntity);
                }
            }

            @Override
            public void defaultConfig() {
                super.defaultConfig();
                append = 2;
            }
        }

        @VoluntarilyRegister
        public static class HTOD_LambdaSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

            @VoluntarilyAssignment
            protected HTOD_LambdaSlashBladeRegister htod_lambdaSlashBladeRegister;

            @VoluntarilyAssignment
            protected HTOD_SlashBladeRegister htod_slashBladeRegister;
            @VoluntarilyAssignment
            protected DivinitySE.DivinityLambdaSE divinityLambdaSE;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack htod_slashBlade = htod_slashBladeRegister.getSlashBladePack();
                htod_slashBlade.getSlashBladeState().setKillCount(20000);
                htod_slashBlade.getSlashBladeState().setRefine(3000);

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                "A A",
                                "AVA",
                                "A A"
                        ),
                        MapUtil.of(
                                "V", new IRecipeInItemPack.OfSlashBlade(htod_slashBlade.getItemStack()),
                                "A", new IRecipeInItemPack.OfItemSE(divinityLambdaSE, 5f)
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(htod_lambdaSlashBladeRegister)
                );
            }
        }


    }
}
