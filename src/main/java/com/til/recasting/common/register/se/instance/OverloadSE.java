package com.til.recasting.common.register.se.instance;

import com.til.glowing_fire_glow.common.CommonPlanRun;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.se.SE_Register;
import mods.flammpfeil.slashblade.specialattack.JudgementCut;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 过载
 * 挥刀的时候有小概率触发SA
 */
@VoluntarilyRegister
public class OverloadSE extends SE_Register {

    @ConfigField
    protected NumberPack probability;


    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.pack.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
        if (event.pack.entity.getRNG().nextDouble() >= probability.of(se_pack.getLevel())) {
            return;
        }
        JudgementCut.doJudgementCut(event.pack.entity);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        probability = new NumberPack(0, 0.01);
    }
}
