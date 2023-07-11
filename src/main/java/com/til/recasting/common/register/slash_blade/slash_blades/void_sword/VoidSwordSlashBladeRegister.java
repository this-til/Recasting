package com.til.recasting.common.register.slash_blade.slash_blades.void_sword;

import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.util.ResourceLocation;

public abstract class VoidSwordSlashBladeRegister extends SlashBladeRegister {

    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(VoidSwordSlashBladeRegister.class), "model.obj"));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid,ResourceLocationUtil.ofPath(VoidSwordSlashBladeRegister.class), "state" + getState(), "texture.png"));

    }

    protected abstract int getState();
}
