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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

public class EffectLimitless extends AbstractEffect implements IPotionEffect {
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

    public void makeLSphere(BlockPos center, Level world, @NotNull Entity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver){
        int radius = (int) (1 + spellStats.getAoeMultiplier());
        double amp = spellStats.hasBuff(AugmentDampen.INSTANCE) ? (spellStats.getAmpMultiplier() / 3) : (spellStats.getAmpMultiplier() / -3);

        Predicate<Double> Sphere = (distance) -> (distance <= radius + 0.5);

            //Target non-living entities like arrows and spell projectiles
            for (Entity entity : world.getEntities(shooter, new AABB(center).inflate(radius, radius, radius))) {
                if (Sphere.test(BlockUtil.distanceFromCenter(entity.blockPosition(), center))) {
                    entity.setDeltaMovement(entity.getDeltaMovement().scale(amp));
                    entity.hurtMarked = true;
                }
            }
            //Target living entities
            for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, new AABB(center).inflate(radius, radius, radius))) {
                if (Sphere.test(BlockUtil.distanceFromCenter(entity.blockPosition(), center))) {
                    entity.setDeltaMovement(entity.getDeltaMovement().scale(amp));
                    entity.hurtMarked = true;
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
                AugmentDampen.INSTANCE
        );
    }

    @Override
    public String getBookDescription() {
        return "Sets the velocity of all entities in an area to 0 (includes spell projectiles and other entities not normally target-able). Applying AOE will increase the radius this effect is applied. Augmenting with Amplify will multiply the velocity by: -X/3. Augmenting with Dampen will multiply the velocity by: -1.";
    }

   @NotNull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.MANIPULATION);
    }

    @Override
    public int getBaseDuration() {
        return POTION_TIME == null ? 2 : POTION_TIME.get();
    }

    @Override
    public int getExtendTimeDuration() {
        return EXTEND_TIME == null ? 1 : EXTEND_TIME.get();
    }
}
