package com.til.recasting.common.register.capability;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.capability.OriginalCapabilityRegister;
import com.til.recasting.common.capability.ISA;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

@VoluntarilyRegister
public class SA_CapabilityRegister extends OriginalCapabilityRegister<ISA> {


    @CapabilityInject(ISA.class)
    public static Capability<ISA> isaCapability;

    @Override
    protected Capability<ISA> initCapability() {
        return isaCapability;
    }
}
