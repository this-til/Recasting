package com.til.recasting.common.data;

import com.til.glowing_fire_glow.common.capability.time_run.ITimeRun;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.capability.capabilitys.TimeRunCapabilityRegister;
import com.til.recasting.common.register.overall_config.AttackOverallConfigRegister;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import com.til.recasting.common.register.util.RayTraceUtil;
import mods.flammpfeil.slashblade.capability.concentrationrank.ConcentrationRankCapabilityProvider;
import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import mods.flammpfeil.slashblade.capability.inputstate.IInputState;
import mods.flammpfeil.slashblade.capability.inputstate.InputStateCapabilityProvider;
import mods.flammpfeil.slashblade.capability.mobeffect.CapabilityMobEffect;
import mods.flammpfeil.slashblade.capability.mobeffect.IMobEffectState;
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

    @VoluntarilyAssignment
    protected static AttackOverallConfigRegister attackOverallConfigRegister;

    protected LivingEntity entity;

    protected IConcentrationRank concentrationRank;
    protected ITimeRun timeRun;
    protected IInputState iInputState;
    protected IMobEffectState iMobEffectState;

    protected SlashBladePack slashBladePack;

    protected boolean basicEffective;

    public UseSlashBladeEntityPack(LivingEntity entity) {
        this.entity = entity;
        try {
            concentrationRank = entity.getCapability(ConcentrationRankCapabilityProvider.RANK_POINT).orElse(null);
            iInputState = entity.getCapability(InputStateCapabilityProvider.INPUT_STATE).orElse(null);
            timeRun = entity.getCapability(timeRunCapabilityRegister.getCapability()).orElse(null);
            iMobEffectState = entity.getCapability(CapabilityMobEffect.MOB_EFFECT).orElse(null);
            slashBladePack = new SlashBladePack(entity.getHeldItemMainhand());
            basicEffective = getConcentrationRank() != null && getInputState() != null && getTimeRun() != null && getMobEffectState() != null;
        } catch (RuntimeException runtimeException) {
            basicEffective = false;
        }
    }

    public boolean isEffective(SlashBladePack.EffectiveType effectiveType) {
        return basicEffective && getSlashBladePack().isEffective(effectiveType);
    }

    public IConcentrationRank.ConcentrationRanks getRank() {
        return concentrationRank.getRank(getEntity().world.getGameTime());
    }

    /***
     * 获取伤害比例
     */
    public double getDamageRatio(double basics) {

        float add = 1;
        add += getRank().level / (7f / attackOverallConfigRegister.getRankMaxAttackBonus());
        add += getSlashBladePack().getSlashBladeState().getRefine() * attackOverallConfigRegister.getRefineAttackBonus();

        if (getSlashBladePack().getSlashBladeState().getKillCount() > 1000) {
            add += attackOverallConfigRegister.getThousandKillReward();
        }
        if (getSlashBladePack().getSlashBladeState().getKillCount() > 10000) {
            add += attackOverallConfigRegister.getTenThousandKillReward();
        }
        if (getSlashBladePack().getSlashBladeState().getRefine() > 1000) {
            add += attackOverallConfigRegister.getThousandRefineReward();
        }
        if (getSlashBladePack().getSlashBladeState().getRefine() > 10000) {
            add += attackOverallConfigRegister.getTenThousandRefineReward();
        }

        return basics * add;
    }

    public Vector3d getAttackPos() {
        Entity attackMove = getSlashBladePack().getSlashBladeState().getTargetEntity(getEntity().world);
        if (attackMove != null) {
            return RayTraceUtil.getPosition(attackMove);
        }
        return defaultTargetSelectorRegister.selector(getEntity()).getHitVec();
    }

    public IConcentrationRank getConcentrationRank() {
        return concentrationRank;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public ITimeRun getTimeRun() {
        return timeRun;
    }

    public IInputState getInputState() {
        return iInputState;
    }

    public SlashBladePack getSlashBladePack() {
        return slashBladePack;
    }

    public IMobEffectState getMobEffectState() {
        return iMobEffectState;
    }
}
