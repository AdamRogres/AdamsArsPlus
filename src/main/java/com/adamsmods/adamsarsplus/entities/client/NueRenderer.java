package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.NueEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NueRenderer extends MobRenderer<NueEntity, NueModel<NueEntity>> {
    public NueRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new NueModel<>(pContext.bakeLayer(ModModelLayers.NUE_LAYER)), 0.7f);

    }

    @Override
    public ResourceLocation getTextureLocation(NueEntity nueEntity) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/entity/ten_shadows/nue_texture.png");
    }

    @Override
    public void render(NueEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}