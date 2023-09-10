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
import com.til.recasting.common.event.EventSlashBladeSA;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.util.JudgementCutManage;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.item.crafting.Ingredient;

/***
 * 断罪
 * 触发sa时追加次元斩攻击
 */
@VoluntarilyRegister
public class ConvictSE extends SE_Register {

    @ConfigField
    protected NumberPack attack;

    protected void onEventSlashBladeSA(EventSlashBladeSA event) {
        if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
        JudgementCutManage.doJudgementCut(event.pack.getEntity(), (float) attack.of(se_pack.getLevel()), 20, null, null, null);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = new NumberPack(0, 0.1);
    }

    @VoluntarilyRegister
    public static class ConvictSERecipeRegister extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @VoluntarilyAssignment
        protected ConvictSE convictSE;

        @VoluntarilyAssignment
        protected StormVariantSE stormVariantSE;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            " A ",
                            "BVB",
                            " A "
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_trapezohedron)),
                            "B", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_tiny)),
                            "V", new IRecipeInItemPack.OfItemSE(stormVariantSE)
                    ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(convictSE))
            );
        }
    }
}
