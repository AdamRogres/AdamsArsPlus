package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;

import com.adamsmods.adamsarsplus.block.DomainShell;
import com.adamsmods.adamsarsplus.block.ModBlocks;
import com.adamsmods.adamsarsplus.block.tile.DomainShellTile;
import com.adamsmods.adamsarsplus.entities.EntityDomainSpell;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentOpenDomain;
import com.adamsmods.adamsarsplus.lib.AdamsLibPotions;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.common.block.MageBlock;
import com.hollingsworth.arsnouveau.common.block.tile.MageBlockTile;
import com.hollingsworth.arsnouveau.common.entity.EntityLingeringSpell;
import com.hollingsworth.arsnouveau.common.items.curios.ShapersFocus;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLinger;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectWall;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.common.block.MageBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Predicate;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.DOMAIN_BURNOUT_EFFECT;
import static com.adamsmods.adamsarsplus.Config.DOMAIN_BURNOUT;

public class EffectDomain extends AbstractEffect {
    public EffectDomain(ResourceLocation tag, String description) {
        super(tag, description);
    }
    public static final EffectDomain INSTANCE = new EffectDomain(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectdomain"), "Domain");

    public boolean canUseDomian(LivingEntity playerEntity) {
        return !isRealPlayer(playerEntity) || isRealPlayer(playerEntity) && (playerEntity.getEffect(DOMAIN_BURNOUT_EFFECT.get()) == null || (playerEntity instanceof Player player && player.isCreative()));
    }

    @Override
    public void onResolve(HitResult rayTraceResult, Level world,@NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super.onResolve(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
        if (!canUseDomian(shooter)) {
            return;
        }

        Vec3 hit = safelyGetHitPos(rayTraceResult);
        EntityDomainSpell entityDomainSpell = new EntityDomainSpell(world, shooter);
        spellContext.setCanceled(true);
        if (spellContext.getCurrentIndex() >= spellContext.getSpell().recipe.size())
            return;
        Spell newSpell = spellContext.getRemainingSpell();
        SpellContext newContext = spellContext.clone().withSpell(newSpell);

        entityDomainSpell.setAoe((float) spellStats.getAoeMultiplier());
        entityDomainSpell.setOpen(spellStats.hasBuff(AugmentOpenDomain.INSTANCE));
        entityDomainSpell.setDome(spellStats.hasBuff(AugmentPierce.INSTANCE));
        entityDomainSpell.setFilter(spellStats.hasBuff(AugmentExtract.INSTANCE));
        entityDomainSpell.setAccelerates((int) spellStats.getAccMultiplier());

        entityDomainSpell.extendedTime = spellStats.getDurationMultiplier();
        entityDomainSpell.shellblocks = 0;
        entityDomainSpell.refinement = spellStats.getAmpMultiplier();

        entityDomainSpell.spellResolver = new SpellResolver(newContext);
        entityDomainSpell.setPos(hit.x, hit.y, hit.z);
        entityDomainSpell.setColor(spellContext.getColors());

        world.addFreshEntity(entityDomainSpell);

        int ticks = (int) (20.0 * (10.0 + spellStats.getDurationMultiplier()));

        if (DOMAIN_BURNOUT.get()) {
            shooter.addEffect(new MobEffectInstance(DOMAIN_BURNOUT_EFFECT.get(), ticks));
        }

        //Create Sphere
        float aoe = entityDomainSpell.getAoe();
        int flatAoe = Math.round(aoe);
        int radius = 4 + flatAoe;
        ANFakePlayer fakePlayer = ANFakePlayer.getPlayer((ServerLevel)world);

        Predicate<Double> Sphere = true ? (distance) -> distance <= radius + 0.5 && distance >= radius - 0.5 : (distance) -> (distance <= radius + 0.5);

        if(!entityDomainSpell.getOpen()){
            for (BlockPos p : BlockPos.withinManhattan(entityDomainSpell.blockPosition(), radius, radius, radius)) {
                if (Sphere.test(BlockUtil.distanceFromCenter(p, entityDomainSpell.blockPosition()))) {
                    if(!entityDomainSpell.getDome() || (entityDomainSpell.blockPosition().getY() - 2 <  p.getY())) {
                        //spellResolver.onResolveEffect(level(), new BlockHitResult(new Vec3(p.getX(), p.getY(), p.getZ()), Direction.UP, p, false));
                        if (world.isInWorldBounds(p) && BlockUtil.destroyRespectsClaim(this.getPlayer(shooter, (ServerLevel)world), world, p)) {
                            BlockState state = world.getBlockState(p);
                            if (state.canBeReplaced() && world.isUnobstructed(((DomainShell) ModBlocks.DOMAIN_SHELL_BLOCK.get()).defaultBlockState(), p, CollisionContext.of(fakePlayer))) {
                                world.setBlockAndUpdate(p, (BlockState)((DomainShell) ModBlocks.DOMAIN_SHELL_BLOCK.get()).defaultBlockState().setValue(DomainShell.TEMPORARY, true));
                                BlockEntity var12 = world.getBlockEntity(p);
                                if (var12 instanceof DomainShellTile) {
                                    DomainShellTile tile = (DomainShellTile)var12;
                                    tile.color = spellContext.getColors();
                                    tile.lengthModifier = spellStats.getDurationMultiplier();
                                    tile.refinement = spellStats.getAmpMultiplier();

                                    world.sendBlockUpdated(p, world.getBlockState(p), world.getBlockState(p), 2);

                                }

                                entityDomainSpell.shellblocks++;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getBookDescription() {
        return "Creates a Sphereical area that applies spells on nearby entities for a short time. Applying Sensitive will make this spell target blocks instead. AOE will expand the effective range, Accelerate will cast spells faster, Dampen will cause the domain to be affected by gravity, and Extend Time will increase the duration. After casting a spell with Domain the caster will be effected by Domain burnout and will be unable to cast another domain.";
    }

    @Override
    public int getDefaultManaCost() {
        return 1000;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentAOE.INSTANCE,            // Barrier size+
                AugmentAccelerate.INSTANCE,     // Cast time+
                AugmentDecelerate.INSTANCE,     // Cast time-
                AugmentExtendTime.INSTANCE,     // Barrier time+
                AugmentDurationDown.INSTANCE,   // Barrier time-
                AugmentPierce.INSTANCE,         // Dome
                AugmentExtract.INSTANCE,        // Filter self
                AugmentAmplify.INSTANCE,        // Refinement+
                AugmentOpenDomain.INSTANCE      // Removes barrier & targets blocks

        );
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        PER_SPELL_LIMIT = builder.comment("The maximum number of times this glyph may appear in a single spell").defineInRange("per_spell_limit", 1, 1, 1);
    }

    @NotNull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.MANIPULATION);
    }

    @Override
    protected void addDefaultInvalidCombos(Set<ResourceLocation> defaults) {
        defaults.add(EffectLinger.INSTANCE.getRegistryName());
        defaults.add(EffectWall.INSTANCE.getRegistryName());
    }

}