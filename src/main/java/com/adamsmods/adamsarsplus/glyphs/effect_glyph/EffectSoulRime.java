package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.FRACTURE_EFFECT;
import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.SOUL_RIME_EFFECT;

public class EffectSoulRime extends AbstractEffect implements IPotionEffect {
    public static EffectSoulRime INSTANCE = new EffectSoulRime(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectsoulrime"), "Soul Rime");

    public EffectSoulRime(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public void applyConfigPotion(LivingEntity entity, MobEffect potionEffect, SpellStats spellStats) {
        this.applyConfigPotion(entity, potionEffect, spellStats, false);
    }

    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Entity var8 = rayTraceResult.getEntity();
        if (var8 instanceof LivingEntity living) {
            this.applyConfigPotion(living, (MobEffect) SOUL_RIME_EFFECT.get(), spellStats);
        }
    }

    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        this.addPotionConfig(builder, 30);
        this.addExtendTimeConfig(builder, 10);
    }

    public int getDefaultManaCost() {
        return 100;
    }

    public String getBookDescription() {
        return "Inflicts the target with Soul Rime. Entities with this effect cannot teleport.";
    }

    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    public @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return this.augmentSetOf(new AbstractAugment[]{AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE});
    }

    public @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(new SpellSchool[]{SpellSchools.ELEMENTAL_WATER});
    }

    public int getBaseDuration() {
        return this.POTION_TIME == null ? 30 : 30;
    }

    public int getExtendTimeDuration() {
        return this.EXTEND_TIME == null ? 10 : 10;
    }
}

