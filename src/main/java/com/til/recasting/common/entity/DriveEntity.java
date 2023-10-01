package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.recasting.common.register.attack_type.instance.DriveSwordAttackType;
import com.til.recasting.common.register.util.AttackManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@StaticVoluntarilyAssignment
public class DriveEntity extends SlashEffectEntity {

    @VoluntarilyAssignment
    protected static DriveSwordAttackType driveSwordAttackType;

    protected static final DataParameter<Float> SEEP = EntityDataManager.createKey(DriveEntity.class, DataSerializers.FLOAT);

    protected static final DataParameter<Boolean> PARAMETER = EntityDataManager.createKey(DriveEntity.class, DataSerializers.BOOLEAN);


    public DriveEntity(EntityType<? extends SlashEffectEntity> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn, shooting);
        setMaxLifeTime(40);
        if (shooting != null) {
            rotationYaw = shooting.rotationYaw;
            rotationPitch = shooting.rotationPitch;
            updateMotion(1);
            lookAt(getMotion(), true, true);
        }
        setAttackInterval(1);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SEEP, 4f);
        this.dataManager.register(PARAMETER, false);
    }

    @Override
    public void lookAt(Vector3d target, boolean isDistance, boolean prevSynchronous) {
        super.lookAt(target, isDistance, prevSynchronous);
        updateMotion(getSeep());
    }


    public void updateMotion(float seep) {
        float fYawDtoR = (rotationYaw / 180F) * (float) Math.PI;
        float fPitDtoR = (rotationPitch / 180F) * (float) Math.PI;
        float motionX = -MathHelper.sin(fYawDtoR) * MathHelper.cos(fPitDtoR) * seep;
        float motionY = -MathHelper.sin(fPitDtoR) * seep;
        float motionZ = MathHelper.cos(fYawDtoR) * MathHelper.cos(fPitDtoR) * seep;
        setMotion(new Vector3d(motionX, motionY, motionZ));
    }

    @Override
    public void tick() {
        super.tick();
        Vector3d motionVec = this.getMotion();
        Vector3d positionVec = this.getPositionVec();
        Vector3d movedVec = positionVec.add(motionVec);
        RayTraceResult rayTraceResult = this.world.rayTraceBlocks(new RayTraceContext(positionVec, movedVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        if (rayTraceResult.getType() != RayTraceResult.Type.MISS) {
            hitBlock(rayTraceResult);
        }
        double mx = motionVec.x;
        double my = motionVec.y;
        double mz = motionVec.z;
        this.setPosition(this.getPosX() + mx, this.getPosY() + my, this.getPosZ() + mz);
    }

    @Override
    protected void onAttack() {
        boolean forceHit = true;
        List<Entity> hits = AttackManager.areaAttack(
                getShooter(),
                this,
                entity -> {
                    backRunPack.runBack(attackBackTypeRegister, a -> a.attack(this, entity));
                    affectEntity(entity, 1);
                },
                getSize() * 4, getDamage(),
                forceHit,
                false,
                true,
                alreadyHits,
                ListUtil.of(driveSwordAttackType));
        if (!isMultipleAttack()) {
            alreadyHits.addAll(hits);
        }
    }

    protected void hitBlock(RayTraceResult rayTraceResult) {
        if (!isPenetrate() && ticksExisted > 10) {
            this.setDead();
        }
    }

    @Override
    protected boolean useBlockParticle() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setVelocity(double x, double y, double z) {
        this.setMotion(x, y, z);
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float) (MathHelper.atan2(y, f) * (double) (180F / (float) Math.PI));
            this.rotationYaw = (float) (MathHelper.atan2(x, z) * (double) (180F / (float) Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
        }
    }


    public float getSeep() {
        return this.dataManager.get(SEEP);
    }

    public void setSeep(float seep) {
        this.dataManager.set(SEEP, seep);
        if (!world.isRemote) {
            updateMotion(seep);
        }
    }

    public boolean isPenetrate() {
        return this.dataManager.get(PARAMETER);
    }

    public void setPenetrate(boolean penetrate) {
        this.dataManager.set(PARAMETER, false);
    }

}
