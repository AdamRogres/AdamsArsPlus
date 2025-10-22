package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.FireEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FireRenderer extends MobRenderer<FireEntity, FireModel<FireEntity>> {
    public FireRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new FireModel<>(pContext.bakeLayer(ModModelLayers.FIRE_LAYER)), 0.2f);
    }

    @Override
    public ResourceLocation getTextureLocation(FireEntity fireEntity) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/entity/fire/fire_texture_" + fireEntity.animation + ".png");
    }

    @Override
    protected int getBlockLightLevel(FireEntity pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public void render(FireEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    protected void scale(FireEntity pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
        pMatrixStack.scale(pLivingEntity.getSize(), pLivingEntity.getSize(), pLivingEntity.getSize());
        //super.scale(pLivingEntity, pMatrixStack, pPartialTickTime);
    }

}
