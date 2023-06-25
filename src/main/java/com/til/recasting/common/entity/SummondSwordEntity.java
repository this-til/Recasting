package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.target_selector.DefaultSummondSwordTargetSelectorRegister;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import com.til.recasting.common.register.target_selector.TargetSelectorRegister;
import com.til.recasting.util.RayTraceUtil;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.IShootable;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

@StaticVoluntarilyAssignment
public class SummondSwordEntity extends Entity implements IShootable {

    @VoluntarilyAssignment
    protected static DefaultTargetSelectorRegister targetSelectorRegister;

    @VoluntarilyAssignment
    protected static DefaultSummondSwordTargetSelectorRegister defaultSummondSwordTargetSelectorRegister;

    @VoluntarilyAssignment
    protected static DefaultEntityPredicateRegister defaultEntityPredicateRegister;


    public static final ResourceLocation defaultModelName = new ResourceLocation(SlashBlade.modid, "model/util/ss.obj");
    public static final ResourceLocation defaultTexture = new ResourceLocation(SlashBlade.modid, "model/util/ss.png");
    /***
     * 幻影剑的颜色
     */
    protected static final DataParameter<Integer> COLOR = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.VARINT);

    /***
     * 模型
     */
    protected static final DataParameter<String> MODEL = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.STRING);

    /***
     * 材质
     */
    protected static final DataParameter<String> TEXTURE = EntityDataManager.createKey(SummondSwordEntity.class, DataSerializers.STRING);


    /***
     * 幻影剑的roll
     */
    protected static final DataParameter<Float> ROLL = EntityDataManager.<Float>createKey(SummondSwordEntity.class, DataSerializers.FLOAT);


    /***
     * 幻影剑的最大生命
     */
    protected int maxLife;

    /***
     * 速度
     */
    protected double seep = 3;

    /***
     * 基础速度
     */
    protected double basicsSeep = 3;

    /***
     *  加速度
     */
    protected double acceleratedSpeed = 1.1;

    /***
     * 旋转速度
     */
    protected double rotateSpeed = 14.4f;

    /***
     * 延迟开始
     */
    protected int delay = 15;

    /***
     * 幻影剑的伤害
     */
    protected double damage = 1;

    /***
     * 尝试攻击的实体
     */
    @Nullable
    protected Entity tryAttackEntity;

    /***
     * 攻击到的对方
     */
    @Nullable
    protected Entity hitEntityEntity;

    /***
     * 攻击者
     */
    protected Entity shooter;

    protected double hitX;
    protected double hitY;
    protected double hitZ;
    protected float hitYaw;
    protected float hitPitch;


    /***
     * 幻影剑状态
     */
    protected SummondSwordEntityState summondSwordEntityState = SummondSwordEntityState.aim;

    protected final SoundEvent hitEntitySound = SoundEvents.ITEM_TRIDENT_HIT;
    protected final SoundEvent hitGroundSound = SoundEvents.ITEM_TRIDENT_HIT_GROUND;


    public SummondSwordEntity(EntityType<? extends SummondSwordEntity> type, World world, Entity shooter) {
        super(type, world);
        this.shooter = shooter;
    }

    @Override
    protected void registerData() {
        this.dataManager.register(COLOR, 0x3333FF);
        this.dataManager.register(MODEL, "");
        this.dataManager.register(TEXTURE, "");
        this.dataManager.register(ROLL, 0f);
    }

    @Deprecated
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
    }

    @Override
    public void tick() {
        super.tick();

        if (shooter == null) {
            setDead();
            return;
        }
        if (ticksExisted > maxLife) {
            setDead();
            return;
        }
        if (world.isRemote) {
            return;
        }
        switch (summondSwordEntityState) {
            case aim:
                break;
            case move:

                Vector3d move = Vector3d.fromPitchYaw(rotationPitch, rotationYaw).scale(seep);
                setMotion(move);
                move(MoverType.SELF, getMotion());

                RayTraceResult rayTraceResult = defaultSummondSwordTargetSelectorRegister.selector(this);
                switch (rayTraceResult.getType()) {
                    case BLOCK:
                        this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, hitGroundSound, SoundCategory.NEUTRAL, 0.25F, 1.6F);
                        summondSwordEntityState = SummondSwordEntityState.attackGround;
                        seep = 0;
                        break;
                    case ENTITY:
                        EntityRayTraceResult entityRayTraceResult = ((EntityRayTraceResult) rayTraceResult);
                        Entity attacked = entityRayTraceResult.getEntity();
                        tryAttack(attacked, AttackTime.hit);
                        this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, hitEntitySound, SoundCategory.NEUTRAL, 0.25F, 1.6F);
                        summondSwordEntityState = SummondSwordEntityState.attack;
                        seep = 0;
                        break;
                }
                break;
            case attack:
                Entity entity = getHitEntity();
                if (entity != null) {
                    double posX = entity.prevPosX + (this.hitX * Math.cos(Math.toRadians(entity.rotationYaw)) - this.hitZ * Math.sin(Math.toRadians(entity.rotationYaw)));
                    double posY = entity.prevPosY + this.hitY;
                    double posZ = entity.prevPosZ + (this.hitX * Math.sin(Math.toRadians(entity.rotationYaw)) + this.hitZ * Math.cos(Math.toRadians(entity.rotationYaw)));
                    rotationPitch = entity.rotationPitch + this.hitPitch;
                    rotationYaw = entity.rotationYaw + this.hitYaw;
                    setPosition(posX, posY, posZ);
                    setRotation(rotationYaw, rotationPitch);
                }
                break;
            case attackGround:
                break;
        }
    }

    @Override
    public void baseTick() {
        this.world.getProfiler().startSection("entityBaseTick");

        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        this.lastTickPosX = prevPosX;
        this.lastTickPosY = prevPosY;
        this.lastTickPosZ = prevPosZ;


        this.firstUpdate = false;
        this.world.getProfiler().endSection();
    }

    @Override
    protected void setDead() {
        super.setDead();
        if (world.isRemote) {
            return;
        }
        if (hitEntityEntity == null) {
            AxisAlignedBB bb = this.getBoundingBox().grow(1.0D, 1.0D, 1.0D);
            List<Entity> list = this.world.getEntitiesInAABBexcluding(this, bb, e -> defaultEntityPredicateRegister.canTarget(getShooter(), e));
            for (Entity target : list) {
                if (target == null) {
                    continue;
                }
                if (target == hitEntityEntity) {
                    continue;
                }
                tryAttack(target, AttackTime.end);
            }
        }
        tryAttack(hitEntityEntity, AttackTime.end);
        this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, hitEntitySound, SoundCategory.NEUTRAL, 0.25F, 1.6F);
    }

    @Override
    public boolean isImmuneToFire() {
        return false;
    }

    @Override
    public void setFire(int seconds) {
    }

    @Override
    public Entity getRidingEntity() {
        return null;
    }

    @Override
    public boolean startRiding(Entity entityIn) {
        return false;
    }

    @Override
    public boolean startRiding(Entity entityIn, boolean force) {
        return false;
    }

    @Override
    public void stopRiding() {
    }

    /***
     * 尝试攻击
     */
    protected void tryAttack(Entity target, AttackTime attackTime) {
        float magicDamage = (float) Math.max(1.0f, damage);
        DamageSource ds = new EntityDamageSource("directMagic", this.getShooter()).setDamageBypassesArmor().setMagicDamage();
        target.hurtResistantTime = 0;
        target.attackEntityFrom(ds, magicDamage);
        target.setMotion(0, 0, 0);
        target.addVelocity(0.0, 0.1D, 0.0);
        if (target instanceof LivingEntity) {
            ((LivingEntity) target).hurtTime = 1;
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

        RayTraceResult rayTraceResult = targetSelectorRegister.selector(shooter);
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

    /***
     * 瞄准实体
     */
    public void faceEntity(Entity viewer, Entity target, float yawStep, float pitchStep) {
        double d0 = target.prevPosX - viewer.prevPosX;
        double d1 = target.prevPosZ - viewer.prevPosZ;
        double d2;

        if (target instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) target;
            d2 = livingEntity.prevPosY + (double) livingEntity.getEyeHeight() - (viewer.prevPosY + viewer.getEyeHeight());
        } else {
            AxisAlignedBB boundingBox = target.getBoundingBox();
            d2 = (boundingBox.minY + boundingBox.maxY) / 2.0D - (viewer.prevPosY + viewer.getEyeHeight());
        }

        double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f2 = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
        float f3 = (float) (-(Math.atan2(d2, d3) * 180.0D / Math.PI));

        rotationPitch = this.tryRotation(rotationPitch, f3, pitchStep);
        rotationYaw = this.tryRotation(rotationYaw, f2, yawStep);

    }

    /***
     * 尝试旋转
     */
    protected float tryRotation(float par1, float par2, float par3) {
        float f3 = MathHelper.wrapDegrees(par2 - par1);
        if (f3 > par3) {
            f3 = par3;
        }
        if (f3 < -par3) {
            f3 = -par3;
        }
        return par1 + f3;
    }


    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getDamage() {
        return damage;
    }


    public int getColor() {
        return dataManager.get(COLOR);
    }

    public SummondSwordEntity setColor(int color) {
        this.dataManager.set(COLOR, color);
        return this;
    }


    @Nullable
    public Entity getHitEntity() {
        return hitEntityEntity;
    }

    @Nullable
    protected ResourceLocation model;

    public ResourceLocation getModel() {
        if (model == null) {
            String modelString = dataManager.get(MODEL);
            if (modelString.isEmpty()) {
                model = defaultModelName;
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
                texture = defaultModelName;
            }
        }
        return texture;
    }

    public SummondSwordEntity setTexture(ResourceLocation texture) {
        dataManager.set(TEXTURE, texture.toString());
        this.texture = texture;
        return this;
    }

    public float getRoll() {
        return dataManager.get(ROLL);
    }

    public SummondSwordEntity setRoll(float roll) {
        dataManager.set(ROLL, roll);
        return this;
    }

    public SummondSwordEntity setDamage(double damage) {
        this.damage = damage;
        return this;
    }

    public SummondSwordEntity setMaxLife(int maxLife) {
        this.maxLife = maxLife;
        return this;
    }

    public SummondSwordEntity setBasicsSeep(double basicsSeep) {
        this.basicsSeep = basicsSeep;
        this.seep = basicsSeep;
        return this;
    }

    public SummondSwordEntity setAcceleratedSpeed(double acceleratedSpeed) {
        this.acceleratedSpeed = acceleratedSpeed;
        return this;
    }

    public SummondSwordEntity setRotateSpeed(double rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
        return this;
    }

    public SummondSwordEntity setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public SummondSwordEntity setTryAttackEntity(Entity tryAttackEntity) {
        this.tryAttackEntity = tryAttackEntity;
        return this;
    }

    public SummondSwordEntity lookAt(Vector3d vector3d) {
        float f = MathHelper.sqrt(vector3d.x * vector3d.x + vector3d.z * vector3d.z);
        this.rotationYaw = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        return this;
    }

    @Override
    public Entity getShooter() {
        return shooter;
    }

    @Override
    public void setShooter(Entity shooter) {
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


    /***
     * 幻影剑攻击类型
     */
    public enum AttackTime {
        hit, end
    }
}
