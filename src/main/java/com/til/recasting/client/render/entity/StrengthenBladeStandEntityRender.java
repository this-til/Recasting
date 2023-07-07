package com.til.recasting.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.til.recasting.common.entity.StrengthenBladeStandEntity;
import mods.flammpfeil.slashblade.client.renderer.util.MSAutoCloser;
import mods.flammpfeil.slashblade.entity.BladeStandEntity;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author til
 */
@OnlyIn(Dist.CLIENT)
public class StrengthenBladeStandEntityRender<E extends StrengthenBladeStandEntity> extends EntityRenderer<E> {
    protected final net.minecraft.client.renderer.ItemRenderer itemRenderer;

    public StrengthenBladeStandEntityRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public ResourceLocation getEntityTexture(E entity) {
        return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(E entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        doRender(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public void doRender(E entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {

        if (entity.currentTypeStack.isEmpty()) {
            if (entity.currentType == null || entity.currentType == Items.AIR) {
                entity.currentTypeStack = new ItemStack(Items.ITEM_FRAME);
            } else {
                entity.currentTypeStack = new ItemStack(entity.currentType);
            }
            entity.currentTypeStack.setAttachedEntity(entity);
        }


        try (MSAutoCloser msac = MSAutoCloser.pushMatrix(matrixStackIn)) {
            BlockPos blockpos = entity.getHangingPosition();
            Vector3d vec = Vector3d.copyCenteredWithVerticalOffset(blockpos, 0.75).subtract(entity.getPositionVec());
            matrixStackIn.translate(vec.x, vec.y, vec.z);
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(entity.rotationPitch));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entity.rotationYaw));

            try (MSAutoCloser msacB = MSAutoCloser.pushMatrix(matrixStackIn)) {
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(entity.getRotation()));


                matrixStackIn.scale(2, 2, 2);
                Item type = entity.currentType;
                if (type == SBItems.bladestand_1) {
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90f));
                } else if (type == SBItems.bladestand_2) {
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90f));
                } else if (type == SBItems.bladestand_v) {
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90f));
                } else if (type == SBItems.bladestand_s) {
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90f));
                } else if (type == SBItems.bladestand_1w) {
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180f));
                    matrixStackIn.translate(0, 0, -0.15f);
                } else if (type == SBItems.bladestand_2w) {
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180f));
                    matrixStackIn.translate(0, 0, -0.15f);
                }

                //stand render
                matrixStackIn.push();
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90));
                matrixStackIn.scale(0.5f, 0.5f, 0.5f);
                matrixStackIn.translate(0, 0, 0.44);
                this.renderItem(entity, entity.currentTypeStack, matrixStackIn, bufferIn, packedLightIn);
                matrixStackIn.pop();

                if (entity.currentType == SBItems.bladestand_1w || type == SBItems.bladestand_2w) {
                    matrixStackIn.translate(0, 0, -0.19f);
                } else if (entity.currentType == SBItems.bladestand_1) {
                }
                //blade render
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-180f));
                this.renderItem(entity, entity.getDisplayedItem(), matrixStackIn, bufferIn, packedLightIn);

            }
        }

        net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(entity, entity.getDisplayName(), this, matrixStackIn, bufferIn, packedLightIn, partialTicks);
        //net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(entity, entity.getDisplayName().getFormatedText(), this, matrixStackIn, bufferIn, packedLightIn);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.canRenderName(entity))) {
            this.renderName(entity, renderNameplateEvent.getContent(), matrixStackIn, bufferIn, packedLightIn);
        }
    }

    protected void renderItem(BladeStandEntity entity, ItemStack itemstack, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (!itemstack.isEmpty()) {
            IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemstack, entity.world, (LivingEntity) null);
            this.itemRenderer.renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED, false, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, ibakedmodel);
        }
    }

    @Override
    protected boolean canRenderName(E entity) {
        if (Minecraft.isGuiEnabled() && !entity.getDisplayedItem().isEmpty() && entity.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == entity) {
            double d0 = this.renderManager.squareDistanceTo(entity);
            float f = entity.isDiscrete() ? 32.0F : 64.0F;
            return d0 < (double) (f * f);
        } else {
            return false;
        }
    }

    @Override
    protected void renderName(E entityIn, ITextComponent displayNameIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.renderName(entityIn, entityIn.getDisplayedItem().getDisplayName(), matrixStackIn, bufferIn, packedLightIn);
    }

}
