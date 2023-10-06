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
import com.til.recasting.common.register.particle.AttackSmallParticleRegister;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

@VoluntarilyRegister
@OnlyIn(Dist.CLIENT)
public class AttackSmallParticleClientRegister extends ParticleClientRegister<AttackSmallParticleRegister> {

    @ConfigField
    protected int number;

    @ConfigField
    protected NumberPack move;

    @ConfigField
    protected NumberPack life;

    @ConfigField
    protected NumberPack size;

    protected Random random = new Random();

    @Override
    public void run(ParticleContext particleContext, ClientWorld world, Pos start, @Nullable Pos end, GlowingFireGlowColor[] color, double density, @Nullable ResourceLocation resourceLocation) {
        for (int i = 0; i < number * density; i++) {
            Vector3d move = RandomUtil.nextVector3dOnCircles(random, 1).scale(this.move.of(random.nextFloat()));//RandomUtil.nextVector3d(random).scale(this.move);
            particleContext.addParticle(new DefaultParticle(world)
                    .setPos(start.x, start.y, start.z)
                    .setMove(move.x, move.y, move.z)
                    .setLifeTime((int) life.of(random.nextFloat()))
                    .setColor(color.length > 0 ? color[0] : GlowingFireGlowColor.DEFAULT)
                    .setSize((float) size.of(random.nextFloat()))
                    .setSizeChangeType(DefaultParticle.SizeChangeType.SQUARE_SIN)
                    .setParticleCollide(false)
                    .setTextureName(resourceLocation)
            );
        }
    }

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        number = 3;
        move = new NumberPack(0, 0.15f);
        life = new NumberPack(9, 27);
        size = new NumberPack(1, 0.5);
    }
}
