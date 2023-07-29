package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.common.capability.time_run.TimerCell;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.MapUtil;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.data.IRecipeInItemPack;
import com.til.recasting.common.data.IResultPack;
import com.til.recasting.common.entity.SlashEffectEntity;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.util.AttackManager;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 协同攻击
 */
@VoluntarilyRegister
public class CooperateWithSE extends SE_Register {

    @ConfigField
    protected NumberPack probability;

    @ConfigField
    protected NumberPack attackRatio;

    @ConfigField
    protected int delay;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.pack.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
        if (event.pack.entity.getRNG().nextDouble() >= probability.of(se_pack.getLevel())) {
            return;
        }
        event.pack.timeRun.addTimerCell(new TimerCell(
                () -> {
                    AttackManager.doSlash(
                            event.pack.entity,
                            event.slashEffectEntity.getRoll(),
                            event.slashEffectEntity.getColor(),
                            event.centerOffset,
                            event.slashEffectEntity.isMute(),
                            event.slashEffectEntity.isThump(),
                            (float) (event.slashEffectEntity.getDamage() * attackRatio.of(se_pack.getLevel())),
                            event.basicsRange,
                            slashEffectEntity -> slashEffectEntity.setBackRunPack(event.slashEffectEntity.getBackRunPack()));
                },
                delay, 0));
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        probability = new NumberPack(0, 0.1);
        attackRatio = new NumberPack(0, 0.1);
        delay = 10;
    }

    @VoluntarilyRegister
    public static class CooperateWithSE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @VoluntarilyAssignment
        protected CooperateWithSE cooperateWithSE;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(

                    ListUtil.of(
                            "AAA",
                            "AVA",
                            "AAA"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfTag(Tags.Items.BOOKSHELVES.getName()),
                            "V", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul_ingot))
                    ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(cooperateWithSE))

            );
        }
    }
}
