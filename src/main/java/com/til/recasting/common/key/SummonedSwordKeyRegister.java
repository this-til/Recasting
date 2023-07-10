package com.til.recasting.common.key;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.key.KeyRegister;
import net.minecraftforge.fml.network.NetworkEvent;

@VoluntarilyRegister
public class SummonedSwordKeyRegister extends KeyRegister {
    @Override
    public void pressedServer(NetworkEvent.Context context) {

    }

    @Override
    public void releaseServer(NetworkEvent.Context context) {

    }
}
