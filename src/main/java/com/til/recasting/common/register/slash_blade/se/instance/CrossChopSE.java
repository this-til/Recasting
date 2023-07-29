package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.entity.SlashEffectEntity;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.entity_type.SlashEffectEntityTypeRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import mods.flammpfeil.slashblade.util.KnockBacks;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 十字斩
 * 挥刀时追加一道剑气
 */
@VoluntarilyRegister
public class CrossChopSE extends SE_Register {

    @ConfigField
    protected NumberPack attackRatio;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.pack.slashBladePack.ise.hasSE(this)) {
            return;
        }

        Vector3d pos = event.pack.entity.getPositionVec()
                .add(0.0D, (double) event.pack.entity.getEyeHeight() * 0.75D, 0.0D)
                .add(event.pack.entity.getLookVec().scale(0.3f));

        pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, event.pack.entity.getYaw(0)).scale(event.centerOffset.y))
                .add(VectorHelper.getVectorForRotation(0, event.pack.entity.getYaw(0) + 90).scale(event.centerOffset.z))
                .add(event.pack.entity.getLookVec().scale(event.centerOffset.z));

        ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
        SlashEffectEntity jc = new SlashEffectEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SlashEffectEntityTypeRegister.class).getEntityType(),
                event.pack.entity.world, event.pack.entity);
        event.pack.slashBladePack.iSlashBladeStateSupplement.decorate(jc);
        jc.setPosition(pos.getX(), pos.getY(), pos.getZ());
        jc.setRoll(event.slashEffectEntity.getRoll() + 90);
        jc.setColor(event.pack.slashBladePack.slashBladeState.getColorCode());
        jc.setMute(event.slashEffectEntity.isMute());
        jc.setDamage((float) (event.slashEffectEntity.getDamage() * attackRatio.of(se_pack.getLevel())));
        jc.setBackRunPack(event.slashEffectEntity.getBackRunPack());
        jc.setSize(event.basicsRange);
        event.pack.entity.world.addEntity(jc);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attackRatio = new NumberPack(0, 0.1);
    }
}
