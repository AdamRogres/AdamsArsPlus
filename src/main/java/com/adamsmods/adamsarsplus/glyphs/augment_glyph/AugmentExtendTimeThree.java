package com.adamsmods.adamsarsplus.glyphs.augment_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.minecraft.resources.ResourceLocation;


public class AugmentExtendTimeThree extends AbstractAugment {

    public static final AugmentExtendTimeThree INSTANCE = new AugmentExtendTimeThree(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_extendtime_three"), "Extend Time III");

    public AugmentExtendTimeThree(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentExtendTime.INSTANCE.getDefaultManaCost() * 9;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addDurationModifier(4.0);
        return super.applyModifiers(builder, spellPart);
    }
    @Override
    public String getBookDescription() {
        return "Immensely extends the time that spells last.";
    }
}
