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

    @ConfigField
    protected float thousandKillReward;

    @ConfigField
    protected float  tenThousandKillReward;

    @ConfigField
    protected float thousandRefineReward;

    @ConfigField
    protected float tenThousandRefineReward;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        refineAttackBonus = 0.002f;
        rankMaxAttackBonus = 0.1f;
        thousandKillReward = 0.1f;
        tenThousandKillReward = 0.1f;
        thousandRefineReward = 0.1f;
        tenThousandRefineReward = 0.1f;
    }

    public float getRefineAttackBonus() {
        return refineAttackBonus;
    }

    public float getRankMaxAttackBonus() {
        return rankMaxAttackBonus;
    }

    public float getThousandKillReward() {
        return thousandKillReward;
    }

    public float getTenThousandKillReward() {
        return tenThousandKillReward;
    }

    public float getThousandRefineReward() {
        return thousandRefineReward;
    }

    public float getTenThousandRefineReward() {
        return tenThousandRefineReward;
    }
}
