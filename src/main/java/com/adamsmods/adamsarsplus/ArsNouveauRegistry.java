package com.adamsmods.adamsarsplus;

import com.adamsmods.adamsarsplus.entities.effects.ManaExhaustEffect;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.*;
import com.adamsmods.adamsarsplus.entities.effects.*;

import com.adamsmods.adamsarsplus.glyphs.method_glyph.MethodDetonate;
import com.adamsmods.adamsarsplus.glyphs.method_glyph.PropagateDetonate;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.perk.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.hollingsworth.arsnouveau.setup.registry.RecipeRegistry;
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
import static com.adamsmods.adamsarsplus.recipe.jei.JEIArsplusPlugin.A_ARMOR_RECIPE_TYPE;
import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;

public class ArsNouveauRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);
    public static final RegistryObject<MobEffect> LIMITLESS_EFFECT = EFFECTS.register(LIMITLESS, LimitlessEffect::new);
    public static final RegistryObject<MobEffect> DOMAIN_BURNOUT_EFFECT = EFFECTS.register(DOMAIN_BURNOUT, DEburnoutEffect::new);
    public static final RegistryObject<MobEffect> MANA_EXHAUST_EFFECT = EFFECTS.register(MANA_EXHAUST, ManaExhaustEffect::new);
    public static final RegistryObject<MobEffect> SIMPLE_DOMAIN_EFFECT = EFFECTS.register(SIMPLE_DOMAIN, simpleDomainEffect::new);
    public static final RegistryObject<MobEffect> ERUPTION_EFFECT = EFFECTS.register(ERUPTION, eruptionEffect::new);


    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>(); //this will come handy for datagen

    public static void registerGlyphs() {
        //Augments
        register(AugmentAccelerateThree.INSTANCE);
        register(AugmentAccelerateTwo.INSTANCE);
        register(AugmentAOEThree.INSTANCE);
        register(AugmentAOETwo.INSTANCE);
        register(AugmentExtendTimeThree.INSTANCE);
        register(AugmentExtendTimeTwo.INSTANCE);
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

        //Methods
        register(MethodDetonate.INSTANCE);
        register(PropagateDetonate.INSTANCE);

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
        PerkRegistry.registerPerkProvider(CADE_BOOTS.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CADE_LEGGINGS.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CADE_ROBES.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CADE_HOOD.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CADE_BOOTS_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CADE_LEGGINGS_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CADE_ROBES_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CADE_HOOD_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));

        // Ryan Armor
        PerkRegistry.registerPerkProvider(RYAN_BOOTS.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(RYAN_LEGGINGS.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(RYAN_ROBES.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(RYAN_HOOD.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(RYAN_BOOTS_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(RYAN_LEGGINGS_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(RYAN_ROBES_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(RYAN_HOOD_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));

        // Nick Armor
        PerkRegistry.registerPerkProvider(NICK_BOOTS.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(NICK_LEGGINGS.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(NICK_ROBES.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(NICK_HOOD.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(NICK_BOOTS_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(NICK_LEGGINGS_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(NICK_ROBES_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(NICK_HOOD_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));

        // Cam Armor
        PerkRegistry.registerPerkProvider(CAMR_BOOTS.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CAMR_LEGGINGS.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CAMR_ROBES.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CAMR_HOOD.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CAMR_BOOTS_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CAMR_LEGGINGS_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CAMR_ROBES_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(CAMR_HOOD_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));

        // Matt Armor
        PerkRegistry.registerPerkProvider(MATT_BOOTS.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(MATT_LEGGINGS.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(MATT_ROBES.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(MATT_HOOD.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(MATT_BOOTS_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(MATT_LEGGINGS_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(MATT_ROBES_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(MATT_HOOD_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));

        // Adam Armor
        PerkRegistry.registerPerkProvider(ADAM_BOOTS.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(ADAM_LEGGINGS.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(ADAM_ROBES.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(ADAM_HOOD.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(ADAM_BOOTS_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(ADAM_LEGGINGS_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(ADAM_ROBES_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));
        PerkRegistry.registerPerkProvider(ADAM_HOOD_A.get(), (stack) -> new ArmorPerkHolder(stack, Arrays.asList(
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE),
                Arrays.asList(PerkSlot.ONE)
        )));

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
