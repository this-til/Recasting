package com.til.recasting.common.mixin;

import mods.flammpfeil.slashblade.ability.LockOnManager;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * @author til
 */
@Mixin(value = {LockOnManager.class}, remap = false)
public class LockOnManagerMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite()
    public void onInputChange(InputCommandEvent event) {
    }
}
