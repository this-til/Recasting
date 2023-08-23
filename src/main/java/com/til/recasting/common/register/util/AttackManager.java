package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.SlashEffectEntity;
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.event.EventSlashBladeAreaAttack;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.entity_type.SlashEffectEntityTypeRegister;
import mods.flammpfeil.slashblade.ability.ArrowReflector;
import mods.flammpfeil.slashblade.ability.TNTExtinguisher;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author til
 */
public class AttackManager {


    public static void doSlash(LivingEntity playerIn, float roll, int colorCode, Vector3d centerOffset, boolean mute, boolean thump, float damage, float basicsRange, @Nullable Consumer<SlashEffectEntity> advanceOperation) {
        if (playerIn.world.isRemote) {
            return;
        }
        UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(playerIn);
        if (!useSlashBladeEntityPack.isEffective(SlashBladePack.EffectiveType.canUse)) {
            return;
        }

        Vector3d pos = playerIn.getPositionVec()
                .add(0.0D, (double) playerIn.getEyeHeight() * 0.75D, 0.0D)
                .add(playerIn.getLookVec().scale(0.3f));

        pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, playerIn.getYaw(0)).scale(centerOffset.y))
                .add(VectorHelper.getVectorForRotation(0, playerIn.getYaw(0) + 90).scale(centerOffset.z))
                .add(playerIn.getLookVec().scale(centerOffset.z));

        SlashEffectEntity jc = new SlashEffectEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SlashEffectEntityTypeRegister.class).getEntityType(),
                playerIn.world, playerIn);
        useSlashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(jc);
        jc.setPosition(pos.getX(), pos.getY(), pos.getZ());
        jc.setRoll(roll);
        jc.setColor(colorCode);
        jc.setMute(mute);
        jc.setThump(thump);
        jc.setDamage(damage);
        jc.setSize(useSlashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().getAttackDistance() * basicsRange);

        if (advanceOperation != null) {
            advanceOperation.accept(jc);
        }

        EventSlashBladeDoSlash eventSlashBladeDoSlash = new EventSlashBladeDoSlash(useSlashBladeEntityPack, jc, centerOffset, basicsRange);
        MinecraftForge.EVENT_BUS.post(eventSlashBladeDoSlash);

        playerIn.world.addEntity(jc);
    }

    public static List<Entity> areaAttack(LivingEntity playerIn, Entity slashEffectEntity, Consumer<LivingEntity> beforeHit, float range, float ratio, boolean forceHit, boolean resetHit, boolean mute, @Nullable List<Entity> exclude) {
        UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(playerIn);
        if (useSlashBladeEntityPack.isEffective(SlashBladePack.EffectiveType.canUse)) {
            EventSlashBladeAreaAttack eventSlashBladeAreaAttack = new EventSlashBladeAreaAttack(useSlashBladeEntityPack, slashEffectEntity, beforeHit, range, ratio, forceHit, resetHit, mute, exclude);
            MinecraftForge.EVENT_BUS.post(eventSlashBladeAreaAttack);
            range = eventSlashBladeAreaAttack.range;
            beforeHit = eventSlashBladeAreaAttack.beforeHit;
            ratio = eventSlashBladeAreaAttack.ratio;
            forceHit = eventSlashBladeAreaAttack.forceHit;
            resetHit = eventSlashBladeAreaAttack.resetHit;
            mute = eventSlashBladeAreaAttack.mute;
            exclude = eventSlashBladeAreaAttack.exclude;
        }

        List<Entity> founds;
        float modifiedRatio =/* EnchantmentHelper.getSweepingDamageRatio(playerIn) **/ ratio;
        founds = HitAssessment.getTargettableEntitiesWithinAABB(playerIn.world, playerIn, slashEffectEntity, range);
        if (exclude != null) {
            founds.removeAll(exclude);
        }
        for (Entity entity : founds) {
            if (entity instanceof LivingEntity) {
                beforeHit.accept((LivingEntity) entity);
            }
            doAttack(playerIn, entity, modifiedRatio, forceHit, resetHit, true);
        }
        if (!mute) {
            playerIn.world.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5F, 0.4F / (playerIn.getRNG().nextFloat() * 0.4F + 0.8F));
        }
        return founds;
    }


    public static void doAttack(LivingEntity attacker, Entity target, float modifiedRatio, boolean forceHit, boolean resetHit, boolean postEvent) {
        if (modifiedRatio == 0) {
            return;
        }
        UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(attacker);
        if (!useSlashBladeEntityPack.isEffective(SlashBladePack.EffectiveType.canUse)) {
            return;
        }
        if (postEvent) {
            EventDoAttack eventDoAttack = new EventDoAttack(useSlashBladeEntityPack, target, modifiedRatio, forceHit, resetHit);
            MinecraftForge.EVENT_BUS.post(eventDoAttack);
            modifiedRatio = eventDoAttack.modifiedRatio;
            forceHit = eventDoAttack.forceHit;
            resetHit = eventDoAttack.resetHit;
        }

        if (forceHit) {
            target.hurtResistantTime = 0;
        }

        AttributeModifier am = new AttributeModifier("RankDamageBonus", useSlashBladeEntityPack.getDamageRatio(modifiedRatio) - 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        try {
            useSlashBladeEntityPack.getSlashBladePack().getSlashBladeState().setOnClick(true);
            attacker.getAttribute(Attributes.ATTACK_DAMAGE).applyNonPersistentModifier(am);
            if (attacker instanceof PlayerEntity) {
                ((PlayerEntity) attacker).attackTargetEntityWithCurrentItem(target);
            } else {
                DamageSource damageSource = DamageSource.causeMobDamage(attacker);
                target.attackEntityFrom(damageSource, (float) attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
            }

            //DamageSource damageSource = DamageSource.causeMobDamage(attacker);
            //target.attackEntityFrom(damageSource, (float) attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
            //attacker.world.playSound((PlayerEntity) null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, attacker.getSoundCategory(), 1.0F, 1.0F);

        } finally {
            attacker.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(am);
            useSlashBladeEntityPack.getSlashBladePack().getSlashBladeState().setOnClick(false);
        }

        if (resetHit) {
            target.hurtResistantTime = 0;
        }

        ArrowReflector.doReflect(target, attacker);
        TNTExtinguisher.doExtinguishing(target, attacker);
    }


}
