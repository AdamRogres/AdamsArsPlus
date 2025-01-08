package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.PerkUtil;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.perk.GlidingPerk;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectGlide;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.SIMPLE_DOMAIN_EFFECT;
import static com.adamsmods.adamsarsplus.lib.AdamsLibPotions.SIMPLE_DOMAIN;

public class EffectSimpleDomain extends AbstractEffect implements IPotionEffect {
    public static EffectSimpleDomain INSTANCE = new EffectSimpleDomain(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectsimpledomain"), "Simple Domain");

    public EffectSimpleDomain(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public void applyConfigPotion(LivingEntity entity, MobEffect potionEffect, SpellStats spellStats) {
        this.applyConfigPotion(entity, potionEffect, spellStats, false);
    }

    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Entity var8 = rayTraceResult.getEntity();
        if (var8 instanceof LivingEntity living) {
            this.applyConfigPotion(living, (MobEffect) SIMPLE_DOMAIN_EFFECT.get(), spellStats);
        }

    }

    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        this.addPotionConfig(builder, 3);
        this.addExtendTimeConfig(builder, 1);
    }

    public int getDefaultManaCost() {
        return 600;
    }

    public String getBookDescription() {
        return "Grants a simple domain that protects against the surehit of domains. ";
    }

    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    public @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return this.augmentSetOf(new AbstractAugment[]{AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE});
    }

    public @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(new SpellSchool[]{SpellSchools.ABJURATION});
    }

    public int getBaseDuration() {
        return this.POTION_TIME == null ? 3 : 3;
    }

    public int getExtendTimeDuration() {
        return this.EXTEND_TIME == null ? 1 : 1;
    }
}

