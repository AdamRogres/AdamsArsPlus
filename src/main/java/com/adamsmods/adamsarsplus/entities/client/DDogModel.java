// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
import com.adamsmods.adamsarsplus.entities.custom.DivineDogEntity;
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
public class DDogModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart waist;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart rightarm;
	private final ModelPart rightpaw;
	private final ModelPart leftarm;
	private final ModelPart leftpaw;
	private final ModelPart lowerbody;
	private final ModelPart tail;
	private final ModelPart rightleg;
	private final ModelPart rightfoot;
	private final ModelPart leftleg;
	private final ModelPart leftfoot;

	public DDogModel(ModelPart root) {
		this.waist = root.getChild("waist");
		this.body = this.waist.getChild("body");
		this.head = this.body.getChild("head");
		this.rightarm = this.body.getChild("rightarm");
		this.rightpaw = this.rightarm.getChild("rightpaw");
		this.leftarm = this.body.getChild("leftarm");
		this.leftpaw = this.leftarm.getChild("leftpaw");
		this.lowerbody = this.body.getChild("lowerbody");
		this.tail = this.lowerbody.getChild("tail");
		this.rightleg = this.lowerbody.getChild("rightleg");
		this.rightfoot = this.rightleg.getChild("rightfoot");
		this.leftleg = this.lowerbody.getChild("leftleg");
		this.leftfoot = this.leftleg.getChild("leftfoot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 24.0F, -2.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 17).addBox(-4.0F, -3.75F, 0.75F, 8.0F, 7.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(1.0F, -11.25F, -1.75F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 27).addBox(-3.0F, -4.375F, -0.625F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(34, 14).addBox(1.0F, -6.375F, 0.375F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(32, 37).addBox(-3.0F, -6.375F, 0.375F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 30).addBox(-1.5F, -1.375F, 3.375F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 1).addBox(-1.5F, -4.375F, 2.375F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 1.125F, 6.375F));

		PartDefinition rightarm = body.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(0, 36).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.75F, 5.0F));

		PartDefinition rightpaw = rightarm.addOrReplaceChild("rightpaw", CubeListBuilder.create().texOffs(12, 30).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.5F, 0.0F));

		PartDefinition leftarm = body.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(8, 37).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 2.75F, 5.0F));

		PartDefinition leftpaw = leftarm.addOrReplaceChild("leftpaw", CubeListBuilder.create().texOffs(20, 30).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.5F, 0.0F));

		PartDefinition lowerbody = body.addOrReplaceChild("lowerbody", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.75F, -9.25F, 6.0F, 6.0F, 11.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition tail = lowerbody.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(28, 17).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.25F, -8.75F, 0.48F, 0.0F, 0.0F));

		PartDefinition rightleg = lowerbody.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(34, 0).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.75F, -8.25F));

		PartDefinition rightfoot = rightleg.addOrReplaceChild("rightfoot", CubeListBuilder.create().texOffs(16, 37).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.5F, 0.0F));

		PartDefinition leftleg = lowerbody.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(34, 7).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 2.75F, -8.25F));

		PartDefinition leftfoot = leftleg.addOrReplaceChild("leftfoot", CubeListBuilder.create().texOffs(24, 37).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationsDefinition.DDOG_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((DivineDogEntity) entity).idleAnimationState, ModAnimationsDefinition.DDOG_IDLE, ageInTicks, 1f);
		this.animate(((DivineDogEntity) entity).sprintAnimationState, ModAnimationsDefinition.DDOG_SPRINT, ageInTicks, 1.66f);
		this.animate(((DivineDogEntity) entity).biteAnimationState, ModAnimationsDefinition.DDOG_BITE, ageInTicks, 1f);
		this.animate(((DivineDogEntity) entity).lungeAnimationState, ModAnimationsDefinition.DDOG_LUNGE, ageInTicks, 1.33f);

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