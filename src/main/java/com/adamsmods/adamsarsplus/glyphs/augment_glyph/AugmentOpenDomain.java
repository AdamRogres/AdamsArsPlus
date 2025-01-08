package com.adamsmods.adamsarsplus.glyphs.augment_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectDomain;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import net.minecraft.resources.ResourceLocation;


public class AugmentOpenDomain extends AbstractAugment {

    public static final AugmentOpenDomain INSTANCE = new AugmentOpenDomain(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_open_domain"), "Open Barrier");

    public AugmentOpenDomain(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() { return 600; }

    @Override
    public String getBookDescription() {
        return "Alters the Domain effect to target the world itself";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

}
