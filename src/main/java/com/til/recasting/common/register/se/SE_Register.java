package com.til.recasting.common.register.se;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.common.save.SaveField;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.register.util.StringFinal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

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
