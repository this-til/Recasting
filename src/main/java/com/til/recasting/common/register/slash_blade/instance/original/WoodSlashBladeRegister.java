package com.til.recasting.common.register.slash_blade.instance.original;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

@VoluntarilyRegister
public class WoodSlashBladeRegister extends SlashBladeRegister {

    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(SlashBlade.modid, "model/named/yamato.obj");
        texture = new ResourceLocation(SlashBlade.modid, "model/wood.png");
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setTranslationKey("item.slashblade.simple.wood");
        slashBladePack.getSlashBladeState().setDestructable(true);
        slashBladePack.getSlashBladeState().setColorCode(new Color(-13421569).getRGB());
    }

    @Override
    public boolean displayItem() {
        return false;
    }

}
