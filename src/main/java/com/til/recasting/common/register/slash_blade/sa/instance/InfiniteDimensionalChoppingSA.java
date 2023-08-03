package com.til.recasting.common.register.slash_blade.sa.instance;

import com.til.glowing_fire_glow.common.capability.time_run.TimerCell;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
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

        Entity targetEntity = slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getTargetEntity(slashBladeEntityPack.getEntity().world);
        RayTraceResult rayTraceResult = targetEntity == null ? defaultTargetSelectorRegister.selector(slashBladeEntityPack.getEntity()) : new EntityRayTraceResult(targetEntity);
        Vector3d attackPos = targetEntity == null ? rayTraceResult.getHitVec() : RayTraceUtil.getPosition(targetEntity);

        List<Entity> attackEntity = slashBladeEntityPack.getEntity().world
                .getEntitiesInAABBexcluding(
                        slashBladeEntityPack.getEntity(),
                        new Pos(attackPos).axisAlignedBB(attackRange),
                        entity -> defaultEntityPredicateRegister.canTarget(slashBladeEntityPack.getEntity(), entity));


        for (int i = 0; i < attackNumber; i++) {
            int _delay = delay * i;
            slashBladeEntityPack.getTimeRun().addTimerCell(new TimerCell(() -> {
                while (true) {
                    if (attackEntity.isEmpty()) {
                        JudgementCutManage.doJudgementCut(slashBladeEntityPack.getEntity(), hit, 10, attackPos, null, null);
                        return;
                    }
                    Entity entity = attackEntity.get(slashBladeEntityPack.getEntity().getRNG().nextInt(attackEntity.size()));
                    if (!entity.isAlive()) {
                        attackEntity.remove(entity);
                        continue;
                    }
                    JudgementCutManage.doJudgementCut(slashBladeEntityPack.getEntity(), hit, 10, RayTraceUtil.getPosition(entity), null, null);
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
