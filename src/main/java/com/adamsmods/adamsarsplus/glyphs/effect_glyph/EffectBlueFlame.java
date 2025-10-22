package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.FireEntity;
import com.adamsmods.adamsarsplus.entities.MeteorProjectile;
import com.adamsmods.adamsarsplus.entities.custom.RDeerEntity;
import com.adamsmods.api.IPropagator;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.TileCaster;
import com.hollingsworth.arsnouveau.common.block.BasicSpellTurret;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EffectBlueFlame extends AbstractEffect {
    public EffectBlueFlame(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public static final EffectBlueFlame INSTANCE = new EffectBlueFlame(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectblueflame"), "Cremation");

    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
    }

    public void summonFlame(Level world, HitResult rayTraceResult, @Nullable LivingEntity shooter, SpellContext spellContext, SpellStats stats){
        FireEntity flame = new FireEntity(world, (float) stats.getAoeMultiplier(), (int) stats.getAmpMultiplier(), (double) stats.getAccMultiplier(), (int)stats.getDurationMultiplier());
        flame.setPos(rayTraceResult.getLocation());
        flame.level().addFreshEntity(flame);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.summonFlame(world, rayTraceResult, shooter, spellContext,spellStats);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.summonFlame(world, rayTraceResult, shooter, spellContext,spellStats);
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentAOE.INSTANCE,
                AugmentAccelerate.INSTANCE,
                AugmentAmplify.INSTANCE,
                AugmentExtendTime.INSTANCE
        );
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public int getDefaultManaCost() {
        return 200;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.ELEMENTAL_FIRE);
    }
}
