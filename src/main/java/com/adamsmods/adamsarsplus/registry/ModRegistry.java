package com.adamsmods.adamsarsplus.registry;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.block.ModBlocks;
import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.entities.effects.*;
import com.adamsmods.adamsarsplus.item.*;
import com.adamsmods.adamsarsplus.block.ModBlocks.*;
import com.adamsmods.adamsarsplus.item.armor.MageMagicArmor;
import com.adamsmods.adamsarsplus.item.eyes.*;
import com.adamsmods.adamsarsplus.recipe.jei.AArmorRecipe;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.ArmorUpgradeRecipe;
import com.hollingsworth.arsnouveau.api.perk.IPerk;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.sound.SpellSound;
import com.hollingsworth.arsnouveau.common.items.ModItem;
import com.hollingsworth.arsnouveau.common.potions.PublicEffect;
import com.hollingsworth.arsnouveau.common.world.processors.WaterloggingFixProcessor;
import com.hollingsworth.arsnouveau.common.world.structure.WildenDen;
import com.hollingsworth.arsnouveau.common.world.structure.WildenGuardianDen;
import com.hollingsworth.arsnouveau.setup.registry.RecipeRegistry;
import com.hollingsworth.arsnouveau.setup.registry.StructureRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;


import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.adamsmods.adamsarsplus.AdamsArsPlus.MOD_ID;
import static com.adamsmods.adamsarsplus.AdamsArsPlus.prefix;
import static com.adamsmods.adamsarsplus.item.armor.MageMagicArmor.cade;
import static com.adamsmods.adamsarsplus.lib.AdamsLibPotions.*;
import static com.hollingsworth.arsnouveau.setup.registry.BlockRegistry.registerBlock;
import static com.hollingsworth.arsnouveau.setup.registry.RecipeRegistry.RECIPE_TYPES;


