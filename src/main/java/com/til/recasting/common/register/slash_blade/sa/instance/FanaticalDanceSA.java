package com.til.recasting.common.register.slash_blade.sa.instance;

import com.til.glowing_fire_glow.common.capability.time_run.TimerCell;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.capability.capabilitys.TimeRunCapabilityRegister;
import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.util.math.vector.Vector3d;

/***
 * 乱舞
 */
@VoluntarilyRegister
public class FanaticalDanceSA extends SA_Register {

    @VoluntarilyAssignment
    protected TimeRunCapabilityRegister timeRunCapabilityRegister;


    @ConfigField
    protected int attackNumber;

    @ConfigField
    protected int attackDeviation;

    @ConfigField
    protected float hit;

    @ConfigField
    protected int delay;

    @ConfigField
    protected int offset;

    @Override
    public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
        int number = attackNumber + slashBladeEntityPack.entity.getRNG().nextInt(attackDeviation);
        for (int i = 0; i < number; i++) {
            int _delay = delay * i;
            slashBladeEntityPack.timeRun.addTimerCell(new TimerCell(() -> {
                AttackManager.doSlash(
                        slashBladeEntityPack.entity,
                        slashBladeEntityPack.entity.getRNG().nextInt(360),
                        new Vector3d(slashBladeEntityPack.entity.getRNG().nextFloat() - 0.5f, slashBladeEntityPack.entity.getRNG().nextFloat() - 0.5f, 0).scale(offset),
                        false,
                        true,
                        hit
                );
            }, _delay, 0));
        }
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attackNumber = 15;
        attackDeviation = 3;
        hit = 0.4f;
        delay = 1;
        offset = 3;
    }
}
