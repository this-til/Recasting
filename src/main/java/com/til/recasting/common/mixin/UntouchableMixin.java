package com.til.recasting.common.mixin;

import mods.flammpfeil.slashblade.ability.Untouchable;
import mods.flammpfeil.slashblade.capability.mobeffect.CapabilityMobEffect;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = Untouchable.class, remap = false)
public class UntouchableMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onLivingTicks(LivingEvent.LivingUpdateEvent event) {

    }


    @SubscribeEvent
    @Overwrite
    public void onLivingDeath(LivingDeathEvent event) {
    }

}
