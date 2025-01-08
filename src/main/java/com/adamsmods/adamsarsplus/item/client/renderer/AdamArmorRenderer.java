package com.adamsmods.adamsarsplus.item.client.renderer;

import com.adamsmods.adamsarsplus.item.armor.MageMagicArmor;
import com.hollingsworth.arsnouveau.client.renderer.tile.GenericModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.Optional;

public class AdamArmorRenderer extends GeoArmorRenderer<MageMagicArmor> {
    public AdamArmorRenderer(GeoModel<MageMagicArmor> modelProvider) {
        super(modelProvider);
    }

    public void renderRecursively(PoseStack poseStack, MageMagicArmor animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getName().equalsIgnoreCase("armorRightArmSlim") || bone.getName().equalsIgnoreCase("armorLeftArmSlim")) {
            bone.setHidden(true);
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void actuallyRender(PoseStack poseStack, MageMagicArmor animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Optional<GeoBone> slimRight = model.getBone("armorRightArmSlim");
        Optional<GeoBone> slimLeft = model.getBone("armorLeftArmSlim");
        slimRight.ifPresent((geoBone) -> geoBone.setHidden(true));
        slimLeft.ifPresent((geoBone) -> geoBone.setHidden(true));
        model.getBone("armorRightArmSlim").ifPresent((geoBone) -> geoBone.setHidden(true));
        model.getBone("armorLeftArmSlim").ifPresent((geoBone) -> geoBone.setHidden(true));
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public ResourceLocation getTextureLocation(MageMagicArmor instance) {
        GeoModel<MageMagicArmor> var3 = this.model;
        if (var3 instanceof AdamGenericModel<MageMagicArmor> genericModel) {
            String var10003 = genericModel.textPathRoot;
            return new ResourceLocation("adamsarsplus", "textures/" + var10003 + "/" + genericModel.name + "_" + instance.getColor(this.getCurrentStack()) + ".png");
        } else {
            return super.getTextureLocation(instance);
        }
    }
}

