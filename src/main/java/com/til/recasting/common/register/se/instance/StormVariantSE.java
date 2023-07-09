package com.til.recasting.common.register.se.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.event.EventDoJudgementCut;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.se.SE_Register;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/***
 * 风暴.变体
 * 触发审判时，召唤幻影剑进行攻击
 */
@VoluntarilyRegister
public class StormVariantSE extends SE_Register {

    @ConfigField
    protected NumberPack attack;

    @ConfigField
    protected NumberPack number;

    @VoluntarilyAssignment
    protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

    @SubscribeEvent
    protected void onEventDoJudgementCut(EventDoJudgementCut event) {
        if (!event.pack.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.pack.slashBladePack.ise.getPack(this);
        Random random = event.pack.entity.getRNG();
        int n = (int) number.of(se_pack.getLevel());
        float a = (float) attack.of(se_pack.getLevel());
        for (int i = 0; i < n; i++) {
            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), event.pack.entity.world, event.pack.entity);
            event.pack.slashBladePack.iSlashBladeStateSupplement.decorate(summondSwordEntity);
            Vector3d pos = event.pos.add(
                    -1.5 + random.nextDouble() * 9,
                    12 + random.nextDouble() * 9,
                    -1.5 + random.nextDouble() * 9);
            summondSwordEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
            summondSwordEntity.lookAt(event.pos, false);
            summondSwordEntity.setColor(event.pack.slashBladePack.slashBladeState.getColorCode());
            summondSwordEntity.setDamage(a);
            summondSwordEntity.setStartDelay(random.nextInt(10));
            event.pack.entity.world.addEntity(summondSwordEntity);
        }
        event.pack.entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 0.2F, 1.45F);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = new NumberPack(0, 0.05);
        number = new NumberPack(4, 2);
    }

}
