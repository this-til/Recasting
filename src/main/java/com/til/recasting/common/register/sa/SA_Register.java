package com.til.recasting.common.register.sa;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.recasting.common.capability.ISA;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public abstract class SA_Register extends RegisterBasics {

    public abstract void trigger(LivingEntity livingEntity, ItemStack itemStack, ISlashBladeState slashBladeState, ISA isa);
}
