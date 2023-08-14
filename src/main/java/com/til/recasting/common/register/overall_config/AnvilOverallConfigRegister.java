package com.til.recasting.common.register.overall_config;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;

@VoluntarilyRegister
public class AnvilOverallConfigRegister extends OverallConfigRegister {

    protected float seCost;

    protected float enchantmentCost;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        seCost = 0.1f;
        enchantmentCost = 1;
    }

    public float getSeCost() {
        return seCost;
    }

    public float getEnchantmentCost() {
        return enchantmentCost;
    }
}
