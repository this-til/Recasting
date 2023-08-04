package com.til.recasting.common.register.slash_blade.sa.instance;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.RandomUtil;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_type.SummondSwordEntityTypeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Random;

/***
 * 剑雨
 */
@VoluntarilyRegister
public class SwordRainSA extends SA_Register {

    @VoluntarilyAssignment
    protected SummondSwordEntityTypeRegister summondSwordEntityTypeRegister;

    @ConfigField
    protected float attack;

    @ConfigField
    protected int attackNumber;

    @ConfigField
    protected float range;

    @Override
    public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
        Vector3d pos = slashBladeEntityPack.getEntity().getPositionVec().add(0, range / 2, 0);
        Random random = slashBladeEntityPack.getEntity().getRNG();
        for (int i = 0; i < attackNumber; i++) {
            SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
            slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
            Vector3d _pos = pos.add(RandomUtil.nextVector3dInCircles(random, range));
            summondSwordEntity.setPosition(_pos.getX(), _pos.getY(), _pos.getZ());
            summondSwordEntity.setSize(0.6f);
            summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
            summondSwordEntity.setDamage(attack);
            summondSwordEntity.setStartDelay(random.nextInt(60));
            slashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
        }
    }


    @Override
    public void defaultConfig() {
        super.defaultConfig();
        attack = 0.05f;
        attackNumber = 150;
        range = 5;
    }

    @VoluntarilyRegister
    public static class VertexSwordRainSA extends SwordRainSA {
        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            Vector3d pos = slashBladeEntityPack.getEntity().getPositionVec().add(0, range / 2, 0);
            Random random = slashBladeEntityPack.getEntity().getRNG();
            Vector3d attackPos = slashBladeEntityPack.getAttackPos();
            for (int i = 0; i < attackNumber; i++) {
                SummondSwordEntity summondSwordEntity = new SummondSwordEntity(summondSwordEntityTypeRegister.getEntityType(), slashBladeEntityPack.getEntity().world, slashBladeEntityPack.getEntity());
                slashBladeEntityPack.getSlashBladePack().getSlashBladeStateSupplement().decorate(summondSwordEntity);
                Vector3d _pos = pos.add(RandomUtil.nextVector3dInCircles(random, range));
                summondSwordEntity.setPosition(_pos.getX(), _pos.getY(), _pos.getZ());
                summondSwordEntity.setSize(0.6f);
                summondSwordEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
                summondSwordEntity.setDamage(attack);
                summondSwordEntity.setStartDelay(random.nextInt(60));
                summondSwordEntity.lookAt(attackPos, false);
                slashBladeEntityPack.getEntity().world.addEntity(summondSwordEntity);
            }
        }
    }

}
