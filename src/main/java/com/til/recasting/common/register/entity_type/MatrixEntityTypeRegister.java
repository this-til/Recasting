package com.til.recasting.common.register.entity_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.entity_type.EntityTypeRegister;
import com.til.recasting.common.entity.MatrixEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

@VoluntarilyRegister
public class MatrixEntityTypeRegister extends EntityTypeRegister<MatrixEntity> {

    @Override
    protected MatrixEntity create(EntityType<MatrixEntity> matrixEntityEntityType, World world) {
        return new MatrixEntity(matrixEntityEntityType, world, null);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        updateInterval = 20;
        disableSerialization = true;
        immuneToFire = true;
    }

}
