package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@VoluntarilyRegister
public class NamelessSlashBladeRegister extends SlashBladeRegister {

    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(SlashBlade.modid, "model/blade.obj");
        texture = new ResourceLocation(SlashBlade.modid, "model/blade.png");
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setTranslationKey("");
    }

    @Override
    public boolean displayItem() {
        return false;
    }
}
