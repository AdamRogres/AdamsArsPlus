package com.adamsmods.adamsarsplus.datagen;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.lib.AdamsEntityTags;
import com.hollingsworth.arsnouveau.common.lib.EntityTags;
import com.hollingsworth.arsnouveau.setup.registry.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
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

    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(AdamsEntityTags.DOMAIN_BLACKLIST).add(   
                ModEntities.LIGHTNING_ENTITY.get(), 
                ModEntities.LINGER_SPELL.get(), 
                ModEntities.WALL_SPELL.get(), 
                AdamsModEntities.DOMAIN_SPELL.get(),
                AdamsModEntities.FIRE_ENTITY.get(),
                AdamsModEntities.METEOR_SPELL.get());

        this.tag(Tags.EntityTypes.BOSSES).add(
                AdamsModEntities.RYAN_ENTITY.get(), 
                AdamsModEntities.CADE_ENTITY.get(), 
                AdamsModEntities.NICK_ENTITY.get(), 
                AdamsModEntities.CAM_ENTITY.get(), 
                AdamsModEntities.MATT_ENTITY.get(), 
                AdamsModEntities.ADAM_ENTITY.get());

        this.tag(EntityTags.JAR_BLACKLIST).add(
                AdamsModEntities.RYAN_ENTITY.get(), 
                AdamsModEntities.CADE_ENTITY.get(), 
                AdamsModEntities.NICK_ENTITY.get(), 
                AdamsModEntities.CAM_ENTITY.get(), 
                AdamsModEntities.MATT_ENTITY.get(), 
                AdamsModEntities.ADAM_ENTITY.get());

        this.tag(EntityTags.DISINTEGRATION_BLACKLIST).add(
                AdamsModEntities.RYAN_ENTITY.get(), 
                AdamsModEntities.CADE_ENTITY.get(), 
                AdamsModEntities.NICK_ENTITY.get(), 
                AdamsModEntities.CAM_ENTITY.get(), 
                AdamsModEntities.MATT_ENTITY.get(), 
                AdamsModEntities.ADAM_ENTITY.get());

        this.tag(EntityTags.MAGIC_FIND).add(
                AdamsModEntities.RYAN_ENTITY.get(), 
                AdamsModEntities.CADE_ENTITY.get(), 
                AdamsModEntities.NICK_ENTITY.get(), 
                AdamsModEntities.CAM_ENTITY.get(), 
                AdamsModEntities.MATT_ENTITY.get(), 
                AdamsModEntities.ADAM_ENTITY.get(), 
                AdamsModEntities.MAGE_ENTITY.get(),
                AdamsModEntities.MAGE_KNIGHT.get(),
                AdamsModEntities.FLAME_MAGE_ENTITY.get(),
                AdamsModEntities.FLAME_KNIGHT.get(),
                AdamsModEntities.FROST_MAGE_ENTITY.get(),
                AdamsModEntities.FROST_KNIGHT.get(),
                AdamsModEntities.EARTH_MAGE_ENTITY.get(),
                AdamsModEntities.EARTH_KNIGHT.get(),
                AdamsModEntities.LIGHTNING_MAGE_ENTITY.get(),
                AdamsModEntities.LIGHTNING_KNIGHT.get(),
                AdamsModEntities.HOLY_MAGE_ENTITY.get(),
                AdamsModEntities.HOLY_KNIGHT.get(),
                AdamsModEntities.VOID_MAGE_ENTITY.get(),
                AdamsModEntities.VOID_KNIGHT.get());

        this.tag(EntityTags.HOSTILE_MOBS).add(
                AdamsModEntities.MAGE_ENTITY.get(),
                AdamsModEntities.MAGE_KNIGHT.get(),
                AdamsModEntities.FLAME_MAGE_ENTITY.get(),
                AdamsModEntities.FLAME_KNIGHT.get(),
                AdamsModEntities.FROST_MAGE_ENTITY.get(),
                AdamsModEntities.FROST_KNIGHT.get(),
                AdamsModEntities.EARTH_MAGE_ENTITY.get(),
                AdamsModEntities.EARTH_KNIGHT.get(),
                AdamsModEntities.LIGHTNING_MAGE_ENTITY.get(),
                AdamsModEntities.LIGHTNING_KNIGHT.get(),
                AdamsModEntities.HOLY_MAGE_ENTITY.get(),
                AdamsModEntities.HOLY_KNIGHT.get(),
                AdamsModEntities.VOID_MAGE_ENTITY.get(),
                AdamsModEntities.VOID_KNIGHT.get());

        this.tag(EntityTags.LINGERING_BLACKLIST).add(
                AdamsModEntities.DOMAIN_SPELL.get(),
                AdamsModEntities.FIRE_ENTITY.get());

    }

    private static TagKey<EntityType<?>> create(ResourceLocation pName) {
        return TagKey.create(Registries.ENTITY_TYPE, pName);
    }
}