package com.til.recasting.common.register.slash_blade.slash_blades;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.register.sa.instance.FanaticalDanceSA;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import net.minecraft.item.ItemStack;

@VoluntarilyRegister
public class CoolMintSlashBladeRegister extends SlashBladeRegister {


    @VoluntarilyAssignment
    protected FanaticalDanceSA fanaticalDanceSA;

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.slashBladeState.setColorCode(0xC7F3CB);
        slashBladePack.slashBladeState.setBaseAttackModifier(5f);
        slashBladePack.slashBladeState.setSlashArtsKey(fanaticalDanceSA.getSlashArts().getName());
    }
}
