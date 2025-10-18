// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
import com.adamsmods.adamsarsplus.entities.custom.MahoragaEntity;
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
public class MahoragaModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart waist;
	private final ModelPart body;
	private final ModelPart upper_body;
	private final ModelPart head;
	private final ModelPart tail_1;
	private final ModelPart tail_2;
	private final ModelPart tail_3;
	private final ModelPart wheel;
	private final ModelPart right_arm;
	private final ModelPart right_arm_lower;
	private final ModelPart sword;
	private final ModelPart left_arm;
	private final ModelPart left_arm_lower;
	private final ModelPart right_leg;
	private final ModelPart right_leg_lower;
	private final ModelPart left_leg;
	private final ModelPart left_leg_lower;

	public MahoragaModel(ModelPart root) {
		this.waist = root.getChild("waist");
		this.body = this.waist.getChild("body");
		this.upper_body = this.body.getChild("upper_body");
		this.head = this.upper_body.getChild("head");
		this.tail_1 = this.head.getChild("tail_1");
		this.tail_2 = this.tail_1.getChild("tail_2");
		this.tail_3 = this.tail_2.getChild("tail_3");
		this.wheel = this.head.getChild("wheel");
		this.right_arm = this.upper_body.getChild("right_arm");
		this.right_arm_lower = this.right_arm.getChild("right_arm_lower");
		this.sword = this.right_arm_lower.getChild("sword");
		this.left_arm = this.upper_body.getChild("left_arm");
		this.left_arm_lower = this.left_arm.getChild("left_arm_lower");
		this.right_leg = this.body.getChild("right_leg");
		this.right_leg_lower = this.right_leg.getChild("right_leg_lower");
		this.left_leg = this.body.getChild("left_leg");
		this.left_leg_lower = this.left_leg.getChild("left_leg_lower");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 33).addBox(-6.0F, -10.75F, -4.0F, 12.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(28, 51).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(40, 33).addBox(-6.0F, -1.75F, -4.0F, 12.0F, 3.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -28.25F, 0.0F));

		PartDefinition upper_body = body.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -10.0F, -5.0F, 18.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.75F, 0.0F));

		PartDefinition head = upper_body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(56, 0).addBox(-4.0F, -5.9952F, -5.5242F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0048F, -2.4758F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(64, 65).addBox(-8.0F, -1.0F, 0.0F, 8.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.9952F, -5.5242F, -0.1238F, -0.3272F, 0.3695F));

		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 76).addBox(0.0F, -1.0F, 0.0F, 8.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.9952F, -5.5242F, -0.1238F, 0.3272F, -0.3695F));

		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(68, 44).addBox(0.0F, -1.0F, 0.0F, 8.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -0.9952F, -5.5242F, -0.0159F, 0.3487F, -0.0464F));

		PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(28, 65).addBox(-8.0F, -1.0F, 0.0F, 8.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -0.9952F, -5.5242F, -0.0159F, -0.3487F, 0.0464F));

		PartDefinition tail_1 = head.addOrReplaceChild("tail_1", CubeListBuilder.create().texOffs(56, 16).addBox(-3.0F, -3.0F, -1.0F, 6.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.9952F, 2.4758F, -0.0873F, 0.0F, 0.0F));

		PartDefinition tail_2 = tail_1.addOrReplaceChild("tail_2", CubeListBuilder.create().texOffs(84, 96).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition tail_3 = tail_2.addOrReplaceChild("tail_3", CubeListBuilder.create().texOffs(24, 97).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.3F, 9.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition wheel = head.addOrReplaceChild("wheel", CubeListBuilder.create().texOffs(0, 20).addBox(-6.0F, -0.5F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(16, 72).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(48, 20).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(48, 24).addBox(-1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(48, 28).addBox(-8.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(28, 60).addBox(6.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(68, 55).addBox(4.0F, -1.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(68, 59).addBox(-6.0F, -1.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(0, 72).addBox(-6.0F, -1.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(8, 72).addBox(4.0F, -1.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, -11.9952F, 2.4758F));

		PartDefinition right_arm = upper_body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(84, 76).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(-12.0F, -8.0F, 0.0F));

		PartDefinition right_arm_lower = right_arm.addOrReplaceChild("right_arm_lower", CubeListBuilder.create().texOffs(0, 87).addBox(-3.0F, 0.0F, -6.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 3.0F));

		PartDefinition sword = right_arm_lower.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(48, 97).addBox(-45.5F, -15.5F, -2.5F, 1.0F, 15.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(42.5F, 24.5F, -3.0F, 0.0F, 0.0F, 0.2443F));

		PartDefinition left_arm = upper_body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(88, 0).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(12.0F, -8.0F, 0.0F));

		PartDefinition left_arm_lower = left_arm.addOrReplaceChild("left_arm_lower", CubeListBuilder.create().texOffs(88, 20).addBox(-3.0F, 0.0F, -6.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 3.0F));

		PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(40, 44).addBox(-3.0F, -0.5F, -4.0F, 6.0F, 13.0F, 8.0F, new CubeDeformation(1.0F)), PartPose.offset(-4.0F, 2.75F, 0.0F));

		PartDefinition right_leg_lower = right_leg.addOrReplaceChild("right_leg_lower", CubeListBuilder.create().texOffs(36, 76).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.75F, 0.0F));

		PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 51).addBox(-3.0F, -0.5F, -4.0F, 6.0F, 13.0F, 8.0F, new CubeDeformation(1.0F)), PartPose.offset(4.0F, 2.75F, 0.0F));

		PartDefinition left_leg_lower = left_leg.addOrReplaceChild("left_leg_lower", CubeListBuilder.create().texOffs(60, 76).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.75F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationsDefinition.MAHO_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((MahoragaEntity) entity).idleAnimationState, ModAnimationsDefinition.MAHO_IDLE, ageInTicks, 1f);
		this.animate(((MahoragaEntity) entity).attackAAnimationState, ModAnimationsDefinition.MAHO_ATTACKA, ageInTicks, 1f);
		this.animate(((MahoragaEntity) entity).attackBAAAnimationState, ModAnimationsDefinition.MAHO_ATTACKBAA, ageInTicks, 1f);
		this.animate(((MahoragaEntity) entity).attackBABAnimationState, ModAnimationsDefinition.MAHO_ATTACKBAB, ageInTicks, 1f);
		this.animate(((MahoragaEntity) entity).attackBBAAnimationState, ModAnimationsDefinition.MAHO_ATTACKBBA, ageInTicks, 0.5f);
		this.animate(((MahoragaEntity) entity).attackCAnimationState, ModAnimationsDefinition.MAHO_ATTACKC, ageInTicks, 1f);
		this.animate(((MahoragaEntity) entity).attackRoarAnimationState, ModAnimationsDefinition.MAHO_ROAR, ageInTicks, 1f);
		this.animate(((MahoragaEntity) entity).wheelAnimationState, ModAnimationsDefinition.MAHO_WHEEL, ageInTicks, 1f);
		this.animate(((MahoragaEntity) entity).sealedAnimationState, ModAnimationsDefinition.MAHO_SEALED, ageInTicks, 1f);
		this.animate(((MahoragaEntity) entity).unsealedAnimationState, ModAnimationsDefinition.MAHO_UNSEALED, ageInTicks, 1f);
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