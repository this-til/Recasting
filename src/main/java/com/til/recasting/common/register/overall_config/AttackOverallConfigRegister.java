package com.til.recasting.common.register.overall_config;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;

@VoluntarilyRegister
public class AttackOverallConfigRegister extends OverallConfigRegister {

    protected float refineAttackBonus;

    protected float rankMaxAttackBonus;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        refineAttackBonus = 0.01f;
        rankMaxAttackBonus = 1;
    }

    public float getRefineAttackBonus() {
        return refineAttackBonus;
    }

    public float getRankMaxAttackBonus() {
        return rankMaxAttackBonus;
    }
}
