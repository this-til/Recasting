package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.Recasting;
import com.til.recasting.common.register.capability.ISlashBladeStateSupplement_CapabilityRegister;
import com.til.recasting.common.register.capability.SE_CapabilityRegister;
import com.til.recasting.common.register.se.AllSE_Register;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEvent implements IWorldComponent {

    public static final ResourceLocation SLASH_BLADE_CAPABILITY = new ResourceLocation(Recasting.MOD_ID, "slash_blade_capability");

    public static final ResourceLocation CAPABILITY = new ResourceLocation(Recasting.MOD_ID, "capability");


    @VoluntarilyAssignment
    protected SE_CapabilityRegister se_capabilityRegister;

    @VoluntarilyAssignment
    protected ISlashBladeStateSupplement_CapabilityRegister slashBladeStateSupplement_capabilityRegister;

    @VoluntarilyAssignment
    protected AllSE_Register se_register;


    @SubscribeEvent
    protected void onAttachCapabilitiesEvent_itemStack(AttachCapabilitiesEvent<ItemStack> event) {


        if (!(event.getObject().getItem() instanceof ItemSlashBlade)) {
            return;
        }

        CapabilityProvider capabilityProvider = new CapabilityProvider();

        capabilityProvider.addCapability(se_capabilityRegister.getCapability(), new ISE.SE());
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
