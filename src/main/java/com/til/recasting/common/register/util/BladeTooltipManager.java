package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import mods.flammpfeil.slashblade.util.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

import static mods.flammpfeil.slashblade.event.BladeMaterialTooltips.BLADE_DATA;

/**
 * @author til
 */
public class BladeTooltipManager implements IWorldComponent {

    @SubscribeEvent
    public void onItemTooltipEvent(ItemTooltipEvent event) {
    }

}
