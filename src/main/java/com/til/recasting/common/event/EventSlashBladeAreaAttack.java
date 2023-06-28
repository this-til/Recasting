package com.til.recasting.common.event;

import com.til.recasting.common.capability.SlashBladePack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;
import java.util.function.Consumer;

public class EventSlashBladeAreaAttack extends EventSlashBlade {
    public Consumer<LivingEntity> beforeHit;
    public float ratio;
    public boolean forceHit;
    public boolean resetHit;
    public boolean mute;
    public List<Entity> exclude;

    public EventSlashBladeAreaAttack(LivingEntity livingEntity, SlashBladePack slashBladePack, Consumer<LivingEntity> beforeHit, float ratio, boolean forceHit, boolean resetHit, boolean mute, List<Entity> exclude) {
        super(livingEntity, slashBladePack);
        this.beforeHit = beforeHit;
        this.ratio = ratio;
        this.forceHit = forceHit;
        this.resetHit = resetHit;
        this.mute = mute;
        this.exclude = exclude;
    }
}