package adamsmods.adamsarsplus.client.entities;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.custom.MahoragaEntity;
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
        return AdamsArsPlus.prefix("textures/entity/ten_shadows/mahoraga" + mahoEntity.sealTexture + "_texture.png");
    }

    @Override
    public void render(MahoragaEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.scale(1.0F, 1.0F, 1.0F);
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.popPose();
    }
}
