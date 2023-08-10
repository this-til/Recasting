package com.til.recasting.client.register.render_type;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.til.glowing_fire_glow.client.register.render_type.RenderTypeRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.client.util.RenderStateManage;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;


@OnlyIn(Dist.CLIENT)
@VoluntarilyRegister
public class LuminousRenderTypeRegister extends RenderTypeRegister {


    @Override
    protected RenderType makeRenderType(ResourceLocation resourceLocation) {
        return RenderStateManage.mackLuminous(resourceLocation);
    }
}
