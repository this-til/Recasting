package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.register.capability.ISlashBladeStateSupplement_CapabilityRegister;
import com.til.recasting.common.register.capability.SE_CapabilityRegister;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.item.ItemStack;

@StaticVoluntarilyAssignment
public class SlashBladePack {


    @VoluntarilyAssignment
    protected static SE_CapabilityRegister se_capabilityRegister;

    @VoluntarilyAssignment
    protected static ISlashBladeStateSupplement_CapabilityRegister iSlashBladeStateSupplement_capabilityRegister;

    public final ItemStack itemStack;
    public final ISlashBladeState slashBladeState;
    public final ISlashBladeStateSupplement iSlashBladeStateSupplement;
    public final ISE ise;


    public SlashBladePack(ItemStack itemStack) {
        this.itemStack = itemStack;
        slashBladeState = itemStack.getCapability(ItemSlashBlade.BLADESTATE).orElse(null);
        iSlashBladeStateSupplement = itemStack.getCapability(iSlashBladeStateSupplement_capabilityRegister.getCapability()).orElse(null);
        ise = itemStack.getCapability(se_capabilityRegister.getCapability()).orElse(null);
    }

    public boolean isEffective() {
        return slashBladeState != null && ise != null && iSlashBladeStateSupplement != null;
    }
}
