package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.util.ListUtil;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.recasting.common.register.attack_type.instance.LightningAttackType;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.util.AttackManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@StaticVoluntarilyAssignment
public class LightningEntity extends StandardizationAttackEntity {

    @VoluntarilyAssignment
    protected static DefaultEntityPredicateRegister defaultEntityPredicateRegister;

    @VoluntarilyAssignment
    protected static LightningAttackType lightningAttackType;

    protected long boltVertex;
    protected boolean isInit;

    public LightningEntity(EntityType<?> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn, shooting);
        setMaxLifeTime(this.rand.nextInt(15) + 5);
        boltVertex = this.rand.nextLong();
    }

    public void tick() {
        super.tick();

        if (ticksExisted % 5 == 0) {
            this.boltVertex = this.rand.nextLong();

        }

        if (world.isRemote) {
            this.world.setTimeLightningFlash(2);
            return;
        }

        if (!isInit) {
            isInit = true;
            if (!isMute()) {
                this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
                this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
            }

            float range = 3 * getSize();
            List<Entity> entityList = world.getEntitiesInAABBexcluding(this, new Pos(this).axisAlignedBB(range), entity -> defaultEntityPredicateRegister.canTarget(getShooter(), entity));
            for (Entity entity : entityList) {
                AttackManager.doAttack(getShooter(), entity, getDamage(), true, true, true, ListUtil.of(lightningAttackType));
                if (entity instanceof  LivingEntity) {
                    affectEntity(((LivingEntity) entity), 1);   
                }
            }
        }
    }

    @Override
    public ResourceLocation getDefaultModel() {
        return null;
    }

    @Override
    public ResourceLocation getDefaultTexture() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = 64.0D * getRenderDistanceWeight();
        return distance < d0 * d0;
    }

    public long getBoltVertex() {
        return boltVertex;
    }
}
