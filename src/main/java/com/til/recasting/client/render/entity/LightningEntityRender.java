package com.til.recasting.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.til.recasting.common.entity.LightningEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class LightningEntityRender extends EntityRenderer<LightningEntity> {
    public LightningEntityRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(LightningEntity entityIn, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float[] afloat = new float[8];
        float[] afloat1 = new float[8];
        float f = 0.0F;
        float f1 = 0.0F;
        Random random = new Random(entityIn.getBoltVertex());

        for (int i = 7; i >= 0; --i) {
            afloat[i] = f;
            afloat1[i] = f1;
            f += (float) (random.nextInt(11) - 5);
            f1 += (float) (random.nextInt(11) - 5);
        }

        Color color = new Color(entityIn.getColor(), false);
        //Color color = new Color(0, 0, 255);
        int r = color.getRed();
        int b = color.getBlue();
        int g = color.getGreen();


        float scale = entityIn.getSize();
        matrixStackIn.scale(scale, scale, scale);

        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getLightning());
        Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();

        for (int j = 0; j < 4; ++j) {
            Random random1 = new Random(entityIn.getBoltVertex());

            for (int k = 0; k < 3; ++k) {
                int l = 7;
                int i1 = 0;
                if (k > 0) {
                    l = 7 - k;
                }

                if (k > 0) {
                    i1 = l - 2;
                }

                float f2 = afloat[l] - f;
                float f3 = afloat1[l] - f1;

                for (int j1 = l; j1 >= i1; --j1) {
                    float f4 = f2;
                    float f5 = f3;
                    if (k == 0) {
                        f2 += (float) (random1.nextInt(11) - 5);
                        f3 += (float) (random1.nextInt(11) - 5);
                    } else {
                        f2 += (float) (random1.nextInt(31) - 15);
                        f3 += (float) (random1.nextInt(31) - 15);
                    }

                    float f6 = 0.5F;
                    float f7 = 0.45F;
                    float f8 = 0.45F;
                    float f9 = 0.5F;
                    float f10 = 0.1F + (float) j * 0.2F;
                    if (k == 0) {
                        f10 = (float) ((double) f10 * ((double) j1 * 0.1D + 1.0D));
                    }

                    float f11 = 0.1F + (float) j * 0.2F;
                    if (k == 0) {
                        f11 *= (float) (j1 - 1) * 0.1F + 1.0F;
                    }


                    func_229116_a_(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, r, b, g, 76, f10, f11, false, false, true, false);
                    func_229116_a_(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, r, b, g, 76, f10, f11, true, false, true, true);
                    func_229116_a_(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, r, b, g, 76, f10, f11, true, true, false, true);
                    func_229116_a_(matrix4f, ivertexbuilder, f2, f3, j1, f4, f5, r, b, g, 76, f10, f11, false, true, false, false);
                }
            }
        }

    }

    private static void func_229116_a_(Matrix4f matrix4f, IVertexBuilder iVertexBuilder, float p_229116_2_, float p_229116_3_, int p_229116_4_, float p_229116_5_, float p_229116_6_, int r, int b, int g, int a, float p_229116_10_, float p_229116_11_, boolean p_229116_12_, boolean p_229116_13_, boolean p_229116_14_, boolean p_229116_15_) {
        iVertexBuilder.pos(matrix4f, p_229116_2_ + (p_229116_12_ ? p_229116_11_ : -p_229116_11_), (float) (p_229116_4_ * 16), p_229116_3_ + (p_229116_13_ ? p_229116_11_ : -p_229116_11_)).color(r, g, b, a).endVertex();
        iVertexBuilder.pos(matrix4f, p_229116_5_ + (p_229116_12_ ? p_229116_10_ : -p_229116_10_), (float) ((p_229116_4_ + 1) * 16), p_229116_6_ + (p_229116_13_ ? p_229116_10_ : -p_229116_10_)).color(r, g, b, a).endVertex();
        iVertexBuilder.pos(matrix4f, p_229116_5_ + (p_229116_14_ ? p_229116_10_ : -p_229116_10_), (float) ((p_229116_4_ + 1) * 16), p_229116_6_ + (p_229116_15_ ? p_229116_10_ : -p_229116_10_)).color(r, g, b, a).endVertex();
        iVertexBuilder.pos(matrix4f, p_229116_2_ + (p_229116_14_ ? p_229116_11_ : -p_229116_11_), (float) (p_229116_4_ * 16), p_229116_3_ + (p_229116_15_ ? p_229116_11_ : -p_229116_11_)).color(r, g, b, a).endVertex();
    }

    @Override
    public ResourceLocation getEntityTexture(LightningEntity entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }


}
