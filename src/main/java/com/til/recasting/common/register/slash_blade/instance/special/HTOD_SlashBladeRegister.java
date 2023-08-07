package com.til.recasting.common.register.slash_blade.instance.special;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

@VoluntarilyRegister
public class HTOD_SlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected HTOD_SlashBladeSA htod_slashBladeSA;

    protected ResourceLocation saModel;

    @Override
    protected void init() {
        super.init();
        summondSwordModel = new ResourceLocation(getName().getNamespace(), String.join("/", "summond_sword", getName().getPath(), "model.obj"));
        saModel = new ResourceLocation(getName().getNamespace(), String.join("/", "special", getName().getPath(), "model.obj"));
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
        slashBladePack.getSlashBladeState().setColorCode(new Color(246, 67, 67).getRGB());
        slashBladePack.getSlashBladeStateSupplement().setDurable(26);
        slashBladePack.setSA(htod_slashBladeSA);
    }


    @VoluntarilyRegister
    public static class HTOD_SlashBladeSA extends SA_Register {
        @VoluntarilyAssignment
        protected HTOD_SlashBladeRegister htod_slashBladeRegister;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {

        }
    }
}
