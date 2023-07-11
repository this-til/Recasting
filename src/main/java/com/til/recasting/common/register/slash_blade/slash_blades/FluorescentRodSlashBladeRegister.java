package com.til.recasting.common.register.slash_blade.slash_blades;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.event.data.EventSlashBladeUpRecipeData;
import com.til.recasting.common.register.recipe.SlashBladeUpRecipeRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@VoluntarilyRegister
public class FluorescentRodSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected TestSlashBladeRegister testSlashBladeRegister;

    @SubscribeEvent
    protected void onEventSlashBladeUpRecipeData(EventSlashBladeUpRecipeData eventSlashBladeUpRecipeData) {
        eventSlashBladeUpRecipeData.put(getName(), new SlashBladeUpRecipeRegister.SlashBladeUpPack(
                ListUtil.of(" A ", "ABA", " A "),
                MapUtil.of(
                        "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul)),
                        "B", new IRecipeInItemPack.OfSlashBladeRegister(testSlashBladeRegister)),
                "B",
                getSlashBladePack().itemStack
        ));
    }

}
