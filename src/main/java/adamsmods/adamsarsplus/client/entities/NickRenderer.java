package adamsmods.adamsarsplus.client.entities;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.custom.NickEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NickRenderer extends MobRenderer<NickEntity, NickModel<NickEntity>> {
    public NickRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new NickModel<>(pContext.bakeLayer(ModModelLayers.NICK_LAYER)), 0.9f);
    }

    @Override
    public ResourceLocation getTextureLocation(NickEntity nickEntity) {
        return AdamsArsPlus.prefix("textures/entity/nick_texture.png");
    }

    @Override
    public void render(NickEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}
