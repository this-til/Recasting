package com.til.recasting.common.register.sa;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.recasting.common.capability.SlashBladePack;
import net.minecraft.entity.LivingEntity;

public abstract class SA_Register extends RegisterBasics {

    public abstract void trigger(LivingEntity livingEntity, SlashBladePack slashBladePack);
}
