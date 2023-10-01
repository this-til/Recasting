package com.til.recasting.common.register.entity_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.entity_type.EntityTypeRegister;
import com.til.recasting.common.entity.BallLightningEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

@VoluntarilyRegister
public class BallLightningEntityTypeRegister extends EntityTypeRegister<BallLightningEntity> {
    @Override
    protected BallLightningEntity create(EntityType<BallLightningEntity> ballLightningEntityEntityType, World world) {
        return new BallLightningEntity(ballLightningEntityEntityType, world, null);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        updateInterval = 20;
        disableSerialization = true;
        immuneToFire = true;
    }
}
