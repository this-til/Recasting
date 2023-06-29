package com.til.recasting.common.capability;

import mods.flammpfeil.slashblade.capability.concentrationrank.ConcentrationRankCapabilityProvider;
import mods.flammpfeil.slashblade.capability.concentrationrank.IConcentrationRank;
import net.minecraft.entity.LivingEntity;

/**
 * @author til
 */
public class UseSlashBladeEntityPack {

    public final LivingEntity entity;

    public final IConcentrationRank concentrationRank;

    public final SlashBladePack slashBladePack;

    public UseSlashBladeEntityPack(LivingEntity entity) {
        this.entity = entity;
        concentrationRank = entity.getCapability(ConcentrationRankCapabilityProvider.RANK_POINT).orElse(null);
        slashBladePack = new SlashBladePack(entity.getHeldItemMainhand());
    }

    public boolean isEffective() {
        return concentrationRank != null && slashBladePack.isEffective();
    }

    public IConcentrationRank.ConcentrationRanks getConcentrationRank() {
        return concentrationRank.getRank(entity.world.getGameTime());
    }

    /***
     * 获取伤害比例
     */
    public double getDamageRatio(double basics) {
        return getConcentrationRank().level * basics * (1 + slashBladePack.slashBladeState.getRefine() * 0.02);
    }

}
