// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
import com.adamsmods.adamsarsplus.entities.custom.DivineDogEntity;
import com.adamsmods.adamsarsplus.entities.custom.RDeerEntity;
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

public class RDeerModel<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart waist;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart lowerbody;
	private final ModelPart tail;
	private final ModelPart rightlegupper2;
	private final ModelPart rightleglower2;
	private final ModelPart leftlegupper2;
	private final ModelPart leftleglower2;
	private final ModelPart rightlegupper;
	private final ModelPart rightleglower;
	private final ModelPart leftlegupper;
	private final ModelPart leftleglower;

	public RDeerModel(ModelPart root) {
		this.waist = root.getChild("waist");
		this.body = this.waist.getChild("body");
		this.head = this.body.getChild("head");
		this.lowerbody = this.body.getChild("lowerbody");
		this.tail = this.lowerbody.getChild("tail");
		this.rightlegupper2 = this.lowerbody.getChild("rightlegupper2");
		this.rightleglower2 = this.rightlegupper2.getChild("rightleglower2");
		this.leftlegupper2 = this.lowerbody.getChild("leftlegupper2");
		this.leftleglower2 = this.leftlegupper2.getChild("leftleglower2");
		this.rightlegupper = this.body.getChild("rightlegupper");
		this.rightleglower = this.rightlegupper.getChild("rightleglower");
		this.leftlegupper = this.body.getChild("leftlegupper");
		this.leftleglower = this.leftlegupper.getChild("leftleglower");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 24).addBox(-5.0F, -5.5F, -6.0F, 10.0F, 11.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.5F, 2.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(44, 24).addBox(-3.0F, -2.4589F, -5.1909F, 6.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0411F, -5.8091F));

		PartDefinition antler5_r1 = head.addOrReplaceChild("antler5_r1", CubeListBuilder.create().texOffs(62, 0).mirror().addBox(-0.5F, -3.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.7923F, -7.1118F, -3.0831F, -0.6471F, 0.4665F, -0.6844F));

		PartDefinition antler4_r1 = head.addOrReplaceChild("antler4_r1", CubeListBuilder.create().texOffs(16, 62).mirror().addBox(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-10.5424F, -6.1118F, -2.3331F, -0.3298F, 0.4017F, -0.8631F));

		PartDefinition antler3_r1 = head.addOrReplaceChild("antler3_r1", CubeListBuilder.create().texOffs(12, 62).mirror().addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.2792F, -4.8632F, -4.968F, -0.5574F, 0.1298F, -1.3814F));

		PartDefinition antler2_r1 = head.addOrReplaceChild("antler2_r1", CubeListBuilder.create().texOffs(8, 62).mirror().addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.2792F, -4.8632F, -4.968F, 0.5771F, 0.1298F, -1.3814F));

		PartDefinition antler_r1 = head.addOrReplaceChild("antler_r1", CubeListBuilder.create().texOffs(60, 61).mirror().addBox(0.0F, -10.0F, -1.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -1.4589F, -3.1909F, 0.2618F, 0.0F, -0.8727F));

		PartDefinition antler5_r2 = head.addOrReplaceChild("antler5_r2", CubeListBuilder.create().texOffs(62, 0).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.7923F, -7.1118F, -3.0831F, -0.6471F, -0.4665F, 0.6844F));

		PartDefinition antler4_r2 = head.addOrReplaceChild("antler4_r2", CubeListBuilder.create().texOffs(16, 62).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5424F, -6.1118F, -2.3331F, -0.3298F, -0.4017F, 0.8631F));

		PartDefinition antler3_r2 = head.addOrReplaceChild("antler3_r2", CubeListBuilder.create().texOffs(12, 62).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2792F, -4.8632F, -4.968F, -0.5574F, -0.1298F, 1.3814F));

		PartDefinition antler2_r2 = head.addOrReplaceChild("antler2_r2", CubeListBuilder.create().texOffs(8, 62).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2792F, -4.8632F, -4.968F, 0.5771F, -0.1298F, 1.3814F));

		PartDefinition antler_r2 = head.addOrReplaceChild("antler_r2", CubeListBuilder.create().texOffs(60, 61).addBox(-1.0F, -10.0F, -1.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -1.4589F, -3.1909F, 0.2618F, 0.0F, 0.8727F));

		PartDefinition ear_r1 = head.addOrReplaceChild("ear_r1", CubeListBuilder.create().texOffs(60, 46).addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.4589F, -3.1909F, 0.0F, -0.7854F, 0.0F));

		PartDefinition ear_r2 = head.addOrReplaceChild("ear_r2", CubeListBuilder.create().texOffs(38, 47).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.4589F, -3.1909F, 0.0F, 0.7854F, 0.0F));

		PartDefinition nose_r1 = head.addOrReplaceChild("nose_r1", CubeListBuilder.create().texOffs(32, 47).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.5411F, -7.1909F, 0.5672F, 0.0F, 0.0F));

		PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(46, 15).addBox(-2.5F, -1.5F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 2.0411F, -4.1909F, 0.5672F, 0.0F, 0.0F));

		PartDefinition lowerbody = body.addOrReplaceChild("lowerbody", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -1.5F, -1.5F, 8.0F, 9.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -3.0F, 6.5F));

		PartDefinition tail = lowerbody.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -0.0425F, 13.2207F));

		PartDefinition cube_r1 = tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, 50).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.4575F, 1.2793F, 0.6109F, 0.0F, 0.0F));

		PartDefinition rightlegupper2 = lowerbody.addOrReplaceChild("rightlegupper2", CubeListBuilder.create().texOffs(0, 47).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 3.0F, 9.5F));

		PartDefinition rightleglower2 = rightlegupper2.addOrReplaceChild("rightleglower2", CubeListBuilder.create().texOffs(60, 35).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

		PartDefinition leftlegupper2 = lowerbody.addOrReplaceChild("leftlegupper2", CubeListBuilder.create().texOffs(16, 47).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 3.0F, 9.5F));

		PartDefinition leftleglower2 = leftlegupper2.addOrReplaceChild("leftleglower2", CubeListBuilder.create().texOffs(48, 61).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

		PartDefinition rightlegupper = body.addOrReplaceChild("rightlegupper", CubeListBuilder.create().texOffs(44, 35).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.0F, 0.0F));

		PartDefinition rightleglower = rightlegupper.addOrReplaceChild("rightleglower", CubeListBuilder.create().texOffs(48, 50).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

		PartDefinition leftlegupper = body.addOrReplaceChild("leftlegupper", CubeListBuilder.create().texOffs(46, 0).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 0.0F, 0.0F));

		PartDefinition leftleglower = leftlegupper.addOrReplaceChild("leftleglower", CubeListBuilder.create().texOffs(56, 50).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationsDefinition.RDEER_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((RDeerEntity) entity).idleAnimationState, ModAnimationsDefinition.RDEER_IDLE, ageInTicks, 1f);
		this.animate(((RDeerEntity) entity).attackAnimationState, ModAnimationsDefinition.RDEER_ATTACK, ageInTicks, 1f);
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