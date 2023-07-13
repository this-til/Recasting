package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.register.recipe.SlashBladeUpRecipeRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.recipe.SlashBladeRecipeRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

@VoluntarilyRegister
public class BlackSlashBladeRegister extends SlashBladeRegister {

    @Override
    protected void init() {
        super.init();
        model = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(BlackSlashBladeRegister.class), "model.obj"));
        texture = new ResourceLocation(getName().getNamespace(), String.join("/", SlashBlade.modid, ResourceLocationUtil.ofPath(BlackSlashBladeRegister.class), "texture.png"));

    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);

        slashBladePack.slashBladeState.setBaseAttackModifier(4f);
        slashBladePack.slashBladeState.setColorCode(0x000000);
    }

    @VoluntarilyRegister
    public static class BlackSlashBladeUpRegister extends SlashBladeRecipeRegister {

        @VoluntarilyAssignment
        protected BlackSlashBladeRegister blackSlashBladeRegister;

        @VoluntarilyAssignment
        protected NamelessSlashBladeRegister namelessSlashBladeRegister;

        @Override
        protected SlashBladeUpRecipeRegister.SlashBladeUpPack defaultConfigSlashBladeUpPack() {
            return new SlashBladeUpRecipeRegister.SlashBladeUpPack(
                    ListUtil.of("AAA", "BCB", "AAA"),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(Items.OBSIDIAN)),
                            "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_sphere)),
                            "C", new IRecipeInItemPack.OfSlashBladeRegister(namelessSlashBladeRegister)),
                    "C",
                    new IResultPack.OfSlashBladeRegister(blackSlashBladeRegister)
            );
        }
    }


}
