package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.GlowingFireGlow;
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
import com.til.recasting.common.event.EventDoJudgementCut;
import com.til.recasting.common.register.entity_type.SlashEffectEntityTypeRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.item.crafting.Ingredient;
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
        if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
        Vector3d attackPos = event.pos;
        Random random = event.pack.getEntity().getRNG();
        double x = random.nextDouble() * 2 - 1;
        double y = random.nextDouble() * 2 - 1;
        double z = random.nextDouble() * 2 - 1;
        double desiredLength = 4 * event.pack.getSlashBladePack().getSlashBladeStateSupplement().getAttackDistance() * range.of(se_pack.getLevel());
        x *= desiredLength;
        y *= desiredLength;
        z *= desiredLength;
        Vector3d pos = attackPos.add(x, y, z);
        SlashEffectEntity jc = new SlashEffectEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SlashEffectEntityTypeRegister.class).getEntityType(),
                event.pack.getEntity().world, event.pack.getEntity());
        event.pack.getSlashBladePack().getSlashBladeStateSupplement().decorate(jc);
        jc.setPosition(pos.getX(), pos.getY(), pos.getZ());
        jc.setRoll(random.nextInt(360));
        jc.lookAt(attackPos, false);
        jc.setColor(event.pack.getSlashBladePack().getSlashBladeState().getColorCode());
        jc.setMute(false);
        jc.setDamage((float) attack.of(se_pack.getLevel()));
        jc.setSize((float) (desiredLength / 4));
        event.pack.getEntity().world.addEntity(jc);
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
        attack = new NumberPack(0, 0.3);
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
