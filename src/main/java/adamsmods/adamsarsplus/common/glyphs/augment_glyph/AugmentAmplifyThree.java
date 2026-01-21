package adamsmods.adamsarsplus.common.glyphs.augment_glyph;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;


public class AugmentAmplifyThree extends AbstractAugment {

    public static final AugmentAmplifyThree INSTANCE = new AugmentAmplifyThree();

    public AugmentAmplifyThree() {
        super("glyph_amplify_three", "Amplify III");
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAmplification(4.0d);
        return super.applyModifiers(builder, spellPart);
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
}
