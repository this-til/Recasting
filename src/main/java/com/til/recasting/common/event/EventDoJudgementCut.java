package com.til.recasting.common.event;

import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.JudgementCutEntity;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class EventDoJudgementCut extends EventSlashBlade {


    @Nullable
    public final Entity attackEntity;

    public final Vector3d pos;

    public final JudgementCutEntity judgementCutEntity;


    public EventDoJudgementCut(UseSlashBladeEntityPack pack, @Nullable Entity attackEntity, JudgementCutEntity judgementCutEntity) {
        super(pack);
        this.attackEntity = attackEntity;
        this.pos = judgementCutEntity.getPositionVec();
        this.judgementCutEntity = judgementCutEntity;
    }
}
