package com.til.recasting.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.client.register.render_type.LuminousRenderTypeRegister;
import com.til.recasting.common.entity.PlanetEntity;
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
@StaticVoluntarilyAssignment
public class PlanetEntityRender<E extends PlanetEntity> extends EntityRenderer<E> {

    @VoluntarilyAssignment
    protected static LuminousRenderTypeRegister luminousRenderTypeRegister;

    public PlanetEntityRender(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(E entity) {
        return entity.getTexture();
    }


    @Override
    public void render(E entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {
            float time = entity.ticksExisted + partialTicks;
            time = time / 20;
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw)));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(entity.getRoll() + time));

            float scale = entity.getSize() * 4 * 0.0075f;
           /* if (entity.isPreparationExplosion()) {
                scale *= (entity.getExplosionTime() - (entity.ticksExisted + partialTicks)) / entity.getPreparationTime();
            }*/
            matrixStack.scale(scale, scale, scale);

            WavefrontObject model = BladeModelManager.getInstance().getModel(entity.getModel());
            BladeRenderState.setCol(entity.getColor());
            //BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "model", getEntityTexture(entity), matrixStack, bufferIn, packedLightIn);
            BladeRenderState.renderOverrided(ItemStack.EMPTY, model, "model", getEntityTexture(entity), matrixStack, bufferIn, BladeRenderState.MAX_LIGHT, luminousRenderTypeRegister::getRenderType, true);
        }
        /*try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {
            //matrixStack.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) - 90.0F));
            //matrixStack.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)));
            //matrixStack.rotate(Vector3f.XP.rotationDegrees(entity.getRoll()));


            //matrixStack.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw)));
            //matrixStack.rotate(Vector3f.ZP.rotationDegrees(-MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)));
            //matrixStack.rotate(Vector3f.XP.rotationDegrees(entity.getRoll()));
            //matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));


            matrixStack.rotate(Vector3f.YP.rotationDegrees(-MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw)));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(entity.getRoll()));


            float scale = entity.getSize() * 0.0075f;
            matrixStack.scale(scale, scale, scale);

            WavefrontObject model = BladeModelManager.getInstance().getModel(entity.getModel());
            BladeRenderState.setCol(entity.getColor());
            BladeRenderState.renderOverridedLuminous(ItemStack.EMPTY, model, "ss", getEntityTexture(entity), matrixStack, bufferIn, packedLightIn);
        }*/
    }
}
