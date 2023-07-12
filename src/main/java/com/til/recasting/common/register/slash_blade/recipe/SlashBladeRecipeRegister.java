package com.til.recasting.common.register.slash_blade.recipe;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.config.ConfigManage;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.Delayed;
import com.til.recasting.common.register.recipe.SlashBladeUpRecipeRegister;

import java.util.function.Supplier;

public abstract class SlashBladeRecipeRegister extends RegisterBasics {
    @VoluntarilyAssignment
    protected ConfigManage configManage;

    @ConfigField
    protected Delayed<SlashBladeUpRecipeRegister.SlashBladeUpPack> slashBladeUpPack;

    @Override
    public void defaultConfig() {
        configManage.addDelayedWrite(this);
        slashBladeUpPack = new SlashBladeUpPackDelayed(this::defaultConfigSlashBladeUpPack);
    }

    protected abstract SlashBladeUpRecipeRegister.SlashBladeUpPack defaultConfigSlashBladeUpPack();

    public SlashBladeUpRecipeRegister.SlashBladeUpPack getSlashBladeUpPack() {
        return slashBladeUpPack.get();
    }

    public static class SlashBladeUpPackDelayed extends Delayed<SlashBladeUpRecipeRegister.SlashBladeUpPack> {
        public SlashBladeUpPackDelayed(Supplier<SlashBladeUpRecipeRegister.SlashBladeUpPack> supplier) {
            super(supplier);
        }
    }
}
