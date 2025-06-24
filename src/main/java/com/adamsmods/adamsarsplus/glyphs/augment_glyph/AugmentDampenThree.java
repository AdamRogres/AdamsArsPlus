package com.adamsmods.adamsarsplus.glyphs.augment_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import net.minecraft.resources.ResourceLocation;


public class AugmentDampenThree extends AbstractAugment {

    public static final AugmentDampenThree INSTANCE = new AugmentDampenThree(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_dampen_three"), "Dampen III");

    public AugmentDampenThree(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentDampen.INSTANCE.getDefaultManaCost() * 9;
    }

    @Override
    public String getBookDescription() {
        return "Massively decreases the power of most spells.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }
    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAmplification(-4.0d);
        return super.applyModifiers(builder, spellPart);
    }

}
