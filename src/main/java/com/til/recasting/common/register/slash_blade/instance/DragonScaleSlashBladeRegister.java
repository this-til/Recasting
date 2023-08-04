package com.til.recasting.common.register.slash_blade.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import com.til.recasting.common.register.target_selector.TargetSelectorRegister;
import com.til.recasting.common.register.util.RayTraceUtil;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.awt.*;

@VoluntarilyRegister
public class DragonScaleSlashBladeRegister extends SlashBladeRegister {


    @VoluntarilyAssignment
    protected EpidemicSummonedSwordSA epidemicSummonedSwordSA;

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setEffectColor(new Color(0xB97910));
        slashBladePack.getSlashBladeState().setBaseAttackModifier(5f);
        slashBladePack.getSlashBladeStateSupplement().setDurable(5);
        slashBladePack.setSA(epidemicSummonedSwordSA);
    }


    /***
     * 风暴幻影剑
     */
    @VoluntarilyRegister
    public static class EpidemicSummonedSwordSA extends SA_Register {


        @ConfigField
        protected TargetSelectorRegister targetSelectorRegister;

        @ConfigField
        protected int number;


        @VoluntarilyAssignment
        protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;


        @VoluntarilyAssignment
        protected DefaultTargetSelectorRegister defaultTargetSelectorRegister;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            World worldIn = slashBladeEntityPack.getEntity().world;
            RayTraceResult rayTraceResult;

            @Nullable
            Entity targetEntity = slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getTargetEntity(worldIn);
            rayTraceResult = targetEntity == null ? targetSelectorRegister.selector(slashBladeEntityPack.getEntity()) : new EntityRayTraceResult(targetEntity);

            Vector3d attackPos = targetEntity == null ? rayTraceResult.getHitVec() : RayTraceUtil.getPosition(targetEntity);

            Vector3d pos = slashBladeEntityPack.getEntity().getEyePosition(1.0f)
                    .add(VectorHelper.getVectorForRotation(0.0f, slashBladeEntityPack.getEntity().getYaw(0) + 90)
                            .scale(slashBladeEntityPack.getEntity().getRNG().nextBoolean() ? 1 : -1));

            for (int i = 0; i < number; i++) {
                SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), worldIn, slashBladeEntityPack.getEntity());
                slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
                summondSwordEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                summondSwordEntity.lookAt(attackPos, false);
                summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                summondSwordEntity.setStartDelay(i);
                summondSwordEntity.setMaxLifeTime(100 + 5 + i);
                worldIn.addEntity(summondSwordEntity);
            }

            slashBladeEntityPack.getEntity().playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);

        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            number = 12;
            targetSelectorRegister = defaultTargetSelectorRegister;
        }

    }
}

