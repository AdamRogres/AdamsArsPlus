package com.adamsmods.adamsarsplus.glyphs.method_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectDomain;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MethodDetonate extends AbstractCastMethod {

    public MethodDetonate(ResourceLocation tag, String description) {
        super(tag, description);
    }
    public static final MethodDetonate INSTANCE = new MethodDetonate(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_methoddetonate"), "Detonate");

    public ForgeConfigSpec.IntValue DETONATE_TTL;
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        this.DETONATE_TTL = builder.comment("Max lifespan of the projectile, in seconds.").defineInRange("max_lifespan", 60, 0, Integer.MAX_VALUE);
    }

    public int getProjectileLifespan() {
        return this.DETONATE_TTL != null ? (Integer)this.DETONATE_TTL.get() : 60;
    }

    public int getDefaultManaCost() {
        return 80;
    }

    public void summonProjectiles(Level world, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {
        int numSplits = 1 + stats.getBuffCount(AugmentSplit.INSTANCE);

        List<DetonateProjectile> projectiles = new ArrayList();

        for(int i = 0; i < numSplits; ++i) {
            DetonateProjectile spell = new DetonateProjectile(world, resolver);
            spell.extendedTime = stats.getDurationMultiplier();
            projectiles.add(spell);
        }

        float velocity = Math.max(0.1F, 0.75F + stats.getAccMultiplier() / 2.0F);
        int opposite = -1;
        int counter = 0;

        for(DetonateProjectile proj : projectiles) {
            proj.shoot(shooter, shooter.getXRot(), shooter.getYRot() + (float)(Math.round((double)counter / (double)2.0F) * 10L * (long)opposite), 0.0F, velocity, 0.8F);
            opposite *= -1;
            ++counter;
            world.addFreshEntity(proj);
        }

    }

    public void summonProjectiles(Level world, BlockPos pos, LivingEntity shooter, SpellStats stats, SpellResolver resolver) {
        ArrayList<DetonateProjectile> projectiles = new ArrayList();
        DetonateProjectile projectileSpell = new DetonateProjectile(world, resolver);
        projectileSpell.setPos((double)pos.getX(), (double)(pos.getY() + 1), (double)pos.getZ());

        projectileSpell.extendedTime = stats.getDurationMultiplier();
        projectiles.add(projectileSpell);
        int numSplits = stats.getBuffCount(AugmentSplit.INSTANCE);

        for(int i = 1; i < numSplits + 1; ++i) {
            Direction offset = shooter.getDirection().getClockWise();
            if (i % 2 == 0) {
                offset = offset.getOpposite();
            }

            BlockPos projPos = pos.relative(offset, i);
            projPos = projPos.offset(0, 2, 0);
            DetonateProjectile spell = new DetonateProjectile(world, resolver);
            spell.setPos((double)projPos.getX(), (double)projPos.getY(), (double)projPos.getZ());

            spell.extendedTime = stats.getDurationMultiplier();
            projectiles.add(spell);
        }

        for(DetonateProjectile proj : projectiles) {
            proj.setDeltaMovement(new Vec3((double)0.0F, -0.1, (double)0.0F));
            world.addFreshEntity(proj);
        }

    }

    public CastResolveType onCast(ItemStack stack, LivingEntity shooter, Level world, SpellStats spellStats, SpellContext context, SpellResolver resolver) {
        this.summonProjectiles(world, shooter, spellStats, resolver);
        return CastResolveType.SUCCESS;
    }

    public CastResolveType onCastOnBlock(UseOnContext context, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Level world = context.getLevel();
        Player shooter = context.getPlayer();
        this.summonProjectiles(world, shooter, spellStats, resolver);
        return CastResolveType.SUCCESS;
    }

    public CastResolveType onCastOnBlock(BlockHitResult blockRayTraceResult, LivingEntity caster, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        caster.lookAt(EntityAnchorArgument.Anchor.EYES, blockRayTraceResult.getLocation().add((double)0.0F, (double)0.0F, (double)0.0F));
        this.summonProjectiles(caster.getCommandSenderWorld(), blockRayTraceResult.getBlockPos(), caster, spellStats, resolver);
        return CastResolveType.SUCCESS;
    }

    public CastResolveType onCastOnEntity(ItemStack stack, LivingEntity caster, Entity target, InteractionHand hand, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.summonProjectiles(caster.getCommandSenderWorld(), caster, spellStats, resolver);
        return CastResolveType.SUCCESS;
    }

    public @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return this.augmentSetOf(new AbstractAugment[]{AugmentPierce.INSTANCE, AugmentSplit.INSTANCE, AugmentAccelerate.INSTANCE, AugmentDecelerate.INSTANCE, AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE, AugmentDampen.INSTANCE});
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    public String getBookDescription() {
        return "Summons a projectile that applies spell effects when this projectile hits a target or block. The projectile will resolve in the air after traveling for a set time. This time can be altered with extend time. Pierce will cause this time to reset on spell resolve.";
    }


}
