package adamsmods.adamsarsplus.client.entities;

import adamsmods.adamsarsplus.common.entity.custom.JoshEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class JoshModel<T extends Entity> extends HierarchicalModel<T> {
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
	private final ModelPart bipedBody;
	private final ModelPart bipedRightArm;
	private final ModelPart bipedRightArmLower;
	private final ModelPart weapon;
	private final ModelPart bipedLeftArm;
	private final ModelPart bipedLeftArmLower;
	private final ModelPart shield;
	private final ModelPart armorLeftLeg;
	private final ModelPart armorLeftLegLower;
	private final ModelPart armorRightLeg;
	private final ModelPart armorRightLegLower;

	public JoshModel(ModelPart root) {
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
		this.bipedBody = this.body.getChild("bipedBody");
		this.bipedRightArm = this.bipedBody.getChild("bipedRightArm");
		this.bipedRightArmLower = this.bipedRightArm.getChild("bipedRightArmLower");
		this.weapon = this.bipedRightArmLower.getChild("weapon");
		this.bipedLeftArm = this.bipedBody.getChild("bipedLeftArm");
		this.bipedLeftArmLower = this.bipedLeftArm.getChild("bipedLeftArmLower");
		this.shield = this.bipedLeftArmLower.getChild("shield");
		this.armorLeftLeg = this.bipedBody.getChild("armorLeftLeg");
		this.armorLeftLegLower = this.armorLeftLeg.getChild("armorLeftLegLower");
		this.armorRightLeg = this.bipedBody.getChild("armorRightLeg");
		this.armorRightLegLower = this.armorRightLeg.getChild("armorRightLegLower");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 32).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -40.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(48, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition rightItem = rightArm.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offset(-1.0F, 7.0F, 1.0F));

		PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, -0.829F, 0.0F, 0.0F));

		PartDefinition leftSleeve_r1 = leftArm.addOrReplaceChild("leftSleeve_r1", CubeListBuilder.create().texOffs(48, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition leftItem = leftArm.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 1.0F));

		PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9F, 10.0F, 0.0F, -0.9938F, 0.4114F, 0.2546F));

		PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.9F, 10.0F, 0.0F, -1.0472F, -0.422F, -0.2359F));

		PartDefinition bipedBody = body.addOrReplaceChild("bipedBody", CubeListBuilder.create().texOffs(16, 80).addBox(-4.0F, 4.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(4.0F))
		.texOffs(16, 96).addBox(-4.0F, -4.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(4.55F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition bipedRightArm = bipedBody.addOrReplaceChild("bipedRightArm", CubeListBuilder.create().texOffs(40, 80).addBox(-5.7608F, 1.4306F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(2.0F))
		.texOffs(0, 96).addBox(-5.7608F, 1.4306F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(2.1F)), PartPose.offsetAndRotation(-9.2392F, -3.4306F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r1 = bipedRightArm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(72, 86).addBox(-4.0F, -5.0F, -0.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(2.0F)), PartPose.offsetAndRotation(-5.7608F, 0.4306F, 2.0F, 3.1416F, 0.0F, 2.9671F));

		PartDefinition bipedRightArmLower = bipedRightArm.addOrReplaceChild("bipedRightArmLower", CubeListBuilder.create().texOffs(40, 80).addBox(-2.0F, 2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(2.0F))
		.texOffs(0, 96).addBox(-2.0F, 2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(2.1F)), PartPose.offset(-3.7608F, 15.4306F, 0.0F));

		PartDefinition weapon = bipedRightArmLower.addOrReplaceChild("weapon", CubeListBuilder.create().texOffs(0, 163).addBox(-10.0F, 17.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 179).addBox(-10.0F, 11.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 162).addBox(-10.5F, -8.0F, -2.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(9, 183).addBox(-16.5F, -7.0F, -3.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(41, 157).addBox(-16.5F, -6.0F, -0.5F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 165).addBox(-16.0F, -6.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 179).addBox(-10.0F, 5.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 173).addBox(-9.5F, 14.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 173).addBox(-9.5F, 8.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 173).addBox(-9.5F, 2.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(16, 177).addBox(-6.0F, -6.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 8.0F, 0.0F));

		PartDefinition bipedLeftArm = bipedBody.addOrReplaceChild("bipedLeftArm", CubeListBuilder.create(), PartPose.offsetAndRotation(8.7608F, -3.4306F, 0.0F, 3.1416F, 0.0F, 3.0543F));

		PartDefinition cube_r2 = bipedLeftArm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(72, 86).addBox(-4.0F, -5.0F, -0.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(2.0F)), PartPose.offsetAndRotation(-5.7608F, 0.4306F, 2.0F, 3.1416F, 0.0F, 2.9671F));

		PartDefinition arm_r1 = bipedLeftArm.addOrReplaceChild("arm_r1", CubeListBuilder.create().texOffs(0, 112).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(2.1F))
		.texOffs(56, 80).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(2.0F)), PartPose.offsetAndRotation(-3.7608F, 7.4306F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition bipedLeftArmLower = bipedLeftArm.addOrReplaceChild("bipedLeftArmLower", CubeListBuilder.create(), PartPose.offset(-3.7608F, 15.4306F, 0.0F));

		PartDefinition arm_r2 = bipedLeftArmLower.addOrReplaceChild("arm_r2", CubeListBuilder.create().texOffs(0, 112).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(2.1F))
		.texOffs(56, 80).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(2.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition shield = bipedLeftArmLower.addOrReplaceChild("shield", CubeListBuilder.create().texOffs(0, 155).addBox(-7.0F, -1.0F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 155).addBox(-8.0F, -11.0F, -19.0F, 1.0F, 24.0F, 38.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition armorLeftLeg = bipedBody.addOrReplaceChild("armorLeftLeg", CubeListBuilder.create().texOffs(16, 112).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(2.0F)), PartPose.offset(4.0F, 20.0F, 0.0F));

		PartDefinition armorLeftLegLower = armorLeftLeg.addOrReplaceChild("armorLeftLegLower", CubeListBuilder.create().texOffs(16, 112).addBox(-2.0F, 2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(2.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));

		PartDefinition armorRightLeg = bipedBody.addOrReplaceChild("armorRightLeg", CubeListBuilder.create().texOffs(16, 128).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(2.0F)), PartPose.offset(-4.0F, 20.0F, 0.0F));

		PartDefinition armorRightLegLower = armorRightLeg.addOrReplaceChild("armorRightLegLower", CubeListBuilder.create().texOffs(16, 128).addBox(-2.0F, 2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(2.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationsDefinition2.JOSH_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((JoshEntity) entity).idleAnimationState, ModAnimationsDefinition2.JOSH_IDLE, ageInTicks, 1f);
		this.animate(((JoshEntity) entity).castingAnimationState, ModAnimationsDefinition2.JOSH_CAST, ageInTicks, 1f);
		this.animate(((JoshEntity) entity).notCastingAnimationState, ModAnimationsDefinition2.JOSH_NO_CAST, ageInTicks, 1f);
		this.animate(((JoshEntity) entity).attackAnimationState, ModAnimationsDefinition2.JOSH_ATTACK, ageInTicks, 1f);
		this.animate(((JoshEntity) entity).blockAnimationState, ModAnimationsDefinition2.JOSH_BLOCK, ageInTicks, 1f);
		this.animate(((JoshEntity) entity).castDomainAnimationState, ModAnimationsDefinition2.JOSH_DOMAIN, ageInTicks, 1f);
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch 	= Mth.clamp(pHeadPitch, -90.0F, 90.0F);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch	 * ((float)Math.PI / 180F);
	}

	/*
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		waist.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	 */

	@Override
	public ModelPart root() {
		return waist;
	}
}