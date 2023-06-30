package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.Recasting;
import com.til.recasting.common.register.capability.ISlashBladeStateSupplement_CapabilityRegister;
import com.til.recasting.common.register.capability.SA_CapabilityRegister;
import com.til.recasting.common.register.capability.SE_CapabilityRegister;
import com.til.recasting.common.register.sa.instance.EpidemicSummonedSwordSA;
import com.til.recasting.common.register.se.AllSE_Register;
import com.til.recasting.common.register.se.SE_Register;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEvent implements IWorldComponent {

    public static final ResourceLocation SLASH_BLADE_CAPABILITY = new ResourceLocation(Recasting.MOD_ID, "slash_blade_capability");

    public static final ResourceLocation CAPABILITY = new ResourceLocation(Recasting.MOD_ID, "capability");

    @VoluntarilyAssignment
    protected SA_CapabilityRegister sa_capabilityRegister;


    @VoluntarilyAssignment
    protected SE_CapabilityRegister se_capabilityRegister;

    @VoluntarilyAssignment
    protected ISlashBladeStateSupplement_CapabilityRegister slashBladeStateSupplement_capabilityRegister;


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
        capabilityProvider.addCapability(slashBladeStateSupplement_capabilityRegister.getCapability(), new ISlashBladeStateSupplement.SlashBladeStateSupplement());

        event.addCapability(SLASH_BLADE_CAPABILITY, capabilityProvider);
    }

    @SubscribeEvent
    protected void onAttachCapabilitiesEvent_itemStack_2(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack itemStack = event.getObject();
        if (!(itemStack.getItem() instanceof ICustomCapability)) {
            return;
        }

        CapabilityProvider capabilityProvider = new CapabilityProvider();
        ((ICustomCapability) itemStack.getItem()).customCapability(itemStack, capabilityProvider);
        event.addCapability(CAPABILITY, capabilityProvider);
    }

    public interface ICustomCapability {
        void customCapability(ItemStack itemStack, CapabilityProvider capabilityProvider);
    }

}
