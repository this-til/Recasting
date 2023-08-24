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
public class BambooSlashBladeRegister extends SlashBladeRegister {


    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(SlashBlade.modid, "model/named/yamato.obj");
        texture = new ResourceLocation(SlashBlade.modid, "model/bamboo.png");
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setTranslationKey("item.slashblade.simple.bamboo");
        slashBladePack.getSlashBladeState().setBaseAttackModifier(1f);
        slashBladePack.getSlashBladeState().setDestructable(true);
        slashBladePack.getSlashBladeState().setColorCode(new Color(-13421569).getRGB());
    }

    @Override
    public boolean displayItem() {
        return false;
    }

    @VoluntarilyRegister
    public static class BambooSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

        @VoluntarilyAssignment
        protected BambooSlashBladeRegister bambooSlashBladeRegister;

        @VoluntarilyAssignment
        protected WoodSlashBladeRegister simpleWoodSlashBladeRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "  A",
                            " A ",
                            "VB "
                    ),
                    MapUtil.of(
                            "V", new IRecipeInItemPack.OfSlashBladeRegister(simpleWoodSlashBladeRegister),
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.BAMBOO)),
                            "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_tiny))
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(bambooSlashBladeRegister)
            );
        }
    }
}
