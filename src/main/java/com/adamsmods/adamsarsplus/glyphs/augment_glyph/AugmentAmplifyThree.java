package com.adamsmods.adamsarsplus.glyphs.augment_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import net.minecraft.resources.ResourceLocation;


public class AugmentAmplifyThree extends AbstractAugment {

    public static final AugmentAmplifyThree INSTANCE = new AugmentAmplifyThree(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_amplify_three"), "Amplify III");

    public AugmentAmplifyThree(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentAmplify.INSTANCE.getDefaultManaCost() * 9;
    }

    @Override
    public String getBookDescription() {
        return "Massively increases the power of most spell effects.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }
    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAmplification(4.0d);
        return super.applyModifiers(builder, spellPart);
    }

}
