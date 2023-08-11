package com.til.recasting.common.register.slash_blade.instance.special;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.RandomUtil;
import com.til.glowing_fire_glow.common.util.ResourceLocationUtil;
import com.til.recasting.common.data.UseSlashBladeEntityPack;
import com.til.recasting.common.entity.DriveEntity;
import com.til.recasting.common.entity.PlanetEntity;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.entity_predicate.DefaultEntityPredicateRegister;
import com.til.recasting.common.register.entity_type.PlanetEntityTypeRegister;
import com.til.recasting.common.register.slash_blade.SlashBladeRegister;
import com.til.recasting.common.register.slash_blade.sa.SA_Register;
import com.til.recasting.common.register.util.AttackManager;
import com.til.recasting.common.register.util.HitAssessment;
import com.til.recasting.common.register.util.StringFinal;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.server.ServerWorld;

import java.awt.*;
import java.util.List;

@VoluntarilyRegister
public class HTOD_SlashBladeRegister extends SlashBladeRegister {

    @VoluntarilyAssignment
    protected HTOD_SlashBladeSA htod_slashBladeSA;

    protected ResourceLocation saModel;

    @Override
    protected void init() {
        super.init();
        summondSwordModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SUMMOND_SWORD, getName().getPath(), "model.obj"));
        saModel = new ResourceLocation(getName().getNamespace(), String.join("/", StringFinal.SPECIAL, getName().getPath(), "model.obj"));
    }

    @Override
    protected void defaultItemStackConfig(ItemStack itemStack) {
        super.defaultItemStackConfig(itemStack);
        slashBladePack.getSlashBladeState().setBaseAttackModifier(7f);
        slashBladePack.getSlashBladeState().setColorCode(new Color(246, 67, 67, 255).getRGB());
        slashBladePack.getSlashBladeStateSupplement().setDurable(26);
        slashBladePack.setSA(htod_slashBladeSA);
    }


    @VoluntarilyRegister
    public static class HTOD_SlashBladeSA extends SA_Register {
        @VoluntarilyAssignment
        protected HTOD_SlashBladeRegister htod_slashBladeRegister;

        @VoluntarilyAssignment
        protected PlanetEntityTypeRegister planetEntityTypeRegister;

        @VoluntarilyAssignment
        protected DefaultEntityPredicateRegister defaultEntityPredicateRegister;

        @ConfigField
        protected float attack;

        @ConfigField
        protected float explosionAttack;

        @ConfigField
        protected float size;

        @Override
        public void trigger(UseSlashBladeEntityPack slashBladeEntityPack) {
            PlanetEntity driveEntity = new PlanetEntity(
                    planetEntityTypeRegister.getEntityType(),
                    slashBladeEntityPack.getEntity().world,
                    slashBladeEntityPack.getEntity()
            );
            driveEntity.setModel(htod_slashBladeRegister.saModel);
            //driveEntity.setModel(SummondSwordEntity.DEFAULT_MODEL_NAME);
            driveEntity.setTexture(SummondSwordEntity.DEFAULT_TEXTURE_NAME);
            driveEntity.setDamage(attack);
            driveEntity.setExplosionDamage(explosionAttack);
            driveEntity.setRoll(slashBladeEntityPack.getEntity().getRNG().nextInt(360));
            driveEntity.setSize(size / 4f);
            driveEntity.setColor(slashBladeEntityPack.getSlashBladePack().getSlashBladeState().getColorCode());
            driveEntity.setPosition(
                    slashBladeEntityPack.getEntity().getPosX(),
                    slashBladeEntityPack.getEntity().getPosY() + 45,
                    slashBladeEntityPack.getEntity().getPosZ()
            );
            driveEntity.setSeep(1);
            driveEntity.lookAt(slashBladeEntityPack.getAttackPos(), false);
            slashBladeEntityPack.getEntity().world.addEntity(driveEntity);
        }

        @Override
        public void defaultConfig() {
            super.defaultConfig();
            attack = 1.35f;
            explosionAttack = 9f;
            size = 12;
        }
    }
}
