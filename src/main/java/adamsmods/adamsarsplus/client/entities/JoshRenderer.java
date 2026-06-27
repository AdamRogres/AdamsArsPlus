package adamsmods.adamsarsplus.client.entities;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.custom.JoshEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class JoshRenderer extends MobRenderer<JoshEntity, JoshModel<JoshEntity>> {
    public JoshRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new JoshModel<>(pContext.bakeLayer(ModModelLayers.JOSH_LAYER)), 2f);

    }

    @Override
    public ResourceLocation getTextureLocation(JoshEntity joshEntity) {
        return AdamsArsPlus.prefix("textures/entity/josh_texture.png");
    }

    @Override
    public void render(JoshEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}
