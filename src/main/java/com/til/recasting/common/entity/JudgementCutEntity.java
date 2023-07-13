package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.common.register.entity_type.SlashEffectEntityTypeRegister;
import com.til.recasting.common.register.util.AttackManager;
import com.til.recasting.common.register.util.HitAssessment;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
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
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@StaticVoluntarilyAssignment
public class JudgementCutEntity extends Entity {

    @VoluntarilyAssignment
    protected static SlashEffectEntityTypeRegister slashEffectEntityTypeRegister;

    protected static final DataParameter<Integer> COLOR = EntityDataManager.createKey(JudgementCutEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> FLAGS = EntityDataManager.createKey(JudgementCutEntity.class, DataSerializers.VARINT);

    protected int lifetime = 10;
    protected int seed = -1;

    protected double damage = 1.0D;

    protected boolean cycleHit = false;

    protected List<Entity> excludeEntity = new ArrayList<>();


    public int getSeed() {
        return seed;
    }

    public boolean doCycleHit() {
        return cycleHit;
    }

    public void setCycleHit(boolean cycleHit) {
        this.cycleHit = cycleHit;
    }


    protected SoundEvent livingEntitySound = SoundEvents.ENTITY_WITHER_HURT;

    public JudgementCutEntity(EntityType<? extends JudgementCutEntity> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn);
        this.setNoGravity(true);
        this.seed = this.rand.nextInt(360);
        if (shooting != null) {
            excludeEntity.add(this);
            excludeEntity.add(shooting);
            setShooter(shooter);
        }
    }

    public static EntityJudgementCut createInstance(FMLPlayMessages.SpawnEntity packet, World worldIn) {
        return new EntityJudgementCut(SlashBlade.RegistryEvents.JudgementCut, worldIn);
    }

    @Override
    protected void registerData() {
        this.dataManager.register(COLOR, 0x3333FF);
        this.dataManager.register(FLAGS, 0);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
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

        if (!world.isRemote && getShooter() == null) {
            this.remove();
            return;
        }

        if (ticksExisted < 8 && ticksExisted % 2 == 0) {
            this.playSound(livingEntitySound, 0.2F, 0.5F + 0.25f * this.rand.nextFloat());
        }

        if (!world.isRemote) {
            //cyclehit
            if (this.ticksExisted % 2 == 0) {
                KnockBacks knockBackType = getIsCritical() ? KnockBacks.toss : KnockBacks.cancel;
                AttackManager.areaAttack(getShooter(), this, knockBackType.action, 4.0f, (float) getDamage(), false, true, true, excludeEntity);
            }

           /* final int count = 3;
            if (0 < ticksExisted && ticksExisted <= count) {

                SlashEffectEntity slashEffectEntity = new SlashEffectEntity(slashEffectEntityTypeRegister.getEntityType(), world, getShooter());
                slashEffectEntity.setPositionAndRotation(this.getPosX(), this.getPosY(), this.getPosZ(), (360.0f / count) * ticksExisted + this.seed, 0);
                slashEffectEntity.setRotationRoll(30);
                slashEffectEntity.setMute(false);
                slashEffectEntity.setIsCritical(getIsCritical());
                slashEffectEntity.setDamage(getDamage());
                slashEffectEntity.setColor(this.getColor());
                slashEffectEntity.setBaseSize(0.5f);
                slashEffectEntity.setKnockBack(KnockBacks.cancel);
                slashEffectEntity.setIndirect(true);
                this.world.addEntity(slashEffectEntity);
            }*/
            if (getLifetime() < this.ticksExisted) {
                this.burst();
            }
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

    protected LivingEntity shooter;

    public LivingEntity getShooter() {
        return shooter;
    }

    public void setShooter(LivingEntity shooter) {
        this.shooter = shooter;
    }

    public List<EffectInstance> getPotionEffects() {
        List<EffectInstance> effects = PotionUtils.getEffectsFromTag(this.getPersistentData());

        if (effects.isEmpty()) {
            effects.add(new EffectInstance(Effects.POISON, 1, 1));
        }

        return effects;
    }

    public void burst() {
        if (!this.world.isRemote) {
            if (this.world instanceof ServerWorld) {
                ((ServerWorld) this.world).spawnParticle(ParticleTypes.CRIT, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
            }
            this.burst(getPotionEffects(), null);
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

    public void setDamage(double damageIn) {
        this.damage = damageIn;
    }

    public double getDamage() {
        return this.damage;
    }


    @Nullable
    public EntityRayTraceResult getRayTrace(Vector3d p_213866_1_, Vector3d p_213866_2_) {
        return ProjectileHelper.rayTraceEntities(this.world, this, p_213866_1_, p_213866_2_, this.getBoundingBox().expand(this.getMotion()).grow(1.0D), (p_213871_1_) -> {
            return !p_213871_1_.isSpectator() && p_213871_1_.isAlive() && p_213871_1_.canBeCollidedWith() && (p_213871_1_ != this.getShooter());
        });
    }

}
