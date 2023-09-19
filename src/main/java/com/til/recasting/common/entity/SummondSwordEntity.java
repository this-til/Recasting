package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.Recasting;
import com.til.recasting.common.register.back_type.SummondSwordBackTypeRegister;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.util.AttackManager;
import com.til.recasting.common.register.util.HitAssessment;
import com.til.recasting.common.register.util.StringFinal;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

@StaticVoluntarilyAssignment
public class SummondSwordEntity extends StandardizationAttackEntity {

    @VoluntarilyAssignment
    public static DefaultEntityPredicateRegister defaultEntityPredicateRegister;

    @VoluntarilyAssignment
    public static SummondSwordBackTypeRegister.SummondSwordTransmitBackTypeRegister transmitBackTypeRegister;


    @VoluntarilyAssignment
    public static SummondSwordBackTypeRegister.SummondSwordAttackBackTypeRegister attackBackTypeRegister;

    @VoluntarilyAssignment
    public static SummondSwordBackTypeRegister.SummondSwordAttackBlockTypeRegister attackBlockTypeRegister;

    @VoluntarilyAssignment
    public static SummondSwordBackTypeRegister.SummondSwordAttackEndBackTypeRegister attackEndBackTypeRegister;


    public static final ResourceLocation DEFAULT_MODEL_NAME = new ResourceLocation(SlashBlade.modid, "model/util/ss.obj");
    public static final ResourceLocation DEFAULT_TEXTURE_NAME = new ResourceLocation(Recasting.MOD_ID, String.join("/", StringFinal.SUMMOND_SWORD, StringFinal.TEXTURE));

