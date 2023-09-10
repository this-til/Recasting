package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.instance.SilverWingSlashBladeRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import com.til.recasting.common.register.world.item.SoulItemRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.registries.ForgeRegistries;

@VoluntarilyRegister
public class DivinitySE extends SE_Register {

    @VoluntarilyRegister
    public static class DivinityLambdaSE extends SE_Register {

        @VoluntarilyRegister
        public static class DivinityLambdaSE_Recipe extends  SpecialRecipeSerializerRegister.SpecialRecipeRegister {

            @VoluntarilyAssignment
            protected SE_DepositItemRegister se_depositItemRegister;

            @VoluntarilyAssignment
            protected DivinityLambdaSE divinityLambdaSE;

            @VoluntarilyAssignment
            protected DivinitySE divinitySE;

            @VoluntarilyAssignment
            protected SilverWingSlashBladeRegister.SilverWingLambdaSlashBladeRegister.CloudWheelStormSA cloudWheelStormSA;

            @Override
            protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
                return new SpecialRecipeSerializerRegister.SpecialRecipePack (
                        ListUtil.of(
                                " CA",
                                " B ",
                                "AD "
                        ),
                        MapUtil.of(
                                "A", new IRecipeInItemPack.OfItemSE(divinitySE),
                                "B",  new IRecipeInItemPack.OfItemSA(cloudWheelStormSA),
                                "C", new IRecipeInItemPack.OfBiome(ForgeRegistries.BIOMES.getValue(Biomes.MUSHROOM_FIELDS.getLocation())),
                                "D", new IRecipeInItemPack.OfBiome(ForgeRegistries.BIOMES.getValue(Biomes.BADLANDS.getLocation()))
                        ),
                        new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(divinityLambdaSE))
                );
            }
        }

    }

    @VoluntarilyRegister
    public static class DivinitySE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @VoluntarilyAssignment
        protected DivinitySE divinitySE;

        @VoluntarilyAssignment
        protected SoulItemRegister.SoulCubeChangeItemRegister soulCubeChangeItemRegister;

        @VoluntarilyAssignment
        protected SoulItemRegister.SoulCubeItemRegister soulCubeItemRegister;

        @VoluntarilyAssignment
        protected GodWishSE godWishSE;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            "BAB",
                            "CVC",
                            "BAB"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeChangeItemRegister.getItem())),
                            "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem())),
                            "C", new IRecipeInItemPack.OfItemSE(godWishSE),
                            "V", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_ingot))
                    ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(divinitySE))

            );
        }
    }

}
