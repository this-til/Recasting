package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.world.item.SoulItemRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

@VoluntarilyRegister
public class HoeSlashBladeRegister extends SlashBladeRegister {

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(5f);
        slashBladePack.getSlashBladeStateSupplement().setDurable(2f);
    }

    @VoluntarilyRegister
    public static class HoeSlashBladeRecipeRegister extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected HoeSlashBladeRegister hoeSlashBladeRegister;

        @VoluntarilyAssignment
        protected SoulItemRegister.SoulCubeItemRegister soulCubeItemRegister;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            " A ",
                            "AVA",
                            " A "
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem())),
                            "V", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.IRON_HOE))),
                    new IResultPack.OfSlashBladeRegister(hoeSlashBladeRegister)
            );
        }
    }

}
