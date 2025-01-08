package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.RyanEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class RyanRenderer extends MobRenderer<RyanEntity, ryan_slayer_of_dragons<RyanEntity>> {
    public RyanRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ryan_slayer_of_dragons<>(pContext.bakeLayer(ModModelLayers.RYAN_LAYER)), 0.7f);

    }

    @Override
    public ResourceLocation getTextureLocation(RyanEntity ryanEntity) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/entity/ryan_slayer_of_dragons.png");
    }

    @Override
    public void render(RyanEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}
