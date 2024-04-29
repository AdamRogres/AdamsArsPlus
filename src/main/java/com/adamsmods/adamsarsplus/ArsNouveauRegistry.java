package com.adamsmods.adamsarsplus;

import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectDomain;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectLimitless;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectSwapTarget;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.SpellEfficiency;
import com.adamsmods.adamsarsplus.entities.effects.LimitlessEffect;

import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

import static com.adamsmods.adamsarsplus.AdamsArsPlus.MOD_ID;
import static com.adamsmods.adamsarsplus.lib.AdamsLibPotions.LIMITLESS;

public class ArsNouveauRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);
    public static final RegistryObject<MobEffect> LIMITLESS_EFFECT = EFFECTS.register(LIMITLESS, LimitlessEffect::new);

    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>(); //this will come handy for datagen

    public static void registerGlyphs() {
       //Augments
        register(AugmentAccelerateThree.INSTANCE);
        register(AugmentAccelerateTwo.INSTANCE);
        register(AugmentAOEThree.INSTANCE);
        register(AugmentAOETwo.INSTANCE);
        register(AugmentExtendTimeThree.INSTANCE);
        register(AugmentExtendTimeTwo.INSTANCE);
        //Effects
        register(SpellEfficiency.INSTANCE);
        register(EffectDomain.INSTANCE);
        register(EffectLimitless.INSTANCE);
        register(EffectSwapTarget.INSTANCE);

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
