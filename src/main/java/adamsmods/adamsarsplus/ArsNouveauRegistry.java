package adamsmods.adamsarsplus;

import adamsmods.adamsarsplus.common.glyphs.augment_glyph.*;
import adamsmods.adamsarsplus.common.glyphs.effect_glyph.*;
import adamsmods.adamsarsplus.common.glyphs.method_glyph.*;
import adamsmods.adamsarsplus.common.perk.*;
import adamsmods.adamsarsplus.common.rituals.*;
import adamsmods.adamsarsplus.registry.ModRegistry;
import adamsmods.adamsarsplus.util.APerkSlot;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.api.registry.SpellCasterRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static adamsmods.adamsarsplus.registry.ModItems.*;

public class ArsNouveauRegistry {
    public static final List<AbstractSpellPart> registeredSpells = new ArrayList<>();
    public static final List<AbstractRitual> registeredRituals = new ArrayList<>();


    public static void init() {
        registerGlyphs();
        registerRituals();
        registerFamiliars();
        registerPerks();
    }

    private static void registerCasters() {


    }

    public static void registerGlyphs() {
        
        //Augments
        register(AugmentAmplifyThree.INSTANCE);
        register(AugmentAmplifyTwo.INSTANCE);
        register(AugmentDampenThree.INSTANCE);
        register(AugmentDampenTwo.INSTANCE);
        register(AugmentAccelerateThree.INSTANCE);
        register(AugmentAccelerateTwo.INSTANCE);
        register(AugmentAOEThree.INSTANCE);
        register(AugmentAOETwo.INSTANCE);
        register(AugmentLesserAOE.INSTANCE);
        register(AugmentExtendTimeThree.INSTANCE);
        register(AugmentExtendTimeTwo.INSTANCE);
        register(AugmentDurationDownThree.INSTANCE);
        register(AugmentDurationDownTwo.INSTANCE);
        register(AugmentOpenDomain.INSTANCE);

        //Effects
        register(SpellEfficiency.INSTANCE);
        register(EffectDomain.INSTANCE);
        register(EffectLimitless.INSTANCE);
        register(EffectSwapTarget.INSTANCE);
        register(EffectSimpleDomain.INSTANCE);
        register(EffectEruption.INSTANCE);
        register(EffectIceburst.INSTANCE);
        register(EffectRaiseEarth.INSTANCE);
        register(EffectDivineSmite.INSTANCE);
        register(EffectSummonUndead_boss.INSTANCE);
        register(EffectMeteorSwarm.INSTANCE);
        register(EffectAnnihilate.INSTANCE);
        register(EffectTenShadows.INSTANCE);
        register(EffectFracture.INSTANCE);
        register(EffectSoulRime.INSTANCE);
        register(EffectDismantle.INSTANCE);
        register(EffectBlueFlame.INSTANCE);
        register(EffectConjureBlade.INSTANCE);
        register(FilterNotSelf.INSTANCE);
        register(EffectConjureArrow.INSTANCE);

        //Methods
        register(MethodDetonate.INSTANCE);
        register(PropagateDetonate.INSTANCE);
    }

    public static void registerRitual(AbstractRitual ritual) {
        RitualRegistry.registerRitual(ritual);
        registeredRituals.add(ritual);
    }

    public static void register(AbstractSpellPart spellPart) {
        GlyphRegistry.registerSpell(spellPart);
        registeredSpells.add(spellPart);
    }

    public static void registerFamiliars() {
        
    }

    public static void registerPerks() {
        // Perks
        PerkRegistry.registerPerk(SixeyesPerk.INSTANCE);
        PerkRegistry.registerPerk(CloudStepsPerk.INSTANCE);
        PerkRegistry.registerPerk(ImmortalPerk.INSTANCE);
        PerkRegistry.registerPerk(DraconicHexPerk.INSTANCE);
        PerkRegistry.registerPerk(AdrenalinePerk.INSTANCE);
        PerkRegistry.registerPerk(InvinciblePerk.INSTANCE);
    }
    
