package com.til.recasting.common.mixin;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.recasting.common.entity.SlashEffectEntity;
import com.til.recasting.common.register.back_type.SlashEffectEntityBackTypeRegister;
import mods.flammpfeil.slashblade.entity.EntitySlashEffect;
import mods.flammpfeil.slashblade.util.AttackManager;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * @author til
 */
@Mixin(value = {AttackManager.class}, remap = false)
public class AttackManagerMixin {

    /**
     * @author til
     * @reason
     */
    @Overwrite
    public static EntitySlashEffect doSlash(LivingEntity playerIn, float roll, int colorCode, Vector3d centerOffset, boolean mute, boolean critical, double damage, KnockBacks knockback) {
        com.til.recasting.common.register.util.AttackManager.doSlash(playerIn, roll, colorCode, centerOffset, mute, critical, (float) damage, 1,
                slashEffectEntity -> slashEffectEntity.getBackRunPack().addRunBack(
                        GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SlashEffectEntityBackTypeRegister.AttackBackTypeRegister.class),
                        (slashEffectEntity1, hitEntity) -> {
                            if (!(hitEntity instanceof LivingEntity)) {
                                return;
                            }
                            knockback.action.accept(((LivingEntity) hitEntity));
                        }
                ));
        return null;
    }


}
