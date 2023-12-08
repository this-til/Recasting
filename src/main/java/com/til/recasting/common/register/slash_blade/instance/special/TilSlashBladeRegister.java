package com.til.recasting.common.register.slash_blade.instance.special;

import com.til.glowing_fire_glow.common.capability.time_run.TimerCell;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.StellarRotationEntity;
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.register.back_type.JudgementCutBackTypeRegister;
import com.til.recasting.common.register.capability.StarBlinkSE_LayerCapabilityRegister;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.entity_type.StellarRotationEntityTypeRegister;
import com.til.recasting.common.register.particle.AttackParticleRegister;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.instance.VoidSwordSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.slash_blade.se.instance.DivinitySE;
import com.til.recasting.common.register.util.AttackManager;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.List;

@VoluntarilyRegister
public class TilSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected StarBlinkSE starBlinkSE;

    @VoluntarilyAssignment
    protected StellarRotation_SA tilSlashBlade_sa;

    protected ResourceLocation seModel;

    protected ResourceLocation seAttackParticle;

    @Override
    protected void init() {
        super.init();
        seModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SPECIAL, getName().getPath(), StringFinal.MODEL));
        seAttackParticle = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.PARTICLE, getName().getPath(), StringFinal.TEXTURE));
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
        slashBladePack.getSlashBladeState().setColorCode(new Color(210, 118, 246).getRGB());
        slashBladePack.getSlashBladeStateSupplement().setDurable(26);
        slashBladePack.getIse().getPack(starBlinkSE).setLevel(1);
        slashBladePack.setSA(tilSlashBlade_sa);
    }


    public ResourceLocation getSeModel() {
        return seModel;
    }

    @VoluntarilyRegister
    public static class StellarRotation_SA extends SA_Register {


        @VoluntarilyAssignment
        protected JudgementCutBackTypeRegister.JudgementCutAttackBackTypeRegister judgementCutAttackBackTypeRegister;

        @VoluntarilyAssignment
        protected DefaultEntityPredicateRegister defaultEntityPredicateRegister;

        @VoluntarilyAssignment
        public StellarRotationEntityTypeRegister stellarRotationEntityRegister;


        @ConfigField
        protected float attack;

        @ConfigField
        protected float moveRange;

        @ConfigField
        protected float size;

        @ConfigField
        protected int attackInterval;

        @ConfigField
        protected int life;


        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            Vector3d attackPos = slashBladeEntityPack.getAttackPos();
           /* BlockState blockState = slashBladeEntityPack.getEntity().world.getBlockState(new BlockPos(attackPos).add(0, -0.5, 0));
            if (!blockState.equals(Blocks.AIR.getDefaultState())) {
                attackPos = attackPos.add(0, size, 0);
            }*/

            List<Entity> entityList = slashBladeEntityPack.getEntity().world.getEntitiesInAABBexcluding(slashBladeEntityPack.getEntity(), new Pos(attackPos).axisAlignedBB(moveRange), entity -> defaultEntityPredicateRegister.canTarget(entity, slashBladeEntityPack.getEntity()));
            for (Entity entity : entityList) {
                //Vector3d _pos = pos.add(RandomUtil.nextVector3dInCircles(slashBladeEntityPack.getEntity().getRNG(), size));
                entity.setPosition(attackPos.getX(), attackPos.getY(), attackPos.getZ());
                            /*if (entity instanceof JudgementCutEntity) {
                                ((JudgementCutEntity) entity).setMaxLifeTime(((JudgementCutEntity) entity).getMaxLifeTime() + entity.ticksExisted);
                            }*/
            }

            StellarRotationEntity jc = new StellarRotationEntity(
                    stellarRotationEntityRegister.getEntityType(),
                    slashBladeEntityPack.getEntity().world,
                    slashBladeEntityPack.getEntity()
            );
            jc.setPosition(attackPos.x, attackPos.y, attackPos.z);
            jc.setPosition(attackPos.getX(), attackPos.getY(), attackPos.getZ());
            jc.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
            jc.setDamage(attack);
            jc.setMaxLifeTime(life);
            jc.setAttackInterval(attackInterval);
            jc.setSize(size);
            jc.getBackRunPack().addRunBack(judgementCutAttackBackTypeRegister, ((judgementCutEntity1, hitEntity) -> hitEntity.setMotion(Vector3d.ZERO)));

            slashBladeEntityPack.getEntity().world.addEntity(jc);
            slashBladeEntityPack.getEntity().world.playSound(null, jc.getPosX(), jc.getPosY(), jc.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.5F, 0.8F / (slashBladeEntityPack.getEntity().getRNG().nextFloat() * 0.4F + 0.8F));
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attack = 0.01f;
            size = 3;
            moveRange = 32;
            attackInterval = 1;
            life = 60;
        }


    }

    @VoluntarilyRegister
    public static class StarBlinkSE extends SE_Register {

        @VoluntarilyAssignment
        protected StarBlinkSE_LayerCapabilityRegister starBlinkSELayerCapabilityRegister;

        @VoluntarilyAssignment
        protected TilSlashBladeRegister tilSlashBladeRegister;

        @VoluntarilyAssignment
        protected AttackParticleRegister attackParticleRegister;

        @ConfigField
        protected int cool;

        @ConfigField
        protected float attack;

        protected final int maxLayer = 4;


        @Override
        public int getMaxLevel() {
            return 1;
        }

        @SubscribeEvent
        protected void onEvent(EventDoAttack event) {
            if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
                return;
            }
            event.pack.getTimeRun().addTimerCell(new TimerCell(
                    () -> event.target.getCapability(starBlinkSELayerCapabilityRegister.getCapability())
                            .ifPresent(starBlinkSELayer -> {
                                starBlinkSELayer.setColor(event.pack.getSlashBladePack().getSlashBladeState().getColorCode());
                                if (!starBlinkSELayer.tryAdd(event.pack.getEntity().world.getGameTime())) {
                                    return;
                                }
                                starBlinkSELayer.reset();
                                attackParticleRegister.add(
                                        event.pack.getEntity().getEntityWorld(),
                                        new GlowingFireGlowColor[]{new GlowingFireGlowColor(event.pack.getSlashBladePack().getSlashBladeState().getColorCode())},
                                        1,
                                        tilSlashBladeRegister.seAttackParticle,
                                        new Pos(event.target));
                                AttackManager.doAttack(
                                        event.pack.getEntity(),
                                        event.target,
                                        attack,
                                        true,
                                        true,
                                        true,
                                        ListUtil.of()
                                );
                                event.target.setMotion(Vector3d.ZERO);
                            }), 0, 0));
        }


        @Override
        public void defaultConfig() {
            super.defaultConfig();
            cool = 0;
            attack = 1.75f;
        }

        public int getCool() {
            return cool;
        }

        public int getMaxLayer() {
            return maxLayer;
        }

    }

    @VoluntarilyRegister
    public static class TilSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected VoidSwordSlashBladeRegister.VoidSword_3_SlashBladeRegister voidSword_3_slashBladeRegister;


        @VoluntarilyAssignment
        protected TilSlashBladeRegister tilSlashBladeRegister;

        @VoluntarilyAssignment
        protected DivinitySE.DivinityLambdaSE divinityLambdaSE;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {

            SlashBladePack voidSword_3_slashBlade = voidSword_3_slashBladeRegister.getSlashBladePack();
            voidSword_3_slashBlade.getSlashBladeState().setKillCount(10000);
            voidSword_3_slashBlade.getSlashBladeState().setRefine(3000);

            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "AVA",
                            " A ",
                            " A "
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfItemSE(divinityLambdaSE, 7.5f),
                            "V", new IRecipeInItemPack.OfSlashBlade(voidSword_3_slashBlade.getItemStack())
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(tilSlashBladeRegister)
            );
        }
    }

    @VoluntarilyRegister
    public static class TilSlashLambdaBladeRegister extends TilSlashBladeRegister {


        @VoluntarilyAssignment
        protected TilSlashBladeRegister tilSlashBladeRegister;

        @VoluntarilyAssignment
        protected StarBlinkLambdaSE starBlinkLambdaSE;


        @Override
        protected void init() {
            super.init();
            model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, tilSlashBladeRegister.getName().getPath(), "model.obj"));
            texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, tilSlashBladeRegister.getName().getPath(), "texture.png"));
        }

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getIse().getPack(starBlinkLambdaSE).setLevel(1);
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
        }

        @VoluntarilyRegister
        public static class StarBlinkLambdaSE extends TilSlashBladeRegister.StarBlinkSE {
            @SubscribeEvent
            @Override
            protected void onEvent(EventDoAttack event) {
                if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
                    return;
                }
                event.target.getCapability(starBlinkSELayerCapabilityRegister.getCapability())
                        .ifPresent(starBlinkSELayer -> starBlinkSELayer.tryAdd(event.pack.getEntity().world.getGameTime()));
            }
        }


        @VoluntarilyRegister
        public static class TilLambdaSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected TilSlashBladeRegister tilSlashBladeRegister;


            @VoluntarilyAssignment
            protected TilSlashBladeRegister.TilSlashLambdaBladeRegister tilSlashLambdaBladeRegister;

            @VoluntarilyAssignment
            protected DivinitySE.DivinityLambdaSE divinityLambdaSE;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {

                SlashBladePack voidSword_3_slashBlade = tilSlashBladeRegister.getSlashBladePack();
                voidSword_3_slashBlade.getSlashBladeState().setKillCount(20000);
                voidSword_3_slashBlade.getSlashBladeState().setRefine(3000);

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                "AVA",
                                " A ",
                                " A "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemSE(divinityLambdaSE, 7.5f),
                                "V", new IRecipeInItemPack.OfSlashBlade(voidSword_3_slashBlade.getItemStack())
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(tilSlashLambdaBladeRegister)
                );
            }
        }


    }

    @VoluntarilyRegister
    public static class TilPillowSlashLambdaBladeRegister extends SlashBladeRegister {

    }
}
