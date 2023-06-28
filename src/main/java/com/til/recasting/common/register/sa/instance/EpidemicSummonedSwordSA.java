package com.til.recasting.common.register.sa.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.sa.SA_Register;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import com.til.recasting.common.register.target_selector.TargetSelectorRegister;
import com.til.recasting.util.RayTraceUtil;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/***
 * 疾袭幻影剑
 */
@VoluntarilyRegister
public class EpidemicSummonedSwordSA extends SA_Register {


    @ConfigField
    protected TargetSelectorRegister targetSelectorRegister;

    @ConfigField
    protected int number;


    @VoluntarilyAssignment
    protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

    @Override
    public void trigger(LivingEntity livingEntity, SlashBladePack slashBladePack) {
        World worldIn = livingEntity.world;
        RayTraceResult rayTraceResult;

        @Nullable
        Entity targetEntity = slashBladePack.slashBladeState.getTargetEntity(worldIn);
        rayTraceResult = targetEntity == null ? targetSelectorRegister.selector(livingEntity) : new EntityRayTraceResult(targetEntity);

        Vector3d attackPos = targetEntity == null ? rayTraceResult.getHitVec() : RayTraceUtil.getPosition(targetEntity);

        Vector3d pos = livingEntity.getEyePosition(1.0f)
                .add(VectorHelper.getVectorForRotation(0.0f, livingEntity.getYaw(0) + 90).scale(livingEntity.getRNG().nextBoolean() ? 1 : -1));

        Vector3d dir = attackPos.subtract(pos).normalize();

        for (int i = 0; i < number; i++) {
            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), worldIn, livingEntity);
            summondSwordEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
            summondSwordEntity.lookAt(dir);
            summondSwordEntity.setColor(slashBladePack.slashBladeState.getColorCode());
            summondSwordEntity.setStartDelay(5 + i);
            summondSwordEntity.setLifeTime(100 + 5 + i);
            worldIn.addEntity(summondSwordEntity);

/*            EntityAbstractSummonedSword ss = new EntityAbstractSummonedSword(SlashBlade.RegistryEvents.SummonedSword, worldIn);
            Vector3d pos = livingEntity.getEyePosition(1.0f)
                    .add(VectorHelper.getVectorForRotation(0.0f, livingEntity.getYaw(0) + 90).scale(livingEntity.getRNG().nextBoolean() ? 1 : -1));
            ss.setPosition(pos.x, pos.y, pos.z);
            Vector3d dir = rayTraceResult.getHitVec().subtract(pos).normalize();
            ss.shoot(dir.x, dir.y, dir.z, 3.0f, 0.0f);
            ss.setShooter(livingEntity);
            ss.setColor(slashBladeState.getColorCode());
            ss.setDelay(i);
            worldIn.addEntity(ss);*/
        }

        livingEntity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);

    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        number = 12;
        targetSelectorRegister = defaultTargetSelectorRegister;
    }

    @VoluntarilyAssignment
    protected DefaultTargetSelectorRegister defaultTargetSelectorRegister;
}