public class ModRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdamsArsPlus.MOD_ID);

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AdamsArsPlus.MOD_ID);

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, AdamsArsPlus.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AdamsArsPlus.MOD_ID);
    public static final DeferredRegister<Attribute> PERKS = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, AdamsArsPlus.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, AdamsArsPlus.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AdamsArsPlus.MOD_ID);

    public static final DeferredRegister<StructureType<?>> STRUCTURES;
    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSOR;


    public static void registerRegistries(IEventBus bus) {
      //  BLOCKS.register(bus);
        ITEMS.register(bus);
        SOUNDS.register(bus);
        ENTITIES.register(bus);
        EFFECTS.register(bus);

        STRUCTURES.register(bus);
        STRUCTURE_PROCESSOR.register(bus);

        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);

        ModBlocks.register(bus);

        AdamsModEntities.ENTITIES.register(bus);
    }

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS;
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES;

    public static final RegistryObject<RecipeType<AArmorRecipe>> A_ARMOR_UP;
    public static final RegistryObject<RecipeSerializer<AArmorRecipe>> A_ARMOR_UP_SERIALIZER;

    //    public static final RegistryObject<Item> EXAMPLE;
    public static final RegistryObject<ModItem> MANA_DIAMOND;
    public static final RegistryObject<ModItem> MAGE_CLOTH;

    public static final RegistryObject<ModItem> FLAME_SOUL;
    public static final RegistryObject<ModItem> FROST_SOUL;
    public static final RegistryObject<ModItem> EARTH_SOUL;
    public static final RegistryObject<ModItem> LIGHTNING_SOUL;
    public static final RegistryObject<ModItem> HERO_SOUL;
    public static final RegistryObject<ModItem> VOID_SOUL;

    public static final RegistryObject<ModItem> ELEMENTAL_SOUL;
    public static final RegistryObject<ModItem> TRUE_ELEMENTAL_SOUL;

    public static final RegistryObject<EnchantersStopwatch> ENCHANTERS_STOPWATCH;
    public static final RegistryObject<GeneralsWheel> GENERALS_WHEEL;
    public static final RegistryObject<MageTome> MAGE_TOME;
    public static final RegistryObject<EyeOfFlame> EYE_OF_FLAME;
    public static final RegistryObject<EyeOfFrost> EYE_OF_FROST;
    public static final RegistryObject<EyeOfEarth> EYE_OF_EARTH;
    public static final RegistryObject<EyeOfLightning> EYE_OF_LIGHTNING;
    public static final RegistryObject<EyeOfHoly> EYE_OF_HOLY;
    public static final RegistryObject<EyeOfVoid> EYE_OF_VOID;

    public static final RegistryObject<MageMagicArmor> CADE_BOOTS;
    public static final RegistryObject<MageMagicArmor> CADE_LEGGINGS;
    public static final RegistryObject<MageMagicArmor> CADE_ROBES;
    public static final RegistryObject<MageMagicArmor> CADE_HOOD;

    public static final RegistryObject<MageMagicArmor> CADE_BOOTS_A;
    public static final RegistryObject<MageMagicArmor> CADE_LEGGINGS_A;
    public static final RegistryObject<MageMagicArmor> CADE_ROBES_A;
    public static final RegistryObject<MageMagicArmor> CADE_HOOD_A;

    public static final RegistryObject<MageMagicArmor> RYAN_BOOTS;
    public static final RegistryObject<MageMagicArmor> RYAN_LEGGINGS;
    public static final RegistryObject<MageMagicArmor> RYAN_ROBES;
    public static final RegistryObject<MageMagicArmor> RYAN_HOOD;

    public static final RegistryObject<MageMagicArmor> RYAN_BOOTS_A;
    public static final RegistryObject<MageMagicArmor> RYAN_LEGGINGS_A;
    public static final RegistryObject<MageMagicArmor> RYAN_ROBES_A;
    public static final RegistryObject<MageMagicArmor> RYAN_HOOD_A;

    public static final RegistryObject<MageMagicArmor> NICK_BOOTS;
    public static final RegistryObject<MageMagicArmor> NICK_LEGGINGS;
    public static final RegistryObject<MageMagicArmor> NICK_ROBES;
    public static final RegistryObject<MageMagicArmor> NICK_HOOD;

    public static final RegistryObject<MageMagicArmor> NICK_BOOTS_A;
    public static final RegistryObject<MageMagicArmor> NICK_LEGGINGS_A;
    public static final RegistryObject<MageMagicArmor> NICK_ROBES_A;
    public static final RegistryObject<MageMagicArmor> NICK_HOOD_A;

    public static final RegistryObject<MageMagicArmor> CAMR_BOOTS;
    public static final RegistryObject<MageMagicArmor> CAMR_LEGGINGS;
    public static final RegistryObject<MageMagicArmor> CAMR_ROBES;
    public static final RegistryObject<MageMagicArmor> CAMR_HOOD;

    public static final RegistryObject<MageMagicArmor> CAMR_BOOTS_A;
    public static final RegistryObject<MageMagicArmor> CAMR_LEGGINGS_A;
    public static final RegistryObject<MageMagicArmor> CAMR_ROBES_A;
    public static final RegistryObject<MageMagicArmor> CAMR_HOOD_A;

    public static final RegistryObject<MageMagicArmor> MATT_BOOTS;
    public static final RegistryObject<MageMagicArmor> MATT_LEGGINGS;
    public static final RegistryObject<MageMagicArmor> MATT_ROBES;
    public static final RegistryObject<MageMagicArmor> MATT_HOOD;

    public static final RegistryObject<MageMagicArmor> MATT_BOOTS_A;
    public static final RegistryObject<MageMagicArmor> MATT_LEGGINGS_A;
    public static final RegistryObject<MageMagicArmor> MATT_ROBES_A;
    public static final RegistryObject<MageMagicArmor> MATT_HOOD_A;

    public static final RegistryObject<MageMagicArmor> ADAM_BOOTS;
    public static final RegistryObject<MageMagicArmor> ADAM_LEGGINGS;
    public static final RegistryObject<MageMagicArmor> ADAM_ROBES;
    public static final RegistryObject<MageMagicArmor> ADAM_HOOD;

    public static final RegistryObject<MageMagicArmor> ADAM_BOOTS_A;
    public static final RegistryObject<MageMagicArmor> ADAM_LEGGINGS_A;
    public static final RegistryObject<MageMagicArmor> ADAM_ROBES_A;
    public static final RegistryObject<MageMagicArmor> ADAM_HOOD_A;

    // Blocks

    public static ResourceLocation getGlyphName(String name) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, ("glyph_" + name));
    }

    public static final RegistryObject<MobEffect> DOMAIN_BURNOUT_EFFECT = EFFECTS.register(DOMAIN_BURNOUT, DEburnoutEffect::new);
    public static final RegistryObject<MobEffect> LIMITLESS_EFFECT = EFFECTS.register(LIMITLESS, LimitlessEffect::new);
    public static final RegistryObject<MobEffect> MANA_EXHAUST_EFFECT = EFFECTS.register(MANA_EXHAUST, ManaExhaustEffect::new);
    public static final RegistryObject<MobEffect> SIMPLE_DOMAIN_EFFECT = EFFECTS.register(SIMPLE_DOMAIN, simpleDomainEffect::new);
    public static final RegistryObject<MobEffect> ERUPTION_EFFECT = EFFECTS.register(ERUPTION, eruptionEffect::new);
    public static final RegistryObject<MobEffect> FRACTURE_EFFECT = EFFECTS.register(FRACTURE, FractureEffect::new);
    public static final RegistryObject<MobEffect> SIX_EYES_EFFECT = EFFECTS.register(SIX_EYES, SixEyesEffect::new);
    public static final RegistryObject<MobEffect> MANA_HEALTH_EFFECT = EFFECTS.register(MANA_HEALTH, ManaHealthEffect::new);
    public static final RegistryObject<MobEffect> ICEBURST_EFFECT = EFFECTS.register(ICEBURST, IceBurstEffect::new);
    public static final RegistryObject<MobEffect> DISRUPTION_EFFECT = EFFECTS.register(DISRUPTION, DisruptionEffect::new);
    public static final RegistryObject<MobEffect> TENSHADOWS_EFFECT = EFFECTS.register(TENSHADOWS, TenShadowsEffect::new);
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

    public static void registerPerk(IPerk perk) {
        PerkRegistry.registerPerk(perk);
    }

    public static SpellSound EXAMPLE_SPELL_SOUND;

    static {
        RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.RECIPE_SERIALIZERS, MOD_ID);
        RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.Keys.RECIPE_TYPES, MOD_ID);

        // Regular Items
        MANA_DIAMOND        = ITEMS.register("mana_diamond",    () -> new RegularItems(new Item.Properties().stacksTo(64).fireResistant(), true));
        MAGE_CLOTH          = ITEMS.register("mage_cloth",      () -> new RegularItems(new Item.Properties().stacksTo(64).fireResistant(), false));

        FLAME_SOUL          = ITEMS.register("flame_soul",      () -> new RegularItems(new Item.Properties().stacksTo(64).fireResistant(), false));
        FROST_SOUL          = ITEMS.register("frost_soul",      () -> new RegularItems(new Item.Properties().stacksTo(64).fireResistant(), false));
        EARTH_SOUL          = ITEMS.register("earth_soul",      () -> new RegularItems(new Item.Properties().stacksTo(64).fireResistant(), false));
        LIGHTNING_SOUL      = ITEMS.register("lightning_soul",  () -> new RegularItems(new Item.Properties().stacksTo(64).fireResistant(), false));
        HERO_SOUL           = ITEMS.register("hero_soul",       () -> new RegularItems(new Item.Properties().stacksTo(64).fireResistant(), false));
        VOID_SOUL           = ITEMS.register("void_soul",       () -> new RegularItems(new Item.Properties().stacksTo(64).fireResistant(), false));

        ELEMENTAL_SOUL      = ITEMS.register("elemental_soul",  () -> new RegularItems(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.RARE), false));
        TRUE_ELEMENTAL_SOUL = ITEMS.register("true_elemental_soul",  () -> new RegularItems(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.RARE), false));

        // Other Items
        ENCHANTERS_STOPWATCH = ITEMS.register("enchanters_stopwatch", () -> new EnchantersStopwatch(new Item.Properties().stacksTo(1)));
        GENERALS_WHEEL       = ITEMS.register("generals_wheel", () -> new GeneralsWheel(new Item.Properties().stacksTo(1)));
        MAGE_TOME            = ITEMS.register("mage_tome", () -> new MageTome(MageTome.tomeTier, new Item.Properties().stacksTo(1).defaultDurability(10)));

        EYE_OF_FLAME        = ITEMS.register("eye_of_flame", () -> new EyeOfFlame(new Item.Properties().stacksTo(64)));
        EYE_OF_FROST        = ITEMS.register("eye_of_frost", () -> new EyeOfFrost(new Item.Properties().stacksTo(64)));
        EYE_OF_EARTH        = ITEMS.register("eye_of_earth", () -> new EyeOfEarth(new Item.Properties().stacksTo(64)));
        EYE_OF_LIGHTNING    = ITEMS.register("eye_of_lightning", () -> new EyeOfLightning(new Item.Properties().stacksTo(64)));
        EYE_OF_HOLY         = ITEMS.register("eye_of_holy", () -> new EyeOfHoly(new Item.Properties().stacksTo(64)));
        EYE_OF_VOID         = ITEMS.register("eye_of_void", () -> new EyeOfVoid(new Item.Properties().stacksTo(64)));

        // Armor Sets
        CADE_BOOTS          = ITEMS.register("cade_boots",      () -> MageMagicArmor.cade(ArmorItem.Type.BOOTS));
        CADE_LEGGINGS       = ITEMS.register("cade_leggings",   () -> MageMagicArmor.cade(ArmorItem.Type.LEGGINGS));
        CADE_ROBES          = ITEMS.register("cade_robes",      () -> MageMagicArmor.cade(ArmorItem.Type.CHESTPLATE));
        CADE_HOOD           = ITEMS.register("cade_hood",       () -> MageMagicArmor.cade(ArmorItem.Type.HELMET));

        CADE_BOOTS_A        = ITEMS.register("cade_boots_a",      () -> MageMagicArmor.cade(ArmorItem.Type.BOOTS));
        CADE_LEGGINGS_A     = ITEMS.register("cade_leggings_a",   () -> MageMagicArmor.cade(ArmorItem.Type.LEGGINGS));
        CADE_ROBES_A        = ITEMS.register("cade_robes_a",      () -> MageMagicArmor.cade(ArmorItem.Type.CHESTPLATE));
        CADE_HOOD_A         = ITEMS.register("cade_hood_a",       () -> MageMagicArmor.cade(ArmorItem.Type.HELMET));

        RYAN_BOOTS          = ITEMS.register("ryan_boots",      () -> MageMagicArmor.ryan(ArmorItem.Type.BOOTS));
        RYAN_LEGGINGS       = ITEMS.register("ryan_leggings",   () -> MageMagicArmor.ryan(ArmorItem.Type.LEGGINGS));
        RYAN_ROBES          = ITEMS.register("ryan_robes",      () -> MageMagicArmor.ryan(ArmorItem.Type.CHESTPLATE));
        RYAN_HOOD           = ITEMS.register("ryan_hood",       () -> MageMagicArmor.ryan(ArmorItem.Type.HELMET));

        RYAN_BOOTS_A        = ITEMS.register("ryan_boots_a",      () -> MageMagicArmor.ryan(ArmorItem.Type.BOOTS));
        RYAN_LEGGINGS_A     = ITEMS.register("ryan_leggings_a",   () -> MageMagicArmor.ryan(ArmorItem.Type.LEGGINGS));
        RYAN_ROBES_A        = ITEMS.register("ryan_robes_a",      () -> MageMagicArmor.ryan(ArmorItem.Type.CHESTPLATE));
        RYAN_HOOD_A         = ITEMS.register("ryan_hood_a",       () -> MageMagicArmor.ryan(ArmorItem.Type.HELMET));

        NICK_BOOTS          = ITEMS.register("nick_boots",      () -> MageMagicArmor.nick(ArmorItem.Type.BOOTS));
        NICK_LEGGINGS       = ITEMS.register("nick_leggings",   () -> MageMagicArmor.nick(ArmorItem.Type.LEGGINGS));
        NICK_ROBES          = ITEMS.register("nick_robes",      () -> MageMagicArmor.nick(ArmorItem.Type.CHESTPLATE));
        NICK_HOOD           = ITEMS.register("nick_hood",       () -> MageMagicArmor.nick(ArmorItem.Type.HELMET));

        NICK_BOOTS_A        = ITEMS.register("nick_boots_a",      () -> MageMagicArmor.nick(ArmorItem.Type.BOOTS));
        NICK_LEGGINGS_A     = ITEMS.register("nick_leggings_a",   () -> MageMagicArmor.nick(ArmorItem.Type.LEGGINGS));
        NICK_ROBES_A        = ITEMS.register("nick_robes_a",      () -> MageMagicArmor.nick(ArmorItem.Type.CHESTPLATE));
        NICK_HOOD_A         = ITEMS.register("nick_hood_a",       () -> MageMagicArmor.nick(ArmorItem.Type.HELMET));

        CAMR_BOOTS          = ITEMS.register("camr_boots",      () -> MageMagicArmor.camr(ArmorItem.Type.BOOTS));
        CAMR_LEGGINGS       = ITEMS.register("camr_leggings",   () -> MageMagicArmor.camr(ArmorItem.Type.LEGGINGS));
        CAMR_ROBES          = ITEMS.register("camr_robes",      () -> MageMagicArmor.camr(ArmorItem.Type.CHESTPLATE));
        CAMR_HOOD           = ITEMS.register("camr_hood",       () -> MageMagicArmor.camr(ArmorItem.Type.HELMET));

        CAMR_BOOTS_A        = ITEMS.register("camr_boots_a",      () -> MageMagicArmor.camr(ArmorItem.Type.BOOTS));
        CAMR_LEGGINGS_A     = ITEMS.register("camr_leggings_a",   () -> MageMagicArmor.camr(ArmorItem.Type.LEGGINGS));
        CAMR_ROBES_A        = ITEMS.register("camr_robes_a",      () -> MageMagicArmor.camr(ArmorItem.Type.CHESTPLATE));
        CAMR_HOOD_A         = ITEMS.register("camr_hood_a",       () -> MageMagicArmor.camr(ArmorItem.Type.HELMET));

        MATT_BOOTS          = ITEMS.register("matt_boots",      () -> MageMagicArmor.matt(ArmorItem.Type.BOOTS));
        MATT_LEGGINGS       = ITEMS.register("matt_leggings",   () -> MageMagicArmor.matt(ArmorItem.Type.LEGGINGS));
        MATT_ROBES          = ITEMS.register("matt_robes",      () -> MageMagicArmor.matt(ArmorItem.Type.CHESTPLATE));
        MATT_HOOD           = ITEMS.register("matt_hood",       () -> MageMagicArmor.matt(ArmorItem.Type.HELMET));

        MATT_BOOTS_A        = ITEMS.register("matt_boots_a",      () -> MageMagicArmor.matt(ArmorItem.Type.BOOTS));
        MATT_LEGGINGS_A     = ITEMS.register("matt_leggings_a",   () -> MageMagicArmor.matt(ArmorItem.Type.LEGGINGS));
        MATT_ROBES_A        = ITEMS.register("matt_robes_a",      () -> MageMagicArmor.matt(ArmorItem.Type.CHESTPLATE));
        MATT_HOOD_A         = ITEMS.register("matt_hood_a",       () -> MageMagicArmor.matt(ArmorItem.Type.HELMET));

        ADAM_BOOTS          = ITEMS.register("adam_boots",      () -> MageMagicArmor.adam(ArmorItem.Type.BOOTS));
        ADAM_LEGGINGS       = ITEMS.register("adam_leggings",   () -> MageMagicArmor.adam(ArmorItem.Type.LEGGINGS));
        ADAM_ROBES          = ITEMS.register("adam_robes",      () -> MageMagicArmor.adam(ArmorItem.Type.CHESTPLATE));
        ADAM_HOOD           = ITEMS.register("adam_hood",       () -> MageMagicArmor.adam(ArmorItem.Type.HELMET));

        ADAM_BOOTS_A        = ITEMS.register("adam_boots_a",      () -> MageMagicArmor.adam(ArmorItem.Type.BOOTS));
        ADAM_LEGGINGS_A     = ITEMS.register("adam_leggings_a",   () -> MageMagicArmor.adam(ArmorItem.Type.LEGGINGS));
        ADAM_ROBES_A        = ITEMS.register("adam_robes_a",      () -> MageMagicArmor.adam(ArmorItem.Type.CHESTPLATE));
        ADAM_HOOD_A         = ITEMS.register("adam_hood_a",       () -> MageMagicArmor.adam(ArmorItem.Type.HELMET));


        A_ARMOR_UP              = RECIPE_TYPES.register("a_armor_upgrade", () -> new ModRecipeType());
        A_ARMOR_UP_SERIALIZER   = RECIPE_SERIALIZERS.register("a_armor_upgrade", () -> new AArmorRecipe.Serializer());

        STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, MOD_ID);
        STRUCTURE_PROCESSOR = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, MOD_ID);

    }

    public static class ModRecipeType<T extends Recipe<?>> implements RecipeType<T> {
        @Override
        public String toString() {
            return BuiltInRegistries.RECIPE_TYPE.getKey(this).toString();
        }
    }

}
