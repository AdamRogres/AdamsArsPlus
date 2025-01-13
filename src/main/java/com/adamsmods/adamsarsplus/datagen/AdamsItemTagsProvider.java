package com.adamsmods.adamsarsplus.datagen;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.adamsmods.adamsarsplus.AdamsArsPlus.MOD_ID;
import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;

public class AdamsItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {


    public static final TagKey<Item> MAGIC_HOOD = ItemTags.create(new ResourceLocation(ArsNouveau.MODID, "hood"));
    public static final TagKey<Item> MAGIC_ROBE = ItemTags.create(new ResourceLocation(ArsNouveau.MODID, "robe"));
    public static final TagKey<Item> MAGIC_LEG  = ItemTags.create(new ResourceLocation(ArsNouveau.MODID, "legs"));
    public static final TagKey<Item> MAGIC_BOOT = ItemTags.create(new ResourceLocation(ArsNouveau.MODID, "boot"));

    public static final TagKey<Item> MAGE_HOOD_A = ItemTags.create(new ResourceLocation(AdamsArsPlus.MOD_ID, "hood_a"));
    public static final TagKey<Item> MAGE_HOOD_B = ItemTags.create(new ResourceLocation(AdamsArsPlus.MOD_ID, "hood_b"));
    public static final TagKey<Item> MAGE_ROBE_A = ItemTags.create(new ResourceLocation(AdamsArsPlus.MOD_ID, "robe_a"));
    public static final TagKey<Item> MAGE_ROBE_B = ItemTags.create(new ResourceLocation(AdamsArsPlus.MOD_ID, "robe_b"));
    public static final TagKey<Item> MAGE_LEG_A  = ItemTags.create(new ResourceLocation(AdamsArsPlus.MOD_ID, "legs_a"));
    public static final TagKey<Item> MAGE_LEG_B  = ItemTags.create(new ResourceLocation(AdamsArsPlus.MOD_ID, "legs_b"));
    public static final TagKey<Item> MAGE_BOOT_A = ItemTags.create(new ResourceLocation(AdamsArsPlus.MOD_ID, "boot_a"));
    public static final TagKey<Item> MAGE_BOOT_B = ItemTags.create(new ResourceLocation(AdamsArsPlus.MOD_ID, "boot_b"));

    public static final TagKey<Item> MAGE_RITUAL = ItemTags.create(new ResourceLocation(AdamsArsPlus.MOD_ID, "mage_r"));

    public AdamsItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.ITEM, future, (item) -> item.builtInRegistryHolder().key(), MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(MAGIC_HOOD).add(ItemsRegistry.BATTLEMAGE_HOOD.get(), ItemsRegistry.ARCANIST_HOOD.get(), ItemsRegistry.SORCERER_HOOD.get());
        tag(MAGIC_ROBE).add(ItemsRegistry.BATTLEMAGE_ROBES.get(), ItemsRegistry.ARCANIST_ROBES.get(), ItemsRegistry.SORCERER_ROBES.get());
        tag(MAGIC_LEG).add(ItemsRegistry.BATTLEMAGE_LEGGINGS.get(), ItemsRegistry.ARCANIST_LEGGINGS.get(), ItemsRegistry.SORCERER_LEGGINGS.get());
        tag(MAGIC_BOOT).add(ItemsRegistry.BATTLEMAGE_BOOTS.get(), ItemsRegistry.ARCANIST_BOOTS.get(), ItemsRegistry.SORCERER_BOOTS.get());

        tag(MAGE_HOOD_A).add(RYAN_HOOD_A.get(),CADE_HOOD_A.get(),NICK_HOOD_A.get());
        tag(MAGE_HOOD_B).add(CAMR_HOOD_A.get(),MATT_HOOD_A.get());

        tag(MAGE_ROBE_A).add(RYAN_ROBES_A.get(),CADE_ROBES_A.get(),NICK_ROBES_A.get());
        tag(MAGE_ROBE_B).add(CAMR_ROBES_A.get(),MATT_ROBES_A.get());

        tag(MAGE_LEG_A).add(RYAN_LEGGINGS_A.get(),CADE_LEGGINGS_A.get(),NICK_LEGGINGS_A.get());
        tag(MAGE_LEG_B).add(CAMR_LEGGINGS_A.get(),MATT_LEGGINGS_A.get());

        tag(MAGE_BOOT_A).add(RYAN_BOOTS_A.get(),CADE_BOOTS_A.get(),NICK_BOOTS_A.get());
        tag(MAGE_BOOT_B).add(CAMR_BOOTS_A.get(),MATT_BOOTS_A.get());

        tag(MAGE_RITUAL).add(ItemsRegistry.FIRE_ESSENCE.get(), ItemsRegistry.WATER_ESSENCE.get(), ItemsRegistry.EARTH_ESSENCE.get(), ItemsRegistry.AIR_ESSENCE.get(), ItemsRegistry.CONJURATION_ESSENCE.get(), ItemsRegistry.ABJURATION_ESSENCE.get(), ELEMENTAL_SOUL.get(), TRUE_ELEMENTAL_SOUL.get());
    }

    @Override
    public @NotNull String getName() {
        return "ArsPlus Item Tags";
    }

    public static class AdamBlockTagsProvider extends BlockTagsProvider {

        public AdamBlockTagsProvider(DataGenerator gen, CompletableFuture<HolderLookup.Provider> provider, @javax.annotation.Nullable ExistingFileHelper existingFileHelper) {
            super(gen.getPackOutput(), provider, MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {


        }
    }

}
