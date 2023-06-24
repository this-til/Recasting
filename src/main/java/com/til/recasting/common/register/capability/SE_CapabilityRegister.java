package com.til.recasting.common.register.capability;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.capability.OriginalCapabilityRegister;
import com.til.recasting.common.capability.ISA;
import com.til.recasting.common.capability.ISE;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

@VoluntarilyRegister
public class SE_CapabilityRegister extends OriginalCapabilityRegister<ISE> {


    @CapabilityInject(ISE.class)
    public static Capability<ISE> isaCapability;

    @Override
    protected Capability<ISE> initCapability() {
        return isaCapability;
    }
}