    protected static final DataParameter<Integer> HIT_ENTITY_ID = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> MAX_PIERCE = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> MAX_DELAY = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> START_DELAY = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Float> SEEP = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.FLOAT);
    @Deprecated
    protected static final DataParameter<Float> ACCELERATION = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.FLOAT);

    /***
     * 表示已经攻击的目标数量
     */
    @Nullable
    protected IntOpenHashSet pierce;

    protected boolean isIgnoringBlock;

    protected boolean inGround;
    protected BlockState inBlockState;

    protected Entity hitEntity = null;

    @OnlyIn(Dist.CLIENT)
    protected double hitX;
    @OnlyIn(Dist.CLIENT)
    protected double hitY;
    @OnlyIn(Dist.CLIENT)
    protected double hitZ;
    @OnlyIn(Dist.CLIENT)
    protected float hitYaw;
    @OnlyIn(Dist.CLIENT)
    protected float hitPitch;

    @OnlyIn(Dist.CLIENT)
    protected boolean recordAttackPos;

    //protected float attackYaw;
    //protected float attackPitch;

    protected boolean isTransmit;

    protected SoundEvent hitEntitySound = SoundEvents.ITEM_TRIDENT_HIT;
    protected SoundEvent hitGroundSound = SoundEvents.ITEM_TRIDENT_HIT_GROUND;

    protected SoundEvent getHitEntitySound() {
        return this.hitEntitySound;
    }

    protected SoundEvent getHitGroundSound() {
        return this.hitGroundSound;
    }

    public SummondSwordEntity(EntityType<? extends SummondSwordEntity> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn, shooting);
        this.setNoGravity(true);


        //设定初始角度等信息
        if (shooting != null) {
            setShooter(shooting);
            float dist = 2.0f;
            double ran = (rand.nextFloat() - 0.5) * 2.0;
            double yaw = Math.toRadians(-shooting.rotationYaw + 90);
            double x = ran * Math.sin(yaw);
            double y = 1.0 - Math.abs(ran);
            double z = ran * Math.cos(yaw);
            x *= dist;
            y *= dist;
            z *= dist;
            setRawPosition(shooting.getPosX() + x, shooting.getPosY() + y, shooting.getPosZ() + z);
            //attackYaw = shooting.rotationYaw;
            //attackPitch = shooting.rotationPitch;
            rotationYaw = shooting.rotationYaw;
            rotationPitch = shooting.rotationPitch;
            updateMotion(1);
            lookAt(getMotion(), true, true);
        }

    }


    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(HIT_ENTITY_ID, -1);
        this.dataManager.register(SEEP, 3.25f);
        //this.dataManager.register(ACCELERATION, 1.05f);
        this.dataManager.register(MAX_PIERCE, 0);
        this.dataManager.register(MAX_DELAY, 20);
        this.dataManager.register(START_DELAY, 0);
    }

    public void lookAt(Vector3d target, boolean isDistance) {
        lookAt(target, isDistance, true);
    }


    public void lookAt(Vector3d target, boolean isDistance, boolean prevSynchronous) {
        Vector3d distance = isDistance ? target : target.subtract(getPositionVec());
        distance = distance.normalize();
        double d0 = distance.x;
        double d1 = distance.y;
        double d2 = distance.z;
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

        //this.attackPitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(d1, d3) * (double) (180F / (float) Math.PI))));
        //this.attackYaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F);

        //this.rotationPitch = MathHelper.wrapDegrees((float) ((MathHelper.atan2(d1, d3)) * (double) (180F / (float) Math.PI)));
        //this.rotationYaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(d0, d2) * (double) (180F / (float) Math.PI)));

        this.rotationPitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(d1, d3) * (double) (180F / (float) Math.PI))));
        this.rotationYaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F);

        if (prevSynchronous) {
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
        }
        updateMotion(getSeep());
    }


    public void updateMotion(float seep) {
        //float fYawDtoR = (attackYaw / 180F) * (float) Math.PI;
        //float fPitDtoR = (attackPitch / 180F) * (float) Math.PI;
        float fYawDtoR = (rotationYaw / 180F) * (float) Math.PI;
        float fPitDtoR = (rotationPitch / 180F) * (float) Math.PI;
        float motionX = -MathHelper.sin(fYawDtoR) * MathHelper.cos(fPitDtoR) * seep;
        float motionY = -MathHelper.sin(fPitDtoR) * seep;
        float motionZ = MathHelper.cos(fYawDtoR) * MathHelper.cos(fPitDtoR) * seep;
        setMotion(new Vector3d(motionX, motionY, motionZ));
    }

    public void normalizeRotation() {
        while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
        }
        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }
        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }
        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = this.getBoundingBox().getAverageEdgeLength() * 10.0D;
        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * getRenderDistanceWeight();
        return distance < d0 * d0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        if (getHitEntity() != null) {
            return;
        }
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setVelocity(double x, double y, double z) {
        if (getHitEntity() != null) {
            return;
        }
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

    @Override
    public void tick() {
        super.tick();

        this.prevRotationPitch = rotationPitch;
        this.prevRotationYaw = rotationYaw;
        this.lastTickPosX = getPosX();
        this.lastTickPosY = getPosY();
        this.lastTickPosZ = getPosZ();

        if (this.ticksExisted <= getStartDelay()) {
            return;
        }
        if (!isTransmit) {
            isTransmit = true;
           /* if (getStartDelay() > 0) {
                updateMotion(getSeep());
            }*/
            backRunPack.runBack(transmitBackTypeRegister, a -> a.transmit(this));
        }

        if (getHitEntity() != null) {
            Entity hits = getHitEntity();

            if (!hits.isAlive()) {
                this.setDead();
                return;
            }

            if (!recordAttackPos) {
                recordAttackPos = true;
                this.hitYaw = this.rotationYaw - hits.rotationYaw;
                this.hitPitch = this.rotationPitch - hits.rotationPitch;
                this.hitX = this.getPosX() - hits.getPosX();
                this.hitY = this.getPosY() - hits.getPosY();
                this.hitZ = this.getPosZ() - hits.getPosZ();
            }

            //this.setPosition(hits.getPosX(), hits.getPosY() + hits.getEyeHeight() * 0.5f, hits.getPosZ());
            double posX = hits.getPosX() + (this.hitX * Math.cos(Math.toRadians(hits.rotationYaw)) - this.hitZ * Math.sin(Math.toRadians(hits.rotationYaw)));
            double posY = hits.getPosY() + this.hitY;
            double posZ = hits.getPosZ() + (this.hitX * Math.sin(Math.toRadians(hits.rotationYaw)) + this.hitZ * Math.cos(Math.toRadians(hits.rotationYaw)));

            setRawPosition(posX, posY, posZ);

            rotationPitch = hits.rotationPitch + this.hitPitch;
            rotationYaw = hits.rotationYaw + this.hitYaw;
            setRotation(rotationYaw, rotationPitch);
            normalizeRotation();

            return;
        }

        /*if (!world.isRemote) {
            setSeep(getSeep() * getAcceleration());
            updateMotion(getSeep());
        }*/

        boolean disallowedHitBlock = this.isIgnoringBlock();

        BlockPos blockpos = new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ());
        BlockState blockstate = this.world.getBlockState(blockpos);
        if (!blockstate.isAir(this.world, blockpos) && !disallowedHitBlock) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.world, blockpos);
            if (!voxelshape.isEmpty()) {
                for (AxisAlignedBB axisalignedbb : voxelshape.toBoundingBoxList()) {
                    if (axisalignedbb.offset(blockpos).contains(new Vector3d(this.getPosX(), this.getPosY(), this.getPosZ()))) {
                        this.inGround = true;
                        setMaxLifeTime(ticksExisted + getMaxDelay());
                        break;
                    }
                }
            }
        }


        if (this.inGround && !disallowedHitBlock) {
            if (this.inBlockState != blockstate && this.world.hasNoCollisions(this.getBoundingBox().grow(0.06D))) {
                //block breaked
                this.setDead();
            }
            return;
        }

        //process pose
        Vector3d motionVec = this.getMotion();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(horizontalMag(motionVec));
            this.rotationYaw = (float) (MathHelper.atan2(motionVec.x, motionVec.z) * (double) (180F / (float) Math.PI));
            this.rotationPitch = (float) (MathHelper.atan2(motionVec.y, f) * (double) (180F / (float) Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }

        //process inAir
        Vector3d positionVec = this.getPositionVec();
        Vector3d movedVec = positionVec.add(motionVec);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(positionVec, movedVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
            movedVec = raytraceresult.getHitVec();
        }

        /*while (this.isAlive()) {
            //todo : replace TargetSelector
            EntityRayTraceResult entityraytraceresult = this.getRayTrace(positionVec, movedVec);
            if (entityraytraceresult != null) {
                raytraceresult = entityraytraceresult;
            }

            if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.ENTITY) {
                assert raytraceresult instanceof EntityRayTraceResult;
                Entity entity = ((EntityRayTraceResult) raytraceresult).getEntity();
                Entity entity1 = this.getShooter();
                if (entity instanceof PlayerEntity && entity1 instanceof PlayerEntity && !((PlayerEntity) entity1).canAttackPlayer((PlayerEntity) entity)) {
                    raytraceresult = null;
                    entityraytraceresult = null;
                }
            }

            if (raytraceresult != null && !disallowedHitBlock && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
                this.isAirBorne = true;
                setMaxLifeTime(getMaxDelay());
            }

            if (entityraytraceresult == null || this.getMaxPierce() <= 0) {
                break;
            }

            raytraceresult = null;
        }*/

        EntityRayTraceResult entityraytraceresult = this.getRayTrace(positionVec, movedVec);
        if (entityraytraceresult != null) {
            raytraceresult = entityraytraceresult;
        }

        if (raytraceresult.getType() == RayTraceResult.Type.ENTITY) {
            assert raytraceresult instanceof EntityRayTraceResult;
            Entity entity = ((EntityRayTraceResult) raytraceresult).getEntity();
            Entity entity1 = this.getShooter();
            if (entity instanceof PlayerEntity && entity1 instanceof PlayerEntity && !((PlayerEntity) entity1).canAttackPlayer((PlayerEntity) entity)) {
                raytraceresult = null;
                entityraytraceresult = null;
            }
        }

        if (raytraceresult != null && !disallowedHitBlock && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
            this.isAirBorne = true;
            if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
                setMaxLifeTime(ticksExisted + getMaxDelay());
            }
        }

        motionVec = this.getMotion();
        double mx = motionVec.x;
        double my = motionVec.y;
        double mz = motionVec.z;
        this.setPosition(this.getPosX() + mx, this.getPosY() + my, this.getPosZ() + mz);

        //TODO 补充关于暴击的效果

          /*  if (this.getIsCritical()) {
                for (int i = 0; i < 4; ++i) {
                    this.world.addParticle(ParticleTypes.CRIT, this.getPosX() + mx * (double) i / 4.0D, this.getPosY() + my * (double) i / 4.0D, this.getPosZ() + mz * (double) i / 4.0D, -mx, -my + 0.2D, -mz);
                }
            }*/


