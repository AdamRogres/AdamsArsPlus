package adamsmods.adamsarsplus.common.glyphs.augment_glyph;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;

public class AugmentDurationDownThree extends AbstractAugment {

    public static final AugmentDurationDownThree INSTANCE = new AugmentDurationDownThree();

    public AugmentDurationDownThree() {
        super("glyph_durationdown_three", "Duration Down III");
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
