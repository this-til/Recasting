package com.til.recasting.client.register.overall_config;

import com.til.glowing_fire_glow.common.config.ConfigField;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.glowing_fire_glow.common.register.overall_config.OverallConfigRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@VoluntarilyRegister
@OnlyIn(Dist.CLIENT)
public class PreloadOverallConfigRegister extends OverallConfigRegister {

    @ConfigField
    protected boolean isPreload;

    @Override
    public void defaultConfig() {
        super.defaultConfig();
        isPreload = true;
    }

    public boolean isPreload() {
        return isPreload;
    }
}
