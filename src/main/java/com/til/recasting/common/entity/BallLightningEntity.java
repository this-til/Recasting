package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.capability.time_run.TimerCell;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.capability.capabilitys.TimeRunCapabilityRegister;
import com.til.glowing_fire_glow.common.register.overall_config.instance.LightningSeepOverallConfigRegister;
import com.til.glowing_fire_glow.common.register.particle_register.particle_registers.LightningParticleRegister;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.RandomUtil;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.register.attack_type.instance.LightningAttackType;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.util.AttackManager;
import com.til.recasting.common.register.util.HitAssessment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

@StaticVoluntarilyAssignment
public class BallLightningEntity extends DriveEntity {

    protected static final Random RANDOM = new Random();

    @VoluntarilyAssignment
    protected static DefaultEntityPredicateRegister defaultEntityPredicateRegister;

    @VoluntarilyAssignment
    protected static LightningParticleRegister lightningParticleRegister;

    @VoluntarilyAssignment
    protected static TimeRunCapabilityRegister timeRunCapabilityRegister;

    @VoluntarilyAssignment
    protected static LightningAttackType lightningAttackType;

    @VoluntarilyAssignment
    protected static LightningSeepOverallConfigRegister lightningSeepOverallConfigRegister;


    public BallLightningEntity(EntityType<? extends SlashEffectEntity> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn, shooting);
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote) {
            return;
        }
        float size  = getSize() / 10;
        lightningParticleRegister.add(
                world,
                new GlowingFireGlowColor[]{new GlowingFireGlowColor(getColor())},
                1,
                null,
                new Pos(this).add(new Pos(RandomUtil.nextVector3dInCircles(rand, size))),
                new Pos(this).add(new Pos(RandomUtil.nextVector3dInCircles(rand, size)))
        );
    }

    @Override
    protected void onAttack() {
        LivingEntity entity = getShooter();
        if (entity == null) {
            return;
        }
        List<Entity> founds = HitAssessment.getTargettableEntitiesWithinAABB(world, getShooter(), this, getSize());
        if (founds.isEmpty()) {
            /*lightningParticleRegister.add(
                    world,
                    new GlowingFireGlowColor[]{new GlowingFireGlowColor(getColor())},
                    1,
                    null,
                    new Pos(this),
                    new Pos(this).add(new Pos(RandomUtil.nextVector3dInCircles(RANDOM, getSize()))));*/
            return;
        }
        Entity attackEntity = founds.get(RANDOM.nextInt(founds.size()));
        Pos s = new Pos(this);
        Pos entityPos = new Pos(attackEntity);
        lightningParticleRegister.add(
                world,
                new GlowingFireGlowColor[]{new GlowingFireGlowColor(getColor())},
                1,
                null,
                s,
                entityPos);
        entity.getCapability(timeRunCapabilityRegister.getCapability()).ifPresent(run -> run.addTimerCell(new TimerCell(
                () -> {
                    if (!attackEntity.getBoundingBox().grow(3).contains(entityPos.vector3d())) {
                        return;
                    }
                    AttackManager.doAttack(
                            entity,
                            attackEntity,
                            getDamage(),
                            true,
                            true,
                            true,
                            ListUtil.of(lightningAttackType)
                    );
                },
                (int) (lightningSeepOverallConfigRegister.getSeep() * s.distance(entityPos)),
                0)));

    }



}
