package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Set;



public class SpellEfficiency extends AbstractEffect {
    public SpellEfficiency(ResourceLocation tag, String description) {
        super(tag, description);
    }
    public static final SpellEfficiency INSTANCE = new SpellEfficiency(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_spellefficiency"), "Spell Efficiency");


    @Override
    protected int getDefaultManaCost() {
        return 0;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return setOf();
    }
}
