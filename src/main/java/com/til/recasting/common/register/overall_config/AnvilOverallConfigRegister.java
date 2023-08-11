package com.til.recasting.common.register.overall_config;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;

@VoluntarilyRegister
public class AnvilOverallConfigRegister extends OverallConfigRegister {

    protected float seCost;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        seCost = 0.1f;
    }

    public float getSeCost() {
        return seCost;
    }
}
