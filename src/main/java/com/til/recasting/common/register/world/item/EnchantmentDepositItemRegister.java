package com.til.recasting.common.register.world.item;

import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import com.til.glowing_fire_glow.common.util.StringUtil;
import com.til.recasting.Recasting;
import com.til.recasting.common.capability.CapabilityEvent;
import com.til.recasting.common.capability.IItemEnchantment;
import com.til.recasting.common.register.capability.ItemEnchantmentCapabilityRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.List;

@VoluntarilyRegister
public class EnchantmentDepositItemRegister extends ItemRegister {

    @VoluntarilyAssignment
    protected ItemEnchantmentCapabilityRegister itemEnchantmentCapabilityRegister;

    @Override
    protected Item initItem() {
        return new EnchantmentItem(new Item.Properties().group(SlashBlade.SLASHBLADE), this);
    }


    public ItemStack mackItemStack(Enchantment enchantment) {
        return mackItemStack(enchantment, 1);
    }

    public ItemStack mackItemStack(Enchantment enchantment, float successRate) {
        ItemStack itemStack = new ItemStack(getItem());
        itemStack.getCapability(itemEnchantmentCapabilityRegister.getCapability()).ifPresent(pack -> {
            pack.setEnchantment(enchantment);
            pack.setBasicsSuccessRate(successRate);
        });
        return itemStack;
    }

    @StaticVoluntarilyAssignment
    public static class EnchantmentItem extends Item implements CapabilityEvent.ICustomCapability {

        @VoluntarilyAssignment
        protected static ItemEnchantmentCapabilityRegister itemEnchantmentCapabilityRegister;

        protected final EnchantmentDepositItemRegister enchantment_depositItemRegister;


        public EnchantmentItem(Properties properties, EnchantmentDepositItemRegister enchantment_depositItemRegister) {
            super(properties);
            this.enchantment_depositItemRegister = enchantment_depositItemRegister;
        }

        @Override
        public boolean hasEffect(ItemStack stack) {
            return true;
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            super.addInformation(stack, worldIn, tooltip, flagIn);

            stack.getCapability(itemEnchantmentCapabilityRegister.getCapability()).ifPresent(pack -> {
                        tooltip.add(new TranslationTextComponent("Enchantment:%s",
                                new TranslationTextComponent(StringUtil.formatLang(pack.getEnchantment().getName()))));
                        NumberFormat fmt = NumberFormat.getPercentInstance();
                        fmt.setMaximumFractionDigits(2);
                        tooltip.add(new TranslationTextComponent("%s:%s",
                                new TranslationTextComponent(StringUtil.formatLang(Recasting.MOD_ID, "basics_success_rate")),
                                new StringTextComponent(fmt.format(pack.getBasicsSuccessRate()))));
                        //tooltip.add(new TranslationTextComponent(pack.isProtect() ? StringUtil.formatLang(Recasting.MOD_ID, "protect.true") : StringUtil.formatLang(Recasting.MOD_ID, "protect.false")));
                        tooltip.add(new TranslationTextComponent("recasting.introduce.enchantment_up"));

                    }
            );

        }

        @Override
        public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
            if (!this.isInGroup(group)) {
                return;
            }
            for (Enchantment value : ForgeRegistries.ENCHANTMENTS.getValues()) {
                items.add(enchantment_depositItemRegister.mackItemStack(value));
            }
        }

        @Override
        public void customCapability(ItemStack itemStack, CapabilityProvider capabilityProvider) {
            capabilityProvider.addCapability(itemEnchantmentCapabilityRegister.getCapability(), new IItemEnchantment.ItemEnchantment());
        }
    }
}
