package com.til.recasting.common.mixin;

import mods.flammpfeil.slashblade.event.BladeMaterialTooltips;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * @author til
 */
@Mixin(value = BladeMaterialTooltips.class, remap = false)
public class BladeMaterialTooltipsMixin {
}
