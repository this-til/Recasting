package com.til.recasting.common.register.entity_predicate;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;

/***
 * 默认攻击的攻击过滤器
 */
@VoluntarilyRegister
public class DefaultEntityPredicateRegister extends LikeOriginalEntityPredicateRegister {

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        friendlyFire = true;
        skipAttackChecks = true;
        allowInvulnerable = true;
        isLivingEntity = true;
    }
}
