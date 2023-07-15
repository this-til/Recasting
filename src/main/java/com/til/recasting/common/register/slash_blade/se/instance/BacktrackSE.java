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
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 回溯
 * 挥刀的时候恢复耐久
 */
@VoluntarilyRegister
public class BacktrackSE extends SE_Register {

    @ConfigField
    protected NumberPack reply;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.pack.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
        event.pack.slashBladePack.itemStack.setDamage(event.pack.slashBladePack.itemStack.getDamage() - (int) reply.of(se_pack.getLevel()));
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        reply = new NumberPack(2, 3);
    }

    @VoluntarilyRegister
    public static class BacktrackSE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @VoluntarilyAssignment
        protected CooperateWithSE cooperateWithSE;

        @VoluntarilyAssignment
        protected BacktrackSE backtrackSE;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            "BAB",
                            "BVB",
                            "BAB"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_crystal)),
                            "B", new IRecipeInItemPack.OfTag(Tags.Items.INGOTS_NETHERITE.getName()),
                            "V", new IRecipeInItemPack.OfItemSE(cooperateWithSE)),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(backtrackSE))
            );
        }
    }

}
