package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.CadeEntity;
import com.adamsmods.adamsarsplus.entities.custom.MysteriousMageEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class MageRenderer extends MobRenderer<MysteriousMageEntity, MageModel<MysteriousMageEntity>> {
    public MageRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MageModel<>(pContext.bakeLayer(ModModelLayers.MAGE_LAYER)), 0.7f);
    }

    @Override
    public ResourceLocation getTextureLocation(MysteriousMageEntity mageEntity) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/entity/mage/" + mageEntity.color + ".png");
    }

    @Override
    public void render(MysteriousMageEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}
