package adamsmods.adamsarsplus.registry;

import adamsmods.adamsarsplus.common.items.*;
import adamsmods.adamsarsplus.common.items.armor.MageMagicArmor;
import adamsmods.adamsarsplus.common.items.eyes.*;
import com.hollingsworth.arsnouveau.common.items.ModItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static adamsmods.adamsarsplus.AdamsArsPlus.MODID;

@SuppressWarnings("SameParameterValue")
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredHolder<Item, ModItem> DEBUG_ICON;

    public static final DeferredHolder<Item, ModItem> MANA_DIAMOND;
    public static final DeferredHolder<Item, ModItem> MAGE_CLOTH;

    public static final DeferredHolder<Item, ModItem> FLAME_SOUL;
    public static final DeferredHolder<Item, ModItem> FROST_SOUL;
    public static final DeferredHolder<Item, ModItem> EARTH_SOUL;
    public static final DeferredHolder<Item, ModItem> LIGHTNING_SOUL;
    public static final DeferredHolder<Item, ModItem> HERO_SOUL;
    public static final DeferredHolder<Item, ModItem> VOID_SOUL;

    public static final DeferredHolder<Item, ModItem> ELEMENTAL_SOUL;
    public static final DeferredHolder<Item, ModItem> TRUE_ELEMENTAL_SOUL;

    public static final DeferredHolder<Item, EnchantersStopwatch> ENCHANTERS_STOPWATCH;
    public static final DeferredHolder<Item, GeneralsWheel> GENERALS_WHEEL;
    public static final DeferredHolder<Item, MageTome> MAGE_TOME;
    public static final DeferredHolder<Item, EyeOfFlame> EYE_OF_FLAME;
    public static final DeferredHolder<Item, EyeOfFrost> EYE_OF_FROST;
    public static final DeferredHolder<Item, EyeOfEarth> EYE_OF_EARTH;
    public static final DeferredHolder<Item, EyeOfLightning> EYE_OF_LIGHTNING;
    public static final DeferredHolder<Item, EyeOfHoly> EYE_OF_HOLY;
    public static final DeferredHolder<Item, EyeOfVoid> EYE_OF_VOID;

    public static final DeferredHolder<Item, MageMagicArmor> CADE_BOOTS;
    public static final DeferredHolder<Item, MageMagicArmor> CADE_LEGGINGS;
    public static final DeferredHolder<Item, MageMagicArmor> CADE_ROBES;
    public static final DeferredHolder<Item, MageMagicArmor> CADE_HOOD;

    public static final DeferredHolder<Item, MageMagicArmor> CADE_BOOTS_A;
    public static final DeferredHolder<Item, MageMagicArmor> CADE_LEGGINGS_A;
    public static final DeferredHolder<Item, MageMagicArmor> CADE_ROBES_A;
    public static final DeferredHolder<Item, MageMagicArmor> CADE_HOOD_A;

    public static final DeferredHolder<Item, MageMagicArmor> RYAN_BOOTS;
    public static final DeferredHolder<Item, MageMagicArmor> RYAN_LEGGINGS;
    public static final DeferredHolder<Item, MageMagicArmor> RYAN_ROBES;
    public static final DeferredHolder<Item, MageMagicArmor> RYAN_HOOD;

    public static final DeferredHolder<Item, MageMagicArmor> RYAN_BOOTS_A;
    public static final DeferredHolder<Item, MageMagicArmor> RYAN_LEGGINGS_A;
    public static final DeferredHolder<Item, MageMagicArmor> RYAN_ROBES_A;
    public static final DeferredHolder<Item, MageMagicArmor> RYAN_HOOD_A;

    public static final DeferredHolder<Item, MageMagicArmor> NICK_BOOTS;
    public static final DeferredHolder<Item, MageMagicArmor> NICK_LEGGINGS;
    public static final DeferredHolder<Item, MageMagicArmor> NICK_ROBES;
    public static final DeferredHolder<Item, MageMagicArmor> NICK_HOOD;

    public static final DeferredHolder<Item, MageMagicArmor> NICK_BOOTS_A;
    public static final DeferredHolder<Item, MageMagicArmor> NICK_LEGGINGS_A;
    public static final DeferredHolder<Item, MageMagicArmor> NICK_ROBES_A;
    public static final DeferredHolder<Item, MageMagicArmor> NICK_HOOD_A;

    public static final DeferredHolder<Item, MageMagicArmor> CAMR_BOOTS;
    public static final DeferredHolder<Item, MageMagicArmor> CAMR_LEGGINGS;
    public static final DeferredHolder<Item, MageMagicArmor> CAMR_ROBES;
    public static final DeferredHolder<Item, MageMagicArmor> CAMR_HOOD;

    public static final DeferredHolder<Item, MageMagicArmor> CAMR_BOOTS_A;
    public static final DeferredHolder<Item, MageMagicArmor> CAMR_LEGGINGS_A;
    public static final DeferredHolder<Item, MageMagicArmor> CAMR_ROBES_A;
    public static final DeferredHolder<Item, MageMagicArmor> CAMR_HOOD_A;

    public static final DeferredHolder<Item, MageMagicArmor> MATT_BOOTS;
    public static final DeferredHolder<Item, MageMagicArmor> MATT_LEGGINGS;
    public static final DeferredHolder<Item, MageMagicArmor> MATT_ROBES;
    public static final DeferredHolder<Item, MageMagicArmor> MATT_HOOD;

    public static final DeferredHolder<Item, MageMagicArmor> MATT_BOOTS_A;
    public static final DeferredHolder<Item, MageMagicArmor> MATT_LEGGINGS_A;
    public static final DeferredHolder<Item, MageMagicArmor> MATT_ROBES_A;
    public static final DeferredHolder<Item, MageMagicArmor> MATT_HOOD_A;

    public static final DeferredHolder<Item, MageMagicArmor> ADAM_BOOTS;
    public static final DeferredHolder<Item, MageMagicArmor> ADAM_LEGGINGS;
    public static final DeferredHolder<Item, MageMagicArmor> ADAM_ROBES;
    public static final DeferredHolder<Item, MageMagicArmor> ADAM_HOOD;

    public static final DeferredHolder<Item, MageMagicArmor> ADAM_BOOTS_A;
    public static final DeferredHolder<Item, MageMagicArmor> ADAM_LEGGINGS_A;
    public static final DeferredHolder<Item, MageMagicArmor> ADAM_ROBES_A;
    public static final DeferredHolder<Item, MageMagicArmor> ADAM_HOOD_A;

    static {
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
        MAGE_TOME            = ITEMS.register("mage_tome", () -> new MageTome(MageTome.tomeTier, new Item.Properties().stacksTo(1).durability(10)));

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

        DEBUG_ICON = ITEMS.register("debug", () -> new ModItem(new Item.Properties()));
    }

}
