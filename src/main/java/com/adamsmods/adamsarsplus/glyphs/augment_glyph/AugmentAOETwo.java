package com.adamsmods.adamsarsplus.glyphs.augment_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import net.minecraft.resources.ResourceLocation;


public class AugmentAOETwo extends AbstractAugment {

    public static final AugmentAOETwo INSTANCE = new AugmentAOETwo(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_aoe_two"), "AOE II");

    public AugmentAOETwo(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentAOE.INSTANCE.getDefaultManaCost() * 4;
    }

    @Override
    public String getBookDescription() {
        return "Spells will affect a huge area around a targeted block.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }
    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAOE(2.0d);
        return super.applyModifiers(builder, spellPart);
    }

}
