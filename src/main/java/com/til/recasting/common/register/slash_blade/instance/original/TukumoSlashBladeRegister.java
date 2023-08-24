package com.til.recasting.common.register.slash_blade.instance.original;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

@VoluntarilyRegister
public class TukumoSlashBladeRegister extends SlashBladeRegister {


    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(SlashBlade.modid, "model/named/agito.obj");
        texture = new ResourceLocation(SlashBlade.modid, "model/named/a_tukumo.png");
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setTranslationKey("item.slashblade.ex.tukumo");
        slashBladePack.getSlashBladeState().setBaseAttackModifier(6f);
        slashBladePack.getSlashBladeState().setColorCode(new Color(16165059).getRGB());
    }

    @Override
    public boolean displayItem() {
        return false;
    }

    @VoluntarilyRegister
    public static class TukumoSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

        @VoluntarilyAssignment
        protected TukumoSlashBladeRegister tukumoSlashBladeRegister;
        @VoluntarilyAssignment
        protected IronSlashBladeRegister ironSlashBladeRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "#SD",
                            "RVL",
                            "ISG"
                    ),
                    MapUtil.of(
                            "#", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.EMERALD_BLOCK)),
                            "S", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_sphere)),
                            "D", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.DIAMOND_BLOCK)),
                            "R", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.REDSTONE_BLOCK)),
                            "L", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.LAPIS_BLOCK)),
                            "I", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.IRON_BLOCK)),
                            "G", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.GOLD_BLOCK)),
                            "V", new IRecipeInItemPack.OfSlashBladeRegister(ironSlashBladeRegister)
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(tukumoSlashBladeRegister)
            );
        }
    }
}
