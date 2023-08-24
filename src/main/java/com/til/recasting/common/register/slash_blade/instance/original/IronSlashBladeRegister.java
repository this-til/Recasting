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
public class IronSlashBladeRegister extends SlashBladeRegister {

    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(SlashBlade.modid, "model/named/yamato.obj");
        texture = new ResourceLocation(SlashBlade.modid, "model/blade.png");
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setTranslationKey("item.slashblade.simple.iron");
        slashBladePack.getSlashBladeState().setBaseAttackModifier(4f);
        slashBladePack.getSlashBladeState().setColorCode(new Color(-13421569).getRGB());
    }

    @Override
    public boolean displayItem() {
        return false;
    }

    @VoluntarilyRegister
    public static class IronSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

        @VoluntarilyAssignment
        protected IronSlashBladeRegister ironSlashBladeRegister;

        @VoluntarilyAssignment
        protected WhiteSlashBladeRegister whiteSlashBladeRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            " /S",
                            "LVH",
                            "AG "
                    ),
                    MapUtil.of(
                            "/", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.BLAZE_ROD)),
                            "S", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_sphere)),
                            "L", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.LAPIS_BLOCK)),
                            "H", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.COAL_BLOCK)),
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.STRING)),
                            "G", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.GOLD_INGOT)),
                            "V", new IRecipeInItemPack.OfSlashBladeRegister(whiteSlashBladeRegister)
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(ironSlashBladeRegister)
            );
        }
    }
}
