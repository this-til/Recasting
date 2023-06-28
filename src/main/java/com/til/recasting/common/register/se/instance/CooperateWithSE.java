package com.til.recasting.common.register.se.instance;

import com.til.glowing_fire_glow.common.CommonPlanRun;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.se.SE_Register;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 协同攻击
 */
@VoluntarilyRegister
public class CooperateWithSE extends SE_Register {

    @ConfigField
    protected NumberPack probability;


    @ConfigField
    protected int delay;

    @VoluntarilyAssignment
    protected CommonPlanRun commonPlanRun;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.slashBladePack.ise.getPack(this);
        if (event.livingEntity.getRNG().nextDouble() >= probability.of(se_pack.getLevel())) {
            return;
        }
        commonPlanRun.add(delay, () -> AttackManager.doSlash(event.livingEntity, event.roll));

    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        probability = new NumberPack(0, 0.1);
        delay = 10;
    }
}
