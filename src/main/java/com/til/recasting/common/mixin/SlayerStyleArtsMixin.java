package com.til.recasting.common.mixin;

import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = SlayerStyleArts.class, remap = false)
public class SlayerStyleArtsMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onInputChange(InputCommandEvent event) {

    }
}
