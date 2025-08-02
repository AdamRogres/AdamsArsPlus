package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.RabbitEEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RabbitERenderer extends MobRenderer<RabbitEEntity, RabbitEModel<RabbitEEntity>> {
    public RabbitERenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new RabbitEModel<>(pContext.bakeLayer(ModModelLayers.RABE_LAYER)), 0.7f);

    }

    @Override
    public ResourceLocation getTextureLocation(RabbitEEntity rabEntity) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/entity/ten_shadows/rabbit_escape_" + rabEntity.color + "_texture.png");
    }

    @Override
    public void render(RabbitEEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}
