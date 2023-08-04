package com.til.recasting.common.mixin;

import mods.flammpfeil.slashblade.ability.Untouchable;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Untouchable.class, remap = false)
public class UntouchableMixin {

/*
    */
/**
     * @author
     * @reason
     *//*

    @Overwrite
    public void onLivingTicks(LivingEvent.LivingUpdateEvent event) {

    }


    */
/**
     * @author
     * @reason
     *//*

    @Overwrite
    public void onLivingDeath(LivingDeathEvent event) {
    }
*/

}
