package adamsmods.adamsarsplus.common.glyphs.augment_glyph;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;

public class AugmentAmplifyTwo extends AbstractAugment {

    public static final AugmentAmplifyTwo INSTANCE = new AugmentAmplifyTwo();

    public AugmentAmplifyTwo() {
        super("glyph_amplify_two", "Amplify II");
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentAmplify.INSTANCE.getDefaultManaCost() * 4;
    }

    @Override
    public String getBookDescription() {
        return "Greatly increases the power of most spell effects.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }
    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAmplification(2.0d);
        return super.applyModifiers(builder, spellPart);
    }

}
