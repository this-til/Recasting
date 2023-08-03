package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.event.EventSlashBladeSA;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.util.JudgementCutManage;

/***
 * 断罪
 * 触发sa时追加次元斩攻击
 */
@VoluntarilyRegister
public class ConvictSE extends SE_Register {

    @ConfigField
    protected NumberPack attack;

    protected void onEventSlashBladeSA(EventSlashBladeSA event) {
        if (!event.pack.getSlashBladePack().getIse().hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.getSlashBladePack().getIse().getPack(this);
        JudgementCutManage.doJudgementCut(event.pack.getEntity(), (float) attack.of(se_pack.getLevel()), 20, null, null, null);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = new NumberPack(0, 0.1);
    }
}
