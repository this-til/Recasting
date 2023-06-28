package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.Recasting;
import com.til.recasting.common.register.capability.SA_CapabilityRegister;
import com.til.recasting.common.register.capability.SE_CapabilityRegister;
import com.til.recasting.common.register.sa.instance.EpidemicSummonedSwordSA;
import com.til.recasting.common.register.se.AllSE_Register;
import com.til.recasting.common.register.se.SE_Register;
import com.til.recasting.common.register.se.instance.CooperateWithSE;
import com.til.recasting.common.register.se.instance.OverloadSE;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEvent implements IWorldComponent {

    public static final ResourceLocation SA_SE_CAPABILITY = new ResourceLocation(Recasting.MOD_ID, "sa_se_capability");

    @VoluntarilyAssignment
    protected SA_CapabilityRegister sa_capabilityRegister;


    @VoluntarilyAssignment
    protected SE_CapabilityRegister se_capabilityRegister;


    @VoluntarilyAssignment
    protected EpidemicSummonedSwordSA epidemicSummonedSwordSA_register;


    @VoluntarilyAssignment
    protected AllSE_Register se_register;


    @SubscribeEvent
    protected void onAttachCapabilitiesEvent_itemStack(AttachCapabilitiesEvent<ItemStack> event) {

        if (!(event.getObject().getItem() instanceof ItemSlashBlade)) {
            return;
        }

        CapabilityProvider capabilityProvider = new CapabilityProvider();

        ISA as = new ISA.SA();
        as.setSA(epidemicSummonedSwordSA_register);
        capabilityProvider.addCapability(sa_capabilityRegister.getCapability(), as);

        ISE ise = new ISE.SE();
        for (SE_Register seRegister : se_register.forAll()) {
            ise.getPack(seRegister).setLevel(5);
        }
        capabilityProvider.addCapability(se_capabilityRegister.getCapability(), ise);

        event.addCapability(SA_SE_CAPABILITY, capabilityProvider);
    }

}
