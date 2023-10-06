package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;
import com.til.glowing_fire_glow.common.register.particle_register.particle_registers.LightningParticleRegister;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.RandomUtil;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.BallLightningEntity;
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.register.attack_type.instance.LightningAttackType;
import com.til.recasting.common.register.capability.ElectrificationCapabilityRegister;
import com.til.recasting.common.register.entity_type.BallLightningEntityTypeRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;


public abstract class LaserSlashBladeRegister extends SlashBladeRegister {
    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(LaserSlashBladeRegister.class), StringFinal.STATE + getState(), StringFinal.MODEL));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(LaserSlashBladeRegister.class), StringFinal.STATE + getState(), StringFinal.TEXTURE));
    }

    protected abstract int getState();

    public abstract static class LaserSlashBlade_SA extends SA_Register {

        @ConfigField
        protected float attack;

        @ConfigField
        protected int interval;

        @ConfigField
        protected float seep;

        @ConfigField
        protected int life;

        @ConfigField
        protected float range;

        @ConfigField
        protected int time;

        @VoluntarilyAssignment
        protected BallLightningEntityTypeRegister ballLightningEntityTypeRegister;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            BallLightningEntity ballLightningEntity = new BallLightningEntity(
                    ballLightningEntityTypeRegister.getEntityType(),
                    slashBladeEntityPack.getEntity().world,
                    slashBladeEntityPack.getEntity()
            );
            ballLightningEntity.setSeep(seep);
            ballLightningEntity.setDamage(attack);
            ballLightningEntity.setMaxLifeTime(life);
            ballLightningEntity.setSize(range);
            ballLightningEntity.setAttackInterval(interval);
            ballLightningEntity.setTime(time);
            slashBladeEntityPack.getEntity().world.addEntity(ballLightningEntity);
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attack = 0.2f;
            interval = 10;
            seep = 0.4f;
            life = 200;
            range = 16;
            time = 40;
        }

        @VoluntarilyRegister
        public static class LaserSlashBlade_SA_OverallConfigRegister extends OverallConfigRegister {
            @ConfigField
            protected float addAttack;

            @ConfigField
            protected float additionalAttack;

            @ConfigField
            protected float probability;

            @ConfigField
            protected int abatement;


            @VoluntarilyAssignment
            protected ElectrificationCapabilityRegister electrificationCapabilityRegister;

            @VoluntarilyAssignment
            protected LightningAttackType lightningAttackType;

            @VoluntarilyAssignment
            protected LightningParticleRegister lightningParticleRegister;

            protected Random random = new Random();


            @SubscribeEvent
            protected void onEvent(EventDoAttack event) {
                event.target.getCapability(electrificationCapabilityRegister.getCapability())
                        .ifPresent(e -> {
                            if (!e.has(event.target.world.getGameTime())) {
                                return;
                            }
                            if (random.nextFloat() < probability) {
                                event.modifiedRatio *= (additionalAttack + 1);
                            }
                            if (event.attackTypeList.contains(lightningAttackType)) {
                                event.modifiedRatio *= (addAttack + 1);
                                lightningParticleRegister.add(
                                        event.target.getEntityWorld(),
                                        new GlowingFireGlowColor[]{new GlowingFireGlowColor(e.getColor())},
                                        0,
                                        null,
                                        new Pos(event.target).add(new Pos(RandomUtil.nextVector3dInCircles(random, 5))),
                                        new Pos(event.target)
                                );
                                e.up(e.getExpireTime() - abatement);
                            }
                        });
            }

            @Override
            public void defaultConfig() {
                super.defaultConfig();
                addAttack = 1;
                additionalAttack = 0.3f;
                probability = 0.3333f;
                abatement = 10;
            }
        }

    }

    @VoluntarilyRegister
    public static class Laser_1_SlashBladeRegister extends LaserSlashBladeRegister {

        @VoluntarilyAssignment
        protected LaserSlashBlade_1_SA laserSlashBlade_1_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(5f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(5);
            slashBladePack.setSA(laserSlashBlade_1_sa);
        }

        @Override
        protected int getState() {
            return 1;
        }

        @VoluntarilyRegister
        public static class LaserSlashBlade_1_SA extends LaserSlashBlade_SA {
        }
    }

    @VoluntarilyRegister
    public static class Laser_2_SlashBladeRegister extends LaserSlashBladeRegister {
        @VoluntarilyAssignment
        protected LaserSlashBlade_2_SA laserSlashBlade_2_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(6f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(10);
            slashBladePack.setSA(laserSlashBlade_2_sa);
        }

        @Override
        protected int getState() {
            return 2;
        }

        @VoluntarilyRegister
        public static class LaserSlashBlade_2_SA extends LaserSlashBlade_SA {
            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                super.trigger(slashBladeEntityPack);
                attack = 0.3f;
                interval = 5;
                range = 20;
                time = 80;
            }
        }
    }

    @VoluntarilyRegister
    public static class Laser_3_SlashBladeRegister extends LaserSlashBladeRegister {

        @VoluntarilyAssignment
        protected LaserSlashBlade_3_SA laserSlashBlade_3_sa;

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(20);
            slashBladePack.getSlashBladeStateSupplement().setAttackDistance(1.2f);
            slashBladePack.setSA(laserSlashBlade_3_sa);
        }

        @Override
        protected int getState() {
            return 3;
        }

        @VoluntarilyRegister
        public static class LaserSlashBlade_3_SA extends LaserSlashBlade_SA {
            @Override
            public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
                super.trigger(slashBladeEntityPack);
                attack = 0.4f;
                interval = 2;
                range = 24;
                time = 120;
            }
        }
    }


}
