package com.til.recasting.common.register.back_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.back_type.BackTypeRegister;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.register.util.JudgementCutManage;
import net.minecraft.entity.Entity;

public abstract class JudgementCutBackTypeRegister<C> extends BackTypeRegister<C> {

    @VoluntarilyRegister
    public static class AttackBackTypeRegister extends JudgementCutBackTypeRegister<AttackBackTypeRegister.IAttack> {
        public interface IAttack {
            void attack(JudgementCutEntity judgementCutEntity, Entity hitEntity);
        }
    }

    @VoluntarilyRegister
    public static class DeathBackTypeRegister extends JudgementCutBackTypeRegister<DeathBackTypeRegister.IDeath> {
        public interface IDeath {
            void death(JudgementCutEntity judgementCutEntity);
        }
    }

}
