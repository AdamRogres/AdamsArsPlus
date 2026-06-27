package adamsmods.adamsarsplus.client.entities;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.custom.MysteriousMageEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MageRenderer extends MobRenderer<MysteriousMageEntity, MageModel<MysteriousMageEntity>> {
    public MageRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MageModel<>(pContext.bakeLayer(ModModelLayers.MAGE_LAYER)), 0.7f);
    }

    @Override
    public ResourceLocation getTextureLocation(MysteriousMageEntity mageEntity) {
        return AdamsArsPlus.prefix("textures/entity/mage/" + mageEntity.color + ".png");
    }

    @Override
    public void render(MysteriousMageEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }
}
