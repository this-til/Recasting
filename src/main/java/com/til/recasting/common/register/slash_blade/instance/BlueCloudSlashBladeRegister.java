package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.se.instance.CooperateWithSE;
import com.til.recasting.common.register.world.item.SoulItemRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

@VoluntarilyRegister
public class BlueCloudSlashBladeRegister extends SlashBladeRegister {

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.slashBladeState.setBaseAttackModifier(6f);
        slashBladePack.slashBladeState.setColorCode(0xA7C683);
        slashBladePack.iSlashBladeStateSupplement.setDurable(4);
    }

    @VoluntarilyRegister
    public static class BlueCloudSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {

        @VoluntarilyAssignment
        protected BlueCloudSlashBladeRegister blueCloudSlashBladeRecipeRegister;

        @VoluntarilyAssignment
        protected BaGuaSlashBladeRegister baGuaSlashBladeRegister;

        @VoluntarilyAssignment
        protected CooperateWithSE cooperateWithSE;

        @VoluntarilyAssignment
        protected SoulItemRegister.SoulCubeItemRegister soulCubeItemRegister;

        @VoluntarilyAssignment
        protected SoulItemRegister.SoulCubeChangeItemRegister soulCubeChangeItemRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {

            SlashBladePack baGuaSlashBlade = baGuaSlashBladeRegister.getSlashBladePack();
            baGuaSlashBlade.slashBladeState.setKillCount(1000);
            baGuaSlashBlade.slashBladeState.setRefine(150);

            EnchantmentHelper.setEnchantments(MapUtil.of(
                            Enchantments.SMITE, 3,
                            Enchantments.BANE_OF_ARTHROPODS, 3,
                            Enchantments.SHARPNESS, 3,
                            Enchantments.EFFICIENCY, 5),
                    baGuaSlashBlade.itemStack);

            baGuaSlashBlade.ise.getPack(cooperateWithSE).setLevel(2);


            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "ABA",
                            "AVA",
                            "ACA"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfItemSE(cooperateWithSE, 2, true),
                            "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeItemRegister.getItem())),
                            "C", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(soulCubeChangeItemRegister.getItem())),
                            "V", new IRecipeInItemPack.OfSlashBlade(baGuaSlashBlade.itemStack)),
                    "V",
                    new IResultPack.OfSlashBladeRegister(blueCloudSlashBladeRecipeRegister)
            );
        }
    }
}
