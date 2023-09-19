package com.til.recasting.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.til.recasting.common.entity.JudgementCutEntity;
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
import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class JudgementCutEntityRender<E extends JudgementCutEntity> extends EntityRenderer<E> {

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(E entity) {
        return entity.getTexture();
    }

    public JudgementCutEntityRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(E entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {

        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStackIn)) {

            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) - 90.0F));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)));


            WavefrontObject model = BladeModelManager.getInstance().getModel(entity.getModel());

            int lifetime = entity.getMaxLifeTime();

            double deathTime = lifetime;
            //double baseAlpha = Math.sin(Math.PI * 0.5 * (Math.min(deathTime, Math.max(0, (lifetime - (entity.ticksExisted) - partialTicks))) / deathTime));
            double baseAlpha = (Math.min(deathTime, Math.max(0, (lifetime - (entity.ticksExisted) - partialTicks))) / deathTime);
            baseAlpha = -Math.pow(baseAlpha - 1, 4.0) + 1.0;

            int seed = entity.getSeed();

            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(seed));


            float scale = entity.getSize() * 0.01f;
            matrixStackIn.scale(scale, scale, scale);

            int color = entity.getColor() & 0xFFFFFF;
            Color col = new Color(color);
            float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
            int baseColor = Color.HSBtoRGB(0.5f + hsb[0], hsb[1], 0.2f/*hsb[2]*/) & 0xFFFFFF;

            try (MSAutoCloser msacB = MSAutoCloser.pushMatrix(matrixStackIn)) {
                for (int l = 0; l < 5; l++) {
                    matrixStackIn.scale(0.95f, 0.95f, 0.95f);

                    BladeRenderState.setCol(baseColor | ((0xFF & (int) (0x66 * baseAlpha)) << 24));
                    BladeRenderState.renderOverridedReverseLuminous(ItemStack.EMPTY, model, "base", this.getEntityTexture(entity), matrixStackIn, bufferIn, packedLightIn);
                }
            }


            int loop = 3;
            for (int l = 0; l < loop; l++) {
                try (MSAutoCloser msacB = MSAutoCloser.pushMatrix(matrixStackIn)) {
                    float cycleTicks = 15;
                    float wave = (entity.ticksExisted + (cycleTicks / (float) loop * l) + partialTicks) % cycleTicks;
                    float waveScale = 1 + 0.03f * wave;
                    matrixStackIn.scale(waveScale, waveScale, waveScale);

                    BladeRenderState.setCol(baseColor | ((int) (0x88 * ((cycleTicks - wave) / cycleTicks) * baseAlpha) << 24));
                    BladeRenderState.renderOverridedReverseLuminous(ItemStack.EMPTY, model, "base", this.getEntityTexture(entity), matrixStackIn, bufferIn, packedLightIn);
                }
            }

            int windCount = 5;
            for (int l = 0; l < windCount; l++) {
                try (MSAutoCloser msacB = MSAutoCloser.pushMatrix(matrixStackIn)) {

                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees((360.0f / windCount) * l));
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(30.0f));

                    double rotWind = 360.0 / 20.0;

                    double offsetBase = 7;

                    double offset = l * offsetBase;

                    double motionLen = offsetBase * (windCount - 1);

                    double ticks = entity.ticksExisted + partialTicks + seed;
                    double offsetTicks = ticks + offset;
                    double progress = (offsetTicks % motionLen) / motionLen;

                    double rad = (Math.PI) * 2.0;
                    rad *= progress;

                    float windScale = (float) (0.4 + progress);
                    matrixStackIn.scale(windScale, windScale, windScale);

                    matrixStackIn.rotate(Vector3f.ZP.rotationDegrees((float) (rotWind * offsetTicks)));

                    Color cc = new Color(col.getRed(), col.getGreen(), col.getBlue(), 0xff & (int) (Math.min(0, 0xFF * Math.sin(rad) * baseAlpha)));
                    BladeRenderState.setCol(cc);
                    BladeRenderState.renderOverridedColorWrite(ItemStack.EMPTY, model, "wind", this.getEntityTexture(entity), matrixStackIn, bufferIn, BladeRenderState.MAX_LIGHT);
                }
            }
        }
    }
}
