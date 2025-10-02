package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.RDeerEntity;
import com.adamsmods.adamsarsplus.entities.custom.RabbitEEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RDeerRenderer extends MobRenderer<RDeerEntity, RDeerModel<RDeerEntity>> {
    public RDeerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new RDeerModel<>(pContext.bakeLayer(ModModelLayers.RDEER_LAYER)), 0.7f);
    }

    @Override
    public ResourceLocation getTextureLocation(RDeerEntity rDeerEntity) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/entity/ten_shadows/rdeer_texture.png");
    }

    @Override
    public void render(RDeerEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.scale(1.5F, 1.5F, 1.5F);
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.popPose();
    }
}
