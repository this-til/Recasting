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
import net.minecraft.util.ResourceLocation;
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
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@StaticVoluntarilyAssignment
public class JudgementCutEntity extends StandardizationAttackEntity {



    protected int seed;
    protected List<Entity> excludeEntity = new ArrayList<>();

    public int getSeed() {
        return seed;
    }

    protected SoundEvent livingEntitySound = SoundEvents.ENTITY_WITHER_HURT;

    public JudgementCutEntity(EntityType<? extends JudgementCutEntity> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn, shooting);
        this.setNoGravity(true);
        this.setMaxLifeTime(10);
        this.seed = this.rand.nextInt(360);
        if (shooting != null) {
            excludeEntity.add(this);
            excludeEntity.add(shooting);
            setShooter(shooter);
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
                KnockBacks knockBackType = KnockBacks.cancel;
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
        }
    }

    protected LivingEntity shooter;

    public LivingEntity getShooter() {
        return shooter;
    }

    public void setShooter(LivingEntity shooter) {
        this.shooter = shooter;
    }


    @Override
    protected void setDead() {
        super.setDead();
        if (this.world.isRemote) {
            return;
        }
        ((ServerWorld) this.world).spawnParticle(ParticleTypes.CRIT, this.getPosX(), this.getPosY(), this.getPosZ(), 16, 0.5, 0.5, 0.5, 0.25f);
        List<Entity> list = HitAssessment.getTargettableEntitiesWithinAABB(this.world, getShooter(), this, 2);
        list.stream().filter(e -> e instanceof LivingEntity).map(e -> (LivingEntity) e).forEach(e -> {
            double distanceSq = this.getDistanceSq(e);
            if (distanceSq < 9.0D * getSize()) {
                double factor = 1.0D - Math.sqrt(distanceSq) / 4.0D;
                affectEntity(e, factor);
            }
        });
    }

    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(SlashBlade.modid, "model/util/slashdim.obj");
    public static final ResourceLocation RESOURCE_LOCATION1 = new ResourceLocation(SlashBlade.modid, "model/util/slashdim.png");
    @Override
    public ResourceLocation getDefaultModel() {
        return RESOURCE_LOCATION;
    }

    @Override
    public ResourceLocation getDefaultTexture() {
        return RESOURCE_LOCATION1;
    }

}
