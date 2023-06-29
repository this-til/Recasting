package com.til.recasting.common.entity;

import com.google.common.collect.Lists;
import com.til.recasting.common.register.util.AttackManager;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.event.FallHandler;
import mods.flammpfeil.slashblade.util.EnumSetConverter;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class SlashEffectEntity extends Entity {
    static private final ResourceLocation DEFAULT_MODEL_NAME = new ResourceLocation(SlashBlade.modid, "model/util/slash.obj");
    static private final ResourceLocation DEFAULT_TEXTURE_NAME = new ResourceLocation(SlashBlade.modid, "model/util/slash.png");

    protected static final DataParameter<Integer> SHOOTING_ENTITY_ID = EntityDataManager.createKey(SlashEffectEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> COLOR = EntityDataManager.createKey(SlashEffectEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> FLAGS = EntityDataManager.createKey(SlashEffectEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Float> ROTATION_OFFSET = EntityDataManager.createKey(SlashEffectEntity.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> ROTATION_ROLL = EntityDataManager.createKey(SlashEffectEntity.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> BASESIZE = EntityDataManager.createKey(SlashEffectEntity.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> RANGE = EntityDataManager.createKey(SlashEffectEntity.class, DataSerializers.FLOAT);

    protected static final DataParameter<String> MODEL = EntityDataManager.createKey(SlashEffectEntity.class, DataSerializers.STRING);
    protected static final DataParameter<String> TEXTURE = EntityDataManager.createKey(SlashEffectEntity.class, DataSerializers.STRING);

    protected int lifetime = 10;
    protected KnockBacks action = KnockBacks.cancel;

    protected double damage = 1.0D;

    protected boolean cycleHit = false;
    protected EnumSet<FlagsState> flags = EnumSet.noneOf(FlagsState.class);
    protected int intFlags = 0;

    protected final List<Entity> alreadyHits = Lists.newArrayList();


    protected SoundEvent livingEntitySound = SoundEvents.ENTITY_WITHER_HURT;

    protected SoundEvent getHitEntitySound() {
        return this.livingEntitySound;
    }

    public SlashEffectEntity(EntityType<? extends SlashEffectEntity> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn);
        this.setNoGravity(true);
        if (shooting != null) {
            setShooter(shooting);
            setRawPosition(shooting.getPosX(), shooting.getPosY(), shooting.getPosZ());
            rotationYaw = shooting.rotationYaw;
            rotationPitch = 0;
        }
    }


    @Override
    protected void registerData() {
        this.dataManager.register(SHOOTING_ENTITY_ID, -1);
        this.dataManager.register(COLOR, 0x3333FF);
        this.dataManager.register(FLAGS, 0);
        this.dataManager.register(ROTATION_OFFSET, 0.0f);
        this.dataManager.register(ROTATION_ROLL, 0.0f);
        this.dataManager.register(BASESIZE, 1.0f);
        this.dataManager.register(RANGE,  2f);
        this.dataManager.register(MODEL, "");
        this.dataManager.register(TEXTURE, "");
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
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

        if (!world.isRemote) {
            if (getShooter() == null) {
                this.remove();
            }
        }

        if (ticksExisted == 2) {

            if (!getMute()) {
                this.playSound(SoundEvents.ITEM_TRIDENT_THROW, 0.80F, 0.625F + 0.1f * this.rand.nextFloat());
            } else {
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.5F, 0.4F / (this.rand.nextFloat() * 0.4F + 0.8F));
            }

            if (getIsCritical()) {
                this.playSound(getHitEntitySound(), 0.2F, 0.4F + 0.25f * this.rand.nextFloat());
            }
        }

        if (getShooter() != null && !getShooter().isWet() && ticksExisted < (getLifetime() * 0.75)) {
            Vector3d start = this.getPositionVec();
            Vector4f normal = new Vector4f(1, 0, 0, 1);

            float progress = this.ticksExisted / (float) lifetime;

            normal.transform(new Quaternion(Vector3f.YP, -this.rotationYaw - 90, true));
            normal.transform(new Quaternion(Vector3f.ZP, this.rotationPitch, true));
            normal.transform(new Quaternion(Vector3f.XP, this.getRotationRoll(), true));
            normal.transform(new Quaternion(Vector3f.YP, 140 + this.getRotationOffset() - 200.0F * progress, true));

            Vector3d normal3d = new Vector3d(normal.getX(), normal.getY(), normal.getZ());

            BlockRayTraceResult rayResult = this.getEntityWorld().rayTraceBlocks(new RayTraceContext(start.add(normal3d.scale(1.5)), start.add(normal3d.scale(3)), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.ANY, null));

            if (rayResult.getType() == RayTraceResult.Type.BLOCK) {
                FallHandler.spawnLandingParticle(this, rayResult.getHitVec(), normal3d, 3);
            }
        }

        if (!this.world.isRemote) {
            if (this.ticksExisted % 2 == 0) {
                boolean forceHit = true;
                float ratio = (float) damage * (getIsCritical() ? 1.1f : 1.0f);
                List<Entity> hits = AttackManager.areaAttack(getShooter(), this, this.action.action, ratio, forceHit, false, true, alreadyHits);
                if (!this.doCycleHit()) {
                    alreadyHits.addAll(hits);
                }
            }
            if (getLifetime() < this.ticksExisted) {
                this.remove();
            }
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
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

    public void setIndirect(boolean value) {
        if (value) {
            setFlags(FlagsState.Indirect);
        } else {
            removeFlags(FlagsState.Indirect);
        }
    }

    public boolean getIndirect() {
        refreshFlags();
        return flags.contains(FlagsState.Indirect);
    }

    public void setMute(boolean value) {
        if (value) {
            setFlags(FlagsState.Mute);
        } else {
            removeFlags(FlagsState.Mute);
        }
    }

    public boolean getMute() {
        refreshFlags();
        return flags.contains(FlagsState.Mute);
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

    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    public void setColor(int value) {
        this.getDataManager().set(COLOR, value);
    }

    public int getLifetime() {
        return Math.min(this.lifetime, 1000);
    }

    public void setLifetime(int value) {
        this.lifetime = value;
    }

    public float getRotationOffset() {
        return this.getDataManager().get(ROTATION_OFFSET);
    }

    public void setRotationOffset(float value) {
        this.getDataManager().set(ROTATION_OFFSET, value);
    }

    public float getRotationRoll() {
        return this.getDataManager().get(ROTATION_ROLL);
    }

    public void setRotationRoll(float value) {
        this.getDataManager().set(ROTATION_ROLL, value);
    }

    public float getBaseSize() {
        return this.getDataManager().get(BASESIZE);
    }

    public void setBaseSize(float value) {
        this.getDataManager().set(BASESIZE, value);
    }

    protected LivingEntity shooting;

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

    public void setDamage(double damageIn) {
        this.damage = damageIn;
    }

    public double getDamage() {
        return this.damage;
    }

    public KnockBacks getKnockBack() {
        return action;
    }

    public void setKnockBack(KnockBacks action) {
        this.action = action;
    }

    public void setKnockBackOrdinal(int ordinal) {
        if (0 <= ordinal && ordinal < KnockBacks.values().length) {
            this.action = KnockBacks.values()[ordinal];
        } else {
            this.action = KnockBacks.cancel;
        }
    }

    public boolean doCycleHit() {
        return cycleHit;
    }

    public void setCycleHit(boolean cycleHit) {
        this.cycleHit = cycleHit;
    }


    public float getRange() {
        return this.dataManager.get(RANGE);
    }

    public void setRange(float range) {
        this.dataManager.set(RANGE, range);
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


    @Nullable
    public EntityRayTraceResult getRayTrace(Vector3d p_213866_1_, Vector3d p_213866_2_) {
        return ProjectileHelper.rayTraceEntities(this.world, this, p_213866_1_, p_213866_2_, this.getBoundingBox().expand(this.getMotion()).grow(1.0D), (p_213871_1_) -> {
            return !p_213871_1_.isSpectator() && p_213871_1_.isAlive() && p_213871_1_.canBeCollidedWith() && (p_213871_1_ != this.getShooter());
        });
    }

    enum FlagsState {
        Critical, NoClip, Mute, Indirect,
    }
}
