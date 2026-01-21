package adamsmods.adamsarsplus.common.glyphs.augment_glyph;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;

public class AugmentExtendTimeThree extends AbstractAugment {

    public static final AugmentExtendTimeThree INSTANCE = new AugmentExtendTimeThree();

    public AugmentExtendTimeThree() {
        super("glyph_extendtime_three", "Extend Time III");
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentExtendTime.INSTANCE.getDefaultManaCost() * 9;
    }

    @Override
    public String getBookDescription() {
        return "Immensely extends the time that spells last.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addDurationModifier(4.0);
        return super.applyModifiers(builder, spellPart);
    }

}
