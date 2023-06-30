package com.til.recasting.common.register.target_selector;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.RegisterBasics;
import com.til.recasting.common.register.entity_predicate.EntityPredicateRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class TargetSelectorRegister extends RegisterBasics {

    @ConfigField
    @Nullable
    protected EntityPredicateRegister entityPredicateRegister;

    /***
     * 射线最远到达区域
     */
    @ConfigField
    protected double range;

    /***
     * 搜寻目标
     */
    public RayTraceResult selector(Entity entity, Entity... otherExclude) {
        Vector3d start = entity.getEyePosition(1.0F);
        Vector3d dir = entity.getLookVec();
        Vector3d end = start.add(dir.scale(range));
        boolean hasOtherExclude = otherExclude != null && otherExclude.length > 0;
        List<Entity> otherEntityLift = hasOtherExclude ? Arrays.asList(otherExclude) : null;

        RayTraceResult raytraceresult = entity.world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));

        double entityReach = range;
        if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
            end = raytraceresult.getHitVec();
            entityReach = start.distanceTo(end);
        }
        AxisAlignedBB area = entity.getBoundingBox().expand(dir.scale(entityReach)).grow(1.0D);

        double currentDist = Double.MAX_VALUE;

        Entity resultEntity = null;
        for (Entity foundEntity : entity.world.getEntitiesInAABBexcluding(entity, area, entityPredicateRegister == null ? null : e -> entityPredicateRegister.canTarget(entity, e))) {
            if (hasOtherExclude && otherEntityLift.contains(foundEntity)) {
                continue;
            }
            AxisAlignedBB axisalignedbb = foundEntity.getBoundingBox().grow(0.5F);
            Optional<Vector3d> optional = axisalignedbb.rayTrace(start, end);
            if (!optional.isPresent()) {
                continue;
            }
            double newDist = start.squareDistanceTo(optional.get());
            if (newDist < currentDist) {
                resultEntity = foundEntity;
                currentDist = newDist;
            }
        }
        if (resultEntity == null) {
            return raytraceresult;
        }

        return new EntityRayTraceResult(resultEntity);

    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        range = 32;
    }
}


