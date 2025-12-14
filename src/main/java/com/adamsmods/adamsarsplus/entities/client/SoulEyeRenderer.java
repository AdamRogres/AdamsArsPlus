package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.EyeOfSoulSeeking;
import com.adamsmods.adamsarsplus.entities.FireEntity;
import com.adamsmods.adamsarsplus.entities.MeteorProjectile;
import com.adamsmods.adamsarsplus.item.eyes.EyeOfFlame;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;

@OnlyIn(Dist.CLIENT)
public class SoulEyeRenderer extends EntityRenderer<EyeOfSoulSeeking> {
    public SoulEyeRenderer(EntityRendererProvider.Context pContext) { super(pContext); }

    protected int getBlockLightLevel(EyeOfSoulSeeking pEntity, BlockPos pPos) {
        return 15;
    }

    public void render(EyeOfSoulSeeking pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.scale(2.0F, 2.0F, 2.0F);
        pMatrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        pMatrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose $$6 = pMatrixStack.last();
        Matrix4f $$7 = $$6.pose();
        Matrix3f $$8 = $$6.normal();
        VertexConsumer $$9 = pBuffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(pEntity)));
        vertex($$9, $$7, $$8, pPackedLight, 0.0F, 0, 0, 1);
        vertex($$9, $$7, $$8, pPackedLight, 1.0F, 0, 1, 1);
        vertex($$9, $$7, $$8, pPackedLight, 1.0F, 1, 1, 0);
        vertex($$9, $$7, $$8, pPackedLight, 0.0F, 1, 0, 0);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    private static void vertex(VertexConsumer pConsumer, Matrix4f p_254477_, Matrix3f p_253948_, int pLightmapUV, float pX, int pY, int pU, int pV) {
        pConsumer.vertex(p_254477_, pX - 0.5F, (float)pY - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float)pU, (float)pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pLightmapUV).normal(p_253948_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(EyeOfSoulSeeking Entity) {
        if(Entity.getItem().getItem() == EYE_OF_FLAME.get()){
            return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/item/eye_of_flame.png");
        } else if(Entity.getItem().getItem() == EYE_OF_FROST.get()){
            return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/item/eye_of_frost.png");
        } else if(Entity.getItem().getItem() == EYE_OF_EARTH.get()){
            return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/item/eye_of_earth.png");
        } else if(Entity.getItem().getItem() == EYE_OF_LIGHTNING.get()){
            return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/item/eye_of_lightning.png");
        } else if(Entity.getItem().getItem() == EYE_OF_HOLY.get()){
            return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/item/eye_of_holy.png");
        } else if(Entity.getItem().getItem() == EYE_OF_VOID.get()){
            return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/item/eye_of_void.png");
        } else {
            return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/item/eye_of_ender.png");
        }
    }

}