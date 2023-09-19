package com.til.recasting.common.register.back_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.back_type.BackTypeRegister;
import com.til.recasting.common.entity.JudgementCutEntity;
import net.minecraft.entity.Entity;

public abstract class JudgementCutBackTypeRegister<C> extends BackTypeRegister<C> {

    @VoluntarilyRegister
    public static class JudgementCutAttackBackTypeRegister extends JudgementCutBackTypeRegister<JudgementCutAttackBackTypeRegister.IAttack> {
        public interface IAttack {
            void attack(JudgementCutEntity judgementCutEntity, Entity hitEntity);
        }
    }

    @VoluntarilyRegister
    public static class JudgementCutDeathBackTypeRegister extends JudgementCutBackTypeRegister<JudgementCutDeathBackTypeRegister.IDeath> {
        public interface IDeath {
            void death(JudgementCutEntity judgementCutEntity);
        }
    }

    @VoluntarilyRegister
    public static class JudgementCutTickBackTypeRegister extends JudgementCutBackTypeRegister<JudgementCutTickBackTypeRegister.ITick> {
        public interface ITick {
            void tick(JudgementCutEntity judgementCutEntity);
        }
    }

}
