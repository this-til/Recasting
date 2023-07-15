package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.GlowingFireGlow;
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
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.event.EventDoJudgementCut;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.entity_type.SlashEffectEntityTypeRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.util.AttackManager;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/***
 * 断却
 * 触发次元斩之后造成一次大伤害和大范围的劈砍
 */
@VoluntarilyRegister
public class SeverBreakSE extends SE_Register {
    @ConfigField
    protected NumberPack attack;

    @ConfigField
    protected NumberPack range;

    @SubscribeEvent
    protected void onEventDoJudgementCut(EventDoJudgementCut event) {
        if (!event.pack.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
        Vector3d attackPos = event.pos;
        Random random = event.pack.entity.getRNG();
        double x = random.nextDouble() * 2 - 1;
        double y = random.nextDouble() * 2 - 1;
        double z = random.nextDouble() * 2 - 1;
        double desiredLength = 4 * event.pack.slashBladePack.iSlashBladeStateSupplement.getAttackDistance() * range.of(se_pack.getLevel());
        x *= desiredLength;
        y *= desiredLength;
        z *= desiredLength;
        Vector3d pos = attackPos.add(x, y, z);
        SlashEffectEntity jc = new SlashEffectEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SlashEffectEntityTypeRegister.class).getEntityType(),
                event.pack.entity.world, event.pack.entity);
        event.pack.slashBladePack.iSlashBladeStateSupplement.decorate(jc);
        jc.setPosition(pos.getX(), pos.getY(), pos.getZ());
        jc.setRotationRoll(random.nextInt(360));
        jc.lookAt(attackPos, false);
        jc.setColor(event.pack.slashBladePack.slashBladeState.getColorCode());
        jc.setMute(false);
        jc.setIsCritical(true);
        jc.setDamage(attack.of(se_pack.getLevel()));
        jc.setKnockBack(KnockBacks.cancel);
        jc.setBaseSize((float) (desiredLength / 4));
        event.pack.entity.world.addEntity(jc);
       /* AttackManager.doSlash(
                event.pack.entity,
                event.pack.entity.getRNG().nextInt(360),
                event.pack.slashBladePack.slashBladeState.getColorCode(),
                Vector3d.ZERO,
                false,
                true,
                attack.of(se_pack.getLevel()),
                range.of(se_pack.getLevel()),
                KnockBacks.cancel);*/
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = new NumberPack(0, 0.2);
        range = new NumberPack(1, 0.1);
    }

    @VoluntarilyRegister
    public static class SeverBreakSE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {


        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @VoluntarilyAssignment
        protected OverloadSE overloadSE;

        @VoluntarilyAssignment
        protected CooperateWithSE cooperateWithSE;

        @VoluntarilyAssignment
        protected SeverBreakSE severBreakSE;


        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            "AAA",
                            "BVB",
                            "AAA"
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfIngredient(Ingredient.fromItems(SBItems.proudsoul)),
                            "B", new IRecipeInItemPack.OfItemSE(cooperateWithSE),
                            "V", new IRecipeInItemPack.OfItemSE(overloadSE)
                    ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(severBreakSE))
            );
        }
    }
}
