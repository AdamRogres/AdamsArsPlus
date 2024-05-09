package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;

import com.adamsmods.adamsarsplus.entities.EntityDomainSpell;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.entity.EntityLingeringSpell;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLinger;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectWall;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectDomain extends AbstractEffect {
    public EffectDomain(ResourceLocation tag, String description) {
        super(tag, description);
    }
    public static final EffectDomain INSTANCE = new EffectDomain(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectdomain"), "Domain");

    @Override
    public void onResolve(HitResult rayTraceResult, Level world,@NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super.onResolve(rayTraceResult, world, shooter, spellStats, spellContext, resolver);
        Vec3 hit = safelyGetHitPos(rayTraceResult);
        EntityDomainSpell entityDomainSpell = new EntityDomainSpell(world, shooter);
        spellContext.setCanceled(true);
        if (spellContext.getCurrentIndex() >= spellContext.getSpell().recipe.size())
            return;
        Spell newSpell = spellContext.getRemainingSpell();
        SpellContext newContext = spellContext.clone().withSpell(newSpell);
        entityDomainSpell.setAoe((float) spellStats.getAoeMultiplier());
        entityDomainSpell.setSensitive(spellStats.isSensitive());
        entityDomainSpell.setDome(spellStats.hasBuff(AugmentPierce.INSTANCE));
        entityDomainSpell.setFilter(spellStats.hasBuff(AugmentExtract.INSTANCE));
        entityDomainSpell.setAccelerates((int) spellStats.getAccMultiplier());
        entityDomainSpell.extendedTime = spellStats.getDurationMultiplier();
        entityDomainSpell.setShouldFall(spellStats.hasBuff(AugmentDampen.INSTANCE));
        entityDomainSpell.spellResolver = new SpellResolver(newContext);
        entityDomainSpell.setPos(hit.x, hit.y, hit.z);
        entityDomainSpell.setColor(spellContext.getColors());
        world.addFreshEntity(entityDomainSpell);
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
                AugmentSensitive.INSTANCE,
                AugmentAOE.INSTANCE,
                AugmentAccelerate.INSTANCE,
                AugmentDecelerate.INSTANCE,
                AugmentExtendTime.INSTANCE,
                AugmentDurationDown.INSTANCE,
                AugmentDampen.INSTANCE,
                AugmentPierce.INSTANCE,
                AugmentExtract.INSTANCE,
                AugmentSplit.INSTANCE);
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