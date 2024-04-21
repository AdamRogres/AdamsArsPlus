package com.adamsmods.adamsarsplus;

import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;

import java.util.ArrayList;
import java.util.List;

public class ArsNouveauRegistry {

    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>(); //this will come handy for datagen

    public static void registerGlyphs() {
        register(AugmentAccelerateThree.INSTANCE);
        register(AugmentAccelerateTwo.INSTANCE);
        register(AugmentAOEThree.INSTANCE);
        register(AugmentAOETwo.INSTANCE);
        register(AugmentExtendTimeThree.INSTANCE);
        register(AugmentExtendTimeTwo.INSTANCE);


//        register(Random25.INSTANCE);
//        register(Random50.INSTANCE);
//        register(Random75.INSTANCE);

        // register omega glyphs



    }

    public static void registerSounds() {
    }

    public static void register(AbstractSpellPart spellPart) {
        GlyphRegistry.registerSpell(spellPart);
        registeredSpells.add(spellPart);
    }

    public static void registerRituals() {
//        registerRitual(new LevelingRitual());
    }

    public static void registerRitual(AbstractRitual ritual) {
        RitualRegistry.registerRitual(ritual);
    }


}
