package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.MageKnightEntity;
import com.adamsmods.adamsarsplus.entities.custom.MysteriousMageEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MageKnightRenderer extends MobRenderer<MageKnightEntity, MageKnightModel<MageKnightEntity>> {
    public MageKnightRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MageKnightModel<>(pContext.bakeLayer(ModModelLayers.MAGEK_LAYER)), 0.9f);
    }

    @Override
    public ResourceLocation getTextureLocation(MageKnightEntity mageEntity) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/entity/mage_knight/" + mageEntity.color + ".png");
    }

    @Override
    public void render(MageKnightEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}
