package adamsmods.adamsarsplus.common.glyphs.augment_glyph;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;

public class AugmentAccelerateThree extends AbstractAugment {

    public static final AugmentAccelerateThree INSTANCE = new AugmentAccelerateThree();

    public AugmentAccelerateThree() {
        super("glyph_accelerate_three", "Accelerate III");
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentAccelerate.INSTANCE.getDefaultManaCost() * 9;
    }

    @Override
    public String getBookDescription() {
        return "Immensely increases the speed of projectile spells.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAccelerationModifier(4.0F);
        return super.applyModifiers(builder, spellPart);
    }

}
