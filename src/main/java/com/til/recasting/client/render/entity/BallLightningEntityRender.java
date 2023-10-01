package com.til.recasting.client.render.entity;

import com.til.recasting.common.entity.BallLightningEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class BallLightningEntityRender<E extends BallLightningEntity> extends EntityRenderer<E> {
    @Nullable
    @Override
    public ResourceLocation getEntityTexture(E entity) {
        return entity.getTexture();
    }

    public BallLightningEntityRender(EntityRendererManager renderManager) {
        super(renderManager);
    }
}
