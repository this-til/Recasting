package com.til.recasting.common.event;

import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import net.minecraftforge.eventbus.api.Event;

/***
 * 对应拔刀剑发生的一些事件
 * @author til
 */
public abstract class EventSlashBlade extends Event {

    public final UseSlashBladeEntityPack pack;

    public EventSlashBlade(UseSlashBladeEntityPack pack) {
        this.pack = pack;
    }
}
