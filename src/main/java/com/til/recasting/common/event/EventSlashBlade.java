package com.til.recasting.common.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

/***
 * 对应拔刀剑发生的一些事件
 */
public abstract class EventSlashBlade extends Event {

    public final LivingEntity livingEntity;
    public final ItemStack itemStack;

    public EventSlashBlade(LivingEntity livingEntity, ItemStack itemStack) {
        this.livingEntity = livingEntity;
        this.itemStack = itemStack;
    }
}
