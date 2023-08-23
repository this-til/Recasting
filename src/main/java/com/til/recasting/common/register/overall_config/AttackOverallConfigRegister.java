package com.til.recasting.common.register.overall_config;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;

@VoluntarilyRegister
public class AttackOverallConfigRegister extends OverallConfigRegister {

    @ConfigField
    protected float refineAttackBonus;

    @ConfigField
    protected float rankMaxAttackBonus;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        refineAttackBonus = 0.003f;
        rankMaxAttackBonus = 0.1f;
    }

    public float getRefineAttackBonus() {
        return refineAttackBonus;
    }

    public float getRankMaxAttackBonus() {
        return rankMaxAttackBonus;
    }
}
