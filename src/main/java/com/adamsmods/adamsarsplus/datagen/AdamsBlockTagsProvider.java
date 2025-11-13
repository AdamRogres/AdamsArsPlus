package com.adamsmods.adamsarsplus.datagen;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.hollingsworth.arsnouveau.common.datagen.BlockTagProvider.BREAK_BLACKLIST;

public class AdamsBlockTagsProvider extends IntrinsicHolderTagsProvider<Block> {
    public AdamsBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.BLOCK, future, block -> block.builtInRegistryHolder().key(), AdamsArsPlus.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ModBlocks.FIRE_SOUL_BRICK_BLOCK.get(),
                ModBlocks.FIRE_SOUL_BRICK_SLAB.get(),
                ModBlocks.FIRE_SOUL_BRICK_STAIR.get(),

                ModBlocks.FROST_SOUL_BRICK_BLOCK.get(),
                ModBlocks.FROST_SOUL_BRICK_SLAB.get(),
                ModBlocks.FROST_SOUL_BRICK_STAIR.get(),

                ModBlocks.EARTH_SOUL_BRICK_BLOCK.get(),
                ModBlocks.EARTH_SOUL_BRICK_SLAB.get(),
                ModBlocks.EARTH_SOUL_BRICK_STAIR.get(),

                ModBlocks.LIGHTNING_SOUL_BRICK_BLOCK.get(),
                ModBlocks.LIGHTNING_SOUL_BRICK_SLAB.get(),
                ModBlocks.LIGHTNING_SOUL_BRICK_STAIR.get(),

                ModBlocks.HOLY_SOUL_BRICK_BLOCK.get(),
                ModBlocks.HOLY_SOUL_BRICK_SLAB.get(),
                ModBlocks.HOLY_SOUL_BRICK_STAIR.get(),

                ModBlocks.VOID_SOUL_BRICK_BLOCK.get(),
                ModBlocks.VOID_SOUL_BRICK_SLAB.get(),
                ModBlocks.VOID_SOUL_BRICK_STAIR.get(),

                ModBlocks.AUTO_TURRET_BLOCK.get()
        );
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
                ModBlocks.FIRE_SOUL_BRICK_BLOCK.get(),
                ModBlocks.FIRE_SOUL_BRICK_SLAB.get(),
                ModBlocks.FIRE_SOUL_BRICK_STAIR.get(),

                ModBlocks.FROST_SOUL_BRICK_BLOCK.get(),
                ModBlocks.FROST_SOUL_BRICK_SLAB.get(),
                ModBlocks.FROST_SOUL_BRICK_STAIR.get(),

                ModBlocks.EARTH_SOUL_BRICK_BLOCK.get(),
                ModBlocks.EARTH_SOUL_BRICK_SLAB.get(),
                ModBlocks.EARTH_SOUL_BRICK_STAIR.get(),

                ModBlocks.LIGHTNING_SOUL_BRICK_BLOCK.get(),
                ModBlocks.LIGHTNING_SOUL_BRICK_SLAB.get(),
                ModBlocks.LIGHTNING_SOUL_BRICK_STAIR.get(),

                ModBlocks.HOLY_SOUL_BRICK_BLOCK.get(),
                ModBlocks.HOLY_SOUL_BRICK_SLAB.get(),
                ModBlocks.HOLY_SOUL_BRICK_STAIR.get(),

                ModBlocks.VOID_SOUL_BRICK_BLOCK.get(),
                ModBlocks.VOID_SOUL_BRICK_SLAB.get(),
                ModBlocks.VOID_SOUL_BRICK_STAIR.get()
        );
        this.tag(BREAK_BLACKLIST).add(
                ModBlocks.DOMAIN_SHELL_BLOCK.get(),

                ModBlocks.FIRE_SOUL_BRICK_BLOCK.get(),
                ModBlocks.FIRE_SOUL_BRICK_SLAB.get(),
                ModBlocks.FIRE_SOUL_BRICK_STAIR.get(),

                ModBlocks.FROST_SOUL_BRICK_BLOCK.get(),
                ModBlocks.FROST_SOUL_BRICK_SLAB.get(),
                ModBlocks.FROST_SOUL_BRICK_STAIR.get(),

                ModBlocks.EARTH_SOUL_BRICK_BLOCK.get(),
                ModBlocks.EARTH_SOUL_BRICK_SLAB.get(),
                ModBlocks.EARTH_SOUL_BRICK_STAIR.get(),

                ModBlocks.LIGHTNING_SOUL_BRICK_BLOCK.get(),
                ModBlocks.LIGHTNING_SOUL_BRICK_SLAB.get(),
                ModBlocks.LIGHTNING_SOUL_BRICK_STAIR.get(),

                ModBlocks.HOLY_SOUL_BRICK_BLOCK.get(),
                ModBlocks.HOLY_SOUL_BRICK_SLAB.get(),
                ModBlocks.HOLY_SOUL_BRICK_STAIR.get(),

                ModBlocks.VOID_SOUL_BRICK_BLOCK.get(),
                ModBlocks.VOID_SOUL_BRICK_SLAB.get(),
                ModBlocks.VOID_SOUL_BRICK_STAIR.get()
        );
    }

    @Override
    public @NotNull String getName() {
        return "ArsPlus Block Tags";
    }
}
