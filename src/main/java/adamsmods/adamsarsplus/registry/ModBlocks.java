package adamsmods.adamsarsplus.registry;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.client.blocks.AutoTurretRenderer;
import adamsmods.adamsarsplus.common.blocks.*;
import com.hollingsworth.arsnouveau.common.block.ModBlock;
import com.hollingsworth.arsnouveau.common.items.RendererBlockItem;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistryWrapper;
import com.hollingsworth.nuggets.common.registry.BlockEntityTypeRegistryWrapper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static adamsmods.adamsarsplus.registry.ModItems.ITEMS;
import static com.hollingsworth.arsnouveau.setup.registry.BlockRegistry.getDefaultBlockItem;
import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.defaultItemProperties;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, AdamsArsPlus.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, AdamsArsPlus.MODID);

    // Blocks ---------------------------------------------------
    public static final BlockRegistryWrapper<ModBlock> FIRE_SOUL_BRICK_BLOCK = registerBlockAndItem("fire_soul_brick_block", () -> new ModBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).lightLevel(new ToIntFunction<BlockState>() {
        @Override
        public int applyAsInt(BlockState value) {
            return 3;
        }
    })));
    public static final BlockRegistryWrapper<SlabBlock> FIRE_SOUL_BRICK_SLAB = registerBlockAndItem("fire_soul_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).lightLevel(new ToIntFunction<BlockState>() {
        @Override
        public int applyAsInt(BlockState value) {
            return 3;
        }
    })));
    public static final BlockRegistryWrapper<StairBlock> FIRE_SOUL_BRICK_STAIR = registerBlockAndItem("fire_soul_brick_stair", () -> new StairBlock(FIRE_SOUL_BRICK_BLOCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).lightLevel(new ToIntFunction<BlockState>() {
        @Override
        public int applyAsInt(BlockState value) {
            return 3;
        }
    })));

    public static final BlockRegistryWrapper<ModBlock> FROST_SOUL_BRICK_BLOCK = registerBlockAndItem("frost_soul_brick_block", () -> new ModBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().friction(0.94F).strength(50.0F, 1200.0F)));
    public static final BlockRegistryWrapper<SlabBlock> FROST_SOUL_BRICK_SLAB = registerBlockAndItem("frost_soul_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().friction(0.94F).strength(50.0F, 1200.0F)));
    public static final BlockRegistryWrapper<StairBlock> FROST_SOUL_BRICK_STAIR = registerBlockAndItem("frost_soul_brick_stair", () -> new StairBlock(FROST_SOUL_BRICK_BLOCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().friction(0.94F).strength(50.0F, 1200.0F)));

    public static final BlockRegistryWrapper<ModBlock> EARTH_SOUL_BRICK_BLOCK = registerBlockAndItem("earth_soul_brick_block", () -> new ModBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final BlockRegistryWrapper<SlabBlock> EARTH_SOUL_BRICK_SLAB = registerBlockAndItem("earth_soul_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final BlockRegistryWrapper<StairBlock> EARTH_SOUL_BRICK_STAIR = registerBlockAndItem("earth_soul_brick_stair", () -> new StairBlock(EARTH_SOUL_BRICK_BLOCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

    public static final BlockRegistryWrapper<ModBlock> LIGHTNING_SOUL_BRICK_BLOCK = registerBlockAndItem("lightning_soul_brick_block", () -> new ModBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final BlockRegistryWrapper<SlabBlock> LIGHTNING_SOUL_BRICK_SLAB = registerBlockAndItem("lightning_soul_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final BlockRegistryWrapper<StairBlock> LIGHTNING_SOUL_BRICK_STAIR = registerBlockAndItem("lightning_soul_brick_stair", () -> new StairBlock(LIGHTNING_SOUL_BRICK_BLOCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

    public static final BlockRegistryWrapper<ModBlock> HOLY_SOUL_BRICK_BLOCK = registerBlockAndItem("holy_soul_brick_block", () -> new ModBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final BlockRegistryWrapper<SlabBlock> HOLY_SOUL_BRICK_SLAB = registerBlockAndItem("holy_soul_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final BlockRegistryWrapper<StairBlock> HOLY_SOUL_BRICK_STAIR = registerBlockAndItem("holy_soul_brick_stair", () -> new StairBlock(HOLY_SOUL_BRICK_BLOCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

    public static final BlockRegistryWrapper<ModBlock> VOID_SOUL_BRICK_BLOCK = registerBlockAndItem("void_soul_brick_block", () -> new ModBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final BlockRegistryWrapper<SlabBlock> VOID_SOUL_BRICK_SLAB = registerBlockAndItem("void_soul_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));
    public static final BlockRegistryWrapper<StairBlock> VOID_SOUL_BRICK_STAIR = registerBlockAndItem("void_soul_brick_stair", () -> new StairBlock(VOID_SOUL_BRICK_BLOCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F)));

    // Other Blocks
    public static final BlockRegistryWrapper<DomainShell> DOMAIN_SHELL_BLOCK = registerBlockAndItem("domain_shell_block", () -> new DomainShell(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).sound(SoundType.GLASS)));
    public static BlockEntityTypeRegistryWrapper<DomainShellTile> DOMAIN_SHELL_BLOCK_TILE = registerTile("domain_shell", DomainShellTile::new, DOMAIN_SHELL_BLOCK);

    public static final BlockRegistryWrapper<AutoSpellTurret> AUTO_TURRET_BLOCK = registerBlockAndItem("auto_turret_block", AutoSpellTurret::new, (reg) -> new RendererBlockItem(reg, defaultItemProperties()) {
        @Override
        public Supplier<BlockEntityWithoutLevelRenderer> getRenderer() {
            return AutoTurretRenderer::getISTER;
        }
    }.withTooltip(Component.translatable("adamsarsplus.turret.tooltip")));
    public static final BlockEntityTypeRegistryWrapper<AutoTurretTile> AUTO_TURRET_BLOCK_TILE = registerTile("auto_turret", AutoTurretTile::new, AUTO_TURRET_BLOCK);

    // ----------------------------------------------------------

    public static <T extends Block> BlockRegistryWrapper<T> registerBlock(String name, Supplier<T> blockSupp) {
        return new BlockRegistryWrapper<>(BLOCKS.register(name, blockSupp));
    }

    public static <T extends Block> BlockRegistryWrapper<T> registerBlockAndItem(String name, Supplier<T> blockSupp) {
        BlockRegistryWrapper<T> blockReg = new BlockRegistryWrapper<>(BLOCKS.register(name, blockSupp));
        ITEMS.register(name, () -> getDefaultBlockItem(blockReg.get()));
        return blockReg;
    }

    public static <T extends Block> BlockRegistryWrapper<T> registerBlockAndItem(String name, Supplier<T> blockSupp, Function<BlockRegistryWrapper<T>, Item> blockItemFunc) {
        BlockRegistryWrapper<T> blockReg = new BlockRegistryWrapper<>(BLOCKS.register(name, blockSupp));
        ITEMS.register(name, () -> blockItemFunc.apply(blockReg));
        return blockReg;
    }

    public static <T extends BlockEntity> BlockEntityTypeRegistryWrapper<T> registerTile(String regName, BlockEntityType.BlockEntitySupplier<T> tile, BlockRegistryWrapper<? extends Block> block) {
        return new BlockEntityTypeRegistryWrapper<>(BLOCK_ENTITIES.register(regName, () -> BlockEntityType.Builder.of(tile, block.get()).build(null)));
    }

}
