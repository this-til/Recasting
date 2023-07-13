package com.til.recasting.common.mixin;

import mods.flammpfeil.slashblade.ability.SummonedSwordArts;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = {SummonedSwordArts.class}, remap = false)
public class SummonedSwordArtsMixin {


    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onInputChange(InputCommandEvent event) {
    }
}
