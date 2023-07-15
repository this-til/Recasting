package com.til.recasting.common.register.world.item;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

public abstract class SoulItemRegister extends ItemRegister {


    @Override
    protected Item initItem() {
        return new Item(new Item.Properties().group(SlashBlade.SLASHBLADE)) {
            @Override
            public boolean hasEffect(ItemStack stack) {
                return true;
            }
        };
    }

    @VoluntarilyRegister(priority = 160)
    public static class SoulCubeItemRegister extends SoulItemRegister {

        @VoluntarilyRegister
        public static class SoulCubeItemRecipeRegister extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

            @VoluntarilyAssignment
            protected SoulCubeItemRegister soulCubeItemRegister;

            @Override
            protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
                return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                        ListUtil.of(
                                "ABC",
                                "DVE",
                                "FGH"
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfTag(Tags.Items.STORAGE_BLOCKS_COAL.getName()),
                                "B", new IRecipeInItemPack.OfTag(Tags.Items.STORAGE_BLOCKS_IRON.getName()),
                                "C", new IRecipeInItemPack.OfTag(Tags.Items.STORAGE_BLOCKS_GOLD.getName()),
                                "D", new IRecipeInItemPack.OfTag(Tags.Items.STORAGE_BLOCKS_DIAMOND.getName()),
                                "E", new IRecipeInItemPack.OfTag(Tags.Items.STORAGE_BLOCKS_LAPIS.getName()),
                                "F", new IRecipeInItemPack.OfTag(Tags.Items.STORAGE_BLOCKS_EMERALD.getName()),
                                "G", new IRecipeInItemPack.OfTag(Tags.Items.STORAGE_BLOCKS_NETHERITE.getName()),
                                "H", new IRecipeInItemPack.OfTag(Tags.Items.STORAGE_BLOCKS_QUARTZ.getName()),
                                "V", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_crystal))
                        ),
                        new IResultPack.OfItemStack(new ItemStack(soulCubeItemRegister.getItem()))
                );
            }
        }

    }

    @VoluntarilyRegister(priority = 150)
    public static class SoulCubeChangeItemRegister extends SoulItemRegister {

        @VoluntarilyRegister
        public static class SoulCubeChangeItemRecipeRegister extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

            @VoluntarilyAssignment
            protected SoulCubeChangeItemRegister soulCubeChangeItemRegister;

            @VoluntarilyAssignment
            protected SoulCubeItemRegister soulCubeItemRegister;

            @Override
            protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
                return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                        ListUtil.of(
                                "ABA",
                                "BVB",
                                "ABA"
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_trapezohedron)),
                                "B", new IRecipeInItemPack.OfTag(Tags.Items.NETHER_STARS.getName()),
                                "V", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem()))
                        ),
                        new IResultPack.OfItemStack(new ItemStack(soulCubeChangeItemRegister.getItem()))
                );
            }
        }
    }
}
