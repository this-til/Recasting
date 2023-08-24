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
public class MuramasaSlashBladeRegister extends SlashBladeRegister {


    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(SlashBlade.modid, "model/named/muramasa/muramasa.obj");
        texture = new ResourceLocation(SlashBlade.modid, "model/named/muramasa/muramasa.png");
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setTranslationKey("item.slashblade.ex.muramasa");
        slashBladePack.getSlashBladeState().setBaseAttackModifier(8f);
        slashBladePack.getSlashBladeState().setColorCode(new Color(16165059).getRGB());
    }

    @Override
    public boolean displayItem() {
        return false;
    }

    @VoluntarilyRegister
    public static class MuramasaSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

        @VoluntarilyAssignment
        protected MuramasaSlashBladeRegister muramasaSlashBladeRegister;

        @VoluntarilyAssignment
        protected IronSlashBladeRegister ironSlashBladeRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "  #",
                            " # ",
                            "VD "
                    ),
                    MapUtil.of(
                            "#", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.SOUL_SAND)),
                            "D", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_trapezohedron)),
                            "V", new IRecipeInItemPack.OfSlashBladeRegister(ironSlashBladeRegister)
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(muramasaSlashBladeRegister)
            );
        }
    }
}
