package com.til.recasting.common.register.entity_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.entity_type.EntityTypeRegister;
import com.til.recasting.common.entity.SummondSwordEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * @author til
 */
@VoluntarilyRegister
public class SummondSwordEntityTypeRegister extends EntityTypeRegister<SummondSwordEntity> {
    @Override
    protected SummondSwordEntity create(EntityType<SummondSwordEntity> summondSwordEntityEntityType, World world) {
        return new SummondSwordEntity(summondSwordEntityEntityType, world, null);
    }
}
