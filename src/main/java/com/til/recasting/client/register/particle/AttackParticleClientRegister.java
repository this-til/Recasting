package com.til.recasting.client.register.particle;

import com.til.glowing_fire_glow.client.particle.DefaultParticle;
import com.til.glowing_fire_glow.client.register.particle_register.ParticleClientRegister;
import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.particle_register.data.ParticleContext;
import com.til.glowing_fire_glow.common.util.GlowingFireGlowColor;
import com.til.glowing_fire_glow.common.util.Pos;
import com.til.glowing_fire_glow.common.util.RandomUtil;
import com.til.glowing_fire_glow.common.util.math.NumberPack;
import com.til.recasting.common.register.particle.AttackParticleRegister;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

@VoluntarilyRegister
@OnlyIn(Dist.CLIENT)
public class AttackParticleClientRegister extends ParticleClientRegister<AttackParticleRegister> {

    @ConfigField
    protected float size;

    @ConfigField
    protected int life;

    @VoluntarilyAssignment
    protected AttackSmallParticleClientRegister attackSmallParticleClientRegister;

    @Override
    public void run(ParticleContext particleContext, ClientWorld world, Pos start, @Nullable Pos end, GlowingFireGlowColor color, double density, @Nullable ResourceLocation resourceLocation) {
        particleContext.addParticle(new DefaultParticle(world)
                .setPos(start.x, start.y, start.z)
                .setSize(size)
                .setSizeChangeType(DefaultParticle.SizeChangeType.SQUARE_SIN)
                .setParticleCollide(false)
                .setLifeTime(life)
                .setColor(color)
                .setTextureName(resourceLocation));
        attackSmallParticleClientRegister.run(particleContext, world, start, end, color, density, resourceLocation);
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        size = 4.5f;
        life = 9;
    }
}
