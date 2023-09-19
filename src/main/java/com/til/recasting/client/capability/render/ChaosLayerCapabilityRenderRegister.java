package com.til.recasting.client.capability.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.til.glowing_fire_glow.client.register.capability.render.CapabilityRenderRegister;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyRegister;
import com.til.recasting.client.register.render_type.LuminousRenderTypeRegister;
import com.til.recasting.common.entity.SummondSwordEntity;
import com.til.recasting.common.register.capability.ChaosLayerCapabilityRegister;
import com.til.recasting.common.register.slash_blade.instance.BaGuaBigSlashBladeRegister;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@VoluntarilyRegister
public class ChaosLayerCapabilityRenderRegister extends CapabilityRenderRegister<
        ChaosLayerCapabilityRegister.ChaosLayer,
        ChaosLayerCapabilityRegister> {

    @VoluntarilyAssignment
    protected LuminousRenderTypeRegister luminousRenderTypeRegister;

    @VoluntarilyAssignment
    protected BaGuaBigSlashBladeRegister baGuaBigSlashBladeRegister;

    @Override
    public void render(Entity entity, ChaosLayerCapabilityRegister.ChaosLayer chaosLayer, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        int l = chaosLayer.getLayer(entity.world.getGameTime());
        if (l <= 0) {
            return;
        }
        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStackIn)) {
            //matrixStackIn.translate(0, -entity.getHeight() / 2, 0);
            matrixStackIn.translate(0, 0.04f, 0);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw)));
            float scale = 0.0075f;
            matrixStackIn.scale(scale, scale, scale);
            WavefrontObject model = BladeModelManager.getInstance().getModel(baGuaBigSlashBladeRegister.getSaModel());
            for (int layer = l; layer > 0; layer--) {
                BladeRenderState.setCol(chaosLayer.getColor());
                //BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, String.valueOf(layer), SummondSwordEntity.DEFAULT_TEXTURE_NAME, matrixStackIn, bufferIn, packedLightIn);
                BladeRenderState.renderOverrided(ItemStack.EMPTY, model, String.valueOf(layer), SummondSwordEntity.DEFAULT_TEXTURE_NAME, matrixStackIn, bufferIn, BladeRenderState.MAX_LIGHT, luminousRenderTypeRegister::getRenderType, true);
            }
        }
    }
}
