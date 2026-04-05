package adamsmods.adamsarsplus.common.glyphs.effect_glyph;

import adamsmods.adamsarsplus.common.entity.MeteorProjectile;
import com.alexthw.sauce.api.IPropagator;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.TileCaster;
import com.hollingsworth.arsnouveau.common.block.BasicSpellTurret;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EffectMeteorSwarm extends AbstractEffect implements IPropagator {

    public EffectMeteorSwarm() {
        super("glyph_effectmeteorswarm", "Meteor Swarm");
    }
    public static final EffectMeteorSwarm INSTANCE = new EffectMeteorSwarm();

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
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

    public void sendPacket(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats stats, SpellContext spellContext, SpellResolver resolver) {
        SpellContext newContext = spellContext.makeChildContext();
        var mutable_spell = newContext.getSpell().mutable();
        mutable_spell.recipe.addFirst(DUMMY);
        newContext.withSpell(mutable_spell.immutable());
        SpellResolver newResolver = resolver.getNewResolver(newContext);
        spellContext.setCanceled(true);
        AbstractCastMethod newCastType = getCastType();
        if (newCastType != null)
            newResolver.castType = newCastType;
        propagate(world, rayTraceResult, shooter, stats, newResolver);
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.sendPacket(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.sendPacket(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
    }

    public int getProjectileLifespan() {
        return 60;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentAOE.INSTANCE,
                AugmentAccelerate.INSTANCE,
                AugmentSensitive.INSTANCE,
                AugmentPierce.INSTANCE
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
