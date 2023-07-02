package com.til.recasting.common.mixin;

import com.google.common.collect.Multimap;
import com.til.glowing_fire_glow.util.StringUtil;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.register.se.SE_Register;
import com.til.recasting.common.register.util.StringFinal;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author til
 */
@Mixin(value = ItemSlashBlade.class, remap = false)
public class ItemSlashBladeMixin {


    @Inject(method = "getAttributeModifiers",
            at = @At(
                    shift = At.Shift.AFTER,
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/util/LazyOptional;ifPresent(Lnet/minecraftforge/common/util/NonNullConsumer;)V",
                    opcode = 1
            ))
    protected void getAttributeModifiers(EquipmentSlotType slot, ItemStack stack, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> callbackInfoReturnable) {

    }

    @Inject(method = "addInformation",
            at = @At(
                    shift = At.Shift.AFTER,
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/util/LazyOptional;ifPresent(Lnet/minecraftforge/common/util/NonNullConsumer;)V",
                    opcode = 1
            ))
    protected void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn, CallbackInfo callbackInfo) {
        SlashBladePack slashBladePack = new SlashBladePack(stack);
        if (!slashBladePack.isEffective()) {
            return;
        }
        boolean hasSE = false;
        for (Map.Entry<SE_Register, ISE.SE_Pack> se_registerSE_packEntry : slashBladePack.ise.getAllSE().entrySet()) {
            if (se_registerSE_packEntry.getValue().isEmpty()) {
                continue;
            }
            if (!hasSE) {
                tooltip.add(new StringTextComponent("SE:"));
                hasSE = true;
            }
            tooltip.add(
                    new TranslationTextComponent("%s    level:%s/%s",
                            new TranslationTextComponent(StringUtil.formatLang(se_registerSE_packEntry.getKey().getName())),
                            se_registerSE_packEntry.getValue().getLevel(),
                            se_registerSE_packEntry.getKey().getMaxLevel()
                    ));
            tooltip.add(new TranslationTextComponent("  %s",
                    new TranslationTextComponent(StringUtil.formatLang(
                            se_registerSE_packEntry.getKey().getName().getNamespace(),
                            se_registerSE_packEntry.getKey().getName().getPath(),
                            StringFinal.INTRODUCE))));
        }

    }

}
