package com.adamsmods.adamsarsplus.datagen;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.lib.AdamsEntityTags;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.common.datagen.*;
import com.hollingsworth.arsnouveau.common.entity.*;
import com.hollingsworth.arsnouveau.common.lib.EntityTags;
import com.hollingsworth.arsnouveau.setup.registry.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AdamsEntityTagProvider extends EntityTypeTagsProvider {
    public AdamsEntityTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, AdamsArsPlus.MOD_ID, existingFileHelper);
    }

    protected void addTags() {
        this.tag(AdamsEntityTags.DOMAIN_BLACKLIST)
                .add(ModEntities.LIGHTNING_ENTITY.get(), ModEntities.LINGER_SPELL.get(), ModEntities.WALL_SPELL.get(), AdamsModEntities.DOMAIN_SPELL.get());

    }

    private static TagKey<EntityType<?>> create(ResourceLocation pName) {
        return TagKey.create(Registries.ENTITY_TYPE, pName);
    }
}