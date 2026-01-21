package adamsmods.adamsarsplus.client.blocks;

import adamsmods.adamsarsplus.common.blocks.AutoTurretTile;
import com.hollingsworth.arsnouveau.client.renderer.item.GenericItemBlockRenderer;
import com.hollingsworth.arsnouveau.client.renderer.tile.ArsGeoBlockRenderer;
import com.hollingsworth.arsnouveau.client.renderer.tile.GenericModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public class AutoTurretRenderer extends ArsGeoBlockRenderer<AutoTurretTile> {
    public static GeoModel model = new GenericModel("basic_spell_turret") {
        @Override
        public void setCustomAnimations(GeoAnimatable animatable, long instanceId, AnimationState animationState) {
            super.setCustomAnimations(animatable, instanceId, animationState);
            Optional<GeoBone> master = this.getBone("spell_turret");
            master.get().setRotX(0);
            master.get().setRotY(0);
            if (animatable instanceof AutoTurretTile tile) {
                master.get().setRotY((tile.getRotationX() + 90.0F) * ((float)Math.PI / 180F));
                master.get().setRotX(tile.getRotationY() * ((float)Math.PI / 180F));
            }

        }
    };

    public AutoTurretRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, model);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, AutoTurretTile tile, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
        super.actuallyRender(poseStack, tile, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
        float rotationX = tile.rotationX;
        float neededRotationX = tile.clientNeededX;
        float rotationY = tile.rotationY;
        float neededRotationY = tile.clientNeededY;
        float step = (0.1f + partialTick);
        if (rotationX != neededRotationX) {
            float diff = neededRotationX - rotationX;
            if (Math.abs(diff) < step) {
                tile.setRotationX(neededRotationX);
            } else {
                tile.setRotationX(rotationX + diff * step);
            }
        }
        if (rotationY != neededRotationY) {
            float diff = neededRotationY - rotationY;
            if (Math.abs(diff) < step) {
                tile.setRotationY(neededRotationY);
            } else {
                tile.setRotationY(rotationY + diff * step);
            }
        }
    }

    @Override
    protected void rotateBlock(Direction facing, PoseStack poseStack) {
    }

    public static GenericItemBlockRenderer getISTER() {
        return new GenericItemBlockRenderer(model);
    }
}
