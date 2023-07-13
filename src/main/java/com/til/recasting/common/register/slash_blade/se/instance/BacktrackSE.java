package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 回溯
 * 挥刀的时候恢复耐久
 */
public class BacktrackSE extends SE_Register {

    @ConfigField
    protected NumberPack reply;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.pack.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
        event.pack.slashBladePack.itemStack.setDamage(event.pack.slashBladePack.itemStack.getDamage() - (int) reply.of(se_pack.getLevel()));
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        reply = new NumberPack(2, 3);
    }
}
