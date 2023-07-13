package com.til.recasting.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.til.recasting.common.entity.SummondSwordEntity;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class SummondSwordEntityRender<T extends SummondSwordEntity> extends EntityRenderer<T> {

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return entity.getTexture();
    }

    public SummondSwordEntityRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {

        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {



          /*  matrixStack.rotate(new Quaternion(
                    MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch),
                    MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw),
                    entity.getRoll(),
                    true
            ));*/
            //GlStateManager.rotatef(lerpDegrees(partialTicks, entity.prevRotationYaw, entity.rotationYaw), 0.0F, 1.0F, 0.0F); //yaw
            //GlStateManager.rotatef(-lerpDegrees(partialTicks, entity.prevRotationPitch, entity.rotationPitch), 1.0F, 0.0F, 0.0F);
            //GlStateManager.rotatef(entity.getRoll(), 0, 0, 1);
            // matrixStack.translate(
            //                    MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch),
            //                    MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw),
            //                    entity.getRoll());

            //matrixStack.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) - 90.0F));
            //matrixStack.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)));
            //matrixStack.rotate(Vector3f.XP.rotationDegrees(entity.getRoll()));

            matrixStack.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) - 90.0F));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(entity.getRoll()));


            float scale = 0.0075f;
            matrixStack.scale(scale, scale, scale);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));

            WavefrontObject model = BladeModelManager.getInstance().getModel(entity.getModel());
            BladeRenderState.setCol(entity.getColor(), false);
            BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "ss", getEntityTexture(entity), matrixStack, bufferIn, packedLightIn);
        }
    }
}