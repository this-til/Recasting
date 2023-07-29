package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.capability.IItemSE;
import com.til.recasting.common.register.capability.ItemSE_CapabilityRegister;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class AnvilRepairManage implements IWorldComponent {

    @VoluntarilyAssignment
    protected ItemSE_CapabilityRegister itemSE_capabilityRegister;

    @VoluntarilyAssignment
    protected SE_DepositItemRegister se_depositItemRegister;

    @SubscribeEvent
    protected void onAnvilUpdateEvent(AnvilUpdateEvent event) {
        ItemStack leftItemStack = event.getLeft();
        ItemStack rightItemStack = event.getRight();
        Optional<IItemSE> leftItemSEOptional = leftItemStack.getCapability(itemSE_capabilityRegister.getCapability()).resolve();
        Optional<IItemSE> rightItemSEOptional = rightItemStack.getCapability(itemSE_capabilityRegister.getCapability()).resolve();
        if (!leftItemSEOptional.isPresent() || !rightItemSEOptional.isPresent()) {
            return;
        }
        IItemSE leftItemSE = leftItemSEOptional.get();
        IItemSE rightItemSE = rightItemSEOptional.get();

        if (!leftItemSE.getSE().equals(rightItemSE.getSE())) {
            return;
        }
        float newSuccessRate = leftItemSE.getBasicsSuccessRate() * leftItemStack.getCount() + rightItemSE.getBasicsSuccessRate() * rightItemStack.getCount();
        event.setOutput(se_depositItemRegister.mackItemStack(leftItemSE.getSE(), newSuccessRate));
        event.setCost(1 + (int) newSuccessRate * 10);
        event.setMaterialCost(rightItemStack.getCount());
    }
}
