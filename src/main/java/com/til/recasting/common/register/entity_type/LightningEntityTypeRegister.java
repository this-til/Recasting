package com.til.recasting.common.register.entity_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.entity_type.EntityTypeRegister;
import com.til.recasting.common.entity.LightningEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

@VoluntarilyRegister
public class LightningEntityTypeRegister extends EntityTypeRegister<LightningEntity> {
    @Override
    protected LightningEntity create(EntityType<LightningEntity> lightningEntityEntityType, World world) {
        return new LightningEntity(getEntityType(), world, null);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        updateInterval = 20;
        disableSerialization = true;
        immuneToFire = true;
    }
}
