package com.til.recasting.common.register.se.instance;

import com.til.glowing_fire_glow.common.CommonPlanRun;
import com.til.glowing_fire_glow.common.capability.time_run.TimerCell;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
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
    protected NumberPack attack;

    @ConfigField
    protected int delay;

    @VoluntarilyAssignment
    protected CommonPlanRun commonPlanRun;

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
                () -> AttackManager.doSlash(event.pack.entity, event.roll, event.colorCode, event.centerOffset, event.mute, event.critical, event.damage * attack.of(se_pack.getLevel()), event.knockback),
                delay, 0));
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        probability = new NumberPack(0, 0.1);
        attack = new NumberPack(0, 0.2);
        delay = 10;
    }
}