    private static void addPerkSlots() {

        // Cade Armor
        PerkRegistry.registerPerkProvider(CADE_BOOTS.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE)));
        PerkRegistry.registerPerkProvider(CADE_LEGGINGS.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(CADE_ROBES.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(CADE_HOOD.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE)));
        PerkRegistry.registerPerkProvider(CADE_BOOTS_A.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(CADE_LEGGINGS_A.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(CADE_ROBES_A.get(), makePerkList(Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(CADE_HOOD_A.get(), makePerkList(Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.ONE)));

        // Ryan Armor
        PerkRegistry.registerPerkProvider(RYAN_BOOTS.get(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(RYAN_LEGGINGS.get(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(RYAN_ROBES.get(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(RYAN_HOOD.get(), makePerkList(Arrays.asList(PerkSlot.ONE, PerkSlot.ONE, PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(RYAN_BOOTS_A.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE)));
        PerkRegistry.registerPerkProvider(RYAN_LEGGINGS_A.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(RYAN_ROBES_A.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(RYAN_HOOD_A.get(), makePerkList(Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.ONE)));

        // Nick Armor
        PerkRegistry.registerPerkProvider(NICK_BOOTS.get(), makePerkList(Arrays.asList(PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(NICK_LEGGINGS.get(), makePerkList(Arrays.asList(PerkSlot.TWO, PerkSlot.ONE)));
        PerkRegistry.registerPerkProvider(NICK_ROBES.get(), makePerkList(Arrays.asList(PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(NICK_HOOD.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.ONE)));
        PerkRegistry.registerPerkProvider(NICK_BOOTS_A.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.ONE)));
        PerkRegistry.registerPerkProvider(NICK_LEGGINGS_A.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.ONE)));
        PerkRegistry.registerPerkProvider(NICK_ROBES_A.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.ONE)));
        PerkRegistry.registerPerkProvider(NICK_HOOD_A.get(), makePerkList(Arrays.asList(APerkSlot.FOUR, PerkSlot.ONE)));

        // Cam Armor
        PerkRegistry.registerPerkProvider(CAMR_BOOTS.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(CAMR_LEGGINGS.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(CAMR_ROBES.get(), makePerkList(Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(CAMR_HOOD.get(), makePerkList(Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.ONE)));
        PerkRegistry.registerPerkProvider(CAMR_BOOTS_A.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(CAMR_LEGGINGS_A.get(), makePerkList(Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.ONE)));
        PerkRegistry.registerPerkProvider(CAMR_ROBES_A.get(), makePerkList(Arrays.asList(APerkSlot.FIVE, PerkSlot.TWO, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(CAMR_HOOD_A.get(), makePerkList(Arrays.asList(APerkSlot.FIVE, PerkSlot.THREE, PerkSlot.ONE)));

        // Matt Armor
        PerkRegistry.registerPerkProvider(MATT_BOOTS.get(), makePerkList(Arrays.asList(PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(MATT_LEGGINGS.get(), makePerkList(Arrays.asList(PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(MATT_ROBES.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(MATT_HOOD.get(), makePerkList(Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(MATT_BOOTS_A.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(MATT_LEGGINGS_A.get(), makePerkList(Arrays.asList(PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(MATT_ROBES_A.get(), makePerkList(Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(MATT_HOOD_A.get(), makePerkList(Arrays.asList(APerkSlot.FIVE, PerkSlot.TWO)));

        // Adam Armor
        PerkRegistry.registerPerkProvider(ADAM_BOOTS.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE)));
        PerkRegistry.registerPerkProvider(ADAM_LEGGINGS.get(), makePerkList(Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE)));
        PerkRegistry.registerPerkProvider(ADAM_ROBES.get(), makePerkList(Arrays.asList(APerkSlot.FIVE, PerkSlot.TWO, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(ADAM_HOOD.get(), makePerkList(Arrays.asList(APerkSlot.FIVE, PerkSlot.THREE, PerkSlot.TWO)));
        PerkRegistry.registerPerkProvider(ADAM_BOOTS_A.get(), makePerkList(Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(ADAM_LEGGINGS_A.get(), makePerkList(Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(ADAM_ROBES_A.get(), makePerkList(Arrays.asList(APerkSlot.SIX, PerkSlot.THREE, PerkSlot.THREE)));
        PerkRegistry.registerPerkProvider(ADAM_HOOD_A.get(), makePerkList(Arrays.asList(APerkSlot.SIX, PerkSlot.THREE, PerkSlot.THREE)));

    }

    private static @NotNull List<List<PerkSlot>> makePerkList(List<PerkSlot> perkSlots) {
        return List.of(perkSlots, perkSlots, perkSlots, perkSlots);
    }

    public static void addAugments(){
        for(AbstractSpellPart part : GlyphRegistry.getSpellpartMap().values()){
            if(part.compatibleAugments.contains(AugmentAmplify.INSTANCE)&&!part.compatibleAugments.contains(AugmentAmplifyThree.INSTANCE)){
                part.compatibleAugments.add(AugmentAmplifyThree.INSTANCE);
            }
            if(part.compatibleAugments.contains(AugmentAmplify.INSTANCE)&&!part.compatibleAugments.contains(AugmentAmplifyTwo.INSTANCE)){
                part.compatibleAugments.add(AugmentAmplifyTwo.INSTANCE);
            }
            if(part.compatibleAugments.contains(AugmentAmplify.INSTANCE)&&!part.compatibleAugments.contains(AugmentDampenThree.INSTANCE)){
                part.compatibleAugments.add(AugmentDampenThree.INSTANCE);
            }
            if(part.compatibleAugments.contains(AugmentAmplify.INSTANCE)&&!part.compatibleAugments.contains(AugmentDampenTwo.INSTANCE)){
                part.compatibleAugments.add(AugmentDampenTwo.INSTANCE);
            }

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
            if(part.compatibleAugments.contains(AugmentExtendTime.INSTANCE)&&!part.compatibleAugments.contains(AugmentDurationDownThree.INSTANCE)){
                part.compatibleAugments.add(AugmentDurationDownThree.INSTANCE);
            }
            if(part.compatibleAugments.contains(AugmentExtendTime.INSTANCE)&&!part.compatibleAugments.contains(AugmentDurationDownTwo.INSTANCE)){
                part.compatibleAugments.add(AugmentDurationDownTwo.INSTANCE);
            }

            if(part.compatibleAugments.contains(AugmentAOE.INSTANCE)&&!part.compatibleAugments.contains(AugmentAOEThree.INSTANCE)){
                part.compatibleAugments.add(AugmentAOEThree.INSTANCE);
            }
            if(part.compatibleAugments.contains(AugmentAOE.INSTANCE)&&!part.compatibleAugments.contains(AugmentAOETwo.INSTANCE)){
                part.compatibleAugments.add(AugmentAOETwo.INSTANCE);
            }
            if(part.compatibleAugments.contains(AugmentAOE.INSTANCE)&&!part.compatibleAugments.contains(AugmentLesserAOE.INSTANCE)){
                part.compatibleAugments.add(AugmentLesserAOE.INSTANCE);
            }
        }
    }

    public static void postInit() {
        registerCasters();

        ArsNouveauRegistry.addAugments();
        ArsNouveauRegistry.addPerkSlots();

        ArsNouveauAPI.getInstance().getEnchantingRecipeTypes().add(ModRegistry.A_ARMOR_UP.get());
    }


    public static void registerRituals() {

        // Rituals
        registerRitual(new RitualMageSummon());
        registerRitual(new RitualTenShadows());

    }


}
