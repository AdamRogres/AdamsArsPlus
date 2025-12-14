package com.adamsmods.adamsarsplus.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.adamsmods.adamsarsplus.AdamsArsPlus.MOD_ID;

public class AdamsStructureTagProvider extends TagsProvider<Structure> {
    public static TagKey<Structure> IP_TAG;
    public static final ResourceKey<Structure> IP;
    public static TagKey<Structure> FL_TAG;
    public static final ResourceKey<Structure> FL;
    public static TagKey<Structure> OB_TAG;
    public static final ResourceKey<Structure> OB;
    public static TagKey<Structure> NR_TAG;
    public static final ResourceKey<Structure> NR;
    public static TagKey<Structure> HM_TAG;
    public static final ResourceKey<Structure> HM;
    public static TagKey<Structure> VF_TAG;
    public static final ResourceKey<Structure> VF;

    public AdamsStructureTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.STRUCTURE, pProvider, MOD_ID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(IP_TAG).add(new ResourceKey[]{IP});
        this.tag(FL_TAG).add(new ResourceKey[]{FL});
        this.tag(OB_TAG).add(new ResourceKey[]{OB});
        this.tag(NR_TAG).add(new ResourceKey[]{NR});
        this.tag(HM_TAG).add(new ResourceKey[]{HM});
        this.tag(VF_TAG).add(new ResourceKey[]{VF});
    }

    public static ResourceKey<Structure> register(String name) {
        return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(MOD_ID, name));
    }

    static {
        IP_TAG = TagKey.create(Registries.STRUCTURE, new ResourceLocation(MOD_ID, "infernal_prison_tag"));
        IP = register("infernal_prison");
        FL_TAG = TagKey.create(Registries.STRUCTURE, new ResourceLocation(MOD_ID, "frozen_library_tag"));
        FL = register("frozen_library");
        OB_TAG = TagKey.create(Registries.STRUCTURE, new ResourceLocation(MOD_ID, "overgrown_barracks_tag"));
        OB = register("overgrown_barracks");
        NR_TAG = TagKey.create(Registries.STRUCTURE, new ResourceLocation(MOD_ID, "nimbostratic_ruins_tag"));
        NR = register("nimbostratic_ruins");
        HM_TAG = TagKey.create(Registries.STRUCTURE, new ResourceLocation(MOD_ID, "holy_mausoleum_tag"));
        HM = register("holy_mausoleum");
        VF_TAG = TagKey.create(Registries.STRUCTURE, new ResourceLocation(MOD_ID, "void_fortress_tag"));
        VF = register("void_fortress");
    }
}