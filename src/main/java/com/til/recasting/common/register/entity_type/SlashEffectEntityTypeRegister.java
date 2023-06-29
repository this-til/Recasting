package com.til.recasting.common.register.entity_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.entity_type.EntityTypeRegister;
import com.til.recasting.common.entity.SlashEffectEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * @author til
 */
@VoluntarilyRegister
public class SlashEffectEntityTypeRegister extends EntityTypeRegister<SlashEffectEntity> {

    @Override
    protected SlashEffectEntity create(EntityType<SlashEffectEntity> slashEffectEntityEntityType, World world) {
        return new SlashEffectEntity(slashEffectEntityEntityType, world, null);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        updateInterval = 20;
    }

}
