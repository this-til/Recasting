package com.til.recasting.common.register.util;

import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.event.EventSlashBladeAreaAttack;
import mods.flammpfeil.slashblade.ability.ArrowReflector;
import mods.flammpfeil.slashblade.ability.TNTExtinguisher;
import mods.flammpfeil.slashblade.util.TargetSelector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author til
 */
public class AttackManager {

    public static List<Entity> areaAttack(LivingEntity playerIn, Entity slashEffectEntity, Consumer<LivingEntity> beforeHit, float ratio, boolean forceHit, boolean resetHit, boolean mute, List<Entity> exclude) {
        UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(playerIn);
        if (useSlashBladeEntityPack.isEffective()) {
            EventSlashBladeAreaAttack eventSlashBladeAreaAttack = new EventSlashBladeAreaAttack(useSlashBladeEntityPack, slashEffectEntity, beforeHit, ratio, forceHit, resetHit, mute, exclude);
            MinecraftForge.EVENT_BUS.post(eventSlashBladeAreaAttack);
            beforeHit = eventSlashBladeAreaAttack.beforeHit;
            ratio = eventSlashBladeAreaAttack.ratio;
            forceHit = eventSlashBladeAreaAttack.forceHit;
            resetHit = eventSlashBladeAreaAttack.resetHit;
            mute = eventSlashBladeAreaAttack.mute;
            exclude = eventSlashBladeAreaAttack.exclude;
        }

        List<Entity> founds;
        float modifiedRatio =  EnchantmentHelper.getSweepingDamageRatio(playerIn) * ratio;
        founds = TargetSelector.getTargettableEntitiesWithinAABB(playerIn.world, playerIn);
        if (exclude != null) {
            founds.removeAll(exclude);
        }
        for (Entity entity : founds) {
            if (entity instanceof LivingEntity) {
                beforeHit.accept((LivingEntity) entity);
            }
            doAttack(playerIn, entity, modifiedRatio, forceHit, resetHit);
        }
        if (!mute) {
            playerIn.world.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5F, 0.4F / (playerIn.getRNG().nextFloat() * 0.4F + 0.8F));
        }
        return founds;
    }


    static public void doAttack(LivingEntity attacker, Entity target, float modifiedRatio, boolean forceHit, boolean resetHit) {
        UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(attacker);
        if (!useSlashBladeEntityPack.isEffective()) {
            return;
        }
        EventDoAttack eventDoAttack = new EventDoAttack(useSlashBladeEntityPack, target, modifiedRatio, forceHit, resetHit);
        MinecraftForge.EVENT_BUS.post(eventDoAttack);
        modifiedRatio = eventDoAttack.modifiedRatio;
        forceHit = eventDoAttack.forceHit;
        resetHit = eventDoAttack.resetHit;

        if (forceHit) {
            target.hurtResistantTime = 0;
        }

        AttributeModifier am = new AttributeModifier("RankDamageBonus", useSlashBladeEntityPack.getDamageRatio(modifiedRatio), AttributeModifier.Operation.ADDITION);
        try {
            useSlashBladeEntityPack.slashBladePack.slashBladeState.setOnClick(true);
            attacker.getAttribute(Attributes.ATTACK_DAMAGE).applyNonPersistentModifier(am);
            if (attacker instanceof PlayerEntity) {
                ((PlayerEntity) attacker).attackTargetEntityWithCurrentItem(target);
            } else {
                DamageSource damageSource = DamageSource.causeMobDamage(attacker);
                target.attackEntityFrom(damageSource, (float) attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
            }
        } finally {
            attacker.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(am);
            useSlashBladeEntityPack.slashBladePack.slashBladeState.setOnClick(false);
        }

        if (resetHit) {
            target.hurtResistantTime = 0;
        }

        ArrowReflector.doReflect(target, attacker);
        TNTExtinguisher.doExtinguishing(target, attacker);
    }


}
