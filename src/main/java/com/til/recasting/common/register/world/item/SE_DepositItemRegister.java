package com.til.recasting.common.register.world.item;

import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import com.til.glowing_fire_glow.common.save.SaveField;
import com.til.recasting.common.capability.CapabilityEvent;
import com.til.recasting.common.capability.IItemSE;
import com.til.recasting.common.register.capability.ItemSE_CapabilityRegister;
import com.til.recasting.common.register.se.AllSE_Register;
import com.til.recasting.common.register.se.SE_Register;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * @author til
 */
@StaticVoluntarilyAssignment
public abstract class SE_DepositItemRegister extends ItemRegister {


    @VoluntarilyAssignment
    protected static ItemSE_CapabilityRegister itemSE_CapabilityRegister;

    @VoluntarilyAssignment
    protected static AllSE_Register allSE_register;


    @ConfigField
    protected float successRate;

    @Override
    protected Item initItem() {
        return new SE_DepositItem(new Item.Properties().group(ItemGroup.COMBAT), this);
    }

    public float getSuccessRate() {
        return successRate;
    }

    public static class SE_DepositItem extends Item implements CapabilityEvent.ICustomCapability {

        protected final SE_DepositItemRegister se_depositItemRegister;

        public SE_DepositItem(Properties properties, SE_DepositItemRegister se_depositItemRegister) {
            super(properties);
            this.se_depositItemRegister = se_depositItemRegister;
        }

        @Override
        public void customCapability(ItemStack itemStack, CapabilityProvider capabilityProvider) {
            capabilityProvider.addCapability(itemSE_CapabilityRegister.getCapability(), new IItemSE.ItemSE());
        }


        @Override
        public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
            if (!this.isInGroup(group)) {
                return;
            }
            for (SE_Register se_register : allSE_register.forAll()) {

                ItemStack itemStack = new ItemStack(this);
                itemStack.getCapability(itemSE_CapabilityRegister.getCapability()).ifPresent(pack -> {
                    pack.setSE(se_register);
                    pack.setBasicsSuccessRate(se_depositItemRegister.getSuccessRate());
                });

                items.add(itemStack);

                ItemStack itemStack_2 = new ItemStack(this);
                itemStack_2.getCapability(itemSE_CapabilityRegister.getCapability()).ifPresent(pack -> {
                    pack.setSE(se_register);
                    pack.setBasicsSuccessRate(se_depositItemRegister.getSuccessRate());
                    pack.setProtect(true);
                });

                items.add(itemStack_2);
            }
        }
    }

    @VoluntarilyRegister
    public static class SE_Deposit_1_ItemRegister extends SE_DepositItemRegister {
        @Override
        public void defaultConfig() {
            super.defaultConfig();
            successRate = 0.05f;
        }
    }

    @VoluntarilyRegister
    public static class SE_Deposit_2_ItemRegister extends SE_DepositItemRegister {
        @Override
        public void defaultConfig() {
            super.defaultConfig();
            successRate = 0.05f;
        }
    }

    @VoluntarilyRegister
    public static class SE_Deposit_3_ItemRegister extends SE_DepositItemRegister {
        @Override
        public void defaultConfig() {
            super.defaultConfig();
            successRate = 0.25f;
        }
    }

    @VoluntarilyRegister
    public static class SE_Deposit_4_ItemRegister extends SE_DepositItemRegister {
        @Override
        public void defaultConfig() {
            super.defaultConfig();
            successRate = 0.5f;
        }
    }

    @VoluntarilyRegister
    public static class SE_Deposit_5_ItemRegister extends SE_DepositItemRegister {
        @Override
        public void defaultConfig() {
            super.defaultConfig();
            successRate = 1f;
        }
    }
}
