package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.RandomUtil;
import com.til.recasting.common.register.attack_type.instance.DriveSwordAttackType;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.util.AttackManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@StaticVoluntarilyAssignment
public class PlanetEntity extends DriveEntity {

    @VoluntarilyAssignment
    protected static DefaultEntityPredicateRegister defaultEntityPredicateRegister;

    @Deprecated
    protected static final DataParameter<Boolean> PREPARATION_EXPLOSION = EntityDataManager.createKey(PlanetEntity.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Float> EXPLOSION_ATTACK = EntityDataManager.createKey(PlanetEntity.class, DataSerializers.FLOAT);
    @Deprecated
    protected static final DataParameter<Integer> EXPLOSION_TIME = EntityDataManager.createKey(PlanetEntity.class, DataSerializers.VARINT);
    @Deprecated
    protected static final DataParameter<Integer> PREPARATION_TIME = EntityDataManager.createKey(PlanetEntity.class, DataSerializers.VARINT);
    @Deprecated
    protected Vector3d explosionPos;

    public PlanetEntity(EntityType<? extends SlashEffectEntity> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn, shooting);
        setMaxLifeTime(200);
    }


    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(PREPARATION_EXPLOSION, false);
        this.dataManager.register(EXPLOSION_ATTACK, 1f);
        this.dataManager.register(EXPLOSION_TIME, 0);
        this.dataManager.register(PREPARATION_TIME, 20);
    }


    @Override
    protected void hitBlock(RayTraceResult rayTraceResult) {

        explosion();

        /*setMotion(0, 0, 0);
        if (isPreparationExplosion()) {
            setPosition(explosionPos.getX(), explosionPos.getY(), explosionPos.getZ());
        } else {
            explosionPos = getPositionVec();
            setPreparationExplosion(true);
            setExplosionTime(ticksExisted + getPreparationTime());
        }*/

    }

/*    @Override
    public void tick() {
        super.tick();
        if (isPreparationExplosion() && ticksExisted > getExplosionTime()) {
            explosion();
        }
    }*/

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        Vector3d move = getMotion();
        move = move.scale(0.5);
        if (world.isRemote) {
            /*int n = 255;
            for (int i = 0; i < n; i++) {
                Vector3d pos = getPositionVec().add(RandomUtil.nextVector3dInCircles(getShooter().getRNG(), getSize() * 4 - 2));
                world.addParticle(ParticleTypes.ENCHANTED_HIT, pos.getX(), pos.getY(), pos.getZ(), -move.getX(), -move.getY(), -move.getZ());
            }*/
        }
    }

    private void explosion() {
        this.setDead();
        if (world.isRemote) {
            return;
        }
        Entity shooter = getShooter();
        if (shooter != null) {
            int es = (int) getSize() * 4 * 2;
            for (int i = 0; i < es; i++) {
                Vector3d pos = getPositionVec().add(RandomUtil.nextVector3dInCircles(getShooter().getRNG(), getSize() * 4 - 2));
                ((ServerWorld) this.world).spawnParticle(ParticleTypes.EXPLOSION_EMITTER, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0, 0, 0);
            }
        }
        this.world.playSound(null, getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, 2 * (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        List<Entity> founds = world.getEntitiesInAABBexcluding(this, new Pos(this).axisAlignedBB(getSize() * 4), entity -> defaultEntityPredicateRegister.canTarget(getShooter(), entity));
        for (Entity found : founds) {
            AttackManager.doAttack(getShooter(), found, getExplosionDamage(), false, false , true, ListUtil.of());
            /*float f2 = getSize() * 4 * 2.0F;
            double d12 = MathHelper.sqrt(found.getDistanceSq(getPositionVec())) / f2;
            if (d12 <= 1.0D) {
                double d5 = found.getPosX() - this.getPosX();
                double d7 = (found instanceof TNTEntity ? found.getPosY() : found.getPosYEye()) - this.getPosZ();
                double d9 = found.getPosZ() - this.getPosZ();
                double d13 = MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                if (d13 != 0.0D) {
                    d5 = d5 / d13;
                    d7 = d7 / d13;
                    d9 = d9 / d13;
                    double d14 = Explosion.getBlockDensity(getPositionVec(), found);
                    double d10 = (1.0D - d12) * d14;
                    //
                    double d11 = d10;
                    found.setMotion(found.getMotion().add(d5 * d11, d7 * d11, d9 * d11));
                }
            }*/
        }
    }

    @Deprecated
    public int getExplosionTime() {
        return this.dataManager.get(EXPLOSION_TIME);
    }

    @Deprecated
    public void setExplosionTime(int explosionTime) {
        this.dataManager.set(EXPLOSION_TIME, explosionTime);
    }

    @Deprecated
    public boolean isPreparationExplosion() {
        return this.dataManager.get(PREPARATION_EXPLOSION);
    }

    @Deprecated
    public void setPreparationExplosion(boolean preparationExplosion) {
        this.dataManager.set(PREPARATION_EXPLOSION, preparationExplosion);
    }

    public void setExplosionDamage(float damageIn) {
        this.dataManager.set(EXPLOSION_ATTACK, damageIn);
    }

    public float getExplosionDamage() {
        return this.dataManager.get(EXPLOSION_ATTACK);
    }

    @Deprecated
    public int getPreparationTime() {
        return this.dataManager.get(PREPARATION_TIME);
    }

    @Deprecated

    public void setPreparationTime(int preparationTime) {
        this.dataManager.set(PREPARATION_TIME, preparationTime);
    }
}
