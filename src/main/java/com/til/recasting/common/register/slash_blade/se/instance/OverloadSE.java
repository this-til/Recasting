package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.specialattack.JudgementCut;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 过载
 * 挥刀的时候有小概率触发审判
 */
@VoluntarilyRegister
public class OverloadSE extends SE_Register {

    @ConfigField
    protected NumberPack probability;


    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.pack.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
        if (event.pack.entity.getRNG().nextDouble() >= probability.of(se_pack.getLevel())) {
            return;
        }
        JudgementCut.doJudgementCut(event.pack.entity);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        probability = new NumberPack(0, 0.03);
    }

    @VoluntarilyRegister
    public static class OverloadSE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @VoluntarilyAssignment
        protected CooperateWithSE cooperateWithSE;

        @VoluntarilyAssignment
        protected OverloadSE overloadSE;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            "FAD",
                            "GVH",
                            "CBE"
                    ) ,
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfTag(Tags.Items.BOOKSHELVES.getName()),
                            "B", new IRecipeInItemPack.OfTag(Tags.Items.DYES.getName()),
                            "C", new IRecipeInItemPack.OfTag(Tags.Items.STRING.getName()),
                            "D", new IRecipeInItemPack.OfTag(Tags.Items.SEEDS.getName()),
                            "E", new IRecipeInItemPack.OfTag(Tags.Items.STONE.getName()),
                            "F", new IRecipeInItemPack.OfTag(Tags.Items.SAND_RED.getName()),
                            "G", new IRecipeInItemPack.OfTag(Tags.Items.GEMS_QUARTZ.getName()),
                            "H", new IRecipeInItemPack.OfTag(Tags.Items.MUSHROOMS.getName()),
                            "V", new IRecipeInItemPack.OfItemSE(cooperateWithSE)
                            ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(overloadSE))
            );
        }
    }
}
