package com.til.recasting.common.register.slash_blade.sa.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.RandomUtil;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.LightningEntity;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.back_type.SummondSwordBackTypeRegister;
import com.til.recasting.common.register.entity_type.LightningEntityTypeRegister;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import net.minecraft.util.math.vector.Vector3d;

import java.awt.*;
import java.util.Random;

@VoluntarilyRegister
public class CloudWheelSA extends SA_Register {
    @VoluntarilyAssignment
    protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

    @VoluntarilyAssignment
    protected LightningEntityTypeRegister lightningEntityTypeRegister;

    @VoluntarilyAssignment
    protected SummondSwordBackTypeRegister.SummondSwordAttackBackTypeRegister attackBackTypeRegister;

    @ConfigField
    protected float attack;

    @ConfigField
    protected int attackNumber;

    @ConfigField
    protected float lightningAttack;

    @Override
    public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
        Vector3d attackPos = slashBladeEntityPack.getAttackPos();
        Random random = slashBladeEntityPack.getEntity().getRNG();

        for (int i = 0; i < attackNumber; i++) {
            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
            slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
            Vector3d pos = attackPos.add(0,5,0).add(RandomUtil.nextVector3dInCircles(random, 2.5));
            summondSwordEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
            summondSwordEntity.lookAt(attackPos, false);
            summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
            summondSwordEntity.setStartDelay(5);
            summondSwordEntity.setDamage(attack);
            summondSwordEntity.setRoll(random.nextInt(360));
            slashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
        }

        SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
        slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
        summondSwordEntity.setColor(new Color(255, 255, 0).getRGB());
        summondSwordEntity.setPosition(attackPos.getX(), attackPos.getY() + 7, attackPos.getZ());
        summondSwordEntity.setSize(1.25f);
        summondSwordEntity.setDamage(attack);
        summondSwordEntity.setStartDelay(10);
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

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = 0.2f;
        attackNumber = 6;
        lightningAttack = 1.35f;
    }

    @VoluntarilyRegister
    public static class CloudWheelStormSA extends CloudWheelSA {

        @ConfigField
        protected int lightningNumber;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            super.trigger(slashBladeEntityPack);
            Vector3d attackPos = slashBladeEntityPack.getAttackPos();

            for (int i = 0; i < lightningNumber; i++) {
                SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
                slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
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
}
