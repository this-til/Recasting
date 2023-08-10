package com.til.recasting.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class StellarRotationEntity extends JudgementCutEntity{
    public StellarRotationEntity(EntityType<? extends JudgementCutEntity> entityTypeIn, World worldIn, LivingEntity shooting) {
        super(entityTypeIn, worldIn, shooting);
    }
}
