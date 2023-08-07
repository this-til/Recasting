package com.til.recasting.common.register.overall_config;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;

@VoluntarilyRegister
public class SlashEffectEntityOverallConfigRegister extends OverallConfigRegister {

    @ConfigField
    protected boolean useBlockParticle;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        useBlockParticle = true;
    }

    public boolean isUseBlockParticle() {
        return useBlockParticle;
    }
}
