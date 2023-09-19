package com.til.recasting.common.mixin;

import mods.flammpfeil.slashblade.event.RefineHandler;
import net.minecraftforge.event.AnvilUpdateEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = RefineHandler.class, remap = false)
public class RefineHandlerMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onAnvilUpdateEvent(AnvilUpdateEvent event) {

    }
}
