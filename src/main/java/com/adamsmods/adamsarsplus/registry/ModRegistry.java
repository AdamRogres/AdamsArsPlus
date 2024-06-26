package com.adamsmods.adamsarsplus.registry;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.entities.effects.DEburnoutEffect;
import com.adamsmods.adamsarsplus.item.MagicItems;
import com.adamsmods.adamsarsplus.item.RegularItems;
import com.hollingsworth.arsnouveau.api.perk.IPerk;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.sound.SpellSound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.adamsmods.adamsarsplus.lib.AdamsLibPotions.DOMAIN_BURNOUT;


public class ModRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdamsArsPlus.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AdamsArsPlus.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AdamsArsPlus.MOD_ID);

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, AdamsArsPlus.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AdamsArsPlus.MOD_ID);
    public static final DeferredRegister<Attribute> PERKS = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, AdamsArsPlus.MOD_ID);


    public static void registerRegistries(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        SOUNDS.register(bus);
        ENTITIES.register(bus);
        EFFECTS.register(bus);
        AdamsModEntities.ENTITIES.register(bus);
    }

    //    public static final RegistryObject<Item> EXAMPLE;
    public static final RegistryObject<Item> MANA_DIAMOND;


    public static ResourceLocation getGlyphName(String name) {
        return new ResourceLocation(AdamsArsPlus.MOD_ID, ("glyph_" + name));
    }

    public static final RegistryObject<MobEffect> DOMAIN_BURNOUT_EFFECT = EFFECTS.register(DOMAIN_BURNOUT, DEburnoutEffect::new);

    public static void registerPerk(IPerk perk) {
        PerkRegistry.registerPerk(perk);
    }

    public static SpellSound EXAMPLE_SPELL_SOUND;

    static {
        MANA_DIAMOND = ITEMS.register("mana_diamond", () -> new RegularItems(new Item.Properties().stacksTo(64).fireResistant(), true));

    }
}
