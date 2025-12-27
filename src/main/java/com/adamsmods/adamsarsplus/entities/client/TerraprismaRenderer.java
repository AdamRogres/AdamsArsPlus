package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.FireEntity;
import com.adamsmods.adamsarsplus.entities.custom.TerraprismaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TerraprismaRenderer extends MobRenderer<TerraprismaEntity, TerraprismaModel<TerraprismaEntity>> {
    public TerraprismaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new TerraprismaModel<>(pContext.bakeLayer(ModModelLayers.SWORD_LAYER)), 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(TerraprismaEntity swordEntity) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, "textures/entity/terraprisma/" + swordEntity.getColor() + ".png");
    }


    @Override
    public void render(TerraprismaEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
