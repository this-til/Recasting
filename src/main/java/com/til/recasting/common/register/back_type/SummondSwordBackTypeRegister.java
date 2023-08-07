package com.til.recasting.common.register.back_type;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.back_type.BackTypeRegister;
import com.til.recasting.common.entity.SummondSwordEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public abstract class SummondSwordBackTypeRegister<C> extends BackTypeRegister<C> {

    @VoluntarilyRegister
    public static class SummondSwordTransmitBackTypeRegister extends SummondSwordBackTypeRegister<SummondSwordTransmitBackTypeRegister.ITransmit> {
        public interface ITransmit {
            void transmit(SummondSwordEntity summondSwordEntity);
        }
    }

    @VoluntarilyRegister
    public static class SummondSwordAttackBackTypeRegister extends SummondSwordBackTypeRegister<SummondSwordAttackBackTypeRegister.IAttackAction> {
        public interface IAttackAction {
            void attack(SummondSwordEntity summondSwordEntity, Entity hitEntity);
        }
    }

    @VoluntarilyRegister
    public static class SummondSwordAttackBlockTypeRegister extends SummondSwordBackTypeRegister<SummondSwordAttackBlockTypeRegister.IAttackBlock> {
        public interface IAttackBlock {
            void attackBlock(SummondSwordEntity summondSwordEntity, BlockState blockState, BlockPos blockPos);
        }
    }


    @VoluntarilyRegister
    public static class SummondSwordAttackEndBackTypeRegister extends SummondSwordBackTypeRegister<SummondSwordAttackEndBackTypeRegister.IAttackEndAction> {
        public interface IAttackEndAction {
            void attack(SummondSwordEntity summondSwordEntity, Entity hitEntity);
        }
    }


}
