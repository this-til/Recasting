package com.til.recasting.common.register.world.item;

import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.capability.CapabilityEvent;
import com.til.recasting.common.capability.IItemSA;
import com.til.recasting.common.capability.IItemSE;
import com.til.recasting.common.register.capability.ItemSA_CapabilityRegister;
import com.til.recasting.common.register.capability.ItemSE_CapabilityRegister;
import com.til.recasting.common.register.sa.AllSARegister;
import com.til.recasting.common.register.sa.SA_Register;
import com.til.recasting.common.register.se.SE_Register;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

@StaticVoluntarilyAssignment
@VoluntarilyRegister
public class SA_DepositItemRegister {

    @VoluntarilyAssignment
    protected static ItemSA_CapabilityRegister itemSA_capabilityRegister;

    @VoluntarilyAssignment
    protected static AllSARegister allSARegister;

    public static class SE_DepositItem extends Item implements CapabilityEvent.ICustomCapability {

        protected final SE_DepositItemRegister se_depositItemRegister;

        public SE_DepositItem(Properties properties, SE_DepositItemRegister se_depositItemRegister) {
            super(properties);
            this.se_depositItemRegister = se_depositItemRegister;
        }

        @Override
        public void customCapability(ItemStack itemStack, CapabilityProvider capabilityProvider) {
            capabilityProvider.addCapability(itemSA_capabilityRegister.getCapability(), new IItemSA.ItemSA());
        }


        @Override
        public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
            if (!this.isInGroup(group)) {
                return;
            }
            for (SA_Register sa_register : allSARegister.forAll()) {
                ItemStack itemStack = new ItemStack(this);
                itemStack.getCapability(itemSA_capabilityRegister.getCapability()).ifPresent(pack -> pack.setSA(sa_register));
                items.add(itemStack);
            }
        }
    }

}
