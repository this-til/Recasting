package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.DriveEntity;
import com.til.recasting.common.register.entity_type.DriveEntityTypeRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.instance.DivinitySE;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Map;

@VoluntarilyRegister
public class XuanYuanSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    public XuanYuanSlashBlade_SA xuanYuanSlashBlade_sa;

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(255, 255, 0));
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
        slashBladePack.setSA(xuanYuanSlashBlade_sa);
    }


    @VoluntarilyRegister
    public static class XuanYuanSlashBlade_SA extends SA_Register {
        @ConfigField
        protected float attack;

        @ConfigField
        protected float attackNumber;

        @ConfigField
        protected int life;

        @ConfigField
        protected float range;


        @VoluntarilyAssignment
        protected DriveEntityTypeRegister driveEntityTypeRegister;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            for (int i = 0; i < attackNumber; i++) {
                DriveEntity driveEntity = new DriveEntity(
                        driveEntityTypeRegister.getEntityType(),
                        slashBladeEntityPack.getEntity().world,
                        slashBladeEntityPack.getEntity()
                );
                driveEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                driveEntity.setSize((slashBladeEntityPack.getEntity().getRNG().nextFloat() * range)
                                    * slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().getAttackDistance());
                driveEntity.setDamage(attack);
                driveEntity.setMaxLifeTime(life);
                driveEntity.setRoll(slashBladeEntityPack.getEntity().getRNG().nextInt(360));
                driveEntity.setSeep(0.45f);
                slashBladeEntityPack.getEntity().world.addEntity(driveEntity);
            }
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attack = 0.45f;
            attackNumber = 8;
            life = 80;
            range = 1;
        }

    }

    @VoluntarilyRegister
    public static class XuanYuanSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected XuanYuanSlashBladeRegister xuanYuanSlashBladeRegister;

        @VoluntarilyAssignment
        protected DharmaStickSlashBladeRegister dharmaStickSlashBladeRegister;

        @VoluntarilyAssignment
        protected FluorescenceSlashBladeRegister.Fluorescence_4_SlashBladeRegister.Fluorescence_4_SA fluorescence_4_sa;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            SlashBladePack dharmaStickSlashBlade = dharmaStickSlashBladeRegister.getSlashBladePack();
            dharmaStickSlashBlade.getSlashBladeState().setRefine(250);
            dharmaStickSlashBlade.getSlashBladeState().setKillCount(3600);

            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "  A",
                            " A ",
                            "V  "
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfItemSA(fluorescence_4_sa),
                            "V", new IRecipeInItemPack.OfSlashBlade(dharmaStickSlashBlade)
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(xuanYuanSlashBladeRegister)
            );
        }
    }

    @VoluntarilyRegister
    public static class XuanYuanLambdaSlashBladeRegister extends XuanYuanSlashBladeRegister {

        @VoluntarilyAssignment
        protected XuanYuanSlashBladeRegister xuanYuanSlashBladeRegister;

        @VoluntarilyAssignment
        protected XuanYuanLambdaSlashBlade_SA xuanYuanLambda_sa;

        @Override
        protected void init() {
            super.init();
            model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, xuanYuanSlashBladeRegister.getName().getPath(), StringFinal.MODEL));
            texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, xuanYuanSlashBladeRegister.getName().getPath(), StringFinal.TEXTURE));
        }

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.setSA(xuanYuanLambda_sa);
        }

        @VoluntarilyRegister
        public static class XuanYuanLambdaSlashBlade_SA extends XuanYuanSlashBlade_SA {
            @Override
            public void defaultConfig() {
                super.defaultConfig();
                attackNumber = 16;
                attack = 0.5f;
            }
        }

        @VoluntarilyRegister
        public static class XuanYuanLambdaSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected DivinitySE divinitySE;

            @VoluntarilyAssignment
            protected XuanYuanSlashBladeRegister xuanYuanSlashBladeRegister;

            @VoluntarilyAssignment
            protected XuanYuanLambdaSlashBladeRegister xuanYuanLambdaSlashBladeRegister;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
                SlashBladePack xuanYuanLambdaSlashBlade = xuanYuanLambdaSlashBladeRegister.getSlashBladePack();
                xuanYuanLambdaSlashBlade.getSlashBladeState().setKillCount(1000);
                xuanYuanLambdaSlashBlade.getSlashBladeState().setRefine(450);

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                "  A",
                                " V ",
                                "A  "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemSE(divinitySE, 2),
                                "V", new IRecipeInItemPack.OfSlashBlade(xuanYuanLambdaSlashBlade)
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(xuanYuanLambdaSlashBladeRegister)
                );
            }
        }
    }


}
