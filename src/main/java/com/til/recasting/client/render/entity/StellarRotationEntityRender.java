package com.til.recasting.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.til.recasting.common.entity.StellarRotationEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class StellarRotationEntityRender<E extends StellarRotationEntity> extends JudgementCutEntityRender<E> {

    private static final float field_229057_l_ = (float) (Math.sqrt(3.0D) / 2.0D);

    public StellarRotationEntityRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(E entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

        Color color = new Color(entity.getColor());

        float f5 = (entity.ticksExisted + partialTicks) / (entity.getMaxLifeTime() - 5);
        float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
        Random random = new Random(432L);
        IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(RenderType.getLightning());
        matrixStackIn.push();
        float size = entity.getSize() / 10;
        matrixStackIn.scale(size, size, size);

        for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + f5 * 90.0F));
            float f3 = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
            float f4 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
            Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
            int j = (int) (255.0F * (1.0F - f7));
            func_229061_a_(ivertexbuilder2, matrix4f, j);
            func_229060_a_(ivertexbuilder2, matrix4f, f3, f4, color);
            func_229062_b_(ivertexbuilder2, matrix4f, f3, f4, color);
            func_229061_a_(ivertexbuilder2, matrix4f, j);
            func_229062_b_(ivertexbuilder2, matrix4f, f3, f4, color);
            func_229063_c_(ivertexbuilder2, matrix4f, f3, f4, color);
            func_229061_a_(ivertexbuilder2, matrix4f, j);
            func_229063_c_(ivertexbuilder2, matrix4f, f3, f4, color);
            func_229060_a_(ivertexbuilder2, matrix4f, f3, f4, color);
        }

        matrixStackIn.pop();
    }

    private static void func_229061_a_(IVertexBuilder p_229061_0_, Matrix4f p_229061_1_, int p_229061_2_) {
        p_229061_0_.pos(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
        p_229061_0_.pos(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
    }

    private static void func_229060_a_(IVertexBuilder p_229060_0_, Matrix4f p_229060_1_, float p_229060_2_, float p_229060_3_, Color color) {
        p_229060_0_.pos(p_229060_1_, -field_229057_l_ * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
    }

    private static void func_229062_b_(IVertexBuilder p_229062_0_, Matrix4f p_229062_1_, float p_229062_2_, float p_229062_3_, Color color) {
        p_229062_0_.pos(p_229062_1_, field_229057_l_ * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
    }

    private static void func_229063_c_(IVertexBuilder p_229063_0_, Matrix4f p_229063_1_, float p_229063_2_, float p_229063_3_, Color color) {
        p_229063_0_.pos(p_229063_1_, 0.0F, p_229063_2_, p_229063_3_).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
    }

}
