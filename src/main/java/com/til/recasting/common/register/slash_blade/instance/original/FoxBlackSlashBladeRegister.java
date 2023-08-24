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
public class FoxBlackSlashBladeRegister extends SlashBladeRegister {


    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(SlashBlade.modid, "model/named/sange/sange.obj");
        texture = new ResourceLocation(SlashBlade.modid, "model/named/sange/black.png");
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setTranslationKey("item.slashblade.ex.fox.black");
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
        slashBladePack.getSlashBladeState().setColorCode(new Color(1118481).getRGB());
    }

    @Override
    public boolean displayItem() {
        return false;
    }

    @VoluntarilyRegister
    public static class FoxBlackSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected FoxBlackSlashBladeRegister foxBlackSlashBladeRegister;
        @VoluntarilyAssignment
        public RubySlashBladeRegister rubySlashBladeRegister;


        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            " QF",
                            "BVS",
                            "WE "
                    ),
                    MapUtil.of(
                            "E", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.OBSIDIAN)),
                            "F", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.FEATHER)),
                            "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.BLAZE_POWDER)),
                            "S", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_crystal)),
                            "W", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.WHEAT)),
                            "Q", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.QUARTZ_BLOCK)),
                            "V", new IRecipeInItemPack.OfSlashBladeRegister(rubySlashBladeRegister)
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(foxBlackSlashBladeRegister)
            );
        }
    }
}
