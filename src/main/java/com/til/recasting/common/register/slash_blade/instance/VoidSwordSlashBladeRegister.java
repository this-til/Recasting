package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class VoidSwordSlashBladeRegister extends SlashBladeRegister {

    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(VoidSwordSlashBladeRegister.class), "model.obj"));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(VoidSwordSlashBladeRegister.class), "state" + getState(), "texture.png"));
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.slashBladeState.setBaseAttackModifier(7f);
        slashBladePack.iSlashBladeStateSupplement.setAttackDistance(1.5f);
    }

    protected abstract int getState();

    @VoluntarilyRegister
    public static class VoidSword_3_BladeRegister extends VoidSwordSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setColorCode(0x3D3D3E);
        }

        @Override
        protected int getState() {
            return 3;
        }
    }

    @VoluntarilyRegister
    public static class VoidSword_2_BladeRegister extends VoidSwordSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setColorCode(0x000000);
        }

        @Override
        protected int getState() {
            return 2;
        }
    }

    @VoluntarilyRegister
    public static class VoidSword_1_BladeRegister extends VoidSwordSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.slashBladeState.setColorCode(0xF5EED3);
        }


        @Override
        protected int getState() {
            return 1;
        }
    }
}
