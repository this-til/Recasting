package com.til.recasting.common.capability;

import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.capability.synchronous.ISynchronousManage;
import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.capability.capabilitys.SynchronousManageCapabilityRegister;
import com.til.recasting.Recasting;
import com.til.recasting.common.register.capability.*;
import com.til.recasting.common.register.slash_blade.se.AllSE_Register;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Supplier;

public class CapabilityEvent implements IWorldComponent {

    public static final ResourceLocation SLASH_BLADE_CAPABILITY = new ResourceLocation(Recasting.MOD_ID, "slash_blade_capability");

    public static final ResourceLocation CAPABILITY = new ResourceLocation(Recasting.MOD_ID, "capability");


    @VoluntarilyAssignment
    protected SE_CapabilityRegister se_capabilityRegister;

    @VoluntarilyAssignment
    protected ISlashBladeStateSupplementCapabilityRegister slashBladeStateSupplement_capabilityRegister;

    @VoluntarilyAssignment
    protected AllSE_Register se_register;

    @VoluntarilyAssignment
    protected StarBlinkSE_LayerCapabilityRegister starBlinkSELayerCapabilityRegister;

    @VoluntarilyAssignment
    protected ChaosLayerCapabilityRegister chaosLayerCapabilityRegister;

    @VoluntarilyAssignment
    protected SynchronousManageCapabilityRegister synchronousManageCapabilityRegister;

    @VoluntarilyAssignment
    @Deprecated
    protected ElectrificationCapabilityRegister electrificationCapabilityRegister;


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

    @SubscribeEvent
    protected void onAttachCapabilitiesEvent_entity(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof LivingEntity)) {
            return;
        }
        CapabilityProvider capabilityProvider = new CapabilityProvider();
        Supplier<ISynchronousManage> synchronousManageSupplier = synchronousManageCapabilityRegister.supplierCapability(event.getObject());
        capabilityProvider.addCapability(starBlinkSELayerCapabilityRegister.getCapability(), new StarBlinkSE_LayerCapabilityRegister.StarBlinkSE_Layer(synchronousManageSupplier));
        capabilityProvider.addCapability(chaosLayerCapabilityRegister.getCapability(), new ChaosLayerCapabilityRegister.ChaosLayer(synchronousManageSupplier));
        //capabilityProvider.addCapability(electrificationCapabilityRegister.getCapability(), new ElectrificationCapabilityRegister.Electrification());
        event.addCapability(CAPABILITY, capabilityProvider);
    }

    public interface ICustomCapability {
        void customCapability(ItemStack itemStack, CapabilityProvider capabilityProvider);
    }

}
