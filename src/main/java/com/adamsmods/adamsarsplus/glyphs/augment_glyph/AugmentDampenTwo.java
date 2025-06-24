package com.adamsmods.adamsarsplus.glyphs.augment_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import net.minecraft.resources.ResourceLocation;


public class AugmentDampenTwo extends AbstractAugment {

    public static final AugmentDampenTwo INSTANCE = new AugmentDampenTwo(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_dampen_two"), "Dampen II");

    public AugmentDampenTwo(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentDampen.INSTANCE.getDefaultManaCost() * 4;
    }

    @Override
    public String getBookDescription() {
        return "Greatly decreases the power of most spells.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }
    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAmplification(-2.0d);
        return super.applyModifiers(builder, spellPart);
    }

}
