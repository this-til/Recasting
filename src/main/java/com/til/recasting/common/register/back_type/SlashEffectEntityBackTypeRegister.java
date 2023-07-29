package com.til.recasting.common.register.back_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.back_type.BackTypeRegister;
import com.til.recasting.common.entity.SlashEffectEntity;
import net.minecraft.entity.Entity;

import java.util.UUID;

public abstract class SlashEffectEntityBackTypeRegister<C> extends BackTypeRegister<C> {

    @VoluntarilyRegister
    public static class AttackBackTypeRegister extends SlashEffectEntityBackTypeRegister<AttackBackTypeRegister.IAttack> {
        public interface IAttack {
            void attack(SlashEffectEntity slashEffectEntity, Entity hitEntity);
        }
    }

}
