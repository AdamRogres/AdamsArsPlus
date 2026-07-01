package adamsmods.adamsarsplus.client.entities;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.custom.RyanEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RyanRenderer extends MobRenderer<RyanEntity, RyanModel<RyanEntity>> {
    public RyanRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new RyanModel<>(pContext.bakeLayer(ModModelLayers.RYAN_LAYER)), 0.7f);
    }

    @Override
    public ResourceLocation getTextureLocation(RyanEntity ryanEntity) {
        return AdamsArsPlus.prefix("textures/entity/ryan_texture.png");
    }

    @Override
    public void render(RyanEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}
