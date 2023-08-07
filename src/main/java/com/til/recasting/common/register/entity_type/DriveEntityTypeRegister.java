package com.til.recasting.common.register.entity_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.entity_type.EntityTypeRegister;
import com.til.recasting.common.entity.DriveEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

@VoluntarilyRegister
public class DriveEntityTypeRegister extends EntityTypeRegister<DriveEntity> {

    @Override
    protected DriveEntity create(EntityType<DriveEntity> driveEntityEntityType, World world) {
        return new DriveEntity(driveEntityEntityType, world, null);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        updateInterval = 20;
        disableSerialization = true;
        immuneToFire = true;
    }
}
