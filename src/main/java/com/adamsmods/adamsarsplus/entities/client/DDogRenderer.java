package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.AdamEntity;
import com.adamsmods.adamsarsplus.entities.custom.DivineDogEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;

public class DDogRenderer extends MobRenderer<DivineDogEntity, DDogModel<DivineDogEntity>> {
    public DDogRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new DDogModel<>(pContext.bakeLayer(ModModelLayers.DDOG_LAYER)), 0.7f);

    }

    @Override
    public ResourceLocation getTextureLocation(DivineDogEntity ddogEntity) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/entity/ten_shadows/dd_" + ddogEntity.color + "_texture.png");
    }

    @Override
    public void render(DivineDogEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}