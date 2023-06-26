package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.target_selector.DefaultSummondSwordTargetSelectorRegister;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.IShootable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.*;


@StaticVoluntarilyAssignment
public class SummondSwordEntity extends Entity implements IShootable {
    @VoluntarilyAssignment
    protected static DefaultTargetSelectorRegister targetSelectorRegister;

    @VoluntarilyAssignment
    protected static DefaultSummondSwordTargetSelectorRegister defaultSummondSwordTargetSelectorRegister;

    @VoluntarilyAssignment
    protected static DefaultEntityPredicateRegister defaultEntityPredicateRegister;

    public static final ResourceLocation DEFAULT_MODEL_NAME = new ResourceLocation(SlashBlade.modid, "model/util/ss.obj");
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(SlashBlade.modid, "model/util/ss.png");

    protected final SoundEvent hitEntitySound = SoundEvents.ITEM_TRIDENT_HIT;
    protected final SoundEvent hitGroundSound = SoundEvents.ITEM_TRIDENT_HIT_GROUND;


    protected Entity thrower;
    protected Entity tryAttackEntity;

    public Entity attackEntity = null;

    double hitX;
    double hitY;
    double hitZ;
    float hitYaw;
    float hitPitch;


    protected float attackLevel = 0.0f;

    protected SummondSwordEntityState summondSwordEntityState = SummondSwordEntityState.aim;

    public SummondSwordEntity(EntityType<? extends SummondSwordEntity> entityType, World par1World, Entity thrower) {
        super(entityType, par1World);
        this.noClip = true;
        if (thrower != null) {
            setShooter(thrower);
            {
                float dist = 2.0f;
                double ran = (rand.nextFloat() - 0.5) * 2.0;
                double yaw = Math.toRadians(-thrower.rotationYaw + 90);
                double x = ran * Math.sin(yaw);
                double y = 1.0 - Math.abs(ran);
                double z = ran * Math.cos(yaw);
                x *= dist;
                y *= dist;
                z *= dist;
                setLocationAndAngles(thrower.getPosX() + x,
                        thrower.getPosY() + y,
                        thrower.getPosZ() + z,
                        thrower.rotationYaw,
                        thrower.rotationPitch);

                iniYaw = thrower.rotationYaw;
                iniPitch = thrower.rotationPitch;
                setDriveVector(1.75f);
            }
        }
    }


