// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
import com.adamsmods.adamsarsplus.entities.custom.CamEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CamModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor

	private final ModelPart waist;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart rightArm;
	private final ModelPart rightItem;
	private final ModelPart leftArm;
	private final ModelPart leftItem;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public CamModel(ModelPart root) {
		this.waist = root.getChild("waist");
		this.body = this.waist.getChild("body");
		this.head = this.body.getChild("head");
		this.hat = this.head.getChild("hat");
		this.rightArm = this.body.getChild("rightArm");
		this.rightItem = this.rightArm.getChild("rightItem");
		this.leftArm = this.body.getChild("leftArm");
		this.leftItem = this.leftArm.getChild("leftItem");
		this.rightLeg = this.body.getChild("rightLeg");
		this.leftLeg = this.body.getChild("leftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(1, 48).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(53, 48).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition rightItem = rightArm.addOrReplaceChild("rightItem", CubeListBuilder.create().texOffs(0, 123).addBox(0.0F, 1.0F, -4.0F, 1.0F, 3.0F, 1.8F, new CubeDeformation(0.2F))
		.texOffs(0, 123).addBox(0.0F, 1.0F, 0.2F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.2F))
		.texOffs(14, 115).addBox(0.5F, 1.0F, -15.0F, 1.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 7.0F, 1.0F));

		PartDefinition bladeC_r1 = rightItem.addOrReplaceChild("bladeC_r1", CubeListBuilder.create().texOffs(46, 117).addBox(-0.5F, -1.9F, -0.8F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 3.0F, -9.0F, 2.7053F, 0.0F, 3.1416F));

		PartDefinition bladeC_r2 = rightItem.addOrReplaceChild("bladeC_r2", CubeListBuilder.create().texOffs(29, 118).addBox(0.5F, -1.4F, -0.8F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -14.0F, -0.6545F, 0.0F, 0.0F));

		PartDefinition bladeB_r1 = rightItem.addOrReplaceChild("bladeB_r1", CubeListBuilder.create().texOffs(15, 121).addBox(0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -6.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(53, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition leftItem = leftArm.addOrReplaceChild("leftItem", CubeListBuilder.create().texOffs(18, 123).addBox(0.0F, 1.0F, 3.0F, 1.0F, 1.0F, -7.0F, new CubeDeformation(0.0F))
		.texOffs(5, 110).addBox(0.0F, 0.0F, -5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(47, 115).addBox(-0.5F, 0.0F, -15.0F, 1.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 1.0F));

		PartDefinition bladeB_r2 = leftItem.addOrReplaceChild("bladeB_r2", CubeListBuilder.create().texOffs(59, 118).addBox(-0.5F, -3.8F, -0.9F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -17.0F, -0.5672F, 0.0F, 0.0F));

		PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationsDefinition.CAM_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((CamEntity) entity).idleAnimationState, ModAnimationsDefinition.CAM_IDLE, ageInTicks, 1f);
		this.animate(((CamEntity) entity).castingAnimationState, ModAnimationsDefinition.CAM_CAST, ageInTicks, 1f);
		this.animate(((CamEntity) entity).attackAAAnimationState, ModAnimationsDefinition.CAM_ATTACKAA, ageInTicks, 1f);
		this.animate(((CamEntity) entity).attackABAnimationState, ModAnimationsDefinition.CAM_ATTACKAB, ageInTicks, 1f);
		this.animate(((CamEntity) entity).attackBAnimationState, ModAnimationsDefinition.CAM_ATTACKB, ageInTicks, 1f);
		this.animate(((CamEntity) entity).castDomainAnimationState, ModAnimationsDefinition.CAM_DOMAIN, ageInTicks, 1f);

	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch 	= Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch	 * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		waist.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return waist;
	}
}