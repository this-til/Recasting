package com.til.recasting.common.event;

import com.til.recasting.common.capability.SlashBladePack;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;

public class EventSlashBladeDoSlash extends EventSlashBlade {
    public float roll;
    public int colorCode;
    public Vector3d centerOffset;
    public boolean mute;
    public boolean critical;
    public double damage;
    public KnockBacks knockback;

    public EventSlashBladeDoSlash(LivingEntity livingEntity, SlashBladePack slashBladePack, float roll, int colorCode, Vector3d centerOffset, boolean mute, boolean critical, double damage, KnockBacks knockback) {
        super(livingEntity, slashBladePack);
        this.roll = roll;
        this.colorCode = colorCode;
        this.centerOffset = centerOffset;
        this.mute = mute;
        this.critical = critical;
        this.damage = damage;
        this.knockback = knockback;
    }
}
