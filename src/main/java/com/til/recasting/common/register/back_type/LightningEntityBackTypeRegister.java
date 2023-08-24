package com.til.recasting.common.register.back_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.back_type.BackTypeRegister;
import com.til.recasting.common.entity.LightningEntity;
import net.minecraft.entity.Entity;

public abstract class LightningEntityBackTypeRegister<C> extends BackTypeRegister<C> {

    @VoluntarilyRegister
    public static class LightningEntityAttackBackTypeRegister extends LightningEntityBackTypeRegister<LightningEntityAttackBackTypeRegister.IAttack> {
        public static interface IAttack {
            void onAttack(LightningEntity lightningEntity, Entity hitEntity);
        }
    }

}
