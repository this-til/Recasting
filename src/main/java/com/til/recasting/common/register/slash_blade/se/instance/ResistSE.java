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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 抵抗
 * 挥刀的时候获得伤害吸收
 */
@VoluntarilyRegister
public class ResistSE extends SE_Register {


    @ConfigField
    protected NumberPack level;

    @ConfigField
    protected NumberPack time;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
        event.pack.getEntity().addPotionEffect(new EffectInstance(Effects.ABSORPTION, (int) time.of(se_pack.getLevel()), (int) level.of(se_pack.getLevel())));
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        level = new NumberPack(1, 0);
        time = new NumberPack(1, 1);
    }

    @VoluntarilyRegister
    public static class ResistSE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @VoluntarilyAssignment
        protected CooperateWithSE cooperateWithSE;

        @VoluntarilyAssignment
        protected ResistSE resistSE;

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
                            "B", new IRecipeInItemPack.OfTag(Tags.Items.INGOTS_GOLD.getName()),
                            "V", new IRecipeInItemPack.OfItemSE(cooperateWithSE)),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(resistSE))
            );
        }
    }


}
