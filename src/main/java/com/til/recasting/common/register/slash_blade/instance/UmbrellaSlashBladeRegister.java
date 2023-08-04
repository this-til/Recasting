package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.*;


public abstract class UmbrellaSlashBladeRegister extends SlashBladeRegister {

    public abstract int getState();

    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(UmbrellaSlashBladeRegister.class), "state" + getState(), "model.obj"));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(UmbrellaSlashBladeRegister.class), "texture.png"));
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(0xC191FF));
        slashBladePack.getSlashBladeStateSupplement().setDurable(8);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7);
    }


    @VoluntarilyRegister
    public static class UmbrellaSlash_1_BladeRegister extends UmbrellaSlashBladeRegister {
        @Override
        public int getState() {
            return 1;
        }
    }

    @VoluntarilyRegister
    public static class UmbrellaSlash_2_BladeRegister extends UmbrellaSlashBladeRegister {
        @Override
        public int getState() {
            return 2;
        }
    }

}
