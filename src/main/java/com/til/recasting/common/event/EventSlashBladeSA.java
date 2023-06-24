package com.til.recasting.common.event;

import com.til.recasting.common.capability.ISA;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/***
 * 触发sa
 */
public class EventSlashBladeSA extends EventSlashBlade {

    public final ISA isa;

    public EventSlashBladeSA(LivingEntity livingEntity, ItemStack itemStack, ISA isa) {
        super(livingEntity, itemStack);
        this.isa = isa;
    }
}
