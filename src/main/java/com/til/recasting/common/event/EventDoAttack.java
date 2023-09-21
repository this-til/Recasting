package com.til.recasting.common.event;

import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.register.attack_type.AttackType;
import net.minecraft.entity.Entity;

import java.util.List;

/**
 * @author til
 */
public class EventDoAttack extends EventSlashBlade {
    public final Entity target;
    public float modifiedRatio;
    public boolean forceHit;
    public boolean resetHit;
    public List<AttackType> attackTypeList;

    public EventDoAttack(UseSlashBladeEntityPack pack, Entity target, float modifiedRatio, boolean forceHit, boolean resetHit, List<AttackType> attackTypeList) {
        super(pack);
        this.target = target;
        this.modifiedRatio = modifiedRatio;
        this.forceHit = forceHit;
        this.resetHit = resetHit;
        this.attackTypeList = attackTypeList;
    }
}
