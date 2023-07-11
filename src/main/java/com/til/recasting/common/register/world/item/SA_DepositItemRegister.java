package com.til.recasting.common.register.world.item;

import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import com.til.glowing_fire_glow.common.util.StringUtil;
import com.til.recasting.common.capability.CapabilityEvent;
import com.til.recasting.common.capability.IItemSA;
import com.til.recasting.common.register.capability.ItemSA_CapabilityRegister;
import com.til.recasting.common.register.sa.AllSARegister;
import com.til.recasting.common.register.sa.SA_Register;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

@StaticVoluntarilyAssignment
@VoluntarilyRegister(priority = 10)
public class SA_DepositItemRegister extends ItemRegister {

    @VoluntarilyAssignment
    protected static ItemSA_CapabilityRegister itemSA_capabilityRegister;

    @VoluntarilyAssignment
    protected static AllSARegister allSARegister;

    @Override
    protected Item initItem() {
        return new SA_DepositItem(new Item.Properties().group(SlashBlade.SLASHBLADE));
    }

    public ItemStack mackItemStack(SA_Register sa_register) {
        ItemStack itemStack = new ItemStack(getItem());
        itemStack.getCapability(itemSA_capabilityRegister.getCapability()).ifPresent(pack -> pack.setSA(sa_register));
        return itemStack;
    }

    @StaticVoluntarilyAssignment
    public static class SA_DepositItem extends Item implements CapabilityEvent.ICustomCapability {

        @VoluntarilyAssignment
        protected static SA_DepositItemRegister sa_depositItemRegister;


        public SA_DepositItem(Properties properties) {
            super(properties);
        }

        @Override
        public void customCapability(ItemStack itemStack, CapabilityProvider capabilityProvider) {
            capabilityProvider.addCapability(itemSA_capabilityRegister.getCapability(), new IItemSA.ItemSA());
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            stack.getCapability(itemSA_capabilityRegister.getCapability()).ifPresent(pack -> {
                SA_Register sa_register = pack.getSA();
                tooltip.add(new TranslationTextComponent("SA:%s",
                        sa_register == null ? "null" : new TranslationTextComponent(StringUtil.formatLang(pack.getSA().getName()))));
                if (sa_register != null) {
                    tooltip.add(new TranslationTextComponent("§8%s",
                            new TranslationTextComponent(StringUtil.formatLang(sa_register.getName().getNamespace(), sa_register.getName().getPath(), StringFinal.INTRODUCE))));
                }
            });
        }

        @Override
        public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
            if (!this.isInGroup(group)) {
                return;
            }
            for (SA_Register sa_register : allSARegister.forAll()) {
                items.add(sa_depositItemRegister.mackItemStack(sa_register));
            }
        }

        @Override
        public boolean hasEffect(ItemStack stack) {
            return true;
        }

    }

}
