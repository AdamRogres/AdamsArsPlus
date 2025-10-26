package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.entities.BladeProjectile;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class BladeRenderer<T extends Entity & ItemSupplier> extends EntityRenderer<T> {
    private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBright;

    public BladeRenderer(EntityRendererProvider.Context pContext, float pScale, boolean pFullBright) {
        super(pContext);
        this.itemRenderer = pContext.getItemRenderer();
        this.scale = pScale;
        this.fullBright = pFullBright;
    }

    public BladeRenderer(EntityRendererProvider.Context pContext) {
        this(pContext, 1.0F, false);
    }

    protected int getBlockLightLevel(T pEntity, BlockPos pPos) {
        return this.fullBright ? 15 : super.getBlockLightLevel(pEntity, pPos);
    }

    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(pEntity) < (double)12.25F)) {
            pMatrixStack.pushPose();
            pMatrixStack.scale(this.scale, this.scale, this.scale);

            float x =       (float)pEntity.getDeltaMovement().x;
            float y =       (float)pEntity.getDeltaMovement().y;
            float z =       (float)pEntity.getDeltaMovement().z;

            if(x != 0 || y != 0 || z != 0){
                float normalH = (float)Math.sqrt(x * x + z * z);
                float normal =  (float)Math.sqrt(normalH * normalH + y * y);

                float xRot = (float)(Math.atan2(x, z) * 180.0 / Math.PI) + 180.0f;
                float yRot = (float)(Math.asin(y / normal) * 180.0 / Math.PI) - 90.0f;

                pMatrixStack.mulPose(Axis.YP.rotationDegrees(xRot));
                pMatrixStack.mulPose(Axis.XP.rotationDegrees(yRot));
            }


            if(((ItemSupplier)pEntity).getItem() != ItemStack.EMPTY){
                this.itemRenderer.renderStatic(((ItemSupplier)pEntity).getItem(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, pPackedLight, OverlayTexture.NO_OVERLAY, pMatrixStack, pBuffer, pEntity.level(), pEntity.getId());
            }
            pMatrixStack.popPose();
            super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        }
    }

    public ResourceLocation getTextureLocation(Entity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}