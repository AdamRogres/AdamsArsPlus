package adamsmods.adamsarsplus.common.glyphs.augment_glyph;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;

public class AugmentAccelerateTwo extends AbstractAugment {

    public static final AugmentAccelerateTwo INSTANCE = new AugmentAccelerateTwo();

    public AugmentAccelerateTwo() {
        super("glyph_accelerate_two", "Accelerate II");
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentAccelerate.INSTANCE.getDefaultManaCost() * 4;
    }

    @Override
    public String getBookDescription() {
        return "Greatly increases the speed of projectile spells.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAccelerationModifier(2.0F);
        return super.applyModifiers(builder, spellPart);
    }

}
