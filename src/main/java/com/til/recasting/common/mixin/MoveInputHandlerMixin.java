package com.til.recasting.common.mixin;

import mods.flammpfeil.slashblade.event.MoveInputHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = MoveInputHandler.class, remap = false)
public class MoveInputHandlerMixin {
    /**
     * @author
     * @reason
     */
    @OnlyIn(Dist.CLIENT)
    @Overwrite
    static public void onPlayerPostTick(TickEvent.PlayerTickEvent event){
    }

}
