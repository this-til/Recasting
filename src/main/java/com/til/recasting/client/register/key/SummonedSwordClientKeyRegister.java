package com.til.recasting.client.register.key;

import com.til.glowing_fire_glow.client.register.key.KeyClientRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.key.SummonedSwordKeyRegister;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@VoluntarilyRegister
public class SummonedSwordClientKeyRegister extends KeyClientRegister<SummonedSwordKeyRegister> {
    @Override
    protected int initInputId() {
        return 2;
    }

    @Override
    protected InputMappings.Type initType() {
        return InputMappings.Type.MOUSE;
    }

    @Override
    protected void pressed() {

    }

    @Override
    protected void release() {

    }
}
