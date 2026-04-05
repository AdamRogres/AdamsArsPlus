// Save this class in your mod and generate all required imports

/**
 * Made with Blockbench 5.0.3
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class TerraprismaModelAnimation {
	public static final AnimationDefinition SWORD_SWIM = AnimationDefinition.Builder.withLength(0.0F).looping()
		.addAnimation("arm1", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("arm1", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition SWORD_IDLE = AnimationDefinition.Builder.withLength(2.381F).looping()
		.addAnimation("sword", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.1905F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.381F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition SWORD_WALK = AnimationDefinition.Builder.withLength(1.1905F).looping()
		.addAnimation("arm1", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("sword", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5952F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.1905F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition SWORD_ATTACK_A = AnimationDefinition.Builder.withLength(0.5952F)
		.addAnimation("arm1", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.124F, KeyframeAnimations.degreeVec(12.9581F, 36.3421F, -23.8008F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.248F, KeyframeAnimations.degreeVec(58.9329F, 22.2015F, -23.8889F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3472F, KeyframeAnimations.degreeVec(118.9498F, -24.0452F, -79.64F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4216F, KeyframeAnimations.degreeVec(171.6506F, -30.5245F, -106.8898F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.496F, KeyframeAnimations.degreeVec(200.6828F, -23.8569F, -136.9884F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5952F, KeyframeAnimations.degreeVec(180.0F, 0.0F, -180.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("arm2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.124F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3472F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.496F, KeyframeAnimations.degreeVec(-2.0483F, 1.4336F, 34.9744F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5952F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("sword", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.124F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.248F, KeyframeAnimations.degreeVec(78.6071F, -11.8925F, -27.7458F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3472F, KeyframeAnimations.degreeVec(116.54F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4216F, KeyframeAnimations.degreeVec(151.58F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.496F, KeyframeAnimations.degreeVec(225.77F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5952F, KeyframeAnimations.degreeVec(290.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition SWORD_ATTACK_B = AnimationDefinition.Builder.withLength(0.5952F)
		.addAnimation("arm1", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0992F, KeyframeAnimations.degreeVec(10.0F, -5.0F, 12.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.248F, KeyframeAnimations.degreeVec(6.4765F, 3.0703F, 17.0618F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.372F, KeyframeAnimations.degreeVec(32.2994F, 10.9299F, 33.3505F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4464F, KeyframeAnimations.degreeVec(71.2823F, 14.6227F, 40.0457F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5208F, KeyframeAnimations.degreeVec(156.6984F, -20.1764F, 117.7018F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5952F, KeyframeAnimations.degreeVec(180.0F, 0.0F, 180.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("arm2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0992F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.248F, KeyframeAnimations.degreeVec(2.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.372F, KeyframeAnimations.degreeVec(2.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4464F, KeyframeAnimations.degreeVec(2.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5208F, KeyframeAnimations.degreeVec(24.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5952F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("sword", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0992F, KeyframeAnimations.degreeVec(32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.248F, KeyframeAnimations.degreeVec(90.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.372F, KeyframeAnimations.degreeVec(132.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4464F, KeyframeAnimations.degreeVec(172.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5208F, KeyframeAnimations.degreeVec(172.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5952F, KeyframeAnimations.degreeVec(290.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();

	public static final AnimationDefinition SWORD_ATTACK_C = AnimationDefinition.Builder.withLength(0.5952F)
		.addAnimation("arm1", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0992F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 25.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.1736F, KeyframeAnimations.degreeVec(34.7577F, -3.5962F, 47.1313F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2728F, KeyframeAnimations.degreeVec(67.1332F, 11.4114F, 62.3343F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3472F, KeyframeAnimations.degreeVec(105.9903F, 18.0719F, 77.3851F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3968F, KeyframeAnimations.degreeVec(146.0505F, 15.8988F, 104.8544F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4464F, KeyframeAnimations.degreeVec(209.1017F, -14.6531F, 112.19F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5208F, KeyframeAnimations.degreeVec(219.1017F, -14.6531F, 112.19F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5952F, KeyframeAnimations.degreeVec(360.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("arm2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0992F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.1736F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2728F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3472F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3968F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5208F, KeyframeAnimations.degreeVec(29.06F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5952F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("sword", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0992F, KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.1736F, KeyframeAnimations.degreeVec(87.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2728F, KeyframeAnimations.degreeVec(110.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3472F, KeyframeAnimations.degreeVec(120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3968F, KeyframeAnimations.degreeVec(132.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.4464F, KeyframeAnimations.degreeVec(141.39F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5208F, KeyframeAnimations.degreeVec(186.39F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5952F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();
}