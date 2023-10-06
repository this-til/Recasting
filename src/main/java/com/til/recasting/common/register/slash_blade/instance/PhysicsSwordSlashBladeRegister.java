package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.slash_blade.sa.instance.EndingYanSakuraSA;
import com.til.recasting.common.register.world.item.SoulItemRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

@VoluntarilyRegister
public class PhysicsSwordSlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected EndingYanSakuraSA endingYanSakuraSA;

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(6f);
        slashBladePack.setSA(endingYanSakuraSA);
    }

    @VoluntarilyRegister
    public static class PhysicsSwordSlashBladeRecipeRegister extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected PhysicsSwordSlashBladeRegister physicsSwordSlashBladeRegister;

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
                            "V", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.TRIDENT))),
                    new IResultPack.OfSlashBladeRegister(physicsSwordSlashBladeRegister)
            );
        }
    }

}
