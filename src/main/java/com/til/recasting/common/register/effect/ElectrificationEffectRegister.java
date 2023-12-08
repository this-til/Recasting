package com.til.recasting.common.register.effect;

import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.effect.EffectRegister;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.awt.*;

@VoluntarilyRegister
public class ElectrificationEffectRegister extends EffectRegister {

    @Override
    protected Effect initEffect() {
        return new EffectMod(EffectType.HARMFUL, new Color(34, 105, 231).getRGB());
    }
}


