package com.til.recasting.common.register.util;

import com.google.common.collect.Lists;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@StaticVoluntarilyAssignment
public class HitAssessment {

    @VoluntarilyAssignment
    protected static DefaultEntityPredicateRegister defaultEntityPredicateRegister;

    public static List<Entity> getTargettableEntitiesWithinAABB(World world, LivingEntity shooter, Entity owner, double reach) {
        AxisAlignedBB aabb = owner.getBoundingBox().grow(reach);

        List<Entity> list1 = Lists.newArrayList();

        list1.addAll(world.getEntitiesWithinAABB(EnderDragonEntity.class, aabb.grow(5)).stream()
                .flatMap(d -> Arrays.stream(d.getDragonParts()))
                .filter(e -> (e.getDistanceSq(owner) < (reach * reach)))
                .collect(Collectors.toList()));


        list1.addAll(world.getEntitiesWithinAABB(LivingEntity.class, aabb, null).stream()
                .filter(t -> defaultEntityPredicateRegister.canTarget(shooter, t))
                .collect(Collectors.toList()));

        return list1;
    }
}
