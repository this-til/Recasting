package com.til.recasting.common.register.back_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.back_type.BackTypeRegister;
import com.til.recasting.common.entity.SlashEffectEntity;
import net.minecraft.entity.Entity;

public abstract class SlashEffectEntityBackTypeRegister<C> extends BackTypeRegister<C> {

    @VoluntarilyRegister
    public static class SlashEffectAttackBackTypeRegister extends SlashEffectEntityBackTypeRegister<SlashEffectAttackBackTypeRegister.IAttack> {
        public interface IAttack {
            void attack(SlashEffectEntity slashEffectEntity, Entity hitEntity);
        }
    }

}
