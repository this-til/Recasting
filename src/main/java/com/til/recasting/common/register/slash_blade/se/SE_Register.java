package com.til.recasting.common.register.slash_blade.se;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.RegisterBasics;

public abstract class SE_Register extends RegisterBasics {


    /***
     * se的最大等级
     */
    @ConfigField
    protected int maxLevel;


    @Override
    public void defaultConfig() {
        super.defaultConfig();
        maxLevel = 5;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
