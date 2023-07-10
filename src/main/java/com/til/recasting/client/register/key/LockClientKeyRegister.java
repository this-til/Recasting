package com.til.recasting.client.register.key;

import com.til.glowing_fire_glow.client.register.key.KeyClientRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.key.LockKeyRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@VoluntarilyRegister
public class LockClientKeyRegister extends KeyClientRegister<LockKeyRegister> {
    @Override
    protected int initInputId() {
        return GLFW.GLFW_KEY_LEFT_SHIFT;
    }

    @Override
    protected void pressed() {

    }

    @Override
    protected void release() {

    }
}
