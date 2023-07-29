package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.capability.back.BackRunPack;
import com.til.glowing_fire_glow.common.capability.back.IBackRunPack;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.back_type.BackTypeRegister;
import com.til.glowing_fire_glow.common.register.capability.capabilitys.BackRunPackCapabilityRegister;
import com.til.recasting.Recasting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@StaticVoluntarilyAssignment
public abstract class StandardizationAttackEntity extends Entity {

    @VoluntarilyAssignment
    protected static BackRunPackCapabilityRegister backRunPackCapabilityRegister;

    protected static final DataParameter<Integer> SHOOTING_ENTITY_ID = EntityDataManager.createKey(StandardizationAttackEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<String> MODEL = EntityDataManager.createKey(StandardizationAttackEntity.class, DataSerializers.STRING);
    protected static final DataParameter<String> TEXTURE = EntityDataManager.createKey(StandardizationAttackEntity.class, DataSerializers.STRING);
    protected static final DataParameter<Integer> MAX_LIFE = EntityDataManager.createKey(StandardizationAttackEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> COLOR = EntityDataManager.createKey(StandardizationAttackEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(StandardizationAttackEntity.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> ROLL = EntityDataManager.createKey(StandardizationAttackEntity.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> SIZE = EntityDataManager.createKey(StandardizationAttackEntity.class, DataSerializers.FLOAT);
    protected static final DataParameter<Boolean> MUTE = EntityDataManager.createKey(StandardizationAttackEntity.class, DataSerializers.BOOLEAN);


    protected IBackRunPack backRunPack = new BackRunPack();
    protected LazyOptional<IBackRunPack> iBackRunPackLazyOptional = LazyOptional.of(() -> backRunPack);

    @Nullable
    protected ResourceLocation model;
    @Nullable
    protected ResourceLocation texture;

    protected LivingEntity shooter;

    protected List<EffectInstance> effectInstanceList = new ArrayList<>();

    public StandardizationAttackEntity(EntityType<?> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn);
        if (!worldIn.isRemote && shooting == null) {
            //Recasting.LOGGER.error("[{}]类型的实体必须有shooting", StandardizationAttackEntity.class.getName());
            this.remove();
        }
        setShooter(shooting);
    }

    @Override
    protected void registerData() {
        this.dataManager.register(MAX_LIFE, 200);
        this.dataManager.register(COLOR, 0x3333FF);
        this.dataManager.register(SHOOTING_ENTITY_ID, -1);
        this.dataManager.register(MODEL, "");
        this.dataManager.register(TEXTURE, "");
        this.dataManager.register(DAMAGE, 1f);
        this.dataManager.register(ROLL, 0f);
        this.dataManager.register(SIZE, 1f);
        this.dataManager.register(MUTE, false);
    }

    @Override
    protected void readAdditional(@Nonnull CompoundNBT compound) {
    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT compound) {
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (world.isRemote) {
            return;
        }
        if (getMaxLifeTime() < ticksExisted) {
            this.setDead();
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(backRunPackCapabilityRegister.getCapability())) {
            return iBackRunPackLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }


    public void affectEntity(LivingEntity focusEntity, double factor) {
        for (EffectInstance effectinstance : effectInstanceList) {
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

    public IBackRunPack getBackRunPack() {
        return backRunPack;
    }

    public void setBackRunPack(IBackRunPack backRunPack) {
        this.backRunPack.copyTo(backRunPack);
    }

    public void setEffectInstanceList(List<EffectInstance> effectInstanceList) {
        this.effectInstanceList = effectInstanceList;
    }

    public LivingEntity getShooter() {
        if (this.shooter == null) {
            int id = this.dataManager.get(SHOOTING_ENTITY_ID);
            if (id > 0) {
                Entity entity = this.getEntityWorld().getEntityByID(id);
                if (entity instanceof LivingEntity) {
                    this.shooter = (LivingEntity) entity;
                }
            }
        }
        return this.shooter;
    }

    public void setShooter(LivingEntity shooter) {
        this.dataManager.set(SHOOTING_ENTITY_ID, shooter != null ? shooter.getEntityId() : -1);
        this.shooter = shooter;
    }


    public int getMaxLifeTime() {
        return this.dataManager.get(MAX_LIFE);
    }

    public void setMaxLifeTime(int lifeTime) {
        this.dataManager.set(MAX_LIFE, lifeTime);
    }

    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    public void setColor(int value) {
        this.getDataManager().set(COLOR, value);
    }

    public float getRoll() {
        return this.dataManager.get(ROLL);
    }

    public void setRoll(float value) {
        this.dataManager.set(ROLL, value);
    }

    public float getSize() {
        return this.dataManager.get(SIZE);
    }

    public void setSize(float size) {
        this.dataManager.set(SIZE, size);
    }

    public void setDamage(float damageIn) {
        this.dataManager.set(DAMAGE, damageIn);
    }

    public float getDamage() {
        return this.dataManager.get(DAMAGE);
    }

    public boolean isMute() {
        return this.dataManager.get(MUTE);
    }

    public void setMute(boolean mute) {
        this.dataManager.set(MUTE, mute);
    }

    public ResourceLocation getModel() {
        if (model == null) {
            String modelString = dataManager.get(MODEL);
            if (modelString.isEmpty()) {
                model = getDefaultModel();
            } else {
                model = new ResourceLocation(modelString);
            }
        }
        return model;
    }

    public void setModel(ResourceLocation model) {
        this.model = model;
        dataManager.set(MODEL, model.toString());
    }

    @Nullable
    public ResourceLocation getTexture() {
        if (texture == null) {
            String textureString = dataManager.get(TEXTURE);
            if (textureString.isEmpty()) {
                texture = getDefaultTexture();
            } else {
                texture = new ResourceLocation(textureString);

            }
        }
        return texture;
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
        dataManager.set(TEXTURE, texture.toString());
    }

    public abstract ResourceLocation getDefaultModel();

    public abstract ResourceLocation getDefaultTexture();


}
