// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
import com.adamsmods.adamsarsplus.entities.custom.AdamEntity;
import com.adamsmods.adamsarsplus.entities.custom.CadeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class AdamModel<T extends Entity> extends HierarchicalModel<T> {
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
	private final ModelPart cape;

	public AdamModel(ModelPart root) {
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
		this.cape = this.body.getChild("cape");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition rightItem = rightArm.addOrReplaceChild("rightItem", CubeListBuilder.create().texOffs(62, 19).addBox(0.0F, -1.5F, -1.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(65, 17).addBox(0.5F, -1.5F, -18.0F, 1.0F, 2.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 9.0F, -3.0F));

		PartDefinition guardC_r1 = rightItem.addOrReplaceChild("guardC_r1", CubeListBuilder.create().texOffs(73, 21).addBox(-0.5F, -5.5F, -1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.5672F, 0.0F, 0.0F));

		PartDefinition guardB_r1 = rightItem.addOrReplaceChild("guardB_r1", CubeListBuilder.create().texOffs(73, 21).addBox(-0.5F, -5.5F, -1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition guardA_r1 = rightItem.addOrReplaceChild("guardA_r1", CubeListBuilder.create().texOffs(73, 21).addBox(-0.5F, -4.5F, -1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition pommel_r1 = rightItem.addOrReplaceChild("pommel_r1", CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, -2.5F, -1.1F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 1.0F, 8.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition leftItem = leftArm.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 1.0F));

		PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition cape = body.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(65, 1).addBox(-4.0F, 0.0F, 2.0F, 8.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);
		this.applyCapeRotation(limbSwing, limbSwingAmount, ageInTicks);

		this.animateWalk(ModAnimationsDefinition.ADAM_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((AdamEntity) entity).idleAnimationState, ModAnimationsDefinition.ADAM_IDLE, ageInTicks, 1f);
		this.animate(((AdamEntity) entity).attackAAAnimationState, ModAnimationsDefinition.ADAM_ATTACKAA, ageInTicks, 1f);
		this.animate(((AdamEntity) entity).attackABAnimationState, ModAnimationsDefinition.ADAM_ATTACKAB, ageInTicks, 1f);
		this.animate(((AdamEntity) entity).attackBAnimationState, ModAnimationsDefinition.ADAM_ATTACKB, ageInTicks, 1f);
		this.animate(((AdamEntity) entity).blockAnimationState, ModAnimationsDefinition.ADAM_BLOCK, ageInTicks, 1f);
		this.animate(((AdamEntity) entity).castAAnimationState, ModAnimationsDefinition.ADAM_CASTA, ageInTicks, 1f);
		this.animate(((AdamEntity) entity).castBAnimationState, ModAnimationsDefinition.ADAM_CASTB, ageInTicks, 1f);
		this.animate(((AdamEntity) entity).castDomainAnimationState, ModAnimationsDefinition.ADAM_DOMAIN, ageInTicks, 1f);
		this.animate(((AdamEntity) entity).castBarrierAnimationState, ModAnimationsDefinition.ADAM_DOMAIN, ageInTicks, 1f);
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch 	= Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch	 * ((float)Math.PI / 180F);
	}

	private void applyCapeRotation(float limbSwing, float limbSwingAmount, float ageInTicks){

		float pNetCape = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

		pNetCape = Mth.abs(pNetCape);
		pNetCape = Mth.clamp(pNetCape, 0.0F, 80.0F);

		this.cape.xRot = pNetCape;
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