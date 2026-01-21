package adamsmods.adamsarsplus.common.glyphs.augment_glyph;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;

public class AugmentOpenDomain extends AbstractAugment {

    public static final AugmentOpenDomain INSTANCE = new AugmentOpenDomain();

    public AugmentOpenDomain() {
        super("glyph_open_domain", "Open Barrier");
    }

    @Override
    public int getDefaultManaCost() { return 600; }

    @Override
    public String getBookDescription() {
        return "Alters the Domain effect to target the world itself";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

}
