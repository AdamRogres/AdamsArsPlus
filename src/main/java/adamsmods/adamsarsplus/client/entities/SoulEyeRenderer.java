package adamsmods.adamsarsplus.client.entities;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.EyeOfSoulSeeking;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static adamsmods.adamsarsplus.registry.ModItems.*;

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
        VertexConsumer $$9 = pBuffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(pEntity)));
        vertex($$9, $$6, pPackedLight, 0.0F, 0, 0, 1);
        vertex($$9, $$6, pPackedLight, 1.0F, 0, 1, 1);
        vertex($$9, $$6, pPackedLight, 1.0F, 1, 1, 0);
        vertex($$9, $$6, pPackedLight, 0.0F, 1, 0, 0);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int packedLight, float x, int y, int u, int v) {
        consumer.addVertex(pose, x - 0.5F, (float)y - 0.25F, 0.0F).setColor(-1).setUv((float)u, (float)v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    public ResourceLocation getTextureLocation(EyeOfSoulSeeking Entity) {
        if(Entity.getItem().getItem() == EYE_OF_FLAME.get()){
            return AdamsArsPlus.prefix("textures/item/eye_of_flame.png");
        } else if(Entity.getItem().getItem() == EYE_OF_FROST.get()){
            return AdamsArsPlus.prefix("textures/item/eye_of_frost.png");
        } else if(Entity.getItem().getItem() == EYE_OF_EARTH.get()){
            return AdamsArsPlus.prefix("textures/item/eye_of_earth.png");
        } else if(Entity.getItem().getItem() == EYE_OF_LIGHTNING.get()){
            return AdamsArsPlus.prefix("textures/item/eye_of_lightning.png");
        } else if(Entity.getItem().getItem() == EYE_OF_HOLY.get()){
            return AdamsArsPlus.prefix("textures/item/eye_of_holy.png");
        } else if(Entity.getItem().getItem() == EYE_OF_VOID.get()){
            return AdamsArsPlus.prefix("textures/item/eye_of_void.png");
        } else {
            return AdamsArsPlus.prefix("textures/item/eye_of_ender.png");
        }
    }

}