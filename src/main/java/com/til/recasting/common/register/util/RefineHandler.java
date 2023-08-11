package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.common.main.IWorldComponent;
import com.til.recasting.common.data.SlashBladePack;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RefineHandler implements IWorldComponent {

    @SubscribeEvent
    public void onAnvilUpdateEvent(AnvilUpdateEvent event) {


        ItemStack leftItemStack = event.getLeft();
        ItemStack rightItemStack = event.getRight();

        if (leftItemStack.isEmpty() || rightItemStack.isEmpty()) {
            return;
        }

        SlashBladePack slashBladePack = new SlashBladePack(leftItemStack);

        if (!slashBladePack.isEffective(SlashBladePack.EffectiveType.isSlashBlade)) {
            return;
        }

        if (!slashBladePack.getItemStack().getItem().getIsRepairable(slashBladePack.getItemStack(), rightItemStack)) {
            return;
        }

        int level = rightItemStack.getHarvestLevel(ToolType.get("proudsoul"), null, null);

        if (level <= 0) {
            return;
        }

        ItemStack result = leftItemStack.copy();
        SlashBladePack resultSlashBladePack = new SlashBladePack(result);

        int cost = rightItemStack.getCount();
        if (slashBladePack.getSlashBladeState().getDamage() > 0) {
            cost = 0;
            while (cost < rightItemStack.getCount() && resultSlashBladePack.getSlashBladeState().getDamage() > 0) {
                cost++;
                resultSlashBladePack.getSlashBladeState().setDamage(resultSlashBladePack.getSlashBladeState().getDamage() - (0.2f + 0.05f * level));
            }
        }
        resultSlashBladePack.getSlashBladeState().setRefine(resultSlashBladePack.getSlashBladeState().getRefine() + cost);
        event.setMaterialCost(cost);
        event.setCost(cost);
        event.setOutput(result);
    }

}
