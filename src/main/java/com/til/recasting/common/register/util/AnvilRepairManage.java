package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.capability.IItemEnchantment;
import com.til.recasting.common.capability.IItemSE;
import com.til.recasting.common.register.capability.ItemEnchantmentCapabilityRegister;
import com.til.recasting.common.register.capability.ItemSE_CapabilityRegister;
import com.til.recasting.common.register.overall_config.AnvilOverallConfigRegister;
import com.til.recasting.common.register.world.item.EnchantmentDepositItemRegister;
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

    @VoluntarilyAssignment
    protected AnvilOverallConfigRegister anvilOverallConfigRegister;

    @VoluntarilyAssignment
    protected ItemEnchantmentCapabilityRegister itemEnchantmentCapabilityRegister;

    @VoluntarilyAssignment
    protected EnchantmentDepositItemRegister enchantmentDepositItemRegister;

    @SubscribeEvent
    protected void onAnvilUpdateEvent_SE(AnvilUpdateEvent event) {
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
        event.setCost((int) (1 + newSuccessRate * 100 * anvilOverallConfigRegister.getSeCost()));
        event.setMaterialCost(rightItemStack.getCount());
    }

    @SubscribeEvent
    protected void onAnvilUpdateEvent_Enchantment(AnvilUpdateEvent event) {
        ItemStack leftItemStack = event.getLeft();
        ItemStack rightItemStack = event.getRight();
        Optional<IItemEnchantment> leftItemEnchantmentOptional = leftItemStack.getCapability(itemEnchantmentCapabilityRegister.getCapability()).resolve();
        Optional<IItemEnchantment> rightItemEnchantmentOptional = rightItemStack.getCapability(itemEnchantmentCapabilityRegister.getCapability()).resolve();
        if (!leftItemEnchantmentOptional.isPresent() || !rightItemEnchantmentOptional.isPresent()) {
            return;
        }
        IItemEnchantment leftItemSE = leftItemEnchantmentOptional.get();
        IItemEnchantment rightItemSE = rightItemEnchantmentOptional.get();

        if (!leftItemSE.getEnchantment().equals(rightItemSE.getEnchantment())) {
            return;
        }
        float newSuccessRate = leftItemSE.getBasicsSuccessRate() * leftItemStack.getCount() + rightItemSE.getBasicsSuccessRate() * rightItemStack.getCount();
        event.setOutput(enchantmentDepositItemRegister.mackItemStack(leftItemSE.getEnchantment(), newSuccessRate));
        event.setCost((int) (1 + newSuccessRate * 100 * anvilOverallConfigRegister.getEnchantmentCost()));
        event.setMaterialCost(rightItemStack.getCount());
    }
}
