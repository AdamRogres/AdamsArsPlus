package adamsmods.adamsarsplus.common.glyphs.augment_glyph;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;

public class AugmentExtendTimeTwo extends AbstractAugment {

    public static final AugmentExtendTimeTwo INSTANCE = new AugmentExtendTimeTwo();

    public AugmentExtendTimeTwo() {
        super("glyph_extendtime_two", "Extend Time II");
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentExtendTime.INSTANCE.getDefaultManaCost() * 4;
    }

    @Override
    public String getBookDescription() {
        return "Greatly extends the time that spells last.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addDurationModifier(2.0);
        return super.applyModifiers(builder, spellPart);
    }

}
