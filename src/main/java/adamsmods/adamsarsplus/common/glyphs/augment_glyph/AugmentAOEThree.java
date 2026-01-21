package adamsmods.adamsarsplus.common.glyphs.augment_glyph;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;

public class AugmentAOEThree extends AbstractAugment {

    public static final AugmentAOEThree INSTANCE = new AugmentAOEThree();

    public AugmentAOEThree() {
        super("glyph_aoe_three", "AOE III");
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentAOE.INSTANCE.getDefaultManaCost() * 9;
    }

    @Override
    public String getBookDescription() {
        return "Spells will affect a gargantuan area around a targeted block.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAOE(4.0d);
        return super.applyModifiers(builder, spellPart);
    }

}
