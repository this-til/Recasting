package com.til.recasting.common.register.slash_blade.instance.original;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.se.instance.FoxWishBasicsSE;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
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

    @VoluntarilyRegister
    public static class FoxBlackLambdaSlashBladeRegister extends SlashBladeRegister {

        @Override
        protected void init() {
            super.init();
            model = new ResourceLocation(SlashBlade.modid, "model/named/sange/sange.obj");
            texture = new ResourceLocation(SlashBlade.modid, "model/named/sange/black.png");
        }

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setTranslationKey("item.slashblade.ex.fox.black.lambda");
            slashBladePack.getSlashBladeState().setBaseAttackModifier(8f);
            slashBladePack.getSlashBladeState().setColorCode(new Color(1118481).getRGB());
        }

        @VoluntarilyRegister
        public static class FoxBlackSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected FoxBlackSlashBladeRegister foxBlackSlashBladeRegister;
            @VoluntarilyAssignment
            public FoxBlackLambdaSlashBladeRegister foxBlackLambdaSlashBladeRegister;
            @VoluntarilyAssignment
            protected FoxWishBasicsSE.BlackFoxWishSE blackFoxWishSE;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {

                SlashBladePack foxBlackSlashBlade = foxBlackSlashBladeRegister.getSlashBladePack();
                foxBlackSlashBlade.getSlashBladeState().setKillCount(200);
                foxBlackSlashBlade.getSlashBladeState().setRefine(50);
                EnchantmentHelper.setEnchantments(
                        MapUtil.of(
                                Enchantments.SMITE, 4,
                                Enchantments.KNOCKBACK, 2,
                                Enchantments.FIRE_ASPECT, 2
                        ),
                        foxBlackSlashBlade.getItemStack()
                );
                foxBlackSlashBlade.getIse().getPack(blackFoxWishSE).setLevel(2);

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                "  A",
                                " V ",
                                "A  "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemSE(blackFoxWishSE, 1),
                                "V", new IRecipeInItemPack.OfSlashBlade(foxBlackSlashBlade)
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(foxBlackLambdaSlashBladeRegister)
                );
            }
        }

    }
}
