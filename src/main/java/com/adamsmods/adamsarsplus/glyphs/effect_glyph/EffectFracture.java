package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.FRACTURE_EFFECT;
import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.LIMITLESS_EFFECT;

public class EffectFracture extends AbstractEffect implements IPotionEffect {
    public static EffectFracture INSTANCE = new EffectFracture(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectfracture"), "Fracture");

    public EffectFracture(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public void applyConfigPotion(LivingEntity entity, MobEffect potionEffect, SpellStats spellStats) {
        this.applyConfigPotion(entity, potionEffect, spellStats, false);
    }

    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Entity var8 = rayTraceResult.getEntity();
        if (var8 instanceof LivingEntity living) {
            this.applyConfigPotion(living, (MobEffect) FRACTURE_EFFECT.get(), spellStats);
        }
    }

    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        this.addPotionConfig(builder, 7);
        this.addExtendTimeConfig(builder, 3);
    }

    public int getDefaultManaCost() {
        return 100;
    }

    public String getBookDescription() {
        return "Inflicts the target with Fracture. Entities with this effect take damage when they make impact with blocks.";
    }

    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    public @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return this.augmentSetOf(new AbstractAugment[]{AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE});
    }

    public @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(new SpellSchool[]{SpellSchools.ELEMENTAL_EARTH});
    }

    public int getBaseDuration() {
        return this.POTION_TIME == null ? 7 : 7;
    }

    public int getExtendTimeDuration() {
        return this.EXTEND_TIME == null ? 3 : 3;
    }
}

