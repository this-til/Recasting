package com.til.recasting.common.event;

import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;

/**
 * @author til
 */
public class EventSlashBladeDoSlash extends EventSlashBlade {
    public float roll;
    public int colorCode;
    public Vector3d centerOffset;
    public boolean mute;
    public boolean critical;
    public double damage;
    public KnockBacks knockback;

    public EventSlashBladeDoSlash(UseSlashBladeEntityPack pack, float roll, int colorCode, Vector3d centerOffset, boolean mute, boolean critical, double damage, KnockBacks knockback) {
        super(pack);
        this.roll = roll;
        this.colorCode = colorCode;
        this.centerOffset = centerOffset;
        this.mute = mute;
        this.critical = critical;
        this.damage = damage;
        this.knockback = knockback;
    }
}
