package adamsmods.adamsarsplus.registry;

import adamsmods.adamsarsplus.common.components.ModDataComponents;
import adamsmods.adamsarsplus.recipe.AArmorRecipe;
import com.hollingsworth.arsnouveau.setup.registry.CreativeTabRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static adamsmods.adamsarsplus.AdamsArsPlus.MODID;
import static adamsmods.adamsarsplus.AdamsArsPlus.prefix;
import static adamsmods.adamsarsplus.common.items.armor.Materials.A_MATERIALS;
import static adamsmods.adamsarsplus.registry.ModBlocks.BLOCKS;
import static adamsmods.adamsarsplus.registry.ModBlocks.BLOCK_ENTITIES;
import static adamsmods.adamsarsplus.registry.ModEntities.ENTITIES;
import static adamsmods.adamsarsplus.registry.ModItems.ITEMS;
import static adamsmods.adamsarsplus.registry.ModPotions.EFFECTS;
import static adamsmods.adamsarsplus.registry.ModPotions.POTIONS;

public class ModRegistry {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, MODID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<DataComponentType<?>> D_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MODID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, MODID);
    public static final DeferredRegister<BlockStateProviderType<?>> BS_PROVIDERS = DeferredRegister.create(Registries.BLOCK_STATE_PROVIDER_TYPE, MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, MODID);

    public static void registerRegistries(IEventBus bus) {
        A_MATERIALS.register(bus);
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
        ITEMS.register(bus);
        ENTITIES.register(bus);
        CONTAINERS.register(bus);
        EFFECTS.register(bus);
        POTIONS.register(bus);
        ENCHANTMENTS.register(bus);
        ATTRIBUTES.register(bus);
        RECIPES.register(bus);
        SERIALIZERS.register(bus);
        TABS.register(bus);
        ModDataComponents.register(bus);
        AdamCapabilityRegistry.ATTACHMENT_TYPES.register(bus);
        //bus.addListener(ModTiles::addBlocksToTiles);
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PLUS_TAB;

    public static final DeferredHolder<RecipeType<?>, RecipeType<AArmorRecipe>> A_ARMOR_UP;
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AArmorRecipe>> A_ARMOR_UP_SERIALIZER;

    static {

        A_ARMOR_UP              = RECIPES.register("a_armor_upgrade", () -> RecipeType.simple(prefix("a_armor_upgrade")));
        A_ARMOR_UP_SERIALIZER   = SERIALIZERS.register("a_armor_upgrade", AArmorRecipe.Serializer::new);

        PLUS_TAB = TABS.register("general", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.adamsarsplus"))
                .icon(() -> ModItems.DEBUG_ICON.get().getDefaultInstance())
                .displayItems((params, output) -> {
                    for (var entry : ITEMS.getEntries()) {
                        output.accept(entry.get().getDefaultInstance());
                    }
                }).withTabsBefore(CreativeTabRegistry.BLOCKS.getId())
                .build());
    }

    static <T> ResourceKey<T> key(ResourceKey<Registry<T>> registryResourceKey, String name) {
        return ResourceKey.create(registryResourceKey, prefix(name));
    }

}
