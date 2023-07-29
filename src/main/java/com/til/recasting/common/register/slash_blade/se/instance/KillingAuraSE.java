package com.til.recasting.common.register.slash_blade.se.instance;

import com.til.glowing_fire_glow.GlowingFireGlow;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.entity.SlashEffectEntity;
import com.til.recasting.common.event.EventDoAttack;
import com.til.recasting.common.register.entity_type.SlashEffectEntityTypeRegister;
import com.til.recasting.common.register.slash_blade.se.SE_Register;
import mods.flammpfeil.slashblade.util.KnockBacks;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/***
 * 杀戮光环
 * 对目标造成伤害后，追加一道剑气攻击目标
 */
@VoluntarilyRegister
public class KillingAuraSE extends SE_Register {

    @ConfigField
    protected NumberPack cool;

    @ConfigField
    protected NumberPack attack;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventDoAttack event) {
        if (!event.pack.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
        if (event.pack.entity.world.getGameTime() - se_pack.getOldTime() < cool.of(se_pack.getLevel())) {
            return;
        }
        se_pack.setOldTime(event.pack.entity.world.getGameTime());

        Vector3d attackPos = event.target.getPositionVec();
        Random random = event.pack.entity.getRNG();
        double x = random.nextDouble() * 2 - 1;
        double y = random.nextDouble() * 2 - 1;
        double z = random.nextDouble() * 2 - 1;
        double desiredLength = 4 * event.pack.slashBladePack.iSlashBladeStateSupplement.getAttackDistance();
        x *= desiredLength;
        y *= desiredLength;
        z *= desiredLength;
        Vector3d pos = attackPos.add(x, y, z);
        SlashEffectEntity jc = new SlashEffectEntity(GlowingFireGlow.getInstance().getReflexManage().getVoluntarilyRegisterOfClass(SlashEffectEntityTypeRegister.class).getEntityType(),
                event.pack.entity.world, event.pack.entity);
        event.pack.slashBladePack.iSlashBladeStateSupplement.decorate(jc);
        jc.setPosition(pos.getX(), pos.getY(), pos.getZ());
        jc.setRoll(random.nextInt(360));
        jc.lookAt(attackPos, false);
        jc.setColor(event.pack.slashBladePack.slashBladeState.getColorCode());
        jc.setMute(false);
        jc.setDamage((float) attack.of(se_pack.getLevel()));
        jc.setSize((float) (desiredLength / 4));
        event.pack.entity.world.addEntity(jc);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        cool = new NumberPack(10, -1);
        attack = new NumberPack(0, 0.2);
    }
}
