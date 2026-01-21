package adamsmods.adamsarsplus.common.glyphs.augment_glyph;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;

public class AugmentLesserAOE extends AbstractAugment {

    public static final AugmentLesserAOE INSTANCE = new AugmentLesserAOE();

    public AugmentLesserAOE() {
        super("glyph_lesser_aoe", "Lesser AOE");
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