    private static final DataParameter<Integer> THROWER_ENTITY_ID = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> LIFETIME = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Float> ROLL = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> TARGET_ENTITY_ID = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> INTERVAL = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);

    protected static final DataParameter<String> MODEL = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.STRING);
    protected static final DataParameter<String> TEXTURE = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.STRING);

    /**
     * ■イニシャライズ
     */
    @Override
    protected void registerData() {
        this.getDataManager().register(THROWER_ENTITY_ID, 0);
        this.getDataManager().register(LIFETIME, 100);
        this.getDataManager().register(ROLL, 0.0f);
        this.getDataManager().register(TARGET_ENTITY_ID, 0);
        this.getDataManager().register(INTERVAL, 7);
        this.getDataManager().register(COLOR, 0x3333FF);
        this.getDataManager().register(MODEL, "");
        this.getDataManager().register(TEXTURE, "");
    }


    protected float speed = 0.0f;
    protected float iniYaw;
    protected float iniPitch;


    public void faceEntity(Entity viewer, Entity target, float yawStep, float pitchStep) {
        double d0 = target.getPosX() - viewer.getPosX();
        double d1 = target.getPosZ() - viewer.getPosZ();
        double d2;

        if (target instanceof LivingEntity) {
            LivingEntity entitylivingbase = (LivingEntity) target;
            d2 = entitylivingbase.getPosY() + (double) entitylivingbase.getEyeHeight() - (viewer.getPosY() + (double) viewer.getEyeHeight());
        } else {
            AxisAlignedBB boundingBox = target.getBoundingBox();
            d2 = (boundingBox.minY + boundingBox.maxY) / 2.0D - (viewer.getPosY() + (double) viewer.getEyeHeight());
        }

        double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f2 = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
        float f3 = (float) (-(Math.atan2(d2, d3) * 180.0D / Math.PI));


        iniPitch = this.updateRotation(iniPitch, f3, pitchStep);
        iniYaw = this.updateRotation(iniYaw, f2, yawStep);

    }

    private float updateRotation(float par1, float par2, float par3) {
        float f3 = MathHelper.wrapDegrees(par2 - par1);

        if (f3 > par3) {
            f3 = par3;
        }

        if (f3 < -par3) {
            f3 = -par3;
        }

        return par1 + f3;
    }

    public void setDriveVector(float fYVecOfset) {
        setDriveVector(fYVecOfset, true);
    }

    /**
     * ■初期ベクトルとかを決めてる�?
     * ■移動�?�度設定
     *
     * @param fYVecOfst
     */
    public void setDriveVector(float fYVecOfst, boolean init) {
        //■角�? -> ラジアン 変換
        float fYawDtoR = (iniYaw / 180F) * (float) Math.PI;
        float fPitDtoR = (iniPitch / 180F) * (float) Math.PI;

        //■単位ベクト�?
        float motionX = -MathHelper.sin(fYawDtoR) * MathHelper.cos(fPitDtoR) * fYVecOfst;
        float motionY = -MathHelper.sin(fPitDtoR) * fYVecOfst;
        float motionZ = MathHelper.cos(fYawDtoR) * MathHelper.cos(fPitDtoR) * fYVecOfst;
        setMotion(new Vector3d(motionX, motionY, motionZ));

        float f3 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
        rotationPitch = (float) ((Math.atan2(motionY, f3) * 180D) / Math.PI);
        if (init) {
            speed = fYVecOfst;
            prevRotationYaw = rotationYaw;
            prevRotationPitch = rotationPitch;
        }
    }

    @Override
    public void updateRidden() {

        Entity ridingEntity = this.attackEntity;

        if (!ridingEntity.isAlive()) {
            this.setDead();
            return;
        }

        double posX = ridingEntity.getPosX() + (this.hitX * Math.cos(Math.toRadians(ridingEntity.rotationYaw)) - this.hitZ * Math.sin(Math.toRadians(ridingEntity.rotationYaw)));
        double posY = ridingEntity.getPosY() + this.hitY;
        double posZ = ridingEntity.getPosZ() + (this.hitX * Math.sin(Math.toRadians(ridingEntity.rotationYaw)) + this.hitZ * Math.cos(Math.toRadians(ridingEntity.rotationYaw)));

        setRawPosition(posX, posY, posZ);

        this.prevRotationPitch = rotationPitch;
        this.prevRotationYaw = rotationYaw;
        rotationPitch = ridingEntity.rotationPitch + this.hitPitch;
        rotationYaw = ridingEntity.rotationYaw + this.hitYaw;

        setPosition(posX, posY, posZ);

        setRotation(rotationYaw, rotationPitch);
    }

    /**
     * 向き初期�?
     */
    protected void updateRotation() {
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            Vector3d move = getMotion();
            float f = MathHelper.sqrt(move.getX() * move.getX() + move.getZ() * move.getZ());
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(move.getX(), move.getZ()) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(move.getY(), (double) f) * 180.0D / Math.PI);
        }
    }

    protected void attackEntity(Entity target, AttackTime attackTime) {
        if (world.isRemote) {
            return;
        }
        float magicDamage = Math.max(1.0f, attackLevel);
        target.hurtResistantTime = 0;
        DamageSource ds = new EntityDamageSource("directMagic", this.getShooter()).setDamageBypassesArmor().setMagicDamage();
        mountEntity(target);
        target.attackEntityFrom(ds, magicDamage);
        target.setMotion(0, 0.1f, 0);
        if (target instanceof LivingEntity) {
            LivingEntity livingEntity = ((LivingEntity) target);
            livingEntity.hurtTime = 1;
        }

    }

    public void spawnParticle() {
    }

    public void calculateSpeed() {
        setMotion(getMotion().scale(1.0));
    }

    //■毎回呼ばれる�?�移動処理とか当り判定とかもろもろ�??
    @Override
    public void tick() {
        lastTickPosX = getPosX();
        lastTickPosY = getPosY();
        lastTickPosZ = getPosZ();
        super.tick();

        if (!world.isRemote && getShooter() == null) {
            remove();
            return;
        }

        if (this.ticksExisted >= getLifeTime()) {
            this.setDead();
            return;
        }

        switch (summondSwordEntityState) {
            case aim:
                doTargeting();
                if (ticksExisted > getInterval()) {
                    summondSwordEntityState = SummondSwordEntityState.move;
                }
                break;
            case move:
                updateRotation();
                calculateSpeed();
                move(MoverType.SELF, getMotion());
                normalizeRotation();
                spawnParticle();
                if (!world.isRemote) {
                    RayTraceResult rayTraceResult = defaultSummondSwordTargetSelectorRegister.selector(this, getShooter());
                    switch (rayTraceResult.getType()) {
                        case BLOCK:
                            this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, hitGroundSound, SoundCategory.NEUTRAL, 0.25F, 1.6F);
                            summondSwordEntityState = SummondSwordEntityState.attackGround;
                            break;
                        case ENTITY:
                            EntityRayTraceResult entityRayTraceResult = ((EntityRayTraceResult) rayTraceResult);
                            Entity attacked = entityRayTraceResult.getEntity();
                            attackEntity(attacked, AttackTime.hit);
                            this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, hitEntitySound, SoundCategory.NEUTRAL, 0.25F, 1.6F);
                            attackEntity = attacked;
                            mountEntity(attacked);
                            summondSwordEntityState = SummondSwordEntityState.attack;
                            break;
                    }
                }
                break;
            case attack:
                updateRidden();
                break;
            case attackGround:
                break;
        }
    }

    /***
     * 进行瞄准
     */

    public void doTargeting() {
        if (tryAttackEntity == null) {
            search();
        }
        if (tryAttackEntity != null) {
            faceEntity(this, tryAttackEntity, ticksExisted * 1.0f, ticksExisted * 1.0f);
        }
    }


    /***
     * 搜寻可攻击目标
     */

    public void search() {
        if (tryAttackEntity != null) {
            return;
        }
        RayTraceResult rayTraceResult = targetSelectorRegister.selector(getShooter());
        if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
            EntityRayTraceResult entityRayTraceResult = ((EntityRayTraceResult) rayTraceResult);
            tryAttackEntity = entityRayTraceResult.getEntity();
        }
        if (tryAttackEntity != null) {
            return;
        }
        rayTraceResult = targetSelectorRegister.selector(this);
        if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
            EntityRayTraceResult entityRayTraceResult = ((EntityRayTraceResult) rayTraceResult);
            tryAttackEntity = entityRayTraceResult.getEntity();
        }
    }


    @Override
    public void setDead() {
        this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 0.25F, 1.6F);
        AxisAlignedBB bb = this.getBoundingBox().grow(1.0D, 1.0D, 1.0D);
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, bb, e -> defaultEntityPredicateRegister.canTarget(getShooter(), e));
        for (Entity target : list) {
            if (target == this) {
                continue;
            }
            if (target == null) {
                continue;
            }
            attackEntity(target, AttackTime.end);
        }
        super.setDead();
    }

    public void normalizeRotation() {

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

    public Random getRand() {
        return this.rand;
    }

    @Override
    public boolean isOffsetPositionInLiquid(double par1, double par3, double par5) {
        return false;
    }


    @Override
    public boolean isInLava() {
        return false;
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    public float getBrightness() {
        float f1 = super.getBrightness();
        float f2 = 0.9F;
        f2 = f2 * f2 * f2 * f2;
        return f1 * (1.0F - f2) + f2;
        //return super.getBrightness();
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    public Entity getRidingEntity() {
        return this.attackEntity;
    }

    /**
     * ■Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity par1Entity) {
        if (par1Entity != null) {
            this.hitYaw = this.rotationYaw - par1Entity.rotationYaw;
            this.hitPitch = this.rotationPitch - par1Entity.rotationPitch;
            this.hitX = this.lastTickPosX - par1Entity.getPosX();
            this.hitY = this.lastTickPosY - par1Entity.getPosY();
            this.hitZ = this.lastTickPosZ - par1Entity.getPosZ();
            this.attackEntity = par1Entity;

            this.ticksExisted = 0;
        }
    }

    /**
     * ■Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
    }

    @Override
    public void setPortal(BlockPos blockPos) {
    }

    @Override
    public boolean isBurning() {
        return false;
    }


    //IProjectile
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {

    }

    @Override
    public Entity getShooter() {
        if (this.thrower == null) {
            int id = getThrowerEntityId();
            if (id != 0) {
                this.thrower = this.getEntityWorld().getEntityByID(id);
            }
        }

        return this.thrower;
    }

    @Override
    public void setShooter(Entity shooter) {
        if (shooter != null) {
            setThrowerEntityId(shooter.getEntityId());
        }
        this.thrower = shooter;
    }


    public int getThrowerEntityId() {
        return this.getDataManager().get(THROWER_ENTITY_ID);
    }

    public void setThrowerEntityId(int entityid) {
        this.getDataManager().set(THROWER_ENTITY_ID, entityid);
    }

    public int getTargetEntityId() {
        return this.getDataManager().get(TARGET_ENTITY_ID);
    }

    public void setTargetEntityId(int entityid) {
        this.getDataManager().set(TARGET_ENTITY_ID, entityid);
    }

    public float getRoll() {
        return this.getDataManager().get(ROLL);
    }

    public void setRoll(float roll) {
        this.getDataManager().set(ROLL, roll);
    }

    public int getLifeTime() {
        return this.getDataManager().get(LIFETIME);
    }

    public void setLifeTime(int lifetime) {
        this.getDataManager().set(LIFETIME, lifetime);
    }

    public int getInterval() {
        return this.getDataManager().get(INTERVAL);
    }

    public void setInterval(int value) {
        this.getDataManager().set(INTERVAL, value);
    }

    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    public void setColor(int value) {
        this.getDataManager().set(COLOR, value);
    }

    @Override
    public double getDamage() {
        return attackLevel;
    }

    public void setTryAttackEntity(Entity tryAttackEntity) {
        this.tryAttackEntity = tryAttackEntity;
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

    public SummondSwordEntity setModel(ResourceLocation model) {
        this.model = model;
        dataManager.set(MODEL, model.toString());
        return this;
    }

    @Nullable
    protected ResourceLocation texture;

    @Nullable
    public ResourceLocation getTexture() {
        if (texture == null) {
            String textureString = dataManager.get(TEXTURE);
            if (textureString.isEmpty()) {
                texture = RESOURCE_LOCATION;
            }
        }
        return texture;
    }

    public SummondSwordEntity setTexture(ResourceLocation texture) {
        this.texture = texture;
        dataManager.set(TEXTURE, texture.toString());
        return this;
    }


    @Override
    public void applyEntityCollision(Entity entityIn) {
        //Suppress velocity change due to collision
        //super.applyEntityCollision(entityIn);
    }

    public enum AttackTime {
        hit, end
    }

    public enum SummondSwordEntityState {

        /***
         * 瞄准
         */

        aim,


        /***
         * 移动
         */

        move,


        /***
         * 攻击到
         */

        attack,


        /***
         * 攻击到地面
         */

        attackGround
    }

}
