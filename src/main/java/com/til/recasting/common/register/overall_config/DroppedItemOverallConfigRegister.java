package com.til.recasting.common.register.overall_config;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;

import java.util.List;

@VoluntarilyRegister
public class DroppedItemOverallConfigRegister extends OverallConfigRegister {

    @ConfigField
    protected float soulDropChance;

    @ConfigField
    protected float tinySoulDropChance;

    @ConfigField
    protected float enchantmentSoulDropChance;

    @ConfigField
    protected float enchantmentSoulSuccessRate;

    @ConfigField
    protected float entityDropChance;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        soulDropChance = 0.1f;
        tinySoulDropChance = 0.2f;
        enchantmentSoulDropChance = 0.05f;
        enchantmentSoulSuccessRate = 0.25f;
        entityDropChance = 0.01f;
    }

    public float getSoulDropChance() {
        return soulDropChance;
    }

    public float getTinySoulDropChance() {
        return tinySoulDropChance;
    }

    public float getEnchantmentSoulDropChance() {
        return enchantmentSoulDropChance;
    }

    public float getEnchantmentSoulSuccessRate() {
        return enchantmentSoulSuccessRate;
    }

    public float getEntityDropChance() {
        return entityDropChance;
    }
}
