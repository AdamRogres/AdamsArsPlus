package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.MahoragaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MahoragaRenderer extends MobRenderer<MahoragaEntity, MahoragaModel<MahoragaEntity>> {
    public MahoragaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MahoragaModel<>(pContext.bakeLayer(ModModelLayers.MAHO_LAYER)), 0.7f);
    }

    @Override
    public ResourceLocation getTextureLocation(MahoragaEntity mahoEntity) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/entity/ten_shadows/mahoraga" + mahoEntity.sealTexture + "_texture.png");
    }

    @Override
    public void render(MahoragaEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.scale(1.0F, 1.0F, 1.0F);
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.popPose();
    }
}
