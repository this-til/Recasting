package com.til.recasting.common.mixin;

import mods.flammpfeil.slashblade.event.RefineHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AnvilUpdateEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Invoker;

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
