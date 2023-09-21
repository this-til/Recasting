package com.til.recasting.common.event;

import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.register.attack_type.AttackType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.List;
import java.util.function.Consumer;

public class EventSlashBladeAreaAttack extends EventSlashBlade {
    public final Entity slashEffectEntity;
    public Consumer<LivingEntity> beforeHit;
    public float range;
    public float ratio;
    public boolean forceHit;
    public boolean resetHit;
    public boolean mute;
    public List<Entity> exclude;
    public List<AttackType> attackTypeList;

    public EventSlashBladeAreaAttack(UseSlashBladeEntityPack pack, Entity slashEffectEntity, Consumer<LivingEntity> beforeHit, float range, float ratio, boolean forceHit, boolean resetHit, boolean mute, List<Entity> exclude, List<AttackType> attackTypeList) {
        super(pack);
        this.slashEffectEntity = slashEffectEntity;
        this.beforeHit = beforeHit;
        this.range = range;
        this.ratio = ratio;
        this.forceHit = forceHit;
        this.resetHit = resetHit;
        this.mute = mute;
        this.exclude = exclude;
        this.attackTypeList = attackTypeList;
    }
}
