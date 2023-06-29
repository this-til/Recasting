package com.til.recasting.common.register.sa.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.sa.SA_Register;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import com.til.recasting.common.register.target_selector.TargetSelectorRegister;
import com.til.recasting.common.register.util.RayTraceUtil;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.entity.Entity;
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
    public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
        World worldIn = slashBladeEntityPack.entity.world;
        RayTraceResult rayTraceResult;

        @Nullable
        Entity targetEntity = slashBladeEntityPack.slashBladePack.slashBladeState.getTargetEntity(worldIn);
        rayTraceResult = targetEntity == null ? targetSelectorRegister.selector(slashBladeEntityPack.entity) : new EntityRayTraceResult(targetEntity);

        Vector3d attackPos = targetEntity == null ? rayTraceResult.getHitVec() : RayTraceUtil.getPosition(targetEntity);

        Vector3d pos = slashBladeEntityPack.entity.getEyePosition(1.0f)
                .add(VectorHelper.getVectorForRotation(0.0f, slashBladeEntityPack.entity.getYaw(0) + 90).scale(slashBladeEntityPack.entity.getRNG().nextBoolean() ? 1 : -1));

        for (int i = 0; i < number; i++) {
            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), worldIn, slashBladeEntityPack.entity);
            slashBladeEntityPack.slashBladePack.iSlashBladeStateSupplement.decorate(summondSwordEntity);
            summondSwordEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
            summondSwordEntity.lookAt(attackPos, false);
            summondSwordEntity.setColor(slashBladeEntityPack.slashBladePack.slashBladeState.getColorCode());
            summondSwordEntity.setStartDelay(5 + i);
            summondSwordEntity.setLifeTime(100 + 5 + i);
            worldIn.addEntity(summondSwordEntity);
        }

        slashBladeEntityPack.entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);

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
