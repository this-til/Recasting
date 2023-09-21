package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.recasting.common.register.attack_type.instance.JudgementCutAttackType;
import com.til.recasting.common.register.back_type.JudgementCutBackTypeRegister;
import com.til.recasting.common.register.util.AttackManager;
import com.til.recasting.common.register.util.HitAssessment;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@StaticVoluntarilyAssignment
public class JudgementCutEntity extends StandardizationAttackEntity {


    @VoluntarilyAssignment
    protected static JudgementCutBackTypeRegister.JudgementCutTickBackTypeRegister judgementCutTickBackTypeRegister;

    @VoluntarilyAssignment
    protected static JudgementCutBackTypeRegister.JudgementCutAttackBackTypeRegister judgementCutAttackBackTypeRegister;

    @VoluntarilyAssignment
    protected static JudgementCutBackTypeRegister.JudgementCutDeathBackTypeRegister judgementCutDeathBackTypeRegister;

    @VoluntarilyAssignment
    protected static JudgementCutAttackType judgementCutAttackType;


    protected static final DataParameter<Integer> ATTACK_INTERVAL = EntityDataManager.createKey(JudgementCutEntity.class, DataSerializers.VARINT);


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
        getBackRunPack().addRunBack(judgementCutAttackBackTypeRegister, (judgementCutEntity, hitEntity) -> {
            if (hitEntity instanceof LivingEntity) {
                KnockBacks.cancel.action.accept((LivingEntity) hitEntity);
            }
        });
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ATTACK_INTERVAL, 2);
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

            getBackRunPack().runBack(judgementCutTickBackTypeRegister, a -> a.tick(this));

            //cyclehit
            if (this.ticksExisted % getAttackInterval() == 0) {
                AttackManager.areaAttack(
                        getShooter(),
                        this,
                        entity -> getBackRunPack().runBack(judgementCutAttackBackTypeRegister, a -> a.attack(this, entity)),
                        getSize(),
                        getDamage(),
                        false,
                        true,
                        true,
                        excludeEntity,
                        ListUtil.of(judgementCutAttackType));
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
        getBackRunPack().runBack(judgementCutDeathBackTypeRegister, a -> a.death(this));
    }


    public int getAttackInterval() {
        return this.dataManager.get(ATTACK_INTERVAL);
    }

    public void setAttackInterval(int attackInterval) {
        this.dataManager.set(ATTACK_INTERVAL, attackInterval);
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
