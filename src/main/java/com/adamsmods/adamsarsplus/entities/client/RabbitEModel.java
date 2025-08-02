// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
import com.adamsmods.adamsarsplus.entities.custom.DivineDogEntity;
import com.adamsmods.adamsarsplus.entities.custom.RabbitEEntity;
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

public class RabbitEModel<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart waist;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart front_paw_r;
	private final ModelPart front_paw_l;
	private final ModelPart back_leg_r;
	private final ModelPart back_paw_r;
	private final ModelPart back_leg_l;
	private final ModelPart back_paw_l;
	private final ModelPart tail;

	public RabbitEModel(ModelPart root) {
		this.waist = root.getChild("waist");
		this.body = this.waist.getChild("body");
		this.head = this.body.getChild("head");
		this.front_paw_r = this.body.getChild("front_paw_r");
		this.front_paw_l = this.body.getChild("front_paw_l");
		this.back_leg_r = this.body.getChild("back_leg_r");
		this.back_paw_r = this.back_leg_r.getChild("back_paw_r");
		this.back_leg_l = this.body.getChild("back_leg_l");
		this.back_paw_l = this.back_leg_l.getChild("back_paw_l");
		this.tail = this.body.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -4.7707F, 2.752F));

		PartDefinition pattern_r1 = body.addOrReplaceChild("pattern_r1", CubeListBuilder.create().texOffs(0, 26).addBox(-1.5F, -2.7293F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, 2.0F, -3.0F, -0.0436F, 0.0F, 0.0F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.5F, -1.0F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.47F)), PartPose.offsetAndRotation(2.0F, 2.7707F, -2.752F, -0.0436F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 12).addBox(-2.0F, -2.5042F, -3.5834F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 11).addBox(2.0F, -0.5042F, -2.5834F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(16, 12).addBox(-3.0F, -0.5042F, -2.5834F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(14, 12).addBox(1.0F, -1.5042F, -2.5834F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
		.texOffs(14, 12).addBox(-2.0F, -1.5042F, -2.5834F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -2.7251F, -3.1686F));

		PartDefinition ear2_r1 = head.addOrReplaceChild("ear2_r1", CubeListBuilder.create().texOffs(24, 14).addBox(-1.0F, -5.0F, -0.5F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2.5042F, -2.0834F, -0.2618F, -0.5236F, -0.0873F));

		PartDefinition ear_r1 = head.addOrReplaceChild("ear_r1", CubeListBuilder.create().texOffs(34, 14).addBox(0.0F, -5.0F, -0.5F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -2.5042F, -2.0834F, -0.2618F, 0.5236F, 0.0873F));

		PartDefinition nose2_r1 = head.addOrReplaceChild("nose2_r1", CubeListBuilder.create().texOffs(7, 22).addBox(-0.5F, -0.9F, -1.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.4958F, -3.5834F, 0.0873F, 0.0F, 0.0F));

		PartDefinition nose_r1 = head.addOrReplaceChild("nose_r1", CubeListBuilder.create().texOffs(16, 12).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.4958F, -3.5834F, 0.0873F, 0.0F, 0.0F));

		PartDefinition front_paw_r = body.addOrReplaceChild("front_paw_r", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 2.7707F, -1.752F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r1 = front_paw_r.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 10).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0834F, 0.091F, 1.5708F, 0.0F, -1.5708F));

		PartDefinition front_paw_l = body.addOrReplaceChild("front_paw_l", CubeListBuilder.create(), PartPose.offset(2.0F, 2.7707F, -1.752F));

		PartDefinition cube_r2 = front_paw_l.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(37, 10).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 1.4835F, 0.0F, 0.0F));

		PartDefinition back_leg_r = body.addOrReplaceChild("back_leg_r", CubeListBuilder.create(), PartPose.offset(-3.0F, 1.0441F, 3.4094F));

		PartDefinition cube_r3 = back_leg_r.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(16, 12).addBox(-1.0F, -2.5293F, -0.852F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.9559F, -0.4094F, -0.2618F, 0.0F, 0.0F));

		PartDefinition back_paw_r = back_leg_r.addOrReplaceChild("back_paw_r", CubeListBuilder.create().texOffs(13, 21).addBox(-1.0F, 0.0F, -2.748F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.7266F, 0.3386F));

		PartDefinition back_leg_l = body.addOrReplaceChild("back_leg_l", CubeListBuilder.create(), PartPose.offset(3.0F, 1.0441F, 3.4094F));

		PartDefinition cube_r4 = back_leg_l.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(16, 12).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition back_paw_l = back_leg_l.addOrReplaceChild("back_paw_l", CubeListBuilder.create().texOffs(13, 17).addBox(-1.0F, 0.0F, -2.748F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.7266F, 0.3386F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -3.0F, 3.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r5 = tail.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(19, 3).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.0872F, 0.9962F, 0.3927F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animate(((RabbitEEntity) entity).idleAnimationState, ModAnimationsDefinition.RABE_IDLE, ageInTicks, 1f);
		this.animate(((RabbitEEntity) entity).hopAnimationState, ModAnimationsDefinition.RABE_HOP, ageInTicks, 1f);
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch 	= Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch	 * -1 * ((float)Math.PI / 180F);
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