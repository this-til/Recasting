package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.capability.time_run.ITimeRun;
import com.til.glowing_fire_glow.common.register.capability.capabilitys.TimeRunCapabilityRegister;
import mods.flammpfeil.slashblade.capability.concentrationrank.ConcentrationRankCapabilityProvider;
import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import net.minecraft.entity.LivingEntity;

/**
 * @author til
 */
public class UseSlashBladeEntityPack {

    public final LivingEntity entity;

    public final IConcentrationRank concentrationRank;
    public final ITimeRun timeRun;

    public final SlashBladePack slashBladePack;

    public UseSlashBladeEntityPack(LivingEntity entity) {
        this.entity = entity;
        concentrationRank = entity.getCapability(ConcentrationRankCapabilityProvider.RANK_POINT).orElse(null);
        timeRun = entity.getCapability(TimeRunCapabilityRegister.timeRunCapability).orElse(null);
        slashBladePack = new SlashBladePack(entity.getHeldItemMainhand());
    }

    public boolean isEffective() {
        return concentrationRank != null && timeRun != null && slashBladePack.isEffective();
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

}