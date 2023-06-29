package com.til.recasting.common.register.util;

import com.google.common.collect.Lists;
import mods.flammpfeil.slashblade.entity.IShootable;
import mods.flammpfeil.slashblade.util.TargetSelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class HitAssessment {

    public static <E extends Entity> List<Entity> getTargettableEntitiesWithinAABB(World world, LivingEntity shooter, double reach, E owner) {
        AxisAlignedBB aabb = owner.getBoundingBox().grow(reach);

        List<Entity> list1 = Lists.newArrayList();

        list1.addAll(world.getEntitiesWithinAABB(EnderDragonEntity.class, aabb.grow(5)).stream()
                .flatMap(d -> Arrays.stream(d.getDragonParts()))
                .filter(e -> (e.getDistanceSq(owner) < (reach * reach)))
                .collect(Collectors.toList()));

        EntityPredicate predicate = TargetSelector.getAreaAttackPredicate(0); //reach check has already been completed

        list1.addAll(world.getEntitiesWithinAABB(LivingEntity.class, aabb, null).stream()
                .filter(t -> predicate.canTarget(shooter, t))
                .collect(Collectors.toList()));

        return list1;
    }
}
