package com.til.recasting.common.register.se.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.util.math.NumberPack;
import com.til.recasting.common.register.se.SE_Register;

public class ImpactSE extends SE_Register {

    @ConfigField
    protected NumberPack probability;

    @ConfigField
    protected NumberPack attack;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        probability = new NumberPack(0, 0.15);
        attack = new NumberPack(0, 0.2);
    }

}
