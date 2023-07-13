package com.til.recasting.common.register.slash_blade.sa.instance;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.capability.time_run.TimerCell;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import com.til.recasting.common.register.util.JudgementCutManage;
import com.til.recasting.common.register.util.RayTraceUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/***
 * 无限次元斩
 */
@VoluntarilyRegister
public class InfiniteDimensionalChoppingSA extends SA_Register {

    @ConfigField
    protected float attackRange;

    @ConfigField
    protected int attackNumber;

    @ConfigField
    protected float hit;

    @ConfigField
    protected int delay;

    @VoluntarilyAssignment
    protected DefaultTargetSelectorRegister defaultTargetSelectorRegister;


    @VoluntarilyAssignment
    protected DefaultEntityPredicateRegister defaultEntityPredicateRegister;


    @Override
    public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {

        Entity targetEntity = slashBladeEntityPack.slashBladePack.slashBladeState.getTargetEntity(slashBladeEntityPack.entity.world);
        RayTraceResult rayTraceResult = targetEntity == null ? defaultTargetSelectorRegister.selector(slashBladeEntityPack.entity) : new EntityRayTraceResult(targetEntity);
        Vector3d attackPos = targetEntity == null ? rayTraceResult.getHitVec() : RayTraceUtil.getPosition(targetEntity);

        List<Entity> attackEntity = slashBladeEntityPack.entity.world
                .getEntitiesInAABBexcluding(
                        slashBladeEntityPack.entity,
                        new Pos(attackPos).axisAlignedBB(attackRange),
                        entity -> defaultEntityPredicateRegister.canTarget(slashBladeEntityPack.entity, entity));


        for (int i = 0; i < attackNumber; i++) {
            int _delay = delay * i;
            slashBladeEntityPack.timeRun.addTimerCell(new TimerCell(() -> {
                while (true) {
                    if (attackEntity.isEmpty()) {
                        JudgementCutManage.doJudgementCut(slashBladeEntityPack.entity, hit, 10, attackPos, null);
                        return;
                    }
                    Entity entity = attackEntity.get(slashBladeEntityPack.entity.getRNG().nextInt(attackEntity.size()));
                    if (!entity.isAlive()) {
                        attackEntity.remove(entity);
                        continue;
                    }
                    JudgementCutManage.doJudgementCut(slashBladeEntityPack.entity, hit, 10, RayTraceUtil.getPosition(entity), null);
                    return;
                }
            }, _delay, 0));
        }
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attackRange = 12f;
        attackNumber = 12;
        hit = 0.5f;
        delay = 2;
    }
}
