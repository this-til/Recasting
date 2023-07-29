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
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.recipe.SpecialRecipeSerializerRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.util.RayTraceUtil;
import com.til.recasting.common.register.world.item.SE_DepositItemRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 须臾
 * 挥刀时召唤幻影剑协同攻击
 */
@VoluntarilyRegister
public class MomentSE extends SE_Register {

    @ConfigField
    protected NumberPack attack;

    @VoluntarilyAssignment
    protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.pack.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
        SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), event.pack.entity.world, event.pack.entity);
        event.pack.slashBladePack.iSlashBladeStateSupplement.decorate(summondSwordEntity);
        summondSwordEntity.setColor(event.pack.slashBladePack.slashBladeState.getColorCode());
        summondSwordEntity.setDamage((float) attack.of(se_pack.getLevel()));
        summondSwordEntity.setMaxDelay(20);
        summondSwordEntity.lookAt(event.pack.getAttackPos(), false);
        event.pack.entity.world.addEntity(summondSwordEntity);
        event.pack.entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
    }


    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = new NumberPack(0, 0.2);
    }

    @VoluntarilyRegister
    public static class MomentSE_Recipe extends SpecialRecipeSerializerRegister.SpecialRecipeRegister {

        @VoluntarilyAssignment
        protected ImpactSE impactSE;

        @VoluntarilyAssignment
        protected MomentSE momentSE;

        @VoluntarilyAssignment
        protected CooperateWithSE cooperateWithSE;

        @VoluntarilyAssignment
        protected SE_DepositItemRegister se_depositItemRegister;

        @Override
        protected SpecialRecipeSerializerRegister.SpecialRecipePack defaultSpecialRecipePackDelayed() {
            return new SpecialRecipeSerializerRegister.SpecialRecipePack(
                    ListUtil.of(
                            "  B",
                            " V ",
                            "A  "
                    ),
                    MapUtil.of(
                            "A", new IRecipeInItemPack.OfTag(Tags.Items.GEMS_EMERALD.getName()),
                            "V", new IRecipeInItemPack.OfItemSE(cooperateWithSE),
                            "B", new IRecipeInItemPack.OfItemSE(impactSE)
                    ),
                    new IResultPack.OfItemStack(se_depositItemRegister.mackItemStack(momentSE))
            );
        }
    }

}
