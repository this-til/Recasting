package com.til.recasting.common.register.slash_blade.sa.instance;

import com.til.glowing_fire_glow.common.capability.time_run.TimerCell;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.util.JudgementCutManage;
import net.minecraft.util.math.vector.Vector3d;

import java.util.concurrent.atomic.AtomicReference;

/***
 * //次元斩·决
 * 多从次元斩
 */
@VoluntarilyRegister
public class MultipleDimensionalChoppingSA extends SA_Register {

    @ConfigField
    protected int attackNumber;

    @ConfigField
    protected float hit;

    @ConfigField
    protected int delay;

    @Override
    public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
        AtomicReference<Vector3d> pos = new AtomicReference<>();
        for (int i = 0; i < attackNumber; i++) {
            int _delay = delay * i;
            slashBladeEntityPack.getTimeRun().addTimerCell(new TimerCell(() -> {
                AtomicReference<JudgementCutEntity> judgementCutEntity = new AtomicReference<>();
                JudgementCutManage.doJudgementCut(slashBladeEntityPack.getEntity(), hit, 10, pos.get(), null, judgementCutEntity::set);
                if (judgementCutEntity.get() != null) {
                    pos.set(judgementCutEntity.get().getPositionVec());
                }
            }, _delay, 0));
        }
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attackNumber = 4;
        delay = 4;
        hit = 0.7f;
    }
}
