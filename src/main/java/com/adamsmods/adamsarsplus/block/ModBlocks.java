package com.adamsmods.adamsarsplus.block;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.block.tile.DomainShellTile;
import com.adamsmods.adamsarsplus.registry.ModRegistry;
import com.hollingsworth.arsnouveau.common.block.tile.MageBlockTile;
import com.hollingsworth.arsnouveau.common.util.RegistryWrapper;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.mojang.datafixers.types.Type;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.hollingsworth.arsnouveau.setup.registry.BlockRegistry.getDefaultBlockItem;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AdamsArsPlus.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AdamsArsPlus.MOD_ID);


    // Blocks ---------------------------------------------------
    public static final RegistryWrapper<DomainShell> DOMAIN_SHELL_BLOCK
            = registerBlockAndItem("domain_shell_block", () -> new DomainShell(BlockBehaviour.Properties.copy(Blocks.GLASS).mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).sound(SoundType.GLASS)));

    public static RegistryWrapper<BlockEntityType<DomainShellTile>> DOMAIN_SHELL_BLOCK_TILE
            = registerTile("domain_shell", DomainShellTile::new, DOMAIN_SHELL_BLOCK);



    // ----------------------------------------------------------


    public static <T extends Block> RegistryWrapper<T> registerBlock(String name, Supplier<T> blockSupp) {
        return new RegistryWrapper(BLOCKS.register(name, blockSupp));
    }

    public static <T extends BlockEntityType> RegistryWrapper<T> registerTile(String regName, BlockEntityType.BlockEntitySupplier tile, RegistryWrapper<? extends Block> block) {
        return new RegistryWrapper(BLOCK_ENTITIES.register(regName, () -> BlockEntityType.Builder.of(tile, new Block[]{(Block)block.get()}).build((Type)null)));
    }

    public static <T extends Block> RegistryWrapper<T> registerBlockAndItem(String name, Supplier<T> blockSupp) {
        RegistryWrapper<T> blockReg = new RegistryWrapper(BLOCKS.register(name, blockSupp));
        ItemsRegistry.ITEMS.register(name, () -> getDefaultBlockItem((Block)blockReg.get()));
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
