package com.til.recasting.common.register.entity_predicate;

import com.til.glowing_fire_glow.common.config.ConfigField;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

import javax.annotation.Nullable;

public abstract class LikeOriginalEntityPredicateRegister extends EntityPredicateRegister {
    @ConfigField
    protected double distance;
    @ConfigField
    protected boolean allowInvulnerable;
    @ConfigField
    protected boolean friendlyFire;
    @ConfigField
    protected boolean ignoresLineOfSight;
    @ConfigField
    protected boolean skipAttackChecks;
    @ConfigField
    protected boolean useVisibilityModifier;

    @ConfigField
    protected boolean isLivingEntity;

    @Override
    public boolean canTarget(@Nullable Entity attacker, @Nullable Entity target) {
        if (target == null) {
            return false;
        }
        if (attacker == target) {
            return false;
        }
        if (target.isSpectator()) {
            return false;
        }
        if (!target.isAlive()) {
            return false;
        }
        if (!this.allowInvulnerable && target.isInvulnerable()) {
            return false;
        }

        if (attacker == null) {
            return true;
        }

        if (isLivingEntity && !(target instanceof LivingEntity)) {
            return false;
        }

        if (!this.skipAttackChecks && attacker instanceof LivingEntity && target instanceof LivingEntity) {
            if (!((LivingEntity) attacker).canAttack(((LivingEntity) target))) {
                return false;
            }
            if (!((LivingEntity) attacker).canAttack(target.getType())) {
                return false;
            }
        }

        if (!this.friendlyFire && attacker.isOnSameTeam(target)) {
            return false;
        }

        if (this.distance > 0.0D) {
            double d0 = this.useVisibilityModifier && target instanceof LivingEntity ? ((LivingEntity) target).getVisibilityMultiplier(attacker) : 1.0D;
            double d1 = Math.max(this.distance * d0, 2.0D);
            double d2 = attacker.getDistanceSq(target.getPosX(), target.getPosY(), target.getPosZ());
            if (d2 > d1 * d1) {
                return false;
            }
        }

        return this.ignoresLineOfSight || !(attacker instanceof MobEntity) || ((MobEntity) attacker).getEntitySenses().canSee(target);
    }


}
