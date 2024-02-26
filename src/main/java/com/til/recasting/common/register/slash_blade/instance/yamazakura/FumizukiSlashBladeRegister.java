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
import com.til.recasting.common.register.slash_blade.sa.instance.EndingYanSakuraSA;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;

import java.awt.*;

@VoluntarilyRegister
public class FumizukiSlashBladeRegister extends SlashBladeRegister {


    @VoluntarilyAssignment
    protected EndingYanSakuraSA endingYanSakuraSA;

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(8);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(13878383));
        slashBladePack.setSA(endingYanSakuraSA);
    }


    @VoluntarilyRegister
    public static class FumizukiSlashBladeRecipeRegister extends SlashBladeRecipeSerializerRegister.SlashBladeRecipeRegister {
        @VoluntarilyAssignment
        protected FumizukiSlashBladeRegister fumizukiSlashBladeRegister;

        @VoluntarilyAssignment
        protected MinazukiSlashBladeRegister minazukiSlashBladeRegister;

        @VoluntarilyAssignment
        protected UzukiSlashBladeRegister uzukiSlashBladeRegister;

        @VoluntarilyAssignment
        protected SatsukiSlashBladeRegister satsukiSlashBladeRegister;


        @Override
        protected SlashBladeRecipeSerializerRegister.SlashBladeRecipeRecipePack defaultConfigSlashBladeRecipeRecipePack() {


            SlashBladePack minazukiSlashBlade = minazukiSlashBladeRegister.getSlashBladePack();
            minazukiSlashBlade.getSlashBladeState().setKillCount(1000);
            minazukiSlashBlade.getSlashBladeState().setRefine(100);

            SlashBladePack kisaragiSlashBlade = uzukiSlashBladeRegister.getSlashBladePack();
            kisaragiSlashBlade.getSlashBladeState().setKillCount(500);
            kisaragiSlashBlade.getSlashBladeState().setRefine(50);

            SlashBladePack yayoiSlashBlade = satsukiSlashBladeRegister.getSlashBladePack();
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
                    new IResultPack.OfSlashBladeRegister(fumizukiSlashBladeRegister)
            );
        }
    }

}
