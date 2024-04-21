package com.adamsmods.adamsarsplus;

import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.spell.augment.*;

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

    public static void addAugments(){
        for(AbstractSpellPart part : GlyphRegistry.getSpellpartMap().values()){
            if(part.compatibleAugments.contains(AugmentAccelerate.INSTANCE)&&!part.compatibleAugments.contains(AugmentAccelerateThree.INSTANCE)){
                part.compatibleAugments.add(AugmentAccelerateThree.INSTANCE);
            }
            if(part.compatibleAugments.contains(AugmentAccelerate.INSTANCE)&&!part.compatibleAugments.contains(AugmentAccelerateTwo.INSTANCE)){
                part.compatibleAugments.add(AugmentAccelerateTwo.INSTANCE);
            }

            if(part.compatibleAugments.contains(AugmentExtendTime.INSTANCE)&&!part.compatibleAugments.contains(AugmentExtendTimeThree.INSTANCE)){
                part.compatibleAugments.add(AugmentExtendTimeThree.INSTANCE);
            }
            if(part.compatibleAugments.contains(AugmentExtendTime.INSTANCE)&&!part.compatibleAugments.contains(AugmentExtendTimeTwo.INSTANCE)){
                part.compatibleAugments.add(AugmentExtendTimeTwo.INSTANCE);
            }

            if(part.compatibleAugments.contains(AugmentAOE.INSTANCE)&&!part.compatibleAugments.contains(AugmentAOEThree.INSTANCE)){
                part.compatibleAugments.add(AugmentAOEThree.INSTANCE);
            }
            if(part.compatibleAugments.contains(AugmentAOE.INSTANCE)&&!part.compatibleAugments.contains(AugmentAOETwo.INSTANCE)){
                part.compatibleAugments.add(AugmentAOETwo.INSTANCE);
            }
        }
    }

}
