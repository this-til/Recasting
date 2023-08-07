package com.til.recasting.common.register.slash_blade.sa.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.DriveEntity;
import com.til.recasting.common.register.entity_type.DriveEntityTypeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;


/***
 * 终焉樱
 */
@VoluntarilyRegister
public class EndingYanSakuraSA extends SA_Register {

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
        DriveEntity driveEntity_1 = new DriveEntity(
                driveEntityTypeRegister.getEntityType(),
                slashBladeEntityPack.getEntity().world,
                slashBladeEntityPack.getEntity()
        );
        driveEntity_1.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
        driveEntity_1.setSize(range * slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().getAttackDistance());
        driveEntity_1.setDamage(attack);
        driveEntity_1.setMaxLifeTime(time);
        driveEntity_1.setRoll(35);
        driveEntity_1.setSeep(0.3f);
        slashBladeEntityPack.getEntity().world.addEntity(driveEntity_1);

        DriveEntity driveEntity_2 = new DriveEntity(
                driveEntityTypeRegister.getEntityType(),
                slashBladeEntityPack.getEntity().world,
                slashBladeEntityPack.getEntity()
        );
        driveEntity_2.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
        driveEntity_2.setSize(range * slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().getAttackDistance());
        driveEntity_2.setDamage(attack);
        driveEntity_2.setMaxLifeTime(time);
        driveEntity_2.setRoll(-35);
        driveEntity_2.setSeep(0.3f);
        slashBladeEntityPack.getEntity().world.addEntity(driveEntity_2);
    }


    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = 0.6f;
        time = 10;
        range = 1;
    }
}
