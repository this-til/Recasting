package com.til.recasting.common.register.sa.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import com.til.recasting.common.register.sa.SA_Register;
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
