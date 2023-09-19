package com.til.recasting.common.register.entity_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.entity_type.EntityTypeRegister;
import com.til.recasting.common.entity.PlanetEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

@VoluntarilyRegister
public class PlanetEntityTypeRegister extends EntityTypeRegister<PlanetEntity> {
    @Override
    protected PlanetEntity create(EntityType<PlanetEntity> planetEntityEntityType, World world) {
        return new PlanetEntity(planetEntityEntityType, world, null);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        updateInterval = 20;
        disableSerialization = true;
        immuneToFire = true;
    }
}
