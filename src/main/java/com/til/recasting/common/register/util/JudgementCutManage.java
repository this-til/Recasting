package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.event.EventDoJudgementCut;
import com.til.recasting.common.register.entity_type.JudgementCutEntityTypeRegister;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;


public class JudgementCutManage {

    public static JudgementCutEntity doJudgementCut(LivingEntity user, float hit, int life, @Nullable Vector3d attackPos, @Nullable Entity targetEntity) {
        UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(user);
        if (!useSlashBladeEntityPack.isEffective()) {
            return null;
        }


        World worldIn = user.world;
        RayTraceResult rayTraceResult;

        if (attackPos == null) {
            targetEntity = targetEntity == null ? useSlashBladeEntityPack.slashBladePack.slashBladeState.getTargetEntity(worldIn) : targetEntity;
            rayTraceResult = targetEntity == null ? GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(DefaultTargetSelectorRegister.class)
                    .selector(user) : new EntityRayTraceResult(targetEntity);

            attackPos = targetEntity == null ? rayTraceResult.getHitVec() : RayTraceUtil.getPosition(targetEntity);


        }
        EventDoJudgementCut eventDoJudgementCut = new EventDoJudgementCut(useSlashBladeEntityPack, targetEntity, attackPos, life, 1f);
        MinecraftForge.EVENT_BUS.post(eventDoJudgementCut);

        hit = eventDoJudgementCut.attack;
        life = eventDoJudgementCut.life;

        JudgementCutEntity jc = new JudgementCutEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(JudgementCutEntityTypeRegister.class).getEntityType(), worldIn, user);
        jc.setPosition(attackPos.x, attackPos.y, attackPos.z);
        jc.setShooter(user);
        jc.setColor(useSlashBladeEntityPack.slashBladePack.slashBladeState.getColorCode());
        jc.setDamage(hit);
        jc.setLifetime(life);
        worldIn.addEntity(jc);
        worldIn.playSound(null, jc.getPosX(), jc.getPosY(), jc.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.5F, 0.8F / (user.getRNG().nextFloat() * 0.4F + 0.8F));
        return jc;
    }

}
