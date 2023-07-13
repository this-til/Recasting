package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import com.til.recasting.common.register.util.RayTraceUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 生长
 * 挥刀时恢复生命
 */
@VoluntarilyRegister
public class GrowSE extends SE_Register {


    @ConfigField
    protected NumberPack life;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.pack.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
        event.pack.entity.setHealth((float) (event.pack.entity.getHealth() + life.of(se_pack.getLevel())));
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        life = new NumberPack(0, 1);
    }
}
