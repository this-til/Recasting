package com.til.recasting.common.event;

import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class EventDoJudgementCut extends EventSlashBlade {


    @Nullable
    public final Entity attackEntity;

    public final Vector3d pos;

    public float attack;

    public EventDoJudgementCut(UseSlashBladeEntityPack pack, @Nullable Entity attackEntity, Vector3d pos, float attack) {
        super(pack);
        this.attackEntity = attackEntity;
        this.pos = pos;
        this.attack = attack;
    }
}
