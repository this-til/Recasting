package com.til.recasting.common.register.world.item;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.world.item.ItemRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.overall_config.Copy_SA_OverallConfigRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.List;

public abstract class SoulItemRegister extends ItemRegister {


    @VoluntarilyRegister(priority = 160)
    public static class SoulCubeItemRegister extends SoulItemRegister {
        @Override
        protected Item initItem() {
            return new Item(new Item.Properties().group(SlashBlade.SLASHBLADE)) {
                @Override
                public boolean hasEffect(ItemStack stack) {
                    return true;
                }
            };
        }

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

        @VoluntarilyAssignment
        protected Copy_SA_OverallConfigRegister copySaOverallConfigRegister;

        @Override
        protected Item initItem() {
            return new Item(new Item.Properties().group(SlashBlade.SLASHBLADE)) {
                @Override
                public boolean hasEffect(ItemStack stack) {
                    return true;
                }

                @Override
                public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> iTextComponents, ITooltipFlag iTooltipFlag) {
                    super.addInformation(itemStack, world, iTextComponents, iTooltipFlag);
                    NumberFormat fmt = NumberFormat.getPercentInstance();
                    fmt.setMaximumFractionDigits(2);
                    iTextComponents.add(new TranslationTextComponent("recasting.introduce.sa_copy",
                            copySaOverallConfigRegister.getMinRefine(),
                            copySaOverallConfigRegister.getMinKill(),
                            fmt.format(copySaOverallConfigRegister.getGetSuccessRate()),
                            fmt.format(copySaOverallConfigRegister.getLossRefine())));
                }
            };
        }


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
                                "AVA",
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
