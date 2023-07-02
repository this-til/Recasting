package com.til.recasting.common.register.sa;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.glowing_fire_glow.util.StringUtil;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import com.til.recasting.common.register.util.StringFinal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public abstract class SA_Register extends RegisterBasics {

    public abstract void trigger(UseSlashBladeEntityPack slashBladeEntityPack);
}
