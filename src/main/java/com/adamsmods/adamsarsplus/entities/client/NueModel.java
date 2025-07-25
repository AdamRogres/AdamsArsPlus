// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
import com.adamsmods.adamsarsplus.entities.custom.NueEntity;
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

public class NueModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart waist;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart lowerbody;
	private final ModelPart rightleg;
	private final ModelPart leftleg;
	private final ModelPart tail;
	private final ModelPart rightwing;
	private final ModelPart rightwing2;
	private final ModelPart leftwing;
	private final ModelPart leftwing2;

	public NueModel(ModelPart root) {
		this.waist = root.getChild("waist");
		this.body = this.waist.getChild("body");
		this.head = this.body.getChild("head");
		this.lowerbody = this.body.getChild("lowerbody");
		this.rightleg = this.lowerbody.getChild("rightleg");
		this.leftleg = this.lowerbody.getChild("leftleg");
		this.tail = this.lowerbody.getChild("tail");
		this.rightwing = this.body.getChild("rightwing");
		this.rightwing2 = this.rightwing.getChild("rightwing2");
		this.leftwing = this.body.getChild("leftwing");
		this.leftwing2 = this.leftwing.getChild("leftwing2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -11.0F, -3.0F, 8.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(30, 31).addBox(-3.0F, -2.5F, -3.0F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-3.0F, -2.5F, -3.0F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -7.5F, -4.0F));

		PartDefinition lowerbody = body.addOrReplaceChild("lowerbody", CubeListBuilder.create().texOffs(0, 14).addBox(-3.0F, -2.5F, 0.0F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -7.5F, 3.5F));

		PartDefinition rightleg = lowerbody.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(44, 41).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(44, 46).addBox(-0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 7.0F));

		PartDefinition leftleg = lowerbody.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(22, 50).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(32, 50).addBox(-0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 7.0F));

		PartDefinition tail = lowerbody.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(30, 22).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 1.0F, 8.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, -2.0F, 6.3579F));

		PartDefinition cube_r1 = tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 44).addBox(-1.5F, -0.5F, -4.0F, 3.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.0F, 3.0F, 0.0F, 0.4363F, 0.0F));

		PartDefinition cube_r2 = tail.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(22, 41).addBox(-1.5F, -0.5F, -4.0F, 3.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, 3.0F, 0.0F, -0.4363F, 0.0F));

		PartDefinition rightwing = body.addOrReplaceChild("rightwing", CubeListBuilder.create(), PartPose.offset(-4.0F, -9.5F, 0.5F));

		PartDefinition cube_r3 = rightwing.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, -0.5F, 0.0F, 8.0F, 1.0F, 6.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(-6.0F, 0.0F, -4.5F, 0.0F, -0.1745F, 0.0F));

		PartDefinition rightwing2 = rightwing.addOrReplaceChild("rightwing2", CubeListBuilder.create().texOffs(0, 26).addBox(-8.0F, -0.5F, -3.5F, 8.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, -1.0F));

		PartDefinition leftwing = body.addOrReplaceChild("leftwing", CubeListBuilder.create(), PartPose.offset(4.0F, -9.5F, 0.5F));

		PartDefinition cube_r4 = leftwing.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(30, 7).addBox(-7.0F, -0.5F, 0.0F, 7.0F, 1.0F, 6.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(6.0F, 0.0F, -4.5F, 0.0F, 0.1745F, 0.0F));

		PartDefinition leftwing2 = leftwing.addOrReplaceChild("leftwing2", CubeListBuilder.create().texOffs(26, 14).addBox(0.0F, -0.5F, -4.5F, 8.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.body.xRot = -1 * Mth.clamp(entity.getXRot(), -25, 25) * ((float)Math.PI / 180F);

		//this.animateWalk(ModAnimationsDefinition.DDOG_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((NueEntity) entity).idleAnimationState, ModAnimationsDefinition.NUE_IDLE, ageInTicks, 1f);
		this.animate(((NueEntity) entity).flyAnimationState, ModAnimationsDefinition.NUE_FLY, ageInTicks, 1f);
		this.animate(((NueEntity) entity).attackAnimationState, ModAnimationsDefinition.NUE_ATTACK, ageInTicks, 1f);
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