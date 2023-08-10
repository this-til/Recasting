package com.til.recasting.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderStateManage extends BladeRenderState {

    @Deprecated
    public RenderStateManage(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
        super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
    }

    protected static final RenderState.TransparencyState LIGHTNING_ADDITIVE_TRANSPARENCY = new RenderState.TransparencyState("lightning_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public static RenderType mackLuminous(ResourceLocation texture) {
        RenderType.State state = RenderType.State.getBuilder()
                .texture(new RenderState.TextureState(texture, true, false))
                .transparency(LIGHTNING_ADDITIVE_TRANSPARENCY)
                .diffuseLighting(RenderState.DIFFUSE_LIGHTING_DISABLED)
                .alpha(DEFAULT_ALPHA)
                .lightmap(RenderState.LIGHTMAP_ENABLED)
                .shadeModel(SHADE_ENABLED)
                .writeMask(COLOR_WRITE)
                .build(false);
        return RenderType.makeType("luminous", WavefrontObject.POSITION_TEX_LMAP_COL_NORMAL, GL11.GL_TRIANGLES, 256, true, false, state);
    }


}
