package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.recasting.common.data.SlashBladePack;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.event.EventDoJudgementCut;
import com.til.recasting.common.register.entity_type.JudgementCutEntityTypeRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class JudgementCutManage {


    public static void doJudgementCut(LivingEntity user, float hit, int life, @Nullable Vector3d attackPos, @Nullable Entity targetEntity, @Nullable Consumer<JudgementCutEntity> advanceOperation) {
        UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(user);
        if (!useSlashBladeEntityPack.isEffective(SlashBladePack.EffectiveType.canUse)) {
            return;
        }

        World worldIn = user.world;
        if (attackPos == null) {
            attackPos = targetEntity != null ? RayTraceUtil.getPosition(targetEntity) : useSlashBladeEntityPack.getAttackPos();
        }
        JudgementCutEntity jc = new JudgementCutEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(JudgementCutEntityTypeRegister.class).getEntityType(), worldIn, user);
        useSlashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(jc);
        jc.setPosition(attackPos.x, attackPos.y, attackPos.z);
        jc.setShooter(user);
        jc.setColor(useSlashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
        jc.setDamage(hit);
        jc.setMaxLifeTime(life);

        if (advanceOperation != null) {
            advanceOperation.accept(jc);
        }

        EventDoJudgementCut eventDoJudgementCut = new EventDoJudgementCut(useSlashBladeEntityPack, targetEntity, jc);
        MinecraftForge.EVENT_BUS.post(eventDoJudgementCut);

        worldIn.addEntity(jc);
        worldIn.playSound(null, jc.getPosX(), jc.getPosY(), jc.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.5F, 0.8F / (user.getRNG().nextFloat() * 0.4F + 0.8F));
    }

}
