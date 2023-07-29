package com.til.recasting.common.data;

import com.til.glowing_fire_glow.common.capability.time_run.ITimeRun;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.capability.capabilitys.TimeRunCapabilityRegister;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import com.til.recasting.common.register.util.RayTraceUtil;
import mods.flammpfeil.slashblade.capability.concentrationrank.ConcentrationRankCapabilityProvider;
import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import mods.flammpfeil.slashblade.capability.inputstate.IInputState;
import mods.flammpfeil.slashblade.capability.inputstate.InputStateCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

/**
 * @author til
 */
@StaticVoluntarilyAssignment
public class UseSlashBladeEntityPack {

    @VoluntarilyAssignment
    protected static TimeRunCapabilityRegister timeRunCapabilityRegister;

    @VoluntarilyAssignment
    protected static DefaultTargetSelectorRegister defaultTargetSelectorRegister;


    public final LivingEntity entity;

    public final IConcentrationRank concentrationRank;
    public final ITimeRun timeRun;
    public final IInputState iInputState;

    public final SlashBladePack slashBladePack;

    public UseSlashBladeEntityPack(LivingEntity entity) {
        this.entity = entity;
        concentrationRank = entity.getCapability(ConcentrationRankCapabilityProvider.RANK_POINT).orElse(null);
        iInputState = entity.getCapability(InputStateCapabilityProvider.INPUT_STATE).orElse(null);
        timeRun = entity.getCapability(timeRunCapabilityRegister.getCapability()).orElse(null);
        slashBladePack = new SlashBladePack(entity.getHeldItemMainhand());
    }

    public boolean isEffective(SlashBladePack.EffectiveType effectiveType) {
        return concentrationRank != null && iInputState != null && timeRun != null && slashBladePack.isEffective(effectiveType);
    }

    public IConcentrationRank.ConcentrationRanks getConcentrationRank() {
        return concentrationRank.getRank(entity.world.getGameTime());
    }

    /***
     * 获取伤害比例
     */
    public double getDamageRatio(double basics) {
        return basics * (1 + getConcentrationRank().level / 7f) * (1 + slashBladePack.slashBladeState.getRefine() * 0.01);
    }

    public Vector3d getAttackPos() {
        Entity attackMove = slashBladePack.slashBladeState.getTargetEntity(entity.world);
        if (attackMove != null) {
            return RayTraceUtil.getPosition(attackMove);
        }
        return defaultTargetSelectorRegister.selector(entity).getHitVec();
    }

}
