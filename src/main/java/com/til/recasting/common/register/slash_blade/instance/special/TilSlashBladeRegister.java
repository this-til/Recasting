package com.til.recasting.common.register.slash_blade.instance.special;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import net.minecraft.item.ItemStack;

import java.awt.*;

@VoluntarilyRegister
public class TilSlashBladeRegister extends SlashBladeRegister {
    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
        slashBladePack.getSlashBladeState().setColorCode(new Color(210, 118, 246).getRGB());
        slashBladePack.getSlashBladeStateSupplement().setDurable(26);
    }

    @Override
    public boolean displayItem() {
        return false;
    }
}
