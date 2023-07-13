package com.til.recasting.common.mixin;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.SlashEffectEntity;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.entity_type.SlashEffectEntityTypeRegister;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.KnockBacks;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

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
        jc.setBaseSize(useSlashBladeEntityPack.slashBladePack.iSlashBladeStateSupplement.getAttackDistance());
        playerIn.world.addEntity(jc);
        return null;
    }


}
