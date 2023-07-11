package com.til.recasting.common.register.slash_blade.slash_blades.star_blade;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import net.minecraft.item.ItemStack;

@VoluntarilyRegister
public class StarBlade_1_SlashBladeRegister extends StarBladeSlashBladeRegister {

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.slashBladeState.setColorCode(0xE7E5E6);
        slashBladePack.slashBladeState.setBaseAttackModifier(4f);
        slashBladePack.iSlashBladeStateSupplement.setAttackDistance(0.75f);
    }

    @Override
    protected int getState() {
        return 1;
    }

}
