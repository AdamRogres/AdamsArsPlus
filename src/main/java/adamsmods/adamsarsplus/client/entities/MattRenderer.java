package adamsmods.adamsarsplus.client.entities;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.custom.MattEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MattRenderer extends MobRenderer<MattEntity, MattModel<MattEntity>> {
    public MattRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MattModel<>(pContext.bakeLayer(ModModelLayers.MATT_LAYER)), 0.9f);
    }

    @Override
    public ResourceLocation getTextureLocation(MattEntity mattEntity) {
        return AdamsArsPlus.prefix("textures/entity/matt_texture.png");
    }

    @Override
    public void render(MattEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}
