package com.til.recasting.common.entity;

import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

@StaticVoluntarilyAssignment
public class BallLightningEntity extends DriveEntity{


    public BallLightningEntity(EntityType<? extends SlashEffectEntity> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn, shooting);
    }

    @Override
    public void tick() {
        super.tick();
        if (world.isRemote) {
            return;
        }
        if (this.ticksExisted % getAttackInterval() != 0) {
            onAttack();
        }

    }

    @Override
    protected void onAttack() {
    }


}
