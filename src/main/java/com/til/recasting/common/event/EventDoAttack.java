package com.til.recasting.common.event;

import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import net.minecraft.entity.Entity;

/**
 * @author til
 */
public class EventDoAttack extends EventSlashBlade {
    public final Entity target;
    public float modifiedRatio;
    public boolean forceHit;
    public boolean resetHit;

    public EventDoAttack(UseSlashBladeEntityPack pack, Entity target, float modifiedRatio, boolean forceHit, boolean resetHit) {
        super(pack);
        this.target = target;
        this.modifiedRatio = modifiedRatio;
        this.forceHit = forceHit;
        this.resetHit = resetHit;
    }
}
