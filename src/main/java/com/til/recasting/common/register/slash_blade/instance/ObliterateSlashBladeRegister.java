package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.util.ResourceLocation;

@VoluntarilyRegister
public class ObliterateSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyRegister
    public static class ObliterateLambdaSlashBladeRegister extends ObliterateSlashBladeRegister {

        @Override
        protected void init() {
            super.init();
            model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, getName().getPath(), StringFinal.LAMBDA, StringFinal.MODEL));
            //texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, getName().getPath(), StringFinal.LAMBDA, StringFinal.TEXTURE));
        }
    }

}
