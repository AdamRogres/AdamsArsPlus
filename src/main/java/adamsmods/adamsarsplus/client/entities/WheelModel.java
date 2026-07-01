package adamsmods.adamsarsplus.client.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;

public class WheelModel extends Model {

	private final ModelPart waist;
	private final ModelPart wheel;

	public WheelModel(ModelPart root) {
		super(RenderType::entityCutout);

		this.waist = root.getChild("waist");
		this.wheel = this.waist.getChild("wheel");
	}

	public static MeshDefinition createMesh() {
		var meshdefinition = new MeshDefinition();
		var partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		partdefinition.addOrReplaceChild("wheel", CubeListBuilder.create().texOffs(0, 20).addBox(-6.0F, -0.5F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(16, 72).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(48, 20).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(48, 24).addBox(-1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(48, 28).addBox(-8.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(28, 60).addBox(6.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(68, 55).addBox(4.0F, -1.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(68, 59).addBox(-6.0F, -1.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(0, 72).addBox(-6.0F, -1.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F))
		.texOffs(8, 72).addBox(4.0F, -1.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, -38.0F, 0.0F));

		return meshdefinition;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, int i2) {
		waist.render(poseStack, vertexConsumer, i, i1, i2);
	}
}