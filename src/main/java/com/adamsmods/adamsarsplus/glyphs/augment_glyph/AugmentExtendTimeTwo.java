package com.adamsmods.adamsarsplus.glyphs.augment_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.minecraft.resources.ResourceLocation;


public class AugmentExtendTimeTwo extends AbstractAugment {

    public static final AugmentExtendTimeTwo INSTANCE = new AugmentExtendTimeTwo(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_extendtime_two"), "Extend Time II");

    public AugmentExtendTimeTwo(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentExtendTime.INSTANCE.getDefaultManaCost() * 4;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addDurationModifier(2.0);
        return super.applyModifiers(builder, spellPart);
    }
    @Override
    public String getBookDescription() {
        return "Greatly extends the time that spells last.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }
}
