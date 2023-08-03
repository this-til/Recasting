package com.til.recasting.common.register.slash_blade.sa.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.LightningEntity;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.back_type.SummondSwordBackTypeRegister;
import com.til.recasting.common.register.entity_type.LightningEntityTypeRegister;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import net.minecraft.util.math.vector.Vector3d;

@VoluntarilyRegister
public class HeavenTwelveHitSA extends SA_Register {

    @VoluntarilyAssignment
    protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

    @VoluntarilyAssignment
    protected LightningEntityTypeRegister lightningEntityTypeRegister;

    @VoluntarilyAssignment
    protected SummondSwordBackTypeRegister.AttackBackTypeRegister attackBackTypeRegister;

    @ConfigField
    protected int lightningNumber;

    @ConfigField
    protected float attack;

    @ConfigField
    protected float lightningAttack;

    @Override
    public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
        Vector3d attackPos = slashBladeEntityPack.getAttackPos();

        for (int i = 0; i < lightningNumber; i++) {
            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
            summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
            summondSwordEntity.setSize(1.25f);
            summondSwordEntity.setDamage(attack);
            //summondSwordEntity.setStartDelay(i * 2);
            summondSwordEntity.lookAt(attackPos, false);
            summondSwordEntity.getBackRunPack().addRunBack(attackBackTypeRegister,
                    (summondSwordEntity1, hitEntity) -> {
                        LightningEntity lightningEntity = new LightningEntity(lightningEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
                        lightningEntity.setPosition(hitEntity.getPosX(), hitEntity.getPosY(), hitEntity.getPosZ());
                        lightningEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                        lightningEntity.setDamage(lightningAttack);
                        slashBladeEntityPack.getEntity().world.addEntity(lightningEntity);
                    });
            slashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
        }
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        lightningNumber = 12;
        attack = 0.1f;
        lightningAttack = 1;
    }
}
