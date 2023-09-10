package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.item.BladeStandItem;
import net.minecraft.item.Item;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * @author til
 */
public class TooltipManager implements IWorldComponent {


    @SubscribeEvent
    public void onItemTooltipEvent(ItemTooltipEvent event) {
        if (!(event.getItemStack().getItem() instanceof BladeStandItem)) {
            return;
        }
        event.getToolTip().add(new TranslationTextComponent("recasting.introduce.blade_stand"));
        event.getToolTip().add(new TranslationTextComponent("recasting.introduce.blade_stand_2"));
        event.getToolTip().add(new TranslationTextComponent("recasting.introduce.blade_stand_3"));
        event.getToolTip().add(new TranslationTextComponent("recasting.introduce.blade_stand_4"));
        event.getToolTip().add(new TranslationTextComponent("recasting.introduce.blade_stand_5"));
        event.getToolTip().add(new TranslationTextComponent("recasting.introduce.blade_stand_6"));
        event.getToolTip().add(new TranslationTextComponent("recasting.introduce.blade_stand_7"));
    }

}
