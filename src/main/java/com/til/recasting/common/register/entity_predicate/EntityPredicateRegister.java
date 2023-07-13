package com.til.recasting.common.register.entity_predicate;

import com.til.glowing_fire_glow.common.register.RegisterBasics;
import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

public abstract class EntityPredicateRegister extends RegisterBasics {

    public abstract boolean canTarget(@Nullable Entity attacker, Entity target);
}
