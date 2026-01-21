package adamsmods.adamsarsplus.common.glyphs.augment_glyph;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;

public class AugmentAOETwo extends AbstractAugment {

    public static final AugmentAOETwo INSTANCE = new AugmentAOETwo();

    public AugmentAOETwo() {
        super("glyph_aoe_two", "AOE II");
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentAOE.INSTANCE.getDefaultManaCost() * 4;
    }

    @Override
    public String getBookDescription() {
        return "Spells will affect a huge area around a targeted block.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }
    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAOE(2.0d);
        return super.applyModifiers(builder, spellPart);
    }

}
