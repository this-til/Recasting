package com.til.recasting.common.register.slash_blade.sa;

import com.til.glowing_fire_glow.common.register.RegisterManage;
import mods.flammpfeil.slashblade.specialattack.SlashArts;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class AllSA_Register extends RegisterManage<SA_Register> {

    protected final Map<SlashArts, SA_Register> slashArtsMap = new HashMap<>();

    @Nullable
    public SA_Register getSA_Register(SlashArts slashArts) {
        return slashArtsMap.get(slashArts);
    }

    @Override
    public void initCommonSetup() {
        super.initCommonSetup();
        for (SA_Register sa_register : forAll()) {
            slashArtsMap.put(sa_register.getSlashArts(), sa_register);
        }
    }
}
