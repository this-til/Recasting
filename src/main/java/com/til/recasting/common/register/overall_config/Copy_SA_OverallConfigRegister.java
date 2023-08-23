package com.til.recasting.common.register.overall_config;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;

@VoluntarilyRegister
public class Copy_SA_OverallConfigRegister extends OverallConfigRegister {

    @ConfigField
    protected float getSuccessRate;

    @ConfigField
    protected float lossRefine;

    @ConfigField
    protected float minRefine;

    @ConfigField
    protected float minKill;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        getSuccessRate = 0.5f;
        lossRefine = 0.2f;
        minRefine = 500;
        minKill = 1000;
    }

    public float getGetSuccessRate() {
        return getSuccessRate;
    }

    public float getLossRefine() {
        return lossRefine;
    }

    public float getMinRefine() {
        return minRefine;
    }

    public float getMinKill() {
        return minKill;
    }
}
