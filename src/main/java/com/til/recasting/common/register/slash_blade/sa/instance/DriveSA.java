package com.til.recasting.common.register.slash_blade.sa.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.DriveEntity;
import com.til.recasting.common.register.entity_type.DriveEntityTypeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;

@VoluntarilyRegister
public class DriveSA extends SA_Register {

    @VoluntarilyAssignment
    protected DriveEntityTypeRegister driveEntityTypeRegister;

    @ConfigField
    protected float attack;

    @ConfigField
    protected int time;

    @ConfigField
    protected float range;

    @Override
    public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
        DriveEntity driveEntity = new DriveEntity(
                driveEntityTypeRegister.getEntityType(),
                slashBladeEntityPack.getEntity().world,
                slashBladeEntityPack.getEntity()
        );
        driveEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
        driveEntity.setSize(range * slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().getAttackDistance());
        driveEntity.setDamage(attack);
        driveEntity.setMaxLifeTime(time);
        driveEntity.setRoll(45);
        slashBladeEntityPack.getEntity().world.addEntity(driveEntity);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = 0.8f;
        time = 40;
        range = 1;
    }
}
