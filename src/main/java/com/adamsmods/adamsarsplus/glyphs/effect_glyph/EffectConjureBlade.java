package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.MeteorProjectile;
import com.adamsmods.api.IPropagator;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.TileCaster;
import com.hollingsworth.arsnouveau.common.block.BasicSpellTurret;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
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

public class EffectConjureBlade extends AbstractEffect implements IPropagator {
    public EffectConjureBlade(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public static final EffectConjureBlade INSTANCE = new EffectConjureBlade(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectconjureblade"), "Conjure Blade");

    public ForgeConfigSpec.IntValue SWORD_TTL;
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        this.SWORD_TTL = builder.comment("Max lifespan of the projectile, in seconds.").defineInRange("max_lifespan", 60, 0, Integer.MAX_VALUE);
    }

    @Override
    public void propagate(Level world, HitResult hitResult, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {

        int numSplits = 1 + stats.getBuffCount(AugmentSplit.INSTANCE);
        int aoe = 4 + 4 * (int) stats.getAoeMultiplier();

        Vec3 pos = hitResult.getLocation();
        List<MeteorProjectile> projectiles = new ArrayList<>();
        for (int i = 0; i < numSplits; i++) {
            projectiles.add(new MeteorProjectile(world, resolver));
        }
        float velocity = Math.max(0.1f, 0.75f + stats.getAccMultiplier() / 2);
        if(stats.getBuffCount(AugmentSensitive.INSTANCE) == 0){
            velocity = 0 + stats.getAccMultiplier() / 2;
        }

        int opposite = -1;
        int counter = 0;

        Vec3 direction = pos.subtract(shooter.position());
        if (resolver.spellContext.getCaster() instanceof TileCaster tc) {
            if (tc.getTile() instanceof RotatingTurretTile rotatingTurretTile) {
                direction = rotatingTurretTile.getShootAngle();
            } else {
                direction = new Vec3(tc.getTile().getBlockState().getValue(BasicSpellTurret.FACING).step());
            }
        }
        for (MeteorProjectile proj : projectiles) {
            proj.setPos(pos.add(0, aoe, 0));
            if (!(shooter instanceof FakePlayer)) {
                proj.shoot(shooter, 90, 0, 0.0F, velocity, 0.8f);
            } else {
                proj.shoot(direction.x, direction.y, direction.z, velocity, 0.8F);
            }
            opposite = opposite * -1;
            counter++;

            if(stats.getBuffCount(AugmentSensitive.INSTANCE) == 0){
                proj.setGravity(true);
            }
            proj.accelerates = stats.getAccMultiplier();

            world.addFreshEntity(proj);
        }

    }

    public void sendPacket(Level world, HitResult rayTraceResult, @Nullable LivingEntity shooter, SpellContext spellContext, SpellStats stats) {
        spellContext.setCanceled(true);
        if (spellContext.getCurrentIndex() < spellContext.getSpell().recipe.size()) {
            Spell newSpell = new Spell(new ArrayList<>(spellContext.getSpell().recipe.subList(spellContext.getCurrentIndex(), spellContext.getSpell().recipe.size())));
            SpellContext newContext = spellContext.clone().withSpell(newSpell);
            SpellResolver resolver = new EntitySpellResolver(newContext);
            //List<AbstractAugment> newAugments = new ArrayList<AbstractAugment>();
            propagate(world, rayTraceResult, shooter, stats, resolver);
        }
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.sendPacket(world, rayTraceResult, shooter, spellContext,spellStats);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.sendPacket(world, rayTraceResult, shooter, spellContext,spellStats);
    }

    public int getProjectileLifespan() {
        return this.SWORD_TTL != null ? (Integer)this.SWORD_TTL.get() : 60;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentAOE.INSTANCE,
                AugmentAccelerate.INSTANCE,
                AugmentSensitive.INSTANCE
        );
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public int getDefaultManaCost() {
        return 400;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.CONJURATION);
    }
}
