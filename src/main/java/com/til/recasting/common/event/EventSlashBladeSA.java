package com.til.recasting.common.event;

import com.til.recasting.common.capability.ISA;
import com.til.recasting.common.capability.SlashBladePack;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/***
 * 触发sa
 */
public class EventSlashBladeSA extends EventSlashBlade {

    public EventSlashBladeSA(LivingEntity livingEntity, SlashBladePack slashBladePack) {
        super(livingEntity, slashBladePack);
    }
}
