package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition2;
import com.adamsmods.adamsarsplus.entities.custom.CadeEntity;
import com.adamsmods.adamsarsplus.entities.custom.MageKnightEntity;
import com.adamsmods.adamsarsplus.entities.custom.MysteriousMageEntity;
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
public class MageKnightModel<T extends Entity> extends HierarchicalModel<T> {

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

	public MageKnightModel(ModelPart root) {
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
				.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.2F))
				.texOffs(56, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 3.0F, 4.0F, new CubeDeformation(0.4F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 64).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition rightColour_r1 = rightArm.addOrReplaceChild("rightColour_r1", CubeListBuilder.create().texOffs(78, 3).addBox(-0.5F, 0.0F, -2.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.2F))
				.texOffs(40, 32).addBox(-0.5F, 0.0F, -2.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.5F, -2.0F, 0.0F, 3.1416F, 0.0F, -3.0543F));

		PartDefinition rightItem = rightArm.addOrReplaceChild("rightItem", CubeListBuilder.create().texOffs(0, 48).addBox(-0.5F, -3.0F, -30.0833F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(10, 48).addBox(-0.5F, -4.0F, -26.0833F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(20, 48).addBox(-0.5F, -1.0F, -22.0833F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(2, 56).addBox(-1.0F, -2.0F, -18.0833F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 48).addBox(-0.5F, -1.1F, -16.0833F, 1.0F, 2.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 9.0F, 0.0833F));

		PartDefinition cube_r1 = rightItem.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 56).addBox(-0.5F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1.0F, -18.0833F, 1.5708F, 0.0F, 0.0F));

		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition leftColour_r1 = leftArm.addOrReplaceChild("leftColour_r1", CubeListBuilder.create().texOffs(78, 3).addBox(-0.5F, 0.0F, -1.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.2F))
				.texOffs(40, 32).addBox(-0.5F, 0.0F, -1.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-0.5F, -2.0F, -1.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition leftItem = leftArm.addOrReplaceChild("leftItem", CubeListBuilder.create().texOffs(37, 60).addBox(-0.1604F, -0.5F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.1604F, 8.0F, 0.0F));

		PartDefinition gemB_r1 = leftItem.addOrReplaceChild("gemB_r1", CubeListBuilder.create().texOffs(37, 48).addBox(-0.3398F, -0.9258F, -1.9399F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -4.5F, -7.0F, -0.3643F, 0.0859F, -0.3934F));

		PartDefinition gemC_r1 = leftItem.addOrReplaceChild("gemC_r1", CubeListBuilder.create().texOffs(37, 48).addBox(-0.3505F, -1.0875F, -1.8247F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 5.5F, -7.0F, 0.3717F, 0.0843F, 0.2628F));

		PartDefinition gemA_r1 = leftItem.addOrReplaceChild("gemA_r1", CubeListBuilder.create().texOffs(37, 48).addBox(-0.0187F, -1.5F, -1.9118F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 1.0F, -7.0F, 0.0F, 0.0873F, 0.0F));

		PartDefinition gemD_r1 = leftItem.addOrReplaceChild("gemD_r1", CubeListBuilder.create().texOffs(47, 49).addBox(0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8396F, 0.5F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r2 = leftItem.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(44, 64).addBox(-1.0F, -6.0F, -8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(46, 66).addBox(-1.0F, -5.0F, -7.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(48, 66).addBox(-1.0F, -4.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(48, 66).addBox(-1.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(48, 65).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(48, 64).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(37, 48).addBox(-1.0F, -6.0F, -15.0F, 1.0F, 6.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.8396F, 0.5F, 7.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition cube_r3 = leftItem.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(68, 70).addBox(-1.0F, 5.0F, -8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(64, 72).addBox(-1.0F, 4.0F, -7.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(64, 72).addBox(-1.0F, 3.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(64, 72).addBox(-1.0F, 2.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(64, 71).addBox(-1.0F, 1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(64, 70).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(55, 54).addBox(-1.0F, 0.0F, -15.0F, 1.0F, 6.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.8396F, 0.5F, 7.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition cape = body.addOrReplaceChild("cape", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 2.0F));

		PartDefinition cape_r1 = cape.addOrReplaceChild("cape_r1", CubeListBuilder.create().texOffs(56, 0).addBox(-5.0F, 0.0F, -0.5F, 10.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 3.0107F, 0.0F, 3.1416F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);
		this.applyCapeRotation(limbSwing, limbSwingAmount, ageInTicks);

		this.animateWalk(ModAnimationsDefinition2.MAGEK_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((MageKnightEntity) entity).idleAnimationState, ModAnimationsDefinition2.MAGEK_IDLE, ageInTicks, 1f);
		this.animate(((MageKnightEntity) entity).attackAnimationState, ModAnimationsDefinition2.MAGEK_ATTACK, ageInTicks, 1f);
		this.animate(((MageKnightEntity) entity).toBlockAnimationState, ModAnimationsDefinition2.MAGEK_IDLE_TO_BLOCK, ageInTicks, 1f);
		this.animate(((MageKnightEntity) entity).blockAnimationState, ModAnimationsDefinition2.MAGEK_BLOCK, ageInTicks, 1f);
		this.animate(((MageKnightEntity) entity).reflectAnimationState, ModAnimationsDefinition2.MAGEK_REFLECT, ageInTicks, 1f);
		this.animate(((MageKnightEntity) entity).counterAnimationState, ModAnimationsDefinition2.MAGEK_COUNTER, ageInTicks, 1f);
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