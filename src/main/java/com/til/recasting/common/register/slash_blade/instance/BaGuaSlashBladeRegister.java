package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;

import java.awt.*;

@VoluntarilyRegister
public class BaGuaSlashBladeRegister extends SlashBladeRegister {
    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(4f);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(255,255,255));
        slashBladePack.getSlashBladeStateSupplement().setDurable(2);
    }

    @VoluntarilyRegister
    public static class BaGuaSlashBladeRegisterRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected BaGuaSlashBladeRegister baGuaSlashBladeRegister;

        @VoluntarilyAssignment
        protected NamelessSlashBladeRegister namelessSlashBladeRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {
            SlashBladePack slashBladePack = namelessSlashBladeRegister.getSlashBladePack();
            slashBladePack.getSlashBladeState().setRefine(45);
            slashBladePack.getSlashBladeState().setKillCount(300);
            EnchantmentHelper.setEnchantments(MapUtil.of(
                            Enchantments.SMITE, 1,
                            Enchantments.BANE_OF_ARTHROPODS, 1,
                            Enchantments.SHARPNESS, 1),
                    slashBladePack.getItemStack());
            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "  A",
                            " V ",
                            "B  "
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfEntity(EntityType.SKELETON),
                            "B", new IRecipeInItemPack.OfEntity(EntityType.WITHER_SKELETON),
                            "V", new IRecipeInItemPack.OfSlashBlade(slashBladePack.getItemStack())
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(baGuaSlashBladeRegister)
            );
        }
    }

}
