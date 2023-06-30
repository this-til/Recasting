package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.util.AttackManager;
import com.til.recasting.common.register.util.HitAssessment;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.StunManager;
import mods.flammpfeil.slashblade.entity.IShootable;
import mods.flammpfeil.slashblade.util.EnumSetConverter;
import mods.flammpfeil.slashblade.util.TargetSelector;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

@StaticVoluntarilyAssignment
public class SummondSwordEntity extends Entity {

    @VoluntarilyAssignment
    public static DefaultEntityPredicateRegister defaultEntityPredicateRegister;

    public static final ResourceLocation DEFAULT_MODEL_NAME = new ResourceLocation(SlashBlade.modid, "model/util/ss.obj");
    public static final ResourceLocation DEFAULT_TEXTURE_NAME = new ResourceLocation(SlashBlade.modid, "model/util/ss.png");


    protected static final DataParameter<Integer> MAX_LIFE = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> COLOR = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> FLAGS = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> SHOOTING_ENTITY_ID = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> HIT_ENTITY_ID = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Float> OFFSET_YAW = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> ROLL = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.FLOAT);
    protected static final DataParameter<Byte> PIERCE = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Integer> DELAY = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> START_DELAY = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Float> SEEP = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> ACCELERATION = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.FLOAT);

    protected static final DataParameter<String> MODEL = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.STRING);
    protected static final DataParameter<String> TEXTURE = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.STRING);


    protected int ticksInGround;
    protected int tickInHit;
    protected boolean inGround;
    protected BlockState inBlockState;
    protected int ticksInAir;
    protected double damage = 1.0D;

    protected IntOpenHashSet alreadyHits;
    protected LivingEntity shooting;
    protected Entity hitEntity = null;

    protected double hitX;
    protected double hitY;
    protected double hitZ;
    protected float hitYaw;
    protected float hitPitch;
    protected boolean recordAttackPos;

    protected float attackYaw;
    protected float attackPitch;

    protected SoundEvent hitEntitySound = SoundEvents.ITEM_TRIDENT_HIT;
    protected SoundEvent hitEntityPlayerSound = SoundEvents.ITEM_TRIDENT_HIT;
    protected SoundEvent hitGroundSound = SoundEvents.ITEM_TRIDENT_HIT_GROUND;

    protected SoundEvent getHitEntitySound() {
        return this.hitEntitySound;
    }

    protected SoundEvent getHitEntityPlayerSound() {
        return this.hitEntityPlayerSound;
    }

    protected SoundEvent getHitGroundSound() {
        return this.hitGroundSound;
    }

    public SummondSwordEntity(EntityType<? extends SummondSwordEntity> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn);
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
            attackYaw = shooting.rotationYaw;
            attackPitch = shooting.rotationPitch;
            updateMotion(1);
            lookAt(getMotion(), true, true);
            setRotation(rotationYaw, rotationPitch);
        }

    }


    @Override
    protected void registerData() {
        this.dataManager.register(MAX_LIFE, 200);
        this.dataManager.register(COLOR, 0x3333FF);
        this.dataManager.register(FLAGS, 0);
        this.dataManager.register(HIT_ENTITY_ID, -1);
        this.dataManager.register(SHOOTING_ENTITY_ID, -1);
        this.dataManager.register(OFFSET_YAW, 0f);
        this.dataManager.register(ROLL, 0f);
        this.dataManager.register(SEEP, 3f);
        this.dataManager.register(ACCELERATION, 1.05f);
        this.dataManager.register(PIERCE, (byte) 0);
        this.dataManager.register(MODEL, "");
        this.dataManager.register(TEXTURE, "");
        this.dataManager.register(DELAY, 20);
        this.dataManager.register(START_DELAY, 0);

    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
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

        this.attackPitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(d1, d3) * (double) (180F / (float) Math.PI))));
        this.attackYaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F);

        this.rotationPitch = MathHelper.wrapDegrees((float) ((MathHelper.atan2(d1, d3)) * (double) (180F / (float) Math.PI)));
        this.rotationYaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(d0, d2) * (double) (180F / (float) Math.PI)));
        if (prevSynchronous) {
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
        }
    }


    public void updateMotion(float seep) {
        float fYawDtoR = (attackYaw / 180F) * (float) Math.PI;
        float fPitDtoR = (attackPitch / 180F) * (float) Math.PI;
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


/*    @Deprecated
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vector3d vec3d = (new Vector3d(x, y, z)).normalize().add(this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy, this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy).scale((double) velocity);
        this.setMotion(vec3d);
        float f = MathHelper.sqrt(horizontalMag(vec3d));
        this.rotationYaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.ticksInGround = 0;
    }*/

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
            this.ticksInGround = 0;
        }
    }

    enum FlagsState {
        Critical, NoClip,
    }

    EnumSet<FlagsState> flags = EnumSet.noneOf(FlagsState.class);
    int intFlags = 0;

    private void setFlags(FlagsState value) {
        this.flags.add(value);
        refreshFlags();
    }

    private void removeFlags(FlagsState value) {
        this.flags.remove(value);
        refreshFlags();
    }

    private void refreshFlags() {
        if (this.world.isRemote) {
            int newValue = this.dataManager.get(FLAGS);
            if (intFlags != newValue) {
                intFlags = newValue;
                EnumSetConverter.convertToEnumSet(flags, FlagsState.values(), intFlags);
            }
        } else {
            int newValue = EnumSetConverter.convertToInt(this.flags);
            if (this.intFlags != newValue) {
                this.dataManager.set(FLAGS, newValue);
                this.intFlags = newValue;
            }
        }
    }


    public void setIsCritical(boolean value) {
        if (value) {
            setFlags(FlagsState.Critical);
        } else {
            removeFlags(FlagsState.Critical);
        }
    }

    public boolean getIsCritical() {
        refreshFlags();
        return flags.contains(FlagsState.Critical);
    }

    public void setNoClip(boolean value) {
        this.noClip = value;
        if (value) {
            setFlags(FlagsState.NoClip);
        } else {
            removeFlags(FlagsState.NoClip);
        }
    }

    //disallowedHitBlock
    public boolean isNoClip() {
        if (!this.world.isRemote) {
            return this.noClip;
        } else {
            refreshFlags();
            return flags.contains(FlagsState.NoClip);
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

        if (!world.isRemote && getShooter() == null) {
            remove();
            return;
        }

        if (this.ticksExisted <= getStartDelay()) {
            return;
        }


        if (getHitEntity() != null) {
            Entity hits = getHitEntity();

            if (!hits.isAlive()) {
                this.burst();
            } else {

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

                tickInHit++;
                if (tickInHit > getDelay()) {
                    this.burst();
                }

            }

            return;
        }

        if (!world.isRemote) {
            setSeep(getSeep() * getAcceleration());
            updateMotion(getSeep());
        }

        boolean disallowedHitBlock = this.isNoClip();

        BlockPos blockpos = new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ());
        BlockState blockstate = this.world.getBlockState(blockpos);
        if (!blockstate.isAir(this.world, blockpos) && !disallowedHitBlock) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.world, blockpos);
            if (!voxelshape.isEmpty()) {
                for (AxisAlignedBB axisalignedbb : voxelshape.toBoundingBoxList()) {
                    if (axisalignedbb.offset(blockpos).contains(new Vector3d(this.getPosX(), this.getPosY(), this.getPosZ()))) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }


        if (this.inGround && !disallowedHitBlock) {
            if (this.inBlockState != blockstate && this.world.hasNoCollisions(this.getBoundingBox().grow(0.06D))) {
                //block breaked
                this.burst();
            } else if (!this.world.isRemote) {
                //onBlock
                this.tryDespawn();
            }
        } else {
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
            ++this.ticksInAir;
            Vector3d positionVec = this.getPositionVec();
            Vector3d movedVec = positionVec.add(motionVec);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(positionVec, movedVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
            if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
                movedVec = raytraceresult.getHitVec();
            }

            while (this.isAlive()) {
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
                }

                if (entityraytraceresult == null || this.getPierce() <= 0) {
                    break;
                }

                raytraceresult = null;
            }

            motionVec = this.getMotion();
            double mx = motionVec.x;
            double my = motionVec.y;
            double mz = motionVec.z;
            if (this.getIsCritical()) {
                for (int i = 0; i < 4; ++i) {
                    this.world.addParticle(ParticleTypes.CRIT, this.getPosX() + mx * (double) i / 4.0D, this.getPosY() + my * (double) i / 4.0D, this.getPosZ() + mz * (double) i / 4.0D, -mx, -my + 0.2D, -mz);
                }
            }

            this.setPosition(this.getPosX() + mx, this.getPosY() + my, this.getPosZ() + mz);

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


        if (!this.world.isRemote && ticksInGround <= 0 && getLifeTime() < this.ticksExisted) {
            this.remove();
        }

    }

    protected void tryDespawn() {
        ++this.ticksInGround;
        if (this.ticksInGround >= getDelay()) {
            this.burst();
        }

    }

    protected void onHit(RayTraceResult raytraceResultIn) {
        RayTraceResult.Type type = raytraceResultIn.getType();
        switch (type) {
            case ENTITY:
                this.onHitEntity((EntityRayTraceResult) raytraceResultIn);
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
        Vector3d vec3d1 = this.getPositionVec().subtract(vec3d.normalize().scale((double) 0.05F));
        this.setPosition(vec3d1.x, vec3d1.y, vec3d1.z);
        this.playSound(this.getHitGroundSound(), 1.0F, 2.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.setIsCritical(false);
        this.setPierce((byte) 0);
        this.resetAlreadyHits();
    }

    public void doForceHitEntity(Entity target) {
        onHitEntity(new EntityRayTraceResult(target));
    }

    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        Entity targetEntity = p_213868_1_.getEntity();
        int i = MathHelper.ceil(this.getDamage());
        if (this.getPierce() > 0) {
            if (this.alreadyHits == null) {
                this.alreadyHits = new IntOpenHashSet(5);
            }

            if (this.alreadyHits.size() >= this.getPierce() + 1) {
                this.burst();
                return;
            }

            this.alreadyHits.add(targetEntity.getEntityId());
        }

        if (this.getIsCritical()) {
            i *= 1.5;
        }
        if (!world.isRemote) {
            AttackManager.doAttack(getShooter(), targetEntity, i, true, true);
            Entity hits = targetEntity;
            if (targetEntity instanceof PartEntity) {
                hits = ((PartEntity<?>) targetEntity).getParent();
            }
            hits.hurtResistantTime = 0;
            hits.setMotion(0, 0.1, 0);

            if (hits instanceof LivingEntity) {
                LivingEntity targetLivingEntity = (LivingEntity) hits;

                targetLivingEntity.hurtTime = 0;

                StunManager.setStun(targetLivingEntity);

                if (!this.world.isRemote && this.getPierce() <= 0) {
                    setHitEntity(hits);
                }


                EnchantmentHelper.applyThornEnchantments(targetLivingEntity, getShooter());
                EnchantmentHelper.applyArthropodEnchantments(getShooter(), targetLivingEntity);
                //this.arrowHit(targetLivingEntity);

                affectEntity(targetLivingEntity, getPotionEffects(), 1.0f);
                //getShooter().playSound(this.getHitEntityPlayerSound(), 0.18F, 0.45F);
            }

            this.playSound(this.getHitEntitySound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            /*if (this.getPierce() <= 0 && (getHitEntity() == null || !getHitEntity().isAlive())) {
                this.burst();
            }*/
        }



      /*  if (targetEntity.attackEntityFrom(damagesource, (float) i)) {

        } else {
            targetEntity.forceFireTicks(fireTime);
            //this.setMotion(this.getMotion().scale(-0.1D));
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
            this.ticksInAir = 0;
            if (!this.world.isRemote && this.getMotion().lengthSquared() < 1.0E-7D) {
                this.burst();
            }
        }*/

    }

    public float getSeep() {
        return this.dataManager.get(SEEP);
    }

    public void setSeep(float seep) {
        this.dataManager.set(SEEP, seep);
    }


    public float getAcceleration() {
        return this.dataManager.get(ACCELERATION);
    }

    public void setAcceleration(float acceleration) {
        this.dataManager.get(ACCELERATION);
    }

    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    public void setColor(int value) {
        this.getDataManager().set(COLOR, value);
    }

    public byte getPierce() {
        return this.getDataManager().get(PIERCE);
    }

    public void setPierce(byte value) {
        this.getDataManager().set(PIERCE, (byte) value);
    }

    public int getDelay() {
        return this.getDataManager().get(DELAY);
    }

    public void setDelay(int value) {
        this.getDataManager().set(DELAY, value);
    }

    @Nullable
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

    public LivingEntity getShooter() {
        if (this.shooting == null) {
            int id = this.dataManager.get(SHOOTING_ENTITY_ID);
            if (id != 0) {
                Entity entity = this.getEntityWorld().getEntityByID(id);
                if (entity instanceof LivingEntity) {
                    this.shooting = (LivingEntity) entity;
                }
            }
        }

        return this.shooting;
    }

    public void setShooter(LivingEntity shooter) {
        if (shooter != null) {
            this.dataManager.set(SHOOTING_ENTITY_ID, shooter.getEntityId());
        }
        this.shooting = shooter;
    }


    public List<EffectInstance> getPotionEffects() {
        List<EffectInstance> effects = PotionUtils.getEffectsFromTag(this.getPersistentData());

        if (effects.isEmpty()) {
            effects.add(new EffectInstance(Effects.POISON, 1, 1));
        }

        return effects;
    }

    public void burst() {
        this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

        if (!this.world.isRemote) {
            if (this.world instanceof ServerWorld) {
                ((ServerWorld) this.world).spawnParticle(ParticleTypes.CRIT, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
            }

            this.burst(getPotionEffects(), null);
        }

        if (getHitEntity() != null) {
            onHitEntity(new EntityRayTraceResult(getHitEntity()));
        }

        super.remove();
    }


    public void burst(List<EffectInstance> effects, @Nullable Entity focusEntity) {
        List<Entity> list = HitAssessment.getTargettableEntitiesWithinAABB(this.world, getShooter(), this, 2);
        list.stream().filter(e -> e instanceof LivingEntity).map(e -> (LivingEntity) e).forEach(e -> {
            double distanceSq = this.getDistanceSq(e);
            if (distanceSq < 9.0D) {
                double factor = 1.0D - Math.sqrt(distanceSq) / 4.0D;
                if (e == focusEntity) {
                    factor = 1.0D;
                }
                affectEntity(e, effects, factor);
            }
        });
    }


    public void affectEntity(LivingEntity focusEntity, List<EffectInstance> effects, double factor) {
        for (EffectInstance effectinstance : getPotionEffects()) {
            Effect effect = effectinstance.getPotion();
            if (effect.isInstant()) {
                effect.affectEntity(this, this.getShooter(), focusEntity, effectinstance.getAmplifier(), factor);
            } else {
                int duration = (int) (factor * (double) effectinstance.getDuration() + 0.5D);
                if (duration > 0) {
                    focusEntity.addPotionEffect(new EffectInstance(effect, duration, effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.doesShowParticles()));
                }
            }
        }
    }

    private void resetAlreadyHits() {
        if (this.alreadyHits != null) {
            alreadyHits.clear();
        }
    }

    public void setHitEntity(Entity hitEntity) {
        if (hitEntity != this) {
            this.dataManager.set(HIT_ENTITY_ID, hitEntity.getEntityId());
            this.dataManager.set(OFFSET_YAW, this.rand.nextFloat() * 360);
        }
    }

    @Nullable
    public Entity getHitEntity() {
        if (hitEntity == null) {
            int id = this.dataManager.get(HIT_ENTITY_ID);
            if (0 <= id) {
                hitEntity = this.world.getEntityByID(id);
            }
        }
        return hitEntity;
    }

    public float getOffsetYaw() {
        return this.dataManager.get(OFFSET_YAW);
    }

    public float getRoll() {
        return this.dataManager.get(ROLL);
    }

    public void setRoll(float value) {
        this.dataManager.set(ROLL, value);
    }

    public void setDamage(double damageIn) {
        this.damage = damageIn;
    }

    public double getDamage() {
        return this.damage;
    }

    public int getLifeTime() {
        return this.dataManager.get(MAX_LIFE);
    }

    public void setLifeTime(int lifeTime) {
        this.dataManager.set(MAX_LIFE, lifeTime);
    }

    public int getStartDelay() {
        return this.dataManager.get(START_DELAY);
    }

    public void setStartDelay(int startDelay) {
        this.dataManager.set(START_DELAY, startDelay);
    }

    @Nullable
    protected ResourceLocation model;

    public ResourceLocation getModel() {
        if (model == null) {
            String modelString = dataManager.get(MODEL);
            if (modelString.isEmpty()) {
                model = DEFAULT_MODEL_NAME;
            }
        }
        return model;
    }

    public void setModel(ResourceLocation model) {
        this.model = model;
        dataManager.set(MODEL, model.toString());
    }

    @Nullable
    protected ResourceLocation texture;

    @Nullable
    public ResourceLocation getTexture() {
        if (texture == null) {
            String textureString = dataManager.get(TEXTURE);
            if (textureString.isEmpty()) {
                texture = DEFAULT_TEXTURE_NAME;
            }
        }
        return texture;
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
        dataManager.set(TEXTURE, texture.toString());
    }


    @Override
    public void applyEntityCollision(Entity entityIn) {
        //Suppress velocity change due to collision
        //super.applyEntityCollision(entityIn);
    }
}
