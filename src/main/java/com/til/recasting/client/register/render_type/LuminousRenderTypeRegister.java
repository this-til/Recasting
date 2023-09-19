package com.til.recasting.client.register.render_type;


import com.til.glowing_fire_glow.client.register.render_type.RenderTypeRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.client.util.RenderStateManage;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
@VoluntarilyRegister
public class LuminousRenderTypeRegister extends RenderTypeRegister {


    @Override
    protected RenderType makeRenderType(ResourceLocation resourceLocation) {
        return RenderStateManage.mackLuminous(resourceLocation);
    }
}
