package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.CadeEntity;
import com.adamsmods.adamsarsplus.entities.custom.RyanEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CadeRenderer extends MobRenderer<CadeEntity, CadeModel<CadeEntity>> {
    public CadeRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new CadeModel<>(pContext.bakeLayer(ModModelLayers.CADE_LAYER)), 0.7f);

    }

    @Override
    public ResourceLocation getTextureLocation(CadeEntity cadeEntity) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/entity/cade_texture.png");
    }

    @Override
    public void render(CadeEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}
