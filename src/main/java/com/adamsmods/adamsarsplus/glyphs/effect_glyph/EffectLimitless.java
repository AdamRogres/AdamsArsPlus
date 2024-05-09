package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.ArsNouveauRegistry;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.entity.EnchantedFallingBlock;
import com.hollingsworth.arsnouveau.common.items.curios.ShapersFocus;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static java.lang.Math.abs;

public class EffectLimitless extends AbstractEffect {
    public static EffectLimitless INSTANCE = new EffectLimitless(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectlimitless"), "Limitless");

    public EffectLimitless(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world,@NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (rayTraceResult.getEntity() instanceof LivingEntity living) {

                makeLSphere(rayTraceResult.getEntity().blockPosition(), world, shooter, spellStats, spellContext, resolver);

        }
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world,@NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {

            makeLSphere(rayTraceResult.getBlockPos(), world, shooter, spellStats, spellContext, resolver);

    }

    public double absDifference(int radius, double ratio){
        double value = 0;

        if(ratio >= 0){
            value = radius - ratio;
        }
        else{
            value = (radius + ratio) * -1;
        }
        return value;
    }

    public void knockback(Entity target, double xPosition, double yPosition, double zPosition, float strength, int radius) {
        double x = xPosition - target.getBlockX();
        double y = yPosition - target.getBlockY();
        double z = zPosition - target.getBlockZ();

        knockback(target, strength, x, y, z, radius);
    }

    public void knockback(Entity entity, double strength, double xRatio, double yRatio, double zRatio, int radius) {

            entity.hasImpulse = true;
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0));
            if(strength <=  0) {
                entity.setDeltaMovement(strength * -0.5 * xRatio, strength * -0.5 * yRatio, strength * -0.5 * zRatio);
            }
            else{
                entity.setDeltaMovement(strength * -0.5 * absDifference(radius, xRatio), strength * -0.5 * absDifference(radius, yRatio), strength * -0.5 * absDifference(radius, zRatio));
            }
    }

    public void makeLSphere(BlockPos center, Level world, @NotNull Entity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver){
        int radius = (int) (1 + spellStats.getAoeMultiplier());
        double amp = spellStats.hasBuff(AugmentDampen.INSTANCE) ? (spellStats.getAmpMultiplier() / 3) : (spellStats.getAmpMultiplier() / -3);

        Predicate<Double> Sphere = (distance) -> (distance <= radius + 0.5);

            //Target non-living entities like arrows and spell projectiles
            for (Entity entity : world.getEntities(shooter, new AABB(center).inflate(radius, radius, radius))) {
                if (Sphere.test(BlockUtil.distanceFromCenter(entity.blockPosition(), center))) {
                    if (spellStats.hasBuff(AugmentSensitive.INSTANCE) || !(spellContext.getUnwrappedCaster().equals(entity))) {
                        if(spellStats.hasBuff(AugmentPierce.INSTANCE)){
                            knockback(entity, center.getX(), center.getY(), center.getZ(), (float) amp, radius);
                        }
                        else {
                            entity.setDeltaMovement(entity.getDeltaMovement().scale(amp));
                            entity.hurtMarked = true;
                        }
                    }
                }
            }
            //Target living entities
            for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, new AABB(center).inflate(radius, radius, radius))) {
                if (Sphere.test(BlockUtil.distanceFromCenter(entity.blockPosition(), center))) {
                    if (spellStats.hasBuff(AugmentSensitive.INSTANCE) || !(spellContext.getUnwrappedCaster().equals(entity))) {
                        if(spellStats.hasBuff(AugmentPierce.INSTANCE)){
                            knockback(entity, center.getX(), center.getY(), center.getZ(), (float) amp, radius);
                        }
                        else {
                            entity.setDeltaMovement(entity.getDeltaMovement().scale(amp));
                            entity.hurtMarked = true;
                        }
                    }
                }
            }
    }

    @Override
    public int getDefaultManaCost() {
        return 90;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addPotionConfig(builder, 2);
        addExtendTimeConfig(builder, 1);
    }

   @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                //AugmentExtendTime.INSTANCE,
                //AugmentDurationDown.INSTANCE,
                AugmentAOE.INSTANCE,
                AugmentAmplify.INSTANCE,
                AugmentDampen.INSTANCE,
                AugmentSensitive.INSTANCE,
                AugmentPierce.INSTANCE
        );
    }

    @Override
    public String getBookDescription() {
        return "Sets the velocity of entities in an area to 0 (includes spell projectiles and other entities not normally target-able). Applying AOE will increase the radius this effect is applied. Augmenting with Amplify will multiply the velocity by: -X/3. Augmenting with Dampen will multiply the velocity by: -1. Augmenting with Sensitive allows the caster to be affected by limitless.";
    }

   @NotNull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.MANIPULATION);
    }

}
