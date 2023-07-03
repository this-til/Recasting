package com.til.recasting.common.register.world.item;

import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import com.til.glowing_fire_glow.common.save.SaveField;
import com.til.glowing_fire_glow.util.StringUtil;
import com.til.recasting.Recasting;
import com.til.recasting.common.capability.CapabilityEvent;
import com.til.recasting.common.capability.IItemSE;
import com.til.recasting.common.register.capability.ItemSE_CapabilityRegister;
import com.til.recasting.common.register.se.AllSE_Register;
import com.til.recasting.common.register.se.SE_Register;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.text.ChoiceFormat;
import java.text.NumberFormat;
import java.util.List;

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
        return new SE_DepositItem(new Item.Properties().group(SlashBlade.SLASHBLADE), this);
    }

    public float getSuccessRate() {
        return successRate;
    }

    public static class SE_DepositItem extends Item implements CapabilityEvent.ICustomCapability {

        protected final SE_DepositItemRegister se_depositItemRegister;

        public SE_DepositItem(Properties properties, SE_DepositItemRegister se_depositItemRegister) {
            super(properties);
            this.se_depositItemRegister = se_depositItemRegister;

            /*this.addPropertyOverride(new ResourceLocation("energy"), (stack, world, entity) ->
            {
                LazyOptional<IEnergyStorage> lazyOptional = stack.getCapability(CapabilityEnergy.ENERGY);
                return lazyOptional.map(e -> (float) e.getEnergyStored() / e.getMaxEnergyStored()).orElse(0.0F);
            });*/
        }

        @Override
        public void customCapability(ItemStack itemStack, CapabilityProvider capabilityProvider) {
            capabilityProvider.addCapability(itemSE_CapabilityRegister.getCapability(), new IItemSE.ItemSE());
        }

        @Override
        public boolean hasEffect(ItemStack stack) {
            IItemSE iItemSE = stack.getCapability(itemSE_CapabilityRegister.getCapability()).orElse(null);
            if (iItemSE == null) {
                return false;
            }
            return iItemSE.isProtect();
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            stack.getCapability(itemSE_CapabilityRegister.getCapability()).ifPresent(pack -> {
                        SE_Register se_register = pack.getSE();
                        if (se_register != null) {
                            tooltip.add(new TranslationTextComponent("SE:%s    maxLevel:%s",
                                    new TranslationTextComponent(StringUtil.formatLang(se_register.getName())),
                                    se_register.getMaxLevel()));
                            tooltip.add(new TranslationTextComponent("§8%s",
                                    new TranslationTextComponent(StringUtil.formatLang(se_register.getName().getNamespace(), se_register.getName().getPath(), StringFinal.INTRODUCE))));
                        }
                        NumberFormat fmt = NumberFormat.getPercentInstance();
                        fmt.setMaximumFractionDigits(2);
                        tooltip.add(new TranslationTextComponent("%s:%s",
                                new TranslationTextComponent(StringUtil.formatLang(Recasting.MOD_ID, "basics_success_rate")),
                                new StringTextComponent(fmt.format(pack.getBasicsSuccessRate()))));
                        tooltip.add(new TranslationTextComponent(pack.isProtect() ? StringUtil.formatLang(Recasting.MOD_ID, "protect.true") : StringUtil.formatLang(Recasting.MOD_ID, "protect.false")));
                    }
            );
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

    @VoluntarilyRegister()
    public static class SE_Deposit_1_ItemRegister extends SE_DepositItemRegister {
        @Override
        public void defaultConfig() {
            super.defaultConfig();
            successRate = 0.05f;
        }
    }

    @VoluntarilyRegister(priority = -10)
    public static class SE_Deposit_2_ItemRegister extends SE_DepositItemRegister {
        @Override
        public void defaultConfig() {
            super.defaultConfig();
            successRate = 0.1f;
        }
    }

    @VoluntarilyRegister(priority = -20)
    public static class SE_Deposit_3_ItemRegister extends SE_DepositItemRegister {
        @Override
        public void defaultConfig() {
            super.defaultConfig();
            successRate = 0.2f;
        }
    }

    @VoluntarilyRegister(priority = -30)
    public static class SE_Deposit_4_ItemRegister extends SE_DepositItemRegister {
        @Override
        public void defaultConfig() {
            super.defaultConfig();
            successRate = 0.4f;
        }
    }

    @VoluntarilyRegister(priority = -40)
    public static class SE_Deposit_5_ItemRegister extends SE_DepositItemRegister {
        @Override
        public void defaultConfig() {
            super.defaultConfig();
            successRate = 0.8f;
        }
    }
}
