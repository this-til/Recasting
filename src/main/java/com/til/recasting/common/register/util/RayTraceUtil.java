package com.til.recasting.common.register.util;

import com.google.common.collect.Lists;
import mods.flammpfeil.slashblade.entity.IShootable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static mods.flammpfeil.slashblade.util.TargetSelector.getAreaAttackPredicate;

public class RayTraceUtil {
    /*    */

    /***
     * 进行射线检查
     *//*
    public static RayTraceResult rayTrace(Entity owner, double par1, float par3) {
        Vector3d Vec3d = getPosition(owner);
        Vector3d Vec3d1 = getLook(owner, par3);
        Vector3d Vec3d2 = Vec3d.add(Vec3d1.x * par1, Vec3d1.y * par1, Vec3d1.z * par1);
        return owner.world.rayTraceBlocks(Vec3d, Vec3d2, false, false, true);
    }*/
    public static Vector3d getPosition(Entity owner) {
        return new Vector3d(owner.getPosX(), owner.getPosY() + owner.getEyeHeight(), owner.getPosZ());
    }

    public static Vector3d getLook(Entity owner, float rotMax) {
        float f1;
        float f2;
        float f3;
        float f4;

        if (rotMax == 1.0F) {
            f1 = MathHelper.cos(-owner.rotationYaw * 0.017453292F - (float) Math.PI);
            f2 = MathHelper.sin(-owner.rotationYaw * 0.017453292F - (float) Math.PI);
            f3 = -MathHelper.cos(-owner.rotationPitch * 0.017453292F);
            f4 = MathHelper.sin(-owner.rotationPitch * 0.017453292F);
            return new Vector3d(f2 * f3, f4, f1 * f3);
        } else {
            f1 = owner.prevRotationPitch + (owner.rotationPitch - owner.prevRotationPitch) * rotMax;
            f2 = owner.prevRotationYaw + (owner.rotationYaw - owner.prevRotationYaw) * rotMax;
            f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
            f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);
            return new Vector3d((f4 * f5), f6, (f3 * f5));
        }
    }


}
