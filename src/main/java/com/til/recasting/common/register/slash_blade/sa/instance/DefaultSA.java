package com.til.recasting.common.register.slash_blade.sa.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import mods.flammpfeil.slashblade.specialattack.SlashArts;

@VoluntarilyRegister
public class DefaultSA extends SA_Register {
    @Override
    public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {

    }

    @Override
    protected SlashArts initSlashArts() {
        return SlashArts.JUDGEMENT_CUT;
    }
}
