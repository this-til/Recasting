package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


public abstract class LaserSlashBladeRegister extends SlashBladeRegister {
    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(LaserSlashBladeRegister.class), StringFinal.STATE + getState(), StringFinal.MODEL));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(LaserSlashBladeRegister.class), StringFinal.STATE + getState(), StringFinal.TEXTURE));
    }

    protected abstract int getState();

    @VoluntarilyRegister
    public static class Laser_1_SlashBladeRegister extends LaserSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(5f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(5);
        }

        @Override
        protected int getState() {
            return 1;
        }
    }

    @VoluntarilyRegister
    public static class Laser_2_SlashBladeRegister extends LaserSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(6f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(10);
        }

        @Override
        protected int getState() {
            return 2;
        }
    }

    @VoluntarilyRegister
    public static class Laser_3_SlashBladeRegister extends LaserSlashBladeRegister {

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
            slashBladePack.getSlashBladeStateSupplement().setDurable(20);
            slashBladePack.getSlashBladeStateSupplement().setAttackDistance(1.2f);
        }

        @Override
        protected int getState() {
            return 3;
        }
    }


}
