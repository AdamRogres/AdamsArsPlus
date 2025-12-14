package com.adamsmods.adamsarsplus.block;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.block.tile.AutoTurretTile;
import com.adamsmods.adamsarsplus.block.tile.DomainShellTile;
import com.adamsmods.adamsarsplus.entities.client.AutoTurretRenderer;
import com.adamsmods.adamsarsplus.registry.ModRegistry;
import com.hollingsworth.arsnouveau.client.renderer.tile.BasicTurretRenderer;
import com.hollingsworth.arsnouveau.common.block.ModBlock;
import com.hollingsworth.arsnouveau.common.block.tile.MageBlockTile;
import com.hollingsworth.arsnouveau.common.items.RendererBlockItem;
import com.hollingsworth.arsnouveau.common.util.RegistryWrapper;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.mojang.datafixers.types.Type;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.hollingsworth.arsnouveau.setup.registry.BlockRegistry.getDefaultBlockItem;
import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.defaultItemProperties;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AdamsArsPlus.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AdamsArsPlus.MOD_ID);


    // Blocks ---------------------------------------------------
    public static final RegistryWrapper<ModBlock> FIRE_SOUL_BRICK_BLOCK = registerBlockAndItem("fire_soul_brick_block", () -> new ModBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).lightLevel(new ToIntFunction<BlockState>() {
        @Override
        public int applyAsInt(BlockState value) {
            return 3;
        }
    })));
    public static final RegistryWrapper<SlabBlock> FIRE_SOUL_BRICK_SLAB = registerBlockAndItem("fire_soul_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).lightLevel(new ToIntFunction<BlockState>() {
        @Override
        public int applyAsInt(BlockState value) {
            return 3;
        }
    })));
    public static final RegistryWrapper<StairBlock> FIRE_SOUL_BRICK_STAIR = registerBlockAndItem("fire_soul_brick_stair", () -> new StairBlock(() -> FIRE_SOUL_BRICK_BLOCK.defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).lightLevel(new ToIntFunction<BlockState>() {
        @Override
        public int applyAsInt(BlockState value) {
            return 3;
        }
    })));

    public static final RegistryWrapper<ModBlock> FROST_SOUL_BRICK_BLOCK = registerBlockAndItem("frost_soul_brick_block", () -> new ModBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().friction(0.94F).strength(50.0F, 1200.0F)));
    public static final RegistryWrapper<SlabBlock> FROST_SOUL_BRICK_SLAB = registerBlockAndItem("frost_soul_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().friction(0.94F).strength(50.0F, 1200.0F)));
    public static final RegistryWrapper<StairBlock> FROST_SOUL_BRICK_STAIR = registerBlockAndItem("frost_soul_brick_stair", () -> new StairBlock(() -> FROST_SOUL_BRICK_BLOCK.defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().friction(0.94F).strength(50.0F, 1200.0F)));

    public static final RegistryWrapper<ModBlock> EARTH_SOUL_BRICK_BLOCK = registerBlockAndItem("earth_soul_brick_block", () -> new ModBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final RegistryWrapper<SlabBlock> EARTH_SOUL_BRICK_SLAB = registerBlockAndItem("earth_soul_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final RegistryWrapper<StairBlock> EARTH_SOUL_BRICK_STAIR = registerBlockAndItem("earth_soul_brick_stair", () -> new StairBlock(() -> EARTH_SOUL_BRICK_BLOCK.defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

    public static final RegistryWrapper<ModBlock> LIGHTNING_SOUL_BRICK_BLOCK = registerBlockAndItem("lightning_soul_brick_block", () -> new ModBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final RegistryWrapper<SlabBlock> LIGHTNING_SOUL_BRICK_SLAB = registerBlockAndItem("lightning_soul_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final RegistryWrapper<StairBlock> LIGHTNING_SOUL_BRICK_STAIR = registerBlockAndItem("lightning_soul_brick_stair", () -> new StairBlock(() -> LIGHTNING_SOUL_BRICK_BLOCK.defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

    public static final RegistryWrapper<ModBlock> HOLY_SOUL_BRICK_BLOCK = registerBlockAndItem("holy_soul_brick_block", () -> new ModBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final RegistryWrapper<SlabBlock> HOLY_SOUL_BRICK_SLAB = registerBlockAndItem("holy_soul_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final RegistryWrapper<StairBlock> HOLY_SOUL_BRICK_STAIR = registerBlockAndItem("holy_soul_brick_stair", () -> new StairBlock(() -> HOLY_SOUL_BRICK_BLOCK.defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

    public static final RegistryWrapper<ModBlock> VOID_SOUL_BRICK_BLOCK = registerBlockAndItem("void_soul_brick_block", () -> new ModBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final RegistryWrapper<SlabBlock> VOID_SOUL_BRICK_SLAB = registerBlockAndItem("void_soul_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final RegistryWrapper<StairBlock> VOID_SOUL_BRICK_STAIR = registerBlockAndItem("void_soul_brick_stair", () -> new StairBlock(() -> VOID_SOUL_BRICK_BLOCK.defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

    // Other Blocks
    public static final RegistryWrapper<DomainShell> DOMAIN_SHELL_BLOCK = registerBlockAndItem("domain_shell_block", () -> new DomainShell(BlockBehaviour.Properties.copy(Blocks.GLASS).mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).sound(SoundType.GLASS)));
    public static RegistryWrapper<BlockEntityType<DomainShellTile>> DOMAIN_SHELL_BLOCK_TILE = registerTile("domain_shell", DomainShellTile::new, DOMAIN_SHELL_BLOCK);

    public static final RegistryWrapper<AutoSpellTurret> AUTO_TURRET_BLOCK = registerBlockAndItem("auto_turret_block", AutoSpellTurret::new, (reg) -> new RendererBlockItem(reg, defaultItemProperties()) {
        @Override
        public Supplier<BlockEntityWithoutLevelRenderer> getRenderer() {
            return AutoTurretRenderer::getISTER;
        }
    }.withTooltip(Component.translatable("adamsarsplus.turret.tooltip")));
    public static RegistryWrapper<BlockEntityType<AutoTurretTile>> AUTO_TURRET_BLOCK_TILE = registerTile("auto_turret", AutoTurretTile::new, AUTO_TURRET_BLOCK);

    // ----------------------------------------------------------

    public static <T extends Block> RegistryWrapper<T> registerBlock(String name, Supplier<T> blockSupp) {
        return new RegistryWrapper(BLOCKS.register(name, blockSupp));
    }

    public static <T extends BlockEntityType> RegistryWrapper<T> registerTile(String regName, BlockEntityType.BlockEntitySupplier tile, RegistryWrapper<? extends Block> block) {
        return new RegistryWrapper(BLOCK_ENTITIES.register(regName, () -> BlockEntityType.Builder.of(tile, new Block[]{(Block)block.get()}).build((Type)null)));
    }

    public static <T extends Block> RegistryWrapper<T> registerBlockAndItem(String name, Supplier<T> blockSupp) {
        RegistryWrapper<T> blockReg = new RegistryWrapper(BLOCKS.register(name, blockSupp));
        ModRegistry.ITEMS.register(name, () -> getDefaultBlockItem((Block)blockReg.get()));
        return blockReg;
    }

    public static <T extends Block> RegistryWrapper<T> registerBlockAndItem(String name, Supplier<T> blockSupp, Function<RegistryWrapper<T>, Item> blockItemFunc) {
        RegistryWrapper<T> blockReg = new RegistryWrapper(BLOCKS.register(name, blockSupp));
        ModRegistry.ITEMS.register(name, () -> (Item)blockItemFunc.apply(blockReg));
        return blockReg;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
