package com.til.recasting.common.entity;

import com.google.common.collect.Lists;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.recasting.common.register.attack_type.instance.SlashBladeAttackType;
import com.til.recasting.common.register.back_type.SlashEffectEntityBackTypeRegister;
import com.til.recasting.common.register.overall_config.SlashEffectEntityOverallConfigRegister;
import com.til.recasting.common.register.util.AttackManager;
import com.til.recasting.common.register.util.RayTraceUtil;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.event.FallHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/**
 * @author til
 */
@StaticVoluntarilyAssignment
public class SlashEffectEntity extends StandardizationAttackEntity {

    @VoluntarilyAssignment
    protected static SlashEffectEntityBackTypeRegister.SlashEffectAttackBackTypeRegister attackBackTypeRegister;

    @VoluntarilyAssignment
    protected static SlashEffectEntityOverallConfigRegister slashEffectEntityOverallConfigRegister;

    @VoluntarilyAssignment
    protected static SlashBladeAttackType slashBladeAttackType;

    static private final ResourceLocation DEFAULT_MODEL_NAME = new ResourceLocation(SlashBlade.modid, "model/util/slash.obj");
    static private final ResourceLocation DEFAULT_TEXTURE_NAME = new ResourceLocation(SlashBlade.modid, "model/util/slash.png");

    protected static final DataParameter<Float> ROTATION_OFFSET = EntityDataManager.createKey(SlashEffectEntity.class, DataSerializers.FLOAT);
    protected static final DataParameter<Boolean> MULTIPLE_ATTACK = EntityDataManager.createKey(SlashEffectEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> THUMP = EntityDataManager.createKey(SlashEffectEntity.class, DataSerializers.BOOLEAN);


    protected final List<Entity> alreadyHits = Lists.newArrayList();

    protected SoundEvent livingEntitySound = SoundEvents.ENTITY_WITHER_HURT;

    protected SoundEvent getHitEntitySound() {
        return this.livingEntitySound;
    }

    public SlashEffectEntity(EntityType<? extends SlashEffectEntity> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn, shooting);
        this.setNoGravity(true);
        setMaxLifeTime(10);
        if (shooting != null) {
            setShooter(shooting);
            Vector3d pos = RayTraceUtil.getPosition(shooting);
            setRawPosition(pos.getX(), pos.getY(), pos.getZ());
            rotationYaw = shooting.rotationYaw;
            rotationPitch = 0;
        }
    }


    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ROTATION_OFFSET, 0.0f);
        this.dataManager.register(MULTIPLE_ATTACK, false);
        this.dataManager.register(THUMP, false);
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

        this.rotationPitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(d1, d3) * (double) (180F / (float) Math.PI))));
        this.rotationYaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F);

        //this.rotationPitch = MathHelper.wrapDegrees((float) ((MathHelper.atan2(d1, d3)) * (double) (180F / (float) Math.PI)));
        //this.rotationYaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(d0, d2) * (double) (180F / (float) Math.PI)));
        if (prevSynchronous) {
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
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
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setVelocity(double x, double y, double z) {
        this.setMotion(0, 0, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (ticksExisted == 2) {

            if (!isMute()) {
                this.playSound(SoundEvents.ITEM_TRIDENT_THROW, 0.80F, 0.625F + 0.1f * this.rand.nextFloat());
            } else {
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.5F, 0.4F / (this.rand.nextFloat() * 0.4F + 0.8F));
            }

            if (isThump()) {
                this.playSound(getHitEntitySound(), 0.2F, 0.4F + 0.25f * this.rand.nextFloat());
            }

        }

        if (useBlockParticle() && !getShooter().isWet() && ticksExisted < (getMaxLifeTime() * 0.75)) {
            Vector3d start = this.getPositionVec();
            Vector4f normal = new Vector4f(1, 0, 0, 1);

            float progress = this.ticksExisted / (float) getMaxLifeTime();

            normal.transform(new Quaternion(Vector3f.YP, -this.rotationYaw - 90, true));
            normal.transform(new Quaternion(Vector3f.ZP, this.rotationPitch, true));
            normal.transform(new Quaternion(Vector3f.XP, this.getRoll(), true));
            normal.transform(new Quaternion(Vector3f.YP, 140 + this.getRotationOffset() - 200.0F * progress, true));

            Vector3d normal3d = new Vector3d(normal.getX(), normal.getY(), normal.getZ());

            BlockRayTraceResult rayResult = this.getEntityWorld().rayTraceBlocks(new RayTraceContext(start.add(normal3d.scale(1.5 * getSize())), start.add(normal3d.scale(3 * getSize())), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null));

            if (rayResult.getType() == RayTraceResult.Type.BLOCK) {
                FallHandler.spawnLandingParticle(this, rayResult.getHitVec(), normal3d, 3);
            }
        }

        if (!this.world.isRemote) {
            if (this.ticksExisted % attackInterval() == 0) {
                onAttack();
            }
        }
    }

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
                ListUtil.of(slashBladeAttackType));
        if (!isMultipleAttack()) {
            alreadyHits.addAll(hits);
        }
    }


    protected boolean useBlockParticle() {
        return slashEffectEntityOverallConfigRegister.isUseBlockParticle();
    }

    protected int attackInterval() {
        return 2;
    }

    public float getRotationOffset() {
        return this.getDataManager().get(ROTATION_OFFSET);
    }

    public void setRotationOffset(float value) {
        this.getDataManager().set(ROTATION_OFFSET, value);
    }


    public boolean isMultipleAttack() {
        return this.getDataManager().get(MULTIPLE_ATTACK);
    }


    public void setMultipleAttack(boolean multipleAttack) {
        this.getDataManager().set(MULTIPLE_ATTACK, multipleAttack);
    }

    public boolean isThump() {
        return this.getDataManager().get(THUMP);
    }

    public void setThump(boolean thump) {
        this.getDataManager().set(THUMP, thump);
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
