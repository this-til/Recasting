package com.til.recasting.common.register.overall_config;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;
import net.minecraft.util.math.vector.Vector3d;

@VoluntarilyRegister
public class SummondSwordOverallConfig extends OverallConfigRegister {

    @ConfigField
    protected float ordinaryAttack;

    @ConfigField
    protected float blisteringAttack;

    @ConfigField
    protected int blisteringAttackNumber;

    @ConfigField
    protected float[] blisteringYOffset;

    @ConfigField
    protected float heavyRainAttack;

    @ConfigField
    protected int heavyRainAttackNumber;

    @ConfigField
    protected float heavyRainYOffset;

    @ConfigField
    protected Vector3d heavyRainOffset;

    @ConfigField
    protected float stormSwordAttack;

    @ConfigField
    protected int stormSwordAttackNumber;

    @ConfigField
    protected float stormSwordDistance;

    @ConfigField
    protected float spiralSwordAttack;

    @ConfigField
    protected int spiralSwordAttackNumber;

    @ConfigField
    protected int spiralSwordPierceNumber;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        ordinaryAttack = 0.2f;

        blisteringAttack = 0.1f;
        blisteringAttackNumber = 8;
        blisteringYOffset = new float[]{
                0.5f, 0.5f,
                0.25f, 0.25f,
                0, 0,
                -0.25f, -0.25f
        };

        heavyRainAttack = 0.12f;
        heavyRainAttackNumber = 16;
        heavyRainYOffset = 10;
        heavyRainOffset = new Vector3d(6, 3, 6);

        stormSwordAttack = 0.1f;
        stormSwordAttackNumber = 8;
        stormSwordDistance = 5;

        spiralSwordAttack = 0.15f;
        spiralSwordAttackNumber = 8;
        spiralSwordPierceNumber = 3;
    }

    public float getOrdinaryAttack() {
        return ordinaryAttack;
    }

    public float getBlisteringAttack() {
        return blisteringAttack;
    }

    public int getBlisteringAttackNumber() {
        return blisteringAttackNumber;
    }

    public float[] getBlisteringYOffset() {
        return blisteringYOffset;
    }

    public float getHeavyRainAttack() {
        return heavyRainAttack;
    }

    public int getHeavyRainAttackNumber() {
        return heavyRainAttackNumber;
    }

    public float getHeavyRainYOffset() {
        return heavyRainYOffset;
    }

    public Vector3d getHeavyRainOffset() {
        return heavyRainOffset;
    }

    public float getStormSwordAttack() {
        return stormSwordAttack;
    }

    public int getStormSwordAttackNumber() {
        return stormSwordAttackNumber;
    }

    public float getStormSwordDistance() {
        return stormSwordDistance;
    }

    public float getSpiralSwordAttack() {
        return spiralSwordAttack;
    }

    public int getSpiralSwordAttackNumber() {
        return spiralSwordAttackNumber;
    }

    public int getSpiralSwordPierceNumber() {
        return spiralSwordPierceNumber;
    }
}
