package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.BallLightningEntity;
import com.til.recasting.common.register.entity_type.BallLightningEntityTypeRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


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
            }
        }
    }


}
