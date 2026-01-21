package adamsmods.adamsarsplus.common.glyphs.augment_glyph;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;

public class AugmentDurationDownTwo extends AbstractAugment {

    public static final AugmentDurationDownTwo INSTANCE = new AugmentDurationDownTwo();

    public AugmentDurationDownTwo() {
        super("glyph_durationdown_two", "Duration Down II");
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentDurationDown.INSTANCE.getDefaultManaCost() * 4;
    }

    @Override
    public String getBookDescription() {
        return "Greatly reduces the duration of spells.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addDurationModifier(-2.0);
        return super.applyModifiers(builder, spellPart);
    }

}
