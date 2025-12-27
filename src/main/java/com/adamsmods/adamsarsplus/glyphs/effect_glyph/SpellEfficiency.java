package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.effects.ManaExhaustEffect;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Set;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.*;
import static com.adamsmods.adamsarsplus.Config.*;
import static com.adamsmods.adamsarsplus.Config.Common.DISCOUNT_BACKLASH;
import static com.adamsmods.adamsarsplus.Config.Common.MAX_DISCOUNTS;


public class SpellEfficiency extends AbstractEffect {
    public SpellEfficiency(ResourceLocation tag, String description) {
        super(tag, description);
    }
    public static final SpellEfficiency INSTANCE = new SpellEfficiency(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_spellefficiency"), "Spell Efficiency");

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (DISCOUNT_BACKLASH.get() && !shooter.hasEffect(SIX_EYES_EFFECT.get())) {

            int X = spellContext.getSpell().getInstanceCount(SpellEfficiency.INSTANCE);
            int ticks = (int) (40.0 * X);

            shooter.addEffect(new MobEffectInstance(MANA_EXHAUST_EFFECT.get(), ticks));
        }
    }

    @Override
    protected int getDefaultManaCost() {
        return 0;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        PER_SPELL_LIMIT = MAX_DISCOUNTS;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return setOf();
    }
}
