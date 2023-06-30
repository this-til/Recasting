package com.til.recasting.common.mixin;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.recasting.common.capability.SlashBladePack;
import com.til.recasting.common.capability.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.JudgementCutEntity;
import com.til.recasting.common.event.EventDoJudgementCut;
import com.til.recasting.common.event.EventSlashBladeSA;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.entity_type.JudgementCutEntityTypeRegister;
import com.til.recasting.common.register.sa.SA_Register;
import com.til.recasting.common.register.target_selector.DefaultTargetSelectorRegister;
import com.til.recasting.common.register.util.RayTraceUtil;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.JudgementCut;
import mods.flammpfeil.slashblade.util.RayTraceHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author til
 */
@Mixin(value = {JudgementCut.class}, remap = false)
public class JudgementCutMixin {


    /**
     * @author
     * @reason
     */
    @Overwrite
    public static EntityJudgementCut doJudgementCutJust(LivingEntity user) {
        return doJudgementCut(user);
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    static public EntityJudgementCut doJudgementCut(LivingEntity user) {

        UseSlashBladeEntityPack useSlashBladeEntityPack = new UseSlashBladeEntityPack(user);
        if (!useSlashBladeEntityPack.isEffective()) {
            return null;
        }

        World worldIn = user.world;
        RayTraceResult rayTraceResult;

        @Nullable
        Entity targetEntity = useSlashBladeEntityPack.slashBladePack.slashBladeState.getTargetEntity(worldIn);
        rayTraceResult = targetEntity == null ? GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(DefaultTargetSelectorRegister.class)
                .selector(user) : new EntityRayTraceResult(targetEntity);

        Vector3d attackPos = targetEntity == null ? rayTraceResult.getHitVec() : RayTraceUtil.getPosition(targetEntity);

        EventDoJudgementCut eventDoJudgementCut = new EventDoJudgementCut(useSlashBladeEntityPack, targetEntity, attackPos, 1f);
        MinecraftForge.EVENT_BUS.post(eventDoJudgementCut);

        JudgementCutEntity jc = new JudgementCutEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(JudgementCutEntityTypeRegister.class).getEntityType(), worldIn, user);
        jc.setPosition(attackPos.x, attackPos.y, attackPos.z);
        jc.setShooter(user);
        jc.setColor(useSlashBladeEntityPack.slashBladePack.slashBladeState.getColorCode());
        worldIn.addEntity(jc);
        worldIn.playSound(null, jc.getPosX(), jc.getPosY(), jc.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.5F, 0.8F / (user.getRNG().nextFloat() * 0.4F + 0.8F));

        return null;
    }
/*    @Inject(at = @At("HEAD"), method = "doJudgementCutJust")
    private static void doJudgementCutJust(LivingEntity user, CallbackInfoReturnable<EntityJudgementCut> cir) {
        ItemStack stack = user.getHeldItemMainhand();
        SlashBladePack slashBladePack = new SlashBladePack(stack);
        if (!slashBladePack.isEffective()) {
            return;
        }
        SA_Register sa_register = slashBladePack.isa.getSA();
        if (sa_register == null) {
            return;
        }
        cir.cancel();
        JudgementCut.doJudgementCut(user);
    }

    @Inject(at = @At("HEAD"), method = "doJudgementCut", cancellable = true)
    private static void doJudgementCut(LivingEntity user, CallbackInfoReturnable<EntityJudgementCut> cir) {
        UseSlashBladeEntityPack slashBladeEntityPack = new UseSlashBladeEntityPack(user);
        if (slashBladeEntityPack.isEffective()) {
            return;
        }

        SA_Register sa_register = slashBladeEntityPack.slashBladePack.isa.getSA();
        if (sa_register == null) {
            return;
        }
        cir.cancel();
        cir.setReturnValue(null);
        MinecraftForge.EVENT_BUS.post(new EventSlashBladeSA(slashBladeEntityPack));
        sa_register.trigger( slashBladeEntityPack);
    }*/
}
