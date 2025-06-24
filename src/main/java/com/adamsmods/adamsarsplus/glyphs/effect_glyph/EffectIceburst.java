package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.DamageUtil;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.items.curios.ShapersFocus;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.setup.registry.DamageTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.ICEBURST_EFFECT;
import static com.hollingsworth.arsnouveau.client.particle.ParticleSparkleData.random;
import static com.hollingsworth.arsnouveau.setup.registry.ModPotions.SHOCKED_EFFECT;

public class EffectIceburst extends AbstractEffect implements IDamageEffect {
    public static EffectIceburst INSTANCE = new EffectIceburst(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effecticeburst"), "Iceburst");

    public EffectIceburst(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world,@NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        double aoe = spellStats.getAoeMultiplier();
        int radius = (int) aoe;
        int MAX_DAMAGE_PROCS = (int) spellStats.getAmpMultiplier() * 2 + 1;

        if(shooter.getEffect((MobEffect) ICEBURST_EFFECT.get()) == null) {
            shooter.addEffect(new MobEffectInstance(ICEBURST_EFFECT.get(), 20, MAX_DAMAGE_PROCS));
        }

        Predicate<Double> Sphere = (distance) -> (distance <= radius + 0.5);

        BlockPos pos = rayTraceResult.getBlockPos();

        for (BlockPos p : BlockPos.withinManhattan(pos, radius, radius, radius)) {
            if (Sphere.test(BlockUtil.distanceFromCenter(p, pos))) {
                this.iceBurst(world, p, spellStats, spellContext, resolver, shooter, MAX_DAMAGE_PROCS);
            }
        }
    }

    @Nullable
    public void iceBurst(Level world, BlockPos p, SpellStats spellStats, SpellContext context, SpellResolver resolver, LivingEntity shooter, int max_procs) {
        BlockState hitState = world.getBlockState(p);

        if (hitState.getBlock() == Blocks.ICE) {
            world.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
            playIceParticle(p,world);

            iceBurstDamage(world, p, spellStats, context, resolver, shooter,1, max_procs);

        } else if (hitState.getBlock() == Blocks.FROSTED_ICE){
            world.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
            playIceParticle(p,world);

            iceBurstDamage(world, p, spellStats, context, resolver, shooter,1.5f, max_procs);

        } else if (hitState.getBlock() == Blocks.PACKED_ICE) {
            world.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
            playIceParticle(p,world);

            iceBurstDamage(world, p, spellStats, context, resolver, shooter,2, max_procs);

        } else if (hitState.getBlock() == Blocks.BLUE_ICE) {
            world.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
            playIceParticle(p,world);

            iceBurstDamage(world, p, spellStats, context, resolver, shooter,2.5f, max_procs);

        }
    }

    public void iceBurstDamage(Level world, BlockPos p, SpellStats spellStats, SpellContext context, SpellResolver resolver, LivingEntity shooter, float bonus, int max_procs){
        double range = 1 + spellStats.getAoeMultiplier();
        float damage = (float) 2 * bonus;

        DamageSource damageType = DamageUtil.source(world, DamageTypesRegistry.COLD_SNAP, shooter);

        for(LivingEntity e : world.getEntitiesOfClass(LivingEntity.class, new AABB(p.getCenter().add(range, range, range), p.getCenter().subtract(range, range, range)))) {
            if (!e.equals(shooter)) {
                attemptDamage(world, shooter, spellStats, context, resolver, e, damageType, damage);

                if(shooter.getEffect((MobEffect) ICEBURST_EFFECT.get())  != null) {
                    MobEffectInstance effectInstance = ((LivingEntity)shooter).getEffect((MobEffect) ICEBURST_EFFECT.get());
                    int amp = effectInstance != null ? effectInstance.getAmplifier() : -1;

                    if (amp != 0) {
                        ((LivingEntity)shooter).removeEffect((MobEffect) ICEBURST_EFFECT.get());
                        ((LivingEntity)shooter).addEffect(new MobEffectInstance((MobEffect) ICEBURST_EFFECT.get(), 20, amp - 1));

                        e.invulnerableTime = 0;
                    }
                }
            }
        }
    }

    public void playIceParticle(BlockPos p, Level world) {

        if (world instanceof ServerLevel level && level.isClientSide) {

            double x = p.getX();
            double y = p.getY();
            double z = p.getZ();

            level.sendParticles(ParticleTypes.SNOWFLAKE, x, y, z, 1, 0, 0, 0, 1);
            level.sendParticles(ParticleTypes.SNOWFLAKE, x, y, z, 1, 0, 0, 0, 1);
            level.sendParticles(ParticleTypes.SNOWFLAKE, x, y, z, 1, -0.5, -0.5, 0, 1);
            level.sendParticles(ParticleTypes.SNOWFLAKE, x, y, z, 1, 0.5, -0.5, 0, 1);
            level.sendParticles(ParticleTypes.SNOWFLAKE, x, y, z, 1, 0.5, 0.5, 0, 1);
            level.sendParticles(ParticleTypes.SNOWFLAKE, x, y, z, 1, -0.5, 0.5, 0, 1);

            level.playLocalSound(x, y, z, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.2F + 0.85F, false);
        }
    }

    @Override
    public int getDefaultManaCost() {
        return 300;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
    }

   @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                //AugmentExtendTime.INSTANCE,
                //AugmentDurationDown.INSTANCE,
                AugmentAOE.INSTANCE,
                AugmentAmplify.INSTANCE,
                AugmentPierce.INSTANCE
        );
    }

    @Override
    public String getBookDescription() {
        return " ";
    }

   @NotNull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.ELEMENTAL_WATER);
    }

    @Override
    public void addDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(),4);
    }
}
