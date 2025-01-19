// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
import com.adamsmods.adamsarsplus.entities.custom.CamEntity;
import com.adamsmods.adamsarsplus.entities.custom.MattEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MattModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor

	private final ModelPart waist;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart rightArm;
	private final ModelPart rightItem;
	private final ModelPart wingA;
	private final ModelPart bladeA1;
	private final ModelPart bladeA2;
	private final ModelPart bladeA3;
	private final ModelPart wingB;
	private final ModelPart bladeB3;
	private final ModelPart bladeB1;
	private final ModelPart bladeB2;
	private final ModelPart leftArm;
	private final ModelPart leftItem;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public MattModel(ModelPart root) {
		this.waist = root.getChild("waist");
		this.body = this.waist.getChild("body");
		this.head = this.body.getChild("head");
		this.hat = this.head.getChild("hat");
		this.rightArm = this.body.getChild("rightArm");
		this.rightItem = this.rightArm.getChild("rightItem");
		this.wingA = this.rightItem.getChild("wingA");
		this.bladeA1 = this.wingA.getChild("bladeA1");
		this.bladeA2 = this.wingA.getChild("bladeA2");
		this.bladeA3 = this.wingA.getChild("bladeA3");
		this.wingB = this.rightItem.getChild("wingB");
		this.bladeB3 = this.wingB.getChild("bladeB3");
		this.bladeB1 = this.wingB.getChild("bladeB1");
		this.bladeB2 = this.wingB.getChild("bladeB2");
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
		.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(53, 48).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition rightItem = rightArm.addOrReplaceChild("rightItem", CubeListBuilder.create().texOffs(0, 95).addBox(0.0F, 1.0F, -16.0F, 1.0F, 1.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(25, 123).addBox(-0.5F, 0.5F, 14.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(25, 123).addBox(-0.5F, 0.5F, -15.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(19, 123).addBox(-0.5F, 0.5F, 16.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(19, 123).addBox(-0.5F, 0.5F, -13.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(15, 112).addBox(0.0F, 0.0F, -21.0F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(23, 112).addBox(0.0F, 1.0F, -20.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(-1.0F, 7.0F, 1.0F));

		PartDefinition wingA = rightItem.addOrReplaceChild("wingA", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -19.0F));

		PartDefinition bladeA1 = wingA.addOrReplaceChild("bladeA1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bladeA1_r1 = bladeA1.addOrReplaceChild("bladeA1_r1", CubeListBuilder.create().texOffs(35, 112).addBox(0.5F, -12.5F, -1.0F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition bladeA2 = wingA.addOrReplaceChild("bladeA2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bladeA2_r1 = bladeA2.addOrReplaceChild("bladeA2_r1", CubeListBuilder.create().texOffs(35, 112).addBox(0.5F, -10.5F, -1.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition bladeA3 = wingA.addOrReplaceChild("bladeA3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition bladeA3_r1 = bladeA3.addOrReplaceChild("bladeA3_r1", CubeListBuilder.create().texOffs(35, 112).addBox(0.5F, -8.5F, -1.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition wingB = rightItem.addOrReplaceChild("wingB", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -19.0F));

		PartDefinition bladeB3 = wingB.addOrReplaceChild("bladeB3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition bladeB3_r1 = bladeB3.addOrReplaceChild("bladeB3_r1", CubeListBuilder.create().texOffs(35, 112).addBox(0.5F, -8.5F, -1.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.0543F, 0.0F, 0.0F));

		PartDefinition bladeB1 = wingB.addOrReplaceChild("bladeB1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bladeB1_r1 = bladeB1.addOrReplaceChild("bladeB1_r1", CubeListBuilder.create().texOffs(35, 112).addBox(0.5F, -12.5F, -1.0F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 2.9671F, 0.0F, 0.0F));

		PartDefinition bladeB2 = wingB.addOrReplaceChild("bladeB2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bladeB2_r1 = bladeB2.addOrReplaceChild("bladeB2_r1", CubeListBuilder.create().texOffs(35, 112).addBox(0.5F, -10.5F, -1.0F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.098F, 0.0F, 0.0F));

		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(53, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition leftItem = leftArm.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 1.0F));

		PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationsDefinition.MATT_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((MattEntity) entity).idleAnimationState, ModAnimationsDefinition.MATT_IDLE, ageInTicks, 1f);
		this.animate(((MattEntity) entity).castingAnimationState, ModAnimationsDefinition.MATT_CAST, ageInTicks, 1f);
		this.animate(((MattEntity) entity).castingBAnimationState, ModAnimationsDefinition.MATT_CAST, ageInTicks, 1f);
		this.animate(((MattEntity) entity).attackAAnimationState, ModAnimationsDefinition.MATT_ATTACKA, ageInTicks, 1f);
		this.animate(((MattEntity) entity).attackBAnimationState, ModAnimationsDefinition.MATT_ATTACKB, ageInTicks, 1f);
		this.animate(((MattEntity) entity).blockAnimationState, ModAnimationsDefinition.MATT_BLOCK, ageInTicks, 1f);
		this.animate(((MattEntity) entity).castDomainAnimationState, ModAnimationsDefinition.MATT_DOMAIN, ageInTicks, 1f);

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