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
public class FoxWhiteSlashBladeRegister extends SlashBladeRegister {


    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(SlashBlade.modid, "model/named/sange/sange.obj");
        texture = new ResourceLocation(SlashBlade.modid, "model/named/sange/white.png");
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setTranslationKey("item.slashblade.ex.fox.white");
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
        slashBladePack.getSlashBladeState().setColorCode(new Color(15658734).getRGB());
    }


    @Override
    public boolean displayItem() {
        return false;
    }

    @VoluntarilyRegister
    public static class FoxWhiteSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected FoxWhiteSlashBladeRegister foxWhiteSlashBladeRegister;

        @VoluntarilyAssignment
        public RubySlashBladeRegister rubySlashBladeRegister;


        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            " EF",
                            "BVS",
                            "WQ "
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
                    new IResultPack.OfSlashBladeRegister(foxWhiteSlashBladeRegister)
            );
        }
    }

    @VoluntarilyRegister
    public static class FoxWhiteLambdaSlashBladeRegister extends SlashBladeRegister {

        @Override
        protected void init() {
            super.init();
            model = new ResourceLocation(SlashBlade.modid, "model/named/sange/sange.obj");
            texture = new ResourceLocation(SlashBlade.modid, "model/named/sange/white.png");
        }

        @Override
        protected void defaultItemStackConfig(ItemStack itemStack) {
            super.defaultItemStackConfig(itemStack);
            slashBladePack.getSlashBladeState().setTranslationKey("item.slashblade.ex.fox.white.lambda");
            slashBladePack.getSlashBladeState().setBaseAttackModifier(8f);
            slashBladePack.getSlashBladeState().setColorCode(new Color(15658734).getRGB());
        }

        @VoluntarilyRegister
        public static class FoxWhiteLambdaSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
            @VoluntarilyAssignment
            protected FoxWhiteSlashBladeRegister foxWhiteSlashBladeRegister;
            @VoluntarilyAssignment
            protected FoxWhiteLambdaSlashBladeRegister foxWhiteLambdaSlashBladeRegister;
            @VoluntarilyAssignment
            protected FoxWishBasicsSE.WhiteFoxWishSE whiteFoxWishSE;

            @Override
            protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {

                SlashBladePack foxBlackSlashBlade = foxWhiteSlashBladeRegister.getSlashBladePack();
                foxBlackSlashBlade.getSlashBladeState().setKillCount(200);
                foxBlackSlashBlade.getSlashBladeState().setRefine(50);
                EnchantmentHelper.setEnchantments(
                        MapUtil.of(
                                Enchantments.UNBREAKING, 3,
                                Enchantments.LOOTING, 3,
                                Enchantments.BANE_OF_ARTHROPODS, 2,
                                Enchantments.KNOCKBACK, 2,
                                Enchantments.FIRE_ASPECT, 2
                        ),
                        foxBlackSlashBlade.getItemStack()
                );
                foxBlackSlashBlade.getIse().getPack(whiteFoxWishSE).setLevel(2);

                return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                        ListUtil.of(
                                "  A",
                                " V ",
                                "A  "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemSE(whiteFoxWishSE, 1),
                                "V", new IRecipeInItemPack.OfSlashBlade(foxBlackSlashBlade)
                        ),
                        "V",
                        new IResultPack.OfSlashBladeRegister(foxWhiteLambdaSlashBladeRegister)
                );
            }
        }
    }


}
