package com.til.recasting.common.mixin;

import com.til.recasting.common.register.util.JudgementCutManage;
import mods.flammpfeil.slashblade.entity.EntityJudgementCut;
import mods.flammpfeil.slashblade.specialattack.JudgementCut;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

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
        JudgementCutManage.doJudgementCut(user, 1, 10, null, null);
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
