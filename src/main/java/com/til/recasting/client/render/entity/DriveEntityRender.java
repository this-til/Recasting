package com.til.recasting.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.til.glowing_fire_glow.common.register.StaticVoluntarilyAssignment;
import com.til.glowing_fire_glow.common.register.VoluntarilyAssignment;
import com.til.recasting.client.register.render_type.LuminousRenderTypeRegister;
import com.til.recasting.common.entity.DriveEntity;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject;
import mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;

@OnlyIn(Dist.CLIENT)
@StaticVoluntarilyAssignment
public class DriveEntityRender<E extends DriveEntity> extends SlashEffectEntityRender<E> {


    @VoluntarilyAssignment
    protected static LuminousRenderTypeRegister luminousRenderTypeRegister;

    public DriveEntityRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(E entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        /*if (entity.isSimpleRender()) {
            try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStack)) {
                matrixStack.rotate(Vector3f.YP.rotationDegrees(-MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw)));
                matrixStack.rotate(Vector3f.XP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(entity.getRoll()));

                float scale = entity.getSize() * 0.0075f;
                matrixStack.scale(scale, scale, scale);

                WavefrontObject model = BladeModelManager.getInstance().getModel(entity.getModel());
                BladeRenderState.setCol(entity.getColor());
                BladeRenderState.renderOverrided(ItemStack.EMPTY, model, "model", getEntityTexture(entity), matrixStack, bufferIn, BladeRenderState.MAX_LIGHT, luminousRenderTypeRegister::getRenderType, true);
            }
            return;
        }*/
        super.render(entity, entityYaw, partialTicks, matrixStack, bufferIn, packedLightIn);
    }

    @Override
    protected int renderTime(E e) {
        return (int) (e.getMaxLifeTime() * 0.75f);
    }

    @Override
    protected float ofPartialTicks(float partialTicks) {
        return 0;
    }
}
