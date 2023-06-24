package com.til.recasting.common.mixin;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.recasting.common.capability.ISA;
import com.til.recasting.common.event.EventSlashBladeSA;
import com.til.recasting.common.register.capability.SA_CapabilityRegister;
import com.til.recasting.common.register.sa.SA_Register;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.specialattack.JudgementCut;
import mods.flammpfeil.slashblade.util.RayTraceHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * @author til
 */
@Mixin(value = {JudgementCut.class}, remap = false)
public class JudgementCutMixin {

    @Inject(at = @At("HEAD"), method = "doJudgementCutJust", cancellable = true)
    private static void doJudgementCutJust(LivingEntity user, CallbackInfoReturnable<EntityJudgementCut> cir) {
        ItemStack stack = user.getHeldItemMainhand();
        ISA isa = stack.getCapability(SA_CapabilityRegister.isaCapability).orElse(null);
        ISlashBladeState slashBladeState = stack.getCapability(ItemSlashBlade.BLADESTATE).orElse(null);
        if (isa == null || slashBladeState == null) {
            return;
        }
        SA_Register sa_register = isa.getSA();
        if (sa_register == null) {
            return;
        }
        cir.cancel();
        JudgementCut.doJudgementCut(user);
    }

    @Inject(at = @At("HEAD"), method = "doJudgementCut", cancellable = true)
    private static void doJudgementCut(LivingEntity user, CallbackInfoReturnable<EntityJudgementCut> cir) {
        ItemStack stack = user.getHeldItemMainhand();
        ISA isa = stack.getCapability(SA_CapabilityRegister.isaCapability).orElse(null);
        ISlashBladeState slashBladeState = stack.getCapability(ItemSlashBlade.BLADESTATE).orElse(null);
        if (isa == null || slashBladeState == null) {
            return;
        }
        SA_Register sa_register = isa.getSA();
        if (sa_register == null) {
            return;
        }
        cir.cancel();
        cir.setReturnValue(null);
        MinecraftForge.EVENT_BUS.post(new EventSlashBladeSA(user, stack, isa));
        sa_register.trigger(user, stack, slashBladeState, isa);
    }


    /**
     * @author til
     * @reason 添加触发sa能力
     */
   /* @Overwrite(remap = false)
    @Mutable
    public static EntityJudgementCut doJudgementCut(LivingEntity user) {

        World worldIn = user.world;
        ItemStack stack = user.getHeldItemMainhand();

        ISA isa = stack.getCapability(SA_CapabilityRegister.isaCapability)

        Vector3d eyePos = user.getEyePosition(1.0f);
        final double airReach = 5;
        final double entityReach = 7;


        ISlashBladeState iSlashBladeState = stack.getCapability(ItemSlashBlade.BLADESTATE).orElse(null);
        if (iSlashBladeState == null) {
            return
        }

        Optional<Vector3d> resultPos = stack.getCapability(ItemSlashBlade.BLADESTATE)
                .filter(s -> s.getTargetEntity(worldIn) != null)
                .map(s -> s.getTargetEntity(worldIn).getEyePosition(1.0f));


        if (!resultPos.isPresent()) {
            Optional<RayTraceResult> raytraceresult = RayTraceHelper.rayTrace(
                    worldIn, user, eyePos, user.getLookVec(), airReach, entityReach,
                    (entity) -> {
                        return !entity.isSpectator() && entity.isAlive() && entity.canBeCollidedWith() && (entity != user);
                    });

            resultPos = raytraceresult.map((rtr) -> {
                Vector3d pos = null;
                RayTraceResult.Type type = rtr.getType();
                switch (type) {
                    case ENTITY:
                        Entity target = ((EntityRayTraceResult) rtr).getEntity();
                        pos = target.getPositionVec().add(0, target.getEyeHeight() / 2.0f, 0);
                        break;
                    case BLOCK:
                        Vector3d hitVec = rtr.getHitVec();
                        pos = hitVec;
                        break;
                }
                return pos;
            });
        }

        Vector3d pos = resultPos.orElseGet(() -> eyePos.add(user.getLookVec().scale(airReach)));

        EntityJudgementCut jc = new EntityJudgementCut(SlashBlade.RegistryEvents.JudgementCut, worldIn);
        jc.setPosition(pos.x, pos.y, pos.z);
        jc.setShooter(user);

        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent((state) -> {
            jc.setColor(state.getColorCode());
        });


        worldIn.addEntity(jc);

        worldIn.playSound(null, jc.getPosX(), jc.getPosY(), jc.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.5F, 0.8F / (user.getRNG().nextFloat() * 0.4F + 0.8F));

        return jc;
    }*/
}