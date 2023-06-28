package com.til.recasting.common.mixin;

import com.google.common.collect.Lists;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.event.EventSlashBladeAreaAttack;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.specialattack.JudgementCut;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.KnockBacks;
import mods.flammpfeil.slashblade.util.TargetSelector;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(value = {AttackManager.class}, remap = false)
public class AttackManagerMixin {

    /**
     * @author til
     * @reason
     */
    @Overwrite
    public static EntitySlashEffect doSlash(LivingEntity playerIn, float roll, int colorCode, Vector3d centerOffset, boolean mute, boolean critical, double damage, KnockBacks knockback) {
        ItemStack itemStack = playerIn.getHeldItemMainhand();
        SlashBladePack slashBladePack = new SlashBladePack(itemStack);
        if (slashBladePack.isEffective() && !playerIn.world.isRemote) {
            EventSlashBladeDoSlash eventSlashBladeDoSlash = new EventSlashBladeDoSlash(playerIn, slashBladePack, roll, colorCode, centerOffset, mute, critical, damage, knockback);
            MinecraftForge.EVENT_BUS.post(eventSlashBladeDoSlash);
            roll = eventSlashBladeDoSlash.roll;
            centerOffset = eventSlashBladeDoSlash.centerOffset;
            mute = eventSlashBladeDoSlash.mute;
            critical = eventSlashBladeDoSlash.critical;
            damage = eventSlashBladeDoSlash.damage;
            knockback = eventSlashBladeDoSlash.knockback;
        }
        if (playerIn.world.isRemote) return null;

        Vector3d pos = playerIn.getPositionVec()
                .add(0.0D, (double) playerIn.getEyeHeight() * 0.75D, 0.0D)
                .add(playerIn.getLookVec().scale(0.3f));

        pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, playerIn.getYaw(0)).scale(centerOffset.y))
                .add(VectorHelper.getVectorForRotation(0, playerIn.getYaw(0) + 90).scale(centerOffset.z))
                .add(playerIn.getLookVec().scale(centerOffset.z));

        EntitySlashEffect jc = new EntitySlashEffect(SlashBlade.RegistryEvents.SlashEffect, playerIn.world);
        jc.setPosition(pos.x, pos.y, pos.z);
        jc.setShooter(playerIn);

        jc.setRotationRoll(roll);
        jc.rotationYaw = playerIn.rotationYaw;
        jc.rotationPitch = 0;

        jc.setColor(colorCode);

        jc.setMute(mute);
        jc.setIsCritical(critical);

        jc.setDamage(damage);

        jc.setKnockBack(knockback);

        playerIn.world.addEntity(jc);

        return jc;
    }

    /**
     * @author til
     * @reason
     */
    @Overwrite
    public static List<Entity> areaAttack(LivingEntity playerIn, Consumer<LivingEntity> beforeHit, float ratio, boolean forceHit, boolean resetHit, boolean mute, List<Entity> exclude) {
        ItemStack itemStack = playerIn.getHeldItemMainhand();
        SlashBladePack slashBladePack = new SlashBladePack(itemStack);
        if (slashBladePack.isEffective() && !playerIn.world.isRemote) {
            EventSlashBladeAreaAttack eventSlashBladeAreaAttack = new EventSlashBladeAreaAttack(playerIn, slashBladePack, beforeHit, ratio, forceHit, resetHit, mute, exclude);
            MinecraftForge.EVENT_BUS.post(eventSlashBladeAreaAttack);
            beforeHit = eventSlashBladeAreaAttack.beforeHit;
            ratio = eventSlashBladeAreaAttack.ratio;
            forceHit = eventSlashBladeAreaAttack.forceHit;
            resetHit = eventSlashBladeAreaAttack.resetHit;
            mute = eventSlashBladeAreaAttack.mute;
            exclude = eventSlashBladeAreaAttack.exclude;
        }

        List<Entity> founds = Lists.newArrayList();
        float modifiedRatio = (1.0F + EnchantmentHelper.getSweepingDamageRatio(playerIn) * 0.5f) * ratio;
        AttributeModifier am = new AttributeModifier("SweepingDamageRatio", modifiedRatio, AttributeModifier.Operation.MULTIPLY_BASE);

        if (!playerIn.world.isRemote()) {
            try {
                playerIn.getAttribute(Attributes.ATTACK_DAMAGE).applyNonPersistentModifier(am);

                founds = TargetSelector.getTargettableEntitiesWithinAABB(playerIn.world, playerIn);

                if (exclude != null)
                    founds.removeAll(exclude);

                for (Entity entity : founds) {
                    if (entity instanceof LivingEntity)
                        beforeHit.accept((LivingEntity) entity);

                    AttackManager.doMeleeAttack(playerIn, entity, forceHit, resetHit);
                }

            } finally {
                playerIn.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(am);
            }
        }

        if (!mute) {
            playerIn.world.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5F, 0.4F / (playerIn.getRNG().nextFloat() * 0.4F + 0.8F));
        }

        return founds;
    }

}
