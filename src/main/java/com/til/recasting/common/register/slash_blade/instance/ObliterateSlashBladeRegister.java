package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

@VoluntarilyRegister
public class ObliterateSlashBladeRegister extends SlashBladeRegister {
    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(255, 0, 0));
        slashBladePack.getSlashBladeStateSupplement().setDurable(12);
    }

    @VoluntarilyRegister
    public static class ObliterateLambdaSlashBladeRegister extends ObliterateSlashBladeRegister {
        @VoluntarilyAssignment
        public ObliterateSlashBladeRegister obliterateSlashBladeRegister;

        @Override
        protected void init() {
            super.init();
            model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, obliterateSlashBladeRegister.getName().getPath(), StringFinal.LAMBDA, StringFinal.MODEL));
            texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, obliterateSlashBladeRegister.getName().getPath(), StringFinal.TEXTURE));

        }

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeStateSupplement().setAttackDistance(1.25f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(16);
        }
    }

}
