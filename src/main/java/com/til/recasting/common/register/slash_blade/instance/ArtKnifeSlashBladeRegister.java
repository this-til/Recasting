package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.awt.*;

@VoluntarilyRegister
public class ArtKnifeSlashBladeRegister extends SlashBladeRegister {
    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(108, 108, 108));
        slashBladePack.getSlashBladeState().setBaseAttackModifier(4f);
        slashBladePack.getSlashBladeStateSupplement().setDurable(2);
    }

    @VoluntarilyRegister
    public static class ArtKnifeSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected ArtKnifeSlashBladeRegister artKnifeSlashBladeRegister;

        @VoluntarilyAssignment
        protected BlackSlashBladeRegister blackSlashBladeRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            SlashBladePack blackSlashBlade = blackSlashBladeRegister.getSlashBladePack();
            blackSlashBlade.getSlashBladeState().setRefine(35);
            blackSlashBlade.getSlashBladeState().setKillCount(350);
            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "  A",
                            " A ",
                            "V  "
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_ingot)),
                            "V", new IRecipeInItemPack.OfSlashBlade(blackSlashBlade))
                    , "V",
                    new IResultPack.OfSlashBladeRegister(artKnifeSlashBladeRegister));
        }
    }

}
