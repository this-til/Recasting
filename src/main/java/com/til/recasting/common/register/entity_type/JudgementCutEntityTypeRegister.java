package com.til.recasting.common.register.entity_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.entity_type.EntityTypeRegister;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.entity.SlashEffectEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

/**
 * @author til
 */
@VoluntarilyRegister
public class JudgementCutEntityTypeRegister extends EntityTypeRegister<JudgementCutEntity> {

    @Override
    protected JudgementCutEntity create(EntityType<JudgementCutEntity> slashEffectEntityEntityType, World world) {
        return new JudgementCutEntity(slashEffectEntityEntityType, world, null);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        updateInterval = 20;
    }

}
