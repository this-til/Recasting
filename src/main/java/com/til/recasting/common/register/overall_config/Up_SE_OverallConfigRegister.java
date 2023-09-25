package com.til.recasting.common.register.overall_config;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;

@VoluntarilyRegister
public class Up_SE_OverallConfigRegister extends OverallConfigRegister {

    @ConfigField
    protected float probabilityFactor;


    @Override
    public void defaultConfig() {
        super.defaultConfig();
        probabilityFactor = 0.1f;
    }

    public float getProbabilityFactor() {
        return probabilityFactor;
    }
}
