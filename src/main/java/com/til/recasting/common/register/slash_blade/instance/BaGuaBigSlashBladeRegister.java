package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.util.StringFinal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

@VoluntarilyRegister
public class BaGuaBigSlashBladeRegister extends SlashBladeRegister {

    protected ResourceLocation saTexture;


    @Override
    protected void init() {
        super.init();
        summondSwordModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SUMMOND_SWORD, getName().getPath(), StringFinal.MODEL));
        summondSwordTexture = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SUMMOND_SWORD, getName().getPath(), StringFinal.TEXTURE));

        saTexture = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SPECIAL, getName().getPath(), StringFinal.TEXTURE));

    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(255, 255, 255));
        slashBladePack.getSlashBladeStateSupplement().setDurable(22);
    }


    @VoluntarilyRegister
    public static class BaGuaBigSlashBlade_SA extends SA_Register {


        @ConfigField
        protected float attack;

        @ConfigField
        protected float attackNumber;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {

        }
    }
}
