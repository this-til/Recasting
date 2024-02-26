package com.til.recasting.common.register.slash_blade.instance.yamazakura;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.register.recipe.SlashBladeRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.instance.original.NamelessSlashBladeRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.awt.*;

@VoluntarilyRegister
public class MinazukiSlashBladeRegister extends SlashBladeRegister {

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(9018536));
    }


    @VoluntarilyRegister
    public static class MinazukiSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected MinazukiSlashBladeRegister minazukiSlashBladeRegister;

        @VoluntarilyAssignment
        protected MutsukiSlashBladeRegister mutsukiSlashBladeRegister;

        @VoluntarilyAssignment
        protected KisaragiSlashBladeRegister kisaragiSlashBladeRegister;

        @VoluntarilyAssignment
        protected YayoiSlashBladeRegister yayoiSlashBladeRegister;

        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {


            SlashBladePack minazukiSlashBlade = mutsukiSlashBladeRegister.getSlashBladePack();
            minazukiSlashBlade.getSlashBladeState().setKillCount(1000);
            minazukiSlashBlade.getSlashBladeState().setRefine(100);

            SlashBladePack kisaragiSlashBlade = kisaragiSlashBladeRegister.getSlashBladePack();
            kisaragiSlashBlade.getSlashBladeState().setKillCount(500);
            kisaragiSlashBlade.getSlashBladeState().setRefine(50);

            SlashBladePack yayoiSlashBlade = yayoiSlashBladeRegister.getSlashBladePack();
            yayoiSlashBlade.getSlashBladeState().setKillCount(500);
            yayoiSlashBlade.getSlashBladeState().setRefine(50);

            return new SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack(
                    ListUtil.of(
                            "AAA",
                            "BVC",
                            "AAA"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfTag(Tags.Items.STORAGE_BLOCKS_EMERALD.getName()),
                            "V", new IRecipeInItemPack.OfSlashBlade(minazukiSlashBlade),
                            "B", new IRecipeInItemPack.OfSlashBlade(kisaragiSlashBlade),
                            "C", new IRecipeInItemPack.OfSlashBlade(yayoiSlashBlade)
                    ),
                    "V",
                    new IResultPack.OfSlashBladeRegister(minazukiSlashBladeRegister)
            );
        }
    }
}
