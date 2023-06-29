package com.til.recasting.common.mixin;

import com.google.common.collect.Lists;
import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.SlashEffectEntity;
import com.til.recasting.common.event.EventSlashBladeAreaAttack;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.entity_type.SlashEffectEntityTypeRegister;
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
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author til
 */
@Mixin(value = {AttackManager.class}, remap = false)
public class AttackManagerMixin {

    /**
     * @author til
     * @reason
     */
    @Overwrite
    public static EntitySlashEffect doSlash(LivingEntity playerIn, float roll, int colorCode, Vector3d centerOffset, boolean mute, boolean critical, double damage, KnockBacks knockback) {
        if (playerIn.world.isRemote) {
            return null;
        }
        UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(playerIn);
        if (!useSlashBladeEntityPack.isEffective()) {
            return null;
        }
        EventSlashBladeDoSlash eventSlashBladeDoSlash = new EventSlashBladeDoSlash(useSlashBladeEntityPack, roll, colorCode, centerOffset, mute, critical, damage, knockback);
        MinecraftForge.EVENT_BUS.post(eventSlashBladeDoSlash);
        roll = eventSlashBladeDoSlash.roll;
        centerOffset = eventSlashBladeDoSlash.centerOffset;
        mute = eventSlashBladeDoSlash.mute;
        critical = eventSlashBladeDoSlash.critical;
        damage = eventSlashBladeDoSlash.damage;
        knockback = eventSlashBladeDoSlash.knockback;


        Vector3d pos = playerIn.getPositionVec()
                .add(0.0D, (double) playerIn.getEyeHeight() * 0.75D, 0.0D)
                .add(playerIn.getLookVec().scale(0.3f));

        pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, playerIn.getYaw(0)).scale(centerOffset.y))
                .add(VectorHelper.getVectorForRotation(0, playerIn.getYaw(0) + 90).scale(centerOffset.z))
                .add(playerIn.getLookVec().scale(centerOffset.z));

        SlashEffectEntity jc = new SlashEffectEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SlashEffectEntityTypeRegister.class).getEntityType(),
                playerIn.world, playerIn);
        useSlashBladeEntityPack.slashBladePack.iSlashBladeStateSupplement.decorate(jc);
        jc.setPosition(pos.getX(), pos.getY(), pos.getZ());
        jc.setRotationRoll(roll);
        jc.setColor(colorCode);
        jc.setMute(mute);
        jc.setIsCritical(critical);
        jc.setDamage(damage);
        jc.setKnockBack(knockback);
        playerIn.world.addEntity(jc);
        return null;
    }


}
