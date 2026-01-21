package adamsmods.adamsarsplus.common.glyphs.example;

import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;

import static adamsmods.adamsarsplus.AdamsArsPlus.prefix;

public abstract class ElementalAbstractForm extends AbstractCastMethod {
    public ElementalAbstractForm(String tag, String description) {
        super(prefix("glyph_" + tag), description);
    }

}