/*            float f4 = MathHelper.sqrt(horizontalMag(motionVec));
            this.rotationYaw = (float) (MathHelper.atan2(mx, mz) * (double) (180F / (float) Math.PI));
            this.rotationPitch = (float) (MathHelper.atan2(my, f4) * (double) (180F / (float) Math.PI));

            while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
                this.prevRotationPitch -= 360.0F;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = MathHelper.lerp(0.5F, this.prevRotationPitch, this.rotationPitch);
            this.rotationYaw = MathHelper.lerp(0.5F, this.prevRotationYaw, this.rotationYaw);*/
        if (this.isInWater()) {
            for (int j = 0; j < 4; ++j) {
                float f3 = 0.25F;
                this.world.addParticle(ParticleTypes.BUBBLE, this.getPosX() - mx * 0.25D, this.getPosY() - my * 0.25D, this.getPosZ() - mz * 0.25D, mx, my, mz);
            }
        }

            /*if (!this.hasNoGravity() && !disallowedHitBlock) {
                Vector3d vec3d3 = this.getMotion();
                this.setMotion(vec3d3.x, vec3d3.y - (double) 0.05F, vec3d3.z);
            }*/

        //this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        this.doBlockCollisions();


    }

    @Override
    protected void setDead() {
        super.setDead();
        this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        if (!this.world.isRemote) {
            if (this.world instanceof ServerWorld) {
                ((ServerWorld) this.world).spawnParticle(ParticleTypes.CRIT, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
            }

            this.burst(null);
        }

        if (getHitEntity() != null) {
            onHitEntity(new EntityRayTraceResult(getHitEntity()), true);
        }
    }

    protected void onHit(RayTraceResult raytraceResultIn) {
        RayTraceResult.Type type = raytraceResultIn.getType();
        switch (type) {
            case ENTITY:
                this.onHitEntity((EntityRayTraceResult) raytraceResultIn, false);
                break;
            case BLOCK:
                this.onHitBlock((BlockRayTraceResult) raytraceResultIn);
                break;
        }
    }

    protected void onHitBlock(BlockRayTraceResult blockraytraceresult) {
        BlockState blockstate = this.world.getBlockState(blockraytraceresult.getPos());
        this.inBlockState = blockstate;
        Vector3d vec3d = blockraytraceresult.getHitVec().subtract(this.getPosX(), this.getPosY(), this.getPosZ());
        this.setMotion(vec3d);
        Vector3d vec3d1 = this.getPositionVec().subtract(vec3d.normalize().scale(0.05F));
        this.setPosition(vec3d1.x, vec3d1.y, vec3d1.z);
        if (!isMute()) {
            this.playSound(this.getHitGroundSound(), 1.0F, 2.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        }
        this.inGround = true;
        this.setMaxLifeTime(this.getMaxDelay());
        this.setMaxPierce((byte) 0);
        backRunPack.runBack(attackBlockTypeRegister, a -> a.attackBlock(this, blockstate, blockraytraceresult.getPos()));
    }

    public void doForceHitEntity(Entity target) {
        doAttackEntity(target, false);
        setHitEntity(target);
    }


    protected void onHitEntity(EntityRayTraceResult entityRayTraceResult, boolean isEnd) {
        Entity targetEntity = entityRayTraceResult.getEntity();

        Entity hits = targetEntity;
       /* if (targetEntity instanceof PartEntity) {
            hits = ((PartEntity<?>) targetEntity).getParent();
        }*/
        if (pierce != null && pierce.contains(targetEntity.getEntityId())) {
            return;
        }
        if (!world.isRemote) {
            doAttackEntity(hits, isEnd);
        }
        if (pierce == null || !this.world.isRemote && this.getMaxPierce() == pierce.size()) {
            setHitEntity(hits);
        }
        if (!isMute()) {
            this.playSound(this.getHitEntitySound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        }

    }

    public void doAttackEntity(Entity target, boolean isEnd) {
        AttackManager.doAttack(getShooter(), target, getDamage(), true, true, true);
        target.hurtResistantTime = 0;
        target.setMotion(0, 0.1, 0);
        if (isEnd) {
            backRunPack.runBack(attackEndBackTypeRegister, a -> a.attack(this, target));
        } else {
            backRunPack.runBack(attackBackTypeRegister, a -> a.attack(this, target));
        }
        if (target instanceof LivingEntity) {
            LivingEntity targetLivingEntity = (LivingEntity) target;

            targetLivingEntity.hurtTime = 0;

            StunManager.setStun(targetLivingEntity);


            EnchantmentHelper.applyThornEnchantments(targetLivingEntity, getShooter());
            EnchantmentHelper.applyArthropodEnchantments(getShooter(), targetLivingEntity);
            //this.arrowHit(targetLivingEntity);

            affectEntity(targetLivingEntity, 1.0f);

            //getShooter().playSound(this.getHitEntityPlayerSound(), 0.18F, 0.45F);

            if (pierce != null && pierce.size() < this.getMaxPierce()) {
                pierce.add(target.getEntityId());
            }
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

    @Deprecated
    public float getAcceleration() {
        return this.dataManager.get(ACCELERATION);
    }

    @Deprecated
    public void setAcceleration(float acceleration) {
        this.dataManager.get(ACCELERATION);
    }

    public int getMaxPierce() {
        return this.getDataManager().get(MAX_PIERCE);
    }

    public void setMaxPierce(int value) {
        this.getDataManager().set(MAX_PIERCE, value);
        if (this.pierce == null) {
            this.pierce = new IntOpenHashSet(value);
        }
    }

    public int getMaxDelay() {
        return this.getDataManager().get(MAX_DELAY);
    }

    public void setMaxDelay(int value) {
        this.getDataManager().set(MAX_DELAY, value);
    }

    protected EntityRayTraceResult getRayTrace(Vector3d p_213866_1_, Vector3d p_213866_2_) {
        return ProjectileHelper.rayTraceEntities(
                this.world,
                this,
                p_213866_1_,
                p_213866_2_,
                this.getBoundingBox().expand(this.getMotion()).grow(1.0D),
                entity -> defaultEntityPredicateRegister.canTarget(getShooter(), entity));
        // (p_213871_1_) -> !p_213871_1_.isSpectator() && p_213871_1_.isAlive() && p_213871_1_.canBeCollidedWith() && (p_213871_1_ != this.getShooter() || this.ticksInAir >= 5) && (this.alreadyHits == null || !this.alreadyHits.contains(p_213871_1_.getEntityId())));
    }

    public void burst(Entity focusEntity) {
        List<Entity> list = HitAssessment.getTargettableEntitiesWithinAABB(this.world, getShooter(), this, 2);
        list.stream().filter(e -> e instanceof LivingEntity).map(e -> (LivingEntity) e).forEach(e -> {
            double distanceSq = this.getDistanceSq(e);
            if (distanceSq < 9.0D) {
                double factor = 1.0D - Math.sqrt(distanceSq) / 4.0D;
                if (e == focusEntity) {
                    factor = 1.0D;
                }
                affectEntity(e, factor);
            }
        });
    }

    public void setHitEntity(Entity hitEntity) {
        if (hitEntity != this) {
            this.dataManager.set(HIT_ENTITY_ID, hitEntity.getEntityId());
        }
    }

    public Entity getHitEntity() {
        if (hitEntity == null) {
            int id = this.dataManager.get(HIT_ENTITY_ID);
            if (0 <= id) {
                hitEntity = this.world.getEntityByID(id);
            }
        }
        return hitEntity;
    }


    public boolean isIgnoringBlock() {
        return isIgnoringBlock;
    }

    public void setIgnoringBlock(boolean ignoringBlock) {
        isIgnoringBlock = ignoringBlock;
    }

    public int getStartDelay() {
        return this.dataManager.get(START_DELAY);
    }

    public void setStartDelay(int startDelay) {
        this.dataManager.set(START_DELAY, startDelay);
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        //Suppress velocity change due to collision
        //super.applyEntityCollision(entityIn);
    }

    @Override
    public ResourceLocation getDefaultModel() {
        return DEFAULT_MODEL_NAME;
    }

    @Override
    public ResourceLocation getDefaultTexture() {
        return DEFAULT_TEXTURE_NAME;
    }
}
