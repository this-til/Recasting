package com.til.recasting.common.register.overall_config;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;

@VoluntarilyRegister
public class SlayerStyleArtsOverallConfigRegister extends OverallConfigRegister {

    @ConfigField
    protected float sprintAttack;

    @ConfigField
    protected float sprintPower;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        sprintPower = 5f;
        sprintAttack = 0.01f;
    }

    public float getSprintPower() {
        return sprintPower;
    }

    public float getSprintAttack() {
        return sprintAttack;
    }
}
