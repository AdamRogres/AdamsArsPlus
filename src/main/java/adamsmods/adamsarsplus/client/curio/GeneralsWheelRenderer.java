package adamsmods.adamsarsplus.client.curio;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.client.entities.ModModelLayers;
import adamsmods.adamsarsplus.client.entities.WheelModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

/** Curios invokes this only for equipped, visible slots. */
public final class GeneralsWheelRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = AdamsArsPlus.prefix("textures/entity/curio/generals_wheel.png");
    // Offsets are blocks relative to the head pivot. The model places the wheel above the head.
    public static final double OFFSET_X = 0.0, OFFSET_Y = 0.0, OFFSET_Z = 0.0;
    public static final float SCALE = 1.0F;
    private final WheelModel model = new WheelModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModModelLayers.WHEEL_LAYER));

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext context,
            PoseStack poses, RenderLayerParent<T, M> parent, MultiBufferSource buffers, int light,
            float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(parent.getModel() instanceof HumanoidModel<?> humanoid) || context.entity().isInvisible()
                || adamsmods.adamsarsplus.util.WheelAdaptation.isSuppressed(context.entity())) return;
        poses.pushPose();
        try {
            humanoid.head.translateAndRotate(poses);
            poses.translate(OFFSET_X, OFFSET_Y, OFFSET_Z);
            poses.scale(SCALE, SCALE, SCALE);
            model.setWheelRotation(adamsmods.adamsarsplus.util.WheelAdaptation.rotation(
                    stack, context.entity().level().getGameTime(), partialTicks));
            model.renderToBuffer(poses, buffers.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)),
                    light, OverlayTexture.NO_OVERLAY, -1);
        } finally {
            poses.popPose();
        }
    }
}
