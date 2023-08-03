package com.til.recasting.common.register.entity_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.entity_type.EntityTypeRegister;
import com.til.recasting.common.entity.StrengthenBladeStandEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * @author til
 */
@VoluntarilyRegister
public class StrengthenBladeStandEntityTypeRegister extends EntityTypeRegister<StrengthenBladeStandEntity> {

    @Override
    protected StrengthenBladeStandEntity create(EntityType<StrengthenBladeStandEntity> strengthenBladeStandEntityEntityType, World world) {
        return new StrengthenBladeStandEntity(strengthenBladeStandEntityEntityType, world);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        updateInterval = 20;
        trackingRange = 10;
        shouldReceiveVelocityUpdates = false;
        immuneToFire = true;
    }
}
