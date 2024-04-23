package com.adamsmods.adamsarsplus.lib;

import com.hollingsworth.arsnouveau.common.datagen.EntityTagProvider;
import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.ArsNouveau;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class AdamsEntityTags {
     public static final TagKey<EntityType<?>> DOMAIN_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(AdamsArsPlus.MOD_ID, "domain_blacklist"));
    }