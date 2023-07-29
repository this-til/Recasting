package com.til.recasting.common.event;

import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.SlashEffectEntity;
import net.minecraft.util.math.vector.Vector3d;

/**
 * @author til
 */
public class EventSlashBladeDoSlash extends EventSlashBlade {

    public final SlashEffectEntity slashEffectEntity;
    public final Vector3d centerOffset;
    public final float basicsRange;

    public EventSlashBladeDoSlash(UseSlashBladeEntityPack pack, SlashEffectEntity slashEffectEntity, Vector3d centerOffset, float basicsRange) {
        super(pack);
        this.slashEffectEntity = slashEffectEntity;
        this.centerOffset = centerOffset;
        this.basicsRange = basicsRange;
    }
}
