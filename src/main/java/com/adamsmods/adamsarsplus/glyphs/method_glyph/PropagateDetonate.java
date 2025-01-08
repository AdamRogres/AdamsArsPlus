package com.adamsmods.adamsarsplus.glyphs.method_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.api.IPropagator;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.TileCaster;
import com.hollingsworth.arsnouveau.common.block.BasicSpellTurret;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
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

public class PropagateDetonate extends AbstractEffect implements IPropagator {

    public PropagateDetonate(ResourceLocation tag, String description) {
        super(tag, description);
    }
    public static final PropagateDetonate INSTANCE = new PropagateDetonate(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_propdetonate"), "Detonate");

    public ForgeConfigSpec.IntValue DETONATE_TTL;
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        this.DETONATE_TTL = builder.comment("Max lifespan of the projectile, in seconds.").defineInRange("max_lifespan", 60, 0, Integer.MAX_VALUE);
    }

    @Override
    public void propagate(Level world, HitResult hitResult, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {

        int numPierce = stats.getBuffCount(AugmentPierce.INSTANCE);
        int numSplits = 1 + stats.getBuffCount(AugmentSplit.INSTANCE);

        Vec3 pos = hitResult.getLocation();
        List<DetonateProjectile> projectiles = new ArrayList<>();
        for (int i = 0; i < numSplits; i++) {
            projectiles.add(new DetonateProjectile(world, resolver));
        }
        float velocity = Math.max(0.1f, 0.75f + stats.getAccMultiplier() / 2);
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
        for (DetonateProjectile proj : projectiles) {
            proj.setPos(pos.add(0, 1, 0));
            if (!(shooter instanceof FakePlayer)) {
                proj.shoot(shooter, shooter.getXRot(), shooter.getYRot() + Math.round(counter / 2.0) * 5 * opposite, 0.0F, velocity, 0.8f);
            } else {
                proj.shoot(direction.x, direction.y, direction.z, velocity, 0.8F);
            }
            opposite = opposite * -1;
            counter++;

            proj.pierceLeft = numPierce;
            proj.extendedTime = stats.getDurationMultiplier();

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

    @Override
    public int getDefaultManaCost() {
        return 100;
    }

    @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return MethodDetonate.INSTANCE.getCompatibleAugments();
    }

    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Nonnull
    public Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.MANIPULATION);
    }

    public String getBookDescription() {
        return "Summons a projectile that applies spell effects when this projectile hits a target or block. The projectile will resolve in the air after traveling for a set time. This time can be altered with extend time. Pierce will cause this time to reset on spell resolve.";
    }

    @Override
    public Integer getTypeIndex() {
        return 8;
    }

}
