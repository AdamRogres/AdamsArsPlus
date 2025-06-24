package com.adamsmods.adamsarsplus.glyphs.augment_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import net.minecraft.resources.ResourceLocation;


public class AugmentLesserAOE extends AbstractAugment {

    public static final AugmentLesserAOE INSTANCE = new AugmentLesserAOE(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_lesser_aoe"), "Lesser AOE");

    public AugmentLesserAOE(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentAOE.INSTANCE.getDefaultManaCost();
    }

    @Override
    public String getBookDescription() {
        return "Spells will affect a decreased area.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }
    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAOE(-1.0d);
        return super.applyModifiers(builder, spellPart);
    }

}
