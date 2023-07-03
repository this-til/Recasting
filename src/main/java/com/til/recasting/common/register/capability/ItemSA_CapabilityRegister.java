package com.til.recasting.common.register.capability;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.capability.OriginalCapabilityRegister;
import com.til.recasting.common.capability.IItemSA;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * @author til
 */
@VoluntarilyRegister
public class ItemSA_CapabilityRegister extends OriginalCapabilityRegister<IItemSA> {


    @CapabilityInject(IItemSA.class)
    public static Capability<IItemSA> iItemSACapability;

    @Override
    protected Capability<IItemSA> initCapability() {
        return iItemSACapability;
    }
}
