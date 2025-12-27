package com.adamsmods.adamsarsplus.entities.client;

import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition2;
import com.adamsmods.adamsarsplus.entities.custom.TerraprismaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TerraprismaModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart waist;
	private final ModelPart arm1;
	private final ModelPart arm2;
	private final ModelPart sword;

	public TerraprismaModel(ModelPart root) {
		this.waist = root.getChild("waist");
		this.arm1 = this.waist.getChild("arm1");
		this.arm2 = this.arm1.getChild("arm2");
		this.sword = this.arm2.getChild("sword");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition arm1 = waist.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(25, 0).addBox(-0.5F, -16.0F, -1.0F, 1.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 12.0F, 0.0F));

		PartDefinition arm2 = arm1.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(25, 0).addBox(-0.5F, -12.0F, -1.0F, 1.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, 0.0F));

		PartDefinition sword = arm2.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(0, 14).addBox(-0.5F, -24.0F, 0.0F, 1.0F, 24.0F, 24.0F, new CubeDeformation(0.01F))
		.texOffs(0, 62).addBox(-0.5F, -24.0F, 23.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 62).addBox(-0.5F, -24.0F, 22.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 62).addBox(-0.5F, -23.0F, 21.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 62).addBox(-0.5F, -23.0F, 20.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 62).addBox(-0.5F, -22.0F, 19.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 62).addBox(-0.5F, -22.0F, 18.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 62).addBox(-0.5F, -21.0F, 17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 62).addBox(-0.5F, -20.0F, 16.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 62).addBox(-0.5F, -19.0F, 15.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 62).addBox(-0.5F, -18.0F, 14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 62).addBox(-0.5F, -17.0F, 13.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 62).addBox(-0.5F, -16.0F, 12.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 62).addBox(-0.5F, -15.0F, 11.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 62).addBox(-0.5F, -14.0F, 10.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 62).addBox(-0.5F, -13.0F, 9.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -12.0F, 8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -11.0F, 7.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -10.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -11.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -12.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -13.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -14.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -14.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -13.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -12.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -11.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -10.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -9.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -8.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -7.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -6.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -5.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -4.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -4.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -2.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -5.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -5.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -4.0F, 7.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -4.0F, 8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -4.0F, 9.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-0.5F, -4.0F, 10.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -4.0F, 11.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -4.0F, 12.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -5.0F, 13.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -6.0F, 13.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -6.0F, 12.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -6.0F, 11.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -6.0F, 10.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -7.0F, 9.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -8.0F, 10.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -9.0F, 11.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 62).addBox(-0.5F, -10.0F, 12.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 62).addBox(-0.5F, -11.0F, 13.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 62).addBox(-0.5F, -12.0F, 14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 62).addBox(-0.5F, -13.0F, 15.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 62).addBox(-0.5F, -14.0F, 16.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 62).addBox(-0.5F, -15.0F, 17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 62).addBox(-0.5F, -16.0F, 18.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 62).addBox(-0.5F, -17.0F, 19.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 62).addBox(-0.5F, -18.0F, 20.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 62).addBox(-0.5F, -19.0F, 21.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 62).addBox(-0.5F, -20.0F, 21.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 62).addBox(-0.5F, -21.0F, 22.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 62).addBox(-0.5F, -22.0F, 22.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 62).addBox(-0.5F, -23.0F, 23.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, -1.0F, -1.7453F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.animateWalk(ModAnimationsDefinition2.SWORD_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.animate(((TerraprismaEntity) entity).idleAnimationState, ModAnimationsDefinition2.SWORD_IDLE, ageInTicks, 1f);
		this.animate(((TerraprismaEntity) entity).attackAAnimationState, ModAnimationsDefinition2.SWORD_ATTACK_A, ageInTicks, 1f);
		this.animate(((TerraprismaEntity) entity).attackBAnimationState, ModAnimationsDefinition2.SWORD_ATTACK_B, ageInTicks, 1f);
		this.animate(((TerraprismaEntity) entity).attackCAnimationState, ModAnimationsDefinition2.SWORD_ATTACK_C, ageInTicks, 1f);

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