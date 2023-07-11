package com.til.recasting.common.register.slash_blade.slash_blades.star_blade;

import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.util.ResourceLocation;

public abstract class StarBladeSlashBladeRegister extends SlashBladeRegister {
    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(StarBladeSlashBladeRegister.class), "state" + getState(), "model.obj"));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(StarBladeSlashBladeRegister.class), "state" + getState(), "texture.png"));

        summondSwordModel = new ResourceLocation(getName().getNamespace(), String.join("/", "summond_sword", "star_blade", "model.obj"));
    }

    protected abstract int getState();
}
