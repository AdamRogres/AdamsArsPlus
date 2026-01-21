package adamsmods.adamsarsplus.common.glyphs.example;

import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;

import static adamsmods.adamsarsplus.AdamsArsPlus.prefix;

public abstract class ElementalAbstractEffect extends AbstractEffect {

    public ElementalAbstractEffect(String tag, String description) {
        super(prefix("glyph_" + tag), description);
    }

}
