package com.til.recasting.common.register.entity_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.entity_type.EntityTypeRegister;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.entity.StellarRotationEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

@VoluntarilyRegister
public class StellarRotationEntityTypeRegister extends EntityTypeRegister<StellarRotationEntity> {

    @Override
    protected StellarRotationEntity create(EntityType<StellarRotationEntity> stellarRotationEntityEntityType, World world) {
        return new StellarRotationEntity(stellarRotationEntityEntityType, world, null);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        updateInterval = 20;
        disableSerialization = true;
        immuneToFire = true;
    }
}
