package com.til.recasting.common.register.back_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.back_type.BackTypeRegister;
import com.til.glowing_fire_glow.common.util.Extension;
import com.til.recasting.common.entity.SummondSwordEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public abstract class SummondSwordBackTypeRegister<C> extends BackTypeRegister<C> {

    @VoluntarilyRegister
    public static class TransmitBackTypeRegister extends SummondSwordBackTypeRegister<TransmitBackTypeRegister.ITransmit> {
        public interface ITransmit {
            void transmit(SummondSwordEntity summondSwordEntity);
        }
    }

    @VoluntarilyRegister
    public static class AttackBackTypeRegister extends SummondSwordBackTypeRegister<AttackBackTypeRegister.IAttackAction> {
        public interface IAttackAction {
            void attack(SummondSwordEntity summondSwordEntity, Entity hitEntity);
        }
    }

    @VoluntarilyRegister
    public static class AttackBlockTypeRegister extends SummondSwordBackTypeRegister<AttackBlockTypeRegister.IAttackBlock> {
        public interface IAttackBlock {
            void attackBlock(SummondSwordEntity summondSwordEntity, BlockState blockState, BlockPos blockPos);
        }
    }


    @VoluntarilyRegister
    public static class AttackEndBackTypeRegister extends SummondSwordBackTypeRegister<AttackEndBackTypeRegister.IAttackEndAction> {
        public interface IAttackEndAction {
            void attack(SummondSwordEntity summondSwordEntity, Entity hitEntity);
        }
    }


}
