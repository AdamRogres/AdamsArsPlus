package adamsmods.adamsarsplus.client.entities;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.custom.CamEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CamRenderer extends MobRenderer<CamEntity, CamModel<CamEntity>> {
    public CamRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new CamModel<>(pContext.bakeLayer(ModModelLayers.CAM_LAYER)), 0.9f);

    }

    @Override
    public ResourceLocation getTextureLocation(CamEntity camEntity) {
        return AdamsArsPlus.prefix("textures/entity/cam_texture.png");
    }

    @Override
    public void render(CamEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}
