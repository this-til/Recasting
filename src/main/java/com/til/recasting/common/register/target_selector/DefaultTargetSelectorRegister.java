package com.til.recasting.common.register.target_selector;

import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;

@VoluntarilyRegister
public class DefaultTargetSelectorRegister extends TargetSelectorRegister {

    @VoluntarilyAssignment
    protected DefaultEntityPredicateRegister defaultEntityPredicateRegister;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        range = 32;
        entityPredicateRegister = defaultEntityPredicateRegister;
    }
}
