package com.til.recasting.client.capability.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.til.glowing_fire_glow.client.register.capability.render.CapabilityRenderRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.capability.StarBlinkSE_LayerCapabilityRegister;
import com.til.recasting.common.register.slash_blade.instance.special.TilSlashBladeRegister;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
@VoluntarilyRegister
public class StarBlinkSE_LayerCapabilityRenderRegister extends CapabilityRenderRegister<
        StarBlinkSE_LayerCapabilityRegister.StarBlinkSE_Layer,
        StarBlinkSE_LayerCapabilityRegister> {


    @VoluntarilyAssignment
    protected TilSlashBladeRegister tilSlashBladeRegister;

    @Override
    public void render(Entity entity, StarBlinkSE_LayerCapabilityRegister.StarBlinkSE_Layer starBlinkSE_layer, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (starBlinkSE_layer.getLayer() <= 0) {
            return;
        }
        String level = String.valueOf(starBlinkSE_layer.getLayer());
        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStackIn)) {
            //matrixStackIn.translate(0, -entity.getHeight() / 2, 0);
            matrixStackIn.translate(0, 0.05f, 0);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw)));
            float scale = 0.0075f;
            matrixStackIn.scale(scale, scale, scale);
            WavefrontObject model = BladeModelManager.getInstance().getModel(tilSlashBladeRegister.getSeModel());
            for (int layer = starBlinkSE_layer.getLayer(); layer > 0; layer--) {
                BladeRenderState.setCol(new Color(210, 118, 246).getRGB());
                BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, String.valueOf(layer), SummondSwordEntity.DEFAULT_TEXTURE_NAME, matrixStackIn, bufferIn, packedLightIn);
            }
        }
    }
}
