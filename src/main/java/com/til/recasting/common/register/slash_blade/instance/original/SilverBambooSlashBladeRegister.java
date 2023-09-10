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
import net.minecraftforge.common.Tags;

import java.awt.*;

@VoluntarilyRegister
public class SilverBambooSlashBladeRegister extends SlashBladeRegister {


    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(SlashBlade.modid, "model/named/yamato.obj");
        texture = new ResourceLocation(SlashBlade.modid, "model/silverbamboo.png");
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setTranslationKey("item.slashblade.simple.silverbamboo");
        slashBladePack.getSlashBladeState().setBaseAttackModifier(5f);
        slashBladePack.getSlashBladeState().setDestructable(true);
        slashBladePack.getSlashBladeState().setColorCode(new Color(-13421569).getRGB());
    }

    @Override
    public boolean displayItem() {
        return false;
    }

    @VoluntarilyRegister
    public static class SilverBambooSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

        @VoluntarilyAssignment
        protected SilverBambooSlashBladeRegister silverBambooSlashBladeRegister;

        @VoluntarilyAssignment
        protected BambooSlashBladeRegister bambooSlashBladeRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            " #I",
                            "SVB",
                            "PT "
                    ),
                    MapUtil.of(
                            "#", new IRecipeInItemPack.OfTag(Tags.Items.EGGS.getName()),
                            "I", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.IRON_NUGGET)),
                            "S", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.STRING)),
                            "T", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_tiny)),
                            "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.BLACK_DYE)),
                            "P", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.PAPER)),
                            "V", new IRecipeInItemPack.OfSlashBladeRegister(bambooSlashBladeRegister)
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(silverBambooSlashBladeRegister)
            );
        }
    }
}
