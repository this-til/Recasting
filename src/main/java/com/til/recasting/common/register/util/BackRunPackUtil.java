package com.til.recasting.common.register.util;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.capability.back.IBackRunPack;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.back_type.BackTypeRegister;
import com.til.recasting.common.entity.SlashEffectEntity;
import com.til.recasting.common.register.back_type.SlashEffectEntityBackTypeRegister;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.entity.LivingEntity;

@StaticVoluntarilyAssignment
public class BackRunPackUtil {

    @VoluntarilyAssignment
    protected static SlashEffectEntityBackTypeRegister.SlashEffectAttackBackTypeRegister slashEffectAttackBackTypeRegister;

    public static void addKnockBacks(SlashEffectEntity slashEffectEntity, KnockBacks knockBacks) {
        slashEffectEntity.getBackRunPack().addRunBack(
                slashEffectAttackBackTypeRegister,
                (slashEffectEntity1, hitEntity) -> {
                    if (!(hitEntity instanceof LivingEntity)) {
                        return;
                    }
                    knockBacks.action.accept(((LivingEntity) hitEntity));
                }
        );
    }
}
