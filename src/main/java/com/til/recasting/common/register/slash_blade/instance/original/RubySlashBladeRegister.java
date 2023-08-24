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
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

@VoluntarilyRegister
public class RubySlashBladeRegister extends SlashBladeRegister {
    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(SlashBlade.modid, "model/named/yamato.obj");
        texture = new ResourceLocation(SlashBlade.modid, "model/ruby.png");
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setTranslationKey("item.slashblade.ex.ruby");
        slashBladePack.getSlashBladeState().setColorCode(new Color(14684511).getRGB());
        slashBladePack.getSlashBladeState().setBaseAttackModifier(6f);
    }

    @Override
    public boolean displayItem() {
        return false;
    }

    @VoluntarilyRegister
    public static class RubySlashSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected RubySlashBladeRegister rubySlashBladeRegister;


        @VoluntarilyAssignment
        protected SilverBambooSlashBladeRegister silverBambooSlashBladeRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "  X",
                            " X ",
                            "VP "
                    ),
                    MapUtil.of(
                            "X", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_ingot)),
                            "P", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_crystal)),
                            "V", new IRecipeInItemPack.OfSlashBladeRegister(silverBambooSlashBladeRegister)
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(rubySlashBladeRegister)
            );
        }
    }
}
