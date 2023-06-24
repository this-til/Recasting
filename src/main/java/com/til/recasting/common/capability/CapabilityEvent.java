package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.Recasting;
import com.til.recasting.common.register.capability.SA_CapabilityRegister;
import com.til.recasting.common.register.capability.SE_CapabilityRegister;
import com.til.recasting.common.register.sa.EpidemicSummonedSwordSA_Register;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateCapabilityProvider;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEvent implements GlowingFireGlow.IWorldComponent {

    public static final ResourceLocation SA_SE_CAPABILITY = new ResourceLocation(Recasting.MOD_ID, "sa_se_capability");

    @VoluntarilyAssignment
    protected SA_CapabilityRegister sa_capabilityRegister;


    @VoluntarilyAssignment
    protected SE_CapabilityRegister se_capabilityRegister;


    @VoluntarilyAssignment
    protected EpidemicSummonedSwordSA_Register epidemicSummonedSwordSA_register;

    @SubscribeEvent
    protected void onAttachCapabilitiesEvent_itemStack(AttachCapabilitiesEvent<ItemStack> event) {

        if (!(event.getObject().getItem() instanceof ItemSlashBlade)) {
            return;
        }

        CapabilityProvider capabilityProvider = new CapabilityProvider();
        ISA as = new ISA.SA();
        as.setSA(epidemicSummonedSwordSA_register);
        capabilityProvider.addCapability(sa_capabilityRegister.getCapability(), as);
        capabilityProvider.addCapability(se_capabilityRegister.getCapability(), new ISE.SE());

        event.addCapability(SA_SE_CAPABILITY, capabilityProvider);
    }

}
