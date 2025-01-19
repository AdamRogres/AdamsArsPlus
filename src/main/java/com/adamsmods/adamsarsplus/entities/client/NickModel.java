// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
import com.adamsmods.adamsarsplus.entities.custom.CadeEntity;
import com.adamsmods.adamsarsplus.entities.custom.NickEntity;
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
public class NickModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "nickmodel"), "main");
	private final ModelPart waist;
	private final ModelPart body;
	private final ModelPart bodylayer;
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart rightArm;
	private final ModelPart rightArmLayer;
	private final ModelPart rightItem;
	private final ModelPart leftArm;
	private final ModelPart leftArmLayer;
	private final ModelPart leftItem;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public NickModel(ModelPart root) {
		this.waist = root.getChild("waist");
		this.body = this.waist.getChild("body");
		this.bodylayer = this.body.getChild("bodylayer");
		this.head = this.body.getChild("head");
		this.hat = this.head.getChild("hat");
		this.rightArm = this.body.getChild("rightArm");
		this.rightArmLayer = this.rightArm.getChild("rightArmLayer");
		this.rightItem = this.rightArm.getChild("rightItem");
		this.leftArm = this.body.getChild("leftArm");
		this.leftArmLayer = this.leftArm.getChild("leftArmLayer");
		this.leftItem = this.leftArm.getChild("leftItem");
		this.rightLeg = this.body.getChild("rightLeg");
		this.leftLeg = this.body.getChild("leftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition bodylayer = body.addOrReplaceChild("bodylayer", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition rightArmLayer = rightArm.addOrReplaceChild("rightArmLayer", CubeListBuilder.create().texOffs(40, 32).addBox(-8.0F, -24.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(5.0F, 22.0F, 0.0F));

		PartDefinition rightItem = rightArm.addOrReplaceChild("rightItem", CubeListBuilder.create().texOffs(0, 114).addBox(0.0F, 1.0F, -8.0F, 1.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(5, 124).addBox(-0.5F, 0.5F, 3.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 114).addBox(0.0F, -4.0F, -9.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 124).addBox(0.0F, -3.0F, -10.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 124).addBox(0.0F, 5.0F, -10.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 124).addBox(-4.0F, 1.0F, -10.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 124).addBox(4.0F, 1.0F, -10.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 102).addBox(-0.5F, 0.0F, -32.0F, 1.0F, 3.0F, 23.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 7.0F, 1.0F));

		PartDefinition guardB_r1 = rightItem.addOrReplaceChild("guardB_r1", CubeListBuilder.create().texOffs(20, 114).addBox(0.0F, -11.0F, -1.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 1.0F, -8.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition leftArmLayer = leftArm.addOrReplaceChild("leftArmLayer", CubeListBuilder.create(), PartPose.offset(-4.0F, 22.0F, 0.0F));

		PartDefinition leftArmLayer_r1 = leftArmLayer.addOrReplaceChild("leftArmLayer_r1", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -12.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.0F, -12.0F, -1.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition leftItem = leftArm.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offset(1.0F, 7.0F, 1.0F));

		PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationsDefinition.NICK_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((NickEntity) entity).idleAnimationState, ModAnimationsDefinition.NICK_IDLE, ageInTicks, 1f);
		this.animate(((NickEntity) entity).attackAAnimationState, ModAnimationsDefinition.NICK_ATTACKA, ageInTicks, 1f);
		this.animate(((NickEntity) entity).attackBAAnimationState, ModAnimationsDefinition.NICK_ATTACKBA, ageInTicks, 1f);
		this.animate(((NickEntity) entity).attackBBAnimationState, ModAnimationsDefinition.NICK_ATTACKBB, ageInTicks, 1f);
		this.animate(((NickEntity) entity).attackCAnimationState, ModAnimationsDefinition.NICK_ATTACKC, ageInTicks, 1f);
		this.animate(((NickEntity) entity).castDomainAnimationState, ModAnimationsDefinition.NICK_CAST, ageInTicks, 1f);
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