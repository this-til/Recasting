package com.til.recasting.common.register.world.item;

import com.til.glowing_fire_glow.common.capability.CapabilityProvider;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import com.til.glowing_fire_glow.common.util.StringUtil;
import com.til.recasting.Recasting;
import com.til.recasting.common.capability.CapabilityEvent;
import com.til.recasting.common.capability.IItemSE;
import com.til.recasting.common.register.capability.ItemSE_CapabilityRegister;
import com.til.recasting.common.register.slash_blade.se.AllSE_Register;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;

/**
 * @author til
 */
@StaticVoluntarilyAssignment
@VoluntarilyRegister
public class SE_DepositItemRegister extends ItemRegister {


    @VoluntarilyAssignment
    protected static ItemSE_CapabilityRegister itemSE_CapabilityRegister;

    @VoluntarilyAssignment
    protected static AllSE_Register allSE_register;


    @ConfigField
    protected float successRate;

    @Override
    protected Item initItem() {
        return new SE_DepositItem(new Item.Properties().group(SlashBlade.SLASHBLADE));
    }

    public float getSuccessRate() {
        return successRate;
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        successRate = 0.25f;
    }

    public ItemStack mackItemStack(SE_Register se_register) {
        return mackItemStack(se_register, successRate);
    }


    public ItemStack mackItemStack(SE_Register se_register, float successRate) {
        ItemStack itemStack = new ItemStack(getItem());
        itemStack.getCapability(itemSE_CapabilityRegister.getCapability()).ifPresent(pack -> {
            pack.setSE(se_register);
            pack.setBasicsSuccessRate(successRate);
        });
        return itemStack;
    }


    @StaticVoluntarilyAssignment
    public static class SE_DepositItem extends Item implements CapabilityEvent.ICustomCapability {

        @VoluntarilyAssignment
        protected static SE_DepositItemRegister se_depositItemRegister;

        public SE_DepositItem(Properties properties) {
            super(properties);
        }

        @Override
        public void customCapability(ItemStack itemStack, CapabilityProvider capabilityProvider) {
            capabilityProvider.addCapability(itemSE_CapabilityRegister.getCapability(), new IItemSE.ItemSE());
        }

        @Override
        public boolean hasEffect(ItemStack stack) {
          /*  IItemSE iItemSE = stack.getCapability(itemSE_CapabilityRegister.getCapability()).orElse(null);
            if (iItemSE == null) {
                return false;
            }
            return iItemSE.isProtect();*/
            return true;
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
            allSE_register.forAll()
                    .stream()
                    .sorted(Comparator.comparing(a -> a.getName().toString()))
                    .forEach(seRegister -> {
                        items.add(se_depositItemRegister.mackItemStack(seRegister));
                        items.add(se_depositItemRegister.mackItemStack(seRegister, Integer.MAX_VALUE));
                    });
        }
    }
}
