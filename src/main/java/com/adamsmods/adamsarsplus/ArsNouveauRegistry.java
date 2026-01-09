package com.adamsmods.adamsarsplus;

import com.adamsmods.adamsarsplus.entities.effects.ManaExhaustEffect;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.*;
import com.adamsmods.adamsarsplus.entities.effects.*;

import com.adamsmods.adamsarsplus.glyphs.method_glyph.MethodDetonate;
import com.adamsmods.adamsarsplus.glyphs.method_glyph.PropagateDetonate;
import com.adamsmods.adamsarsplus.item.armor.MageArmorPerkHolder;
import com.adamsmods.adamsarsplus.perk.*;
import com.adamsmods.adamsarsplus.ritual.RitualMageSummon;
import com.adamsmods.adamsarsplus.ritual.RitualTenShadows;
import com.adamsmods.api.APerkSlot;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.perk.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.adamsmods.adamsarsplus.AdamsArsPlus.MOD_ID;
import static com.adamsmods.adamsarsplus.lib.AdamsLibPotions.*;
import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;
import static com.hollingsworth.arsnouveau.setup.registry.APIRegistry.registerRitual;

public class ArsNouveauRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);
    public static final RegistryObject<MobEffect> LIMITLESS_EFFECT = EFFECTS.register(LIMITLESS, LimitlessEffect::new);
    public static final RegistryObject<MobEffect> DOMAIN_BURNOUT_EFFECT = EFFECTS.register(DOMAIN_BURNOUT, DEburnoutEffect::new);
    public static final RegistryObject<MobEffect> MANA_EXHAUST_EFFECT = EFFECTS.register(MANA_EXHAUST, ManaExhaustEffect::new);
    public static final RegistryObject<MobEffect> SIMPLE_DOMAIN_EFFECT = EFFECTS.register(SIMPLE_DOMAIN, simpleDomainEffect::new);
    public static final RegistryObject<MobEffect> ERUPTION_EFFECT = EFFECTS.register(ERUPTION, eruptionEffect::new);
    public static final RegistryObject<MobEffect> FRACTURE_EFFECT = EFFECTS.register(FRACTURE, FractureEffect::new);
    public static final RegistryObject<MobEffect> SIX_EYES_EFFECT = EFFECTS.register(SIX_EYES, SixEyesEffect::new);
    public static final RegistryObject<MobEffect> MANA_HEALTH_EFFECT = EFFECTS.register(MANA_HEALTH, ManaHealthEffect::new);
    public static final RegistryObject<MobEffect> ICEBURST_EFFECT = EFFECTS.register(ICEBURST, IceBurstEffect::new);
    public static final RegistryObject<MobEffect> TENSHADOWS_EFFECT = EFFECTS.register(TENSHADOWS, TenShadowsEffect::new);
    public static final RegistryObject<MobEffect> DISRUPTION_EFFECT = EFFECTS.register(DISRUPTION, DisruptionEffect::new);
    public static final RegistryObject<MobEffect> SOUL_RIME_EFFECT = EFFECTS.register(SOUL_RIME, SoulRimeEffect::new);
    public static final RegistryObject<MobEffect> CLOUD_STEPS_EFFECT = EFFECTS.register(CLOUD_STEPS, CloudStepsEffect::new);
    public static final RegistryObject<MobEffect> LEAP_FATIGUE_EFFECT = EFFECTS.register(LEAP_FATIGUE, LeapFatigueEffect::new);
    public static final RegistryObject<MobEffect> FLAME_DEITY_EFFECT = EFFECTS.register(FLAME_DEITY, FlameDeityAuraEffect::new);
    public static final RegistryObject<MobEffect> MARKED_CREMATION_EFFECT = EFFECTS.register(MARKED_CREMATION, MarkedForCremationEffect::new);
    public static final RegistryObject<MobEffect> WALKING_BLIZZARD_EFFECT = EFFECTS.register(WALKING_BLIZZARD, WalkingBlizzardEffect::new);
    public static final RegistryObject<MobEffect> EARTHEN_HEART_EFFECT = EFFECTS.register(EARTHEN_HEART, EarthSetbonusEffect::new);
    public static final RegistryObject<MobEffect> LIGHTNING_STEPS_EFFECT = EFFECTS.register(LIGHTNING_STEPS, LightningStepsEffect::new);
    public static final RegistryObject<MobEffect> HOLY_LEGION_EFFECT = EFFECTS.register(HOLY_LEGION, HolyLegionEffect::new);
    public static final RegistryObject<MobEffect> ABYSSAL_DOMINATION_EFFECT = EFFECTS.register(ABYSSAL_DOMINATION, AbyssalDominationEffect::new);

    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>(); //this will come handy for datagen

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

        //Methods
        register(MethodDetonate.INSTANCE);
        register(PropagateDetonate.INSTANCE);

        // Rituals
        registerRitual(new RitualMageSummon());
        registerRitual(new RitualTenShadows());

        // Perks
        registerPerk(SixeyesPerk.INSTANCE);
        registerPerk(CloudStepsPerk.INSTANCE);
        registerPerk(ImmortalPerk.INSTANCE);
        registerPerk(DraconicHexPerk.INSTANCE);
        registerPerk(AdrenalinePerk.INSTANCE);
        registerPerk(InvinciblePerk.INSTANCE);

    }

    public static void registerSounds() {
    }

    public static void register(AbstractSpellPart spellPart) {
        GlyphRegistry.registerSpell(spellPart);
        registeredSpells.add(spellPart);
    }

    public static void registerPerks(){
        ArsNouveauAPI api = ArsNouveauAPI.getInstance();

        api.getEnchantingRecipeTypes().add((RecipeType) A_ARMOR_UP.get());

        // Cade Armor
        PerkRegistry.registerPerkProvider(CADE_BOOTS.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CADE_LEGGINGS.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(CADE_ROBES.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(CADE_HOOD.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CADE_BOOTS_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(CADE_LEGGINGS_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.TWO),
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(CADE_ROBES_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(CADE_HOOD_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.ONE)
        )));

        // Ryan Armor
        PerkRegistry.registerPerkProvider(RYAN_BOOTS.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(RYAN_LEGGINGS.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(RYAN_ROBES.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(RYAN_HOOD.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE, PerkSlot.ONE, PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE, PerkSlot.ONE, PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE, PerkSlot.ONE, PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(RYAN_BOOTS_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(RYAN_LEGGINGS_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(RYAN_ROBES_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.TWO),
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(RYAN_HOOD_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.ONE)
        )));

        // Nick Armor
        PerkRegistry.registerPerkProvider(NICK_BOOTS.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(NICK_LEGGINGS.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.TWO, PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(NICK_ROBES.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(NICK_HOOD.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(NICK_BOOTS_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(NICK_LEGGINGS_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(NICK_ROBES_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(NICK_HOOD_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FOUR, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.ONE)
        )));

        // Cam Armor
        PerkRegistry.registerPerkProvider(CAMR_BOOTS.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(CAMR_LEGGINGS.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(CAMR_ROBES.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(CAMR_HOOD.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO, PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CAMR_BOOTS_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.TWO),
                Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(CAMR_LEGGINGS_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CAMR_ROBES_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FIVE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FIVE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(APerkSlot.FIVE, PerkSlot.TWO, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(CAMR_HOOD_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FIVE, PerkSlot.THREE, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FIVE, PerkSlot.THREE, PerkSlot.ONE),
                Arrays.asList(APerkSlot.FIVE, PerkSlot.THREE, PerkSlot.ONE)
        )));

        // Matt Armor
        PerkRegistry.registerPerkProvider(MATT_BOOTS.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE),
                Arrays.asList(PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(MATT_LEGGINGS.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE),
                Arrays.asList(PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(MATT_ROBES.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(MATT_HOOD.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(MATT_BOOTS_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(MATT_LEGGINGS_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE),
                Arrays.asList(PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(MATT_ROBES_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(MATT_HOOD_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FIVE, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FIVE, PerkSlot.TWO),
                Arrays.asList(APerkSlot.FIVE, PerkSlot.TWO)
        )));

        // Adam Armor
        PerkRegistry.registerPerkProvider(ADAM_BOOTS.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(ADAM_LEGGINGS.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE),
                Arrays.asList(PerkSlot.THREE, PerkSlot.TWO, PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(ADAM_ROBES.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FIVE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FIVE, PerkSlot.TWO, PerkSlot.TWO),
                Arrays.asList(APerkSlot.FIVE, PerkSlot.TWO, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(ADAM_HOOD.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FIVE, PerkSlot.THREE, PerkSlot.TWO),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FIVE, PerkSlot.THREE, PerkSlot.TWO),
                Arrays.asList(APerkSlot.FIVE, PerkSlot.THREE, PerkSlot.TWO)
        )));
        PerkRegistry.registerPerkProvider(ADAM_BOOTS_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.THREE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(ADAM_LEGGINGS_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.THREE),
                Arrays.asList(APerkSlot.FOUR, PerkSlot.THREE, PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(ADAM_ROBES_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.SIX, PerkSlot.THREE, PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.SIX, PerkSlot.THREE, PerkSlot.THREE),
                Arrays.asList(APerkSlot.SIX, PerkSlot.THREE, PerkSlot.THREE)
        )));
        PerkRegistry.registerPerkProvider(ADAM_HOOD_A.get(), (stack) -> new MageArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(APerkSlot.SIX, PerkSlot.THREE, PerkSlot.THREE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(APerkSlot.SIX, PerkSlot.THREE, PerkSlot.THREE),
                Arrays.asList(APerkSlot.SIX, PerkSlot.THREE, PerkSlot.THREE)
        )));

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

}
