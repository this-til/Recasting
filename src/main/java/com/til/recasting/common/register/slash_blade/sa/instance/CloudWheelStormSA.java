package com.til.recasting.common.register.slash_blade.sa.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.LightningEntity;
import com.til.recasting.common.entity.SummondSwordEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.awt.*;

@VoluntarilyRegister
public class CloudWheelStormSA extends CloudWheelSA {

    @ConfigField
    protected int lightningNumber;

    @Override
    public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
        super.trigger(slashBladeEntityPack);
        Vector3d attackPos = slashBladeEntityPack.getAttackPos();

        for (int i = 0; i < lightningNumber; i++) {
            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
            summondSwordEntity.setColor(new Color(255, 255, 0).getRGB());
            summondSwordEntity.setSize(1.25f);
            summondSwordEntity.setDamage(attack);
            summondSwordEntity.setStartDelay(i * 2);
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
        attack = 0.15f;
        attackNumber = 10;
        lightningNumber = 7;
    }
}
