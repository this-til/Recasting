package com.til.recasting.common.event;

import com.til.recasting.common.capability.SlashBladePack;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

/***
 * 对应拔刀剑发生的一些事件
 */
public abstract class EventSlashBlade extends Event {

    public final LivingEntity livingEntity;
    public final SlashBladePack slashBladePack;

    public EventSlashBlade(LivingEntity livingEntity, SlashBladePack slashBladePack) {
        this.livingEntity = livingEntity;
        this.slashBladePack = slashBladePack;
    }
}
