package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.AdamEntity;
import com.adamsmods.adamsarsplus.entities.custom.CadeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AdamRenderer extends MobRenderer<AdamEntity, AdamModel<AdamEntity>> {
    public AdamRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new AdamModel<>(pContext.bakeLayer(ModModelLayers.ADAM_LAYER)), 0.7f);

    }

    @Override
    public ResourceLocation getTextureLocation(AdamEntity adamEntity) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/entity/adam_texture.png");
    }

    @Override
    public void render(AdamEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}
