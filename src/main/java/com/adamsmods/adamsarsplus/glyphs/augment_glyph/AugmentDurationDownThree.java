package com.adamsmods.adamsarsplus.glyphs.augment_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import net.minecraft.resources.ResourceLocation;


public class AugmentDurationDownThree extends AbstractAugment {

    public static final AugmentDurationDownThree INSTANCE = new AugmentDurationDownThree(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_durationdown_three"), "Duration Down III");

    public AugmentDurationDownThree(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentDurationDown.INSTANCE.getDefaultManaCost() * 9;
    }

    @Override
    public String getBookDescription() {
        return "Massively reduces the duration of spells";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addDurationModifier(-4.0);
        return super.applyModifiers(builder, spellPart);
    }

}
