package com.til.recasting.common.register.se.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.util.math.NumberPack;
import com.til.recasting.common.capability.ISE;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.event.EventSlashBladeDoSlash;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.se.SE_Register;
import com.til.recasting.util.RayTraceUtil;
import mods.flammpfeil.slashblade.specialattack.JudgementCut;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/***
 * 须臾
 * 挥刀时召唤幻影剑协同攻击
 */
@VoluntarilyRegister
public class MomentSE extends SE_Register {

    @ConfigField
    protected NumberPack attack;

    @VoluntarilyAssignment
    protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

    @SubscribeEvent
    protected void onEventSlashBladeDoSlash(EventSlashBladeDoSlash event) {
        if (!event.slashBladePack.ise.hasSE(this)) {
            return;
        }
        ISE.SE_Pack se_pack = event.slashBladePack.ise.getPack(this);
        Entity targetEntity = event.slashBladePack.slashBladeState.getTargetEntity(event.livingEntity.world);
        SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), event.livingEntity.world, event.livingEntity);
        summondSwordEntity.setColor(event.slashBladePack.slashBladeState.getColorCode());
        summondSwordEntity.setDamage(attack.of(se_pack.getLevel()));
        if (targetEntity != null) {
            summondSwordEntity.lookAt(RayTraceUtil.getPosition(targetEntity));
        }
        event.livingEntity.world.addEntity(summondSwordEntity);
    }


    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = new NumberPack(0, 0.2);
    }
}
