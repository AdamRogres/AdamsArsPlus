package com.adamsmods.adamsarsplus.datagen;

import com.adamsmods.adamsarsplus.ArsNouveauRegistry;
import com.adamsmods.adamsarsplus.AdamsArsPlus;

import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.method_glyph.MethodDetonate;
import com.adamsmods.adamsarsplus.glyphs.method_glyph.PropagateDetonate;
import com.adamsmods.adamsarsplus.lib.AdamsEntityTags;
import com.adamsmods.adamsarsplus.recipe.jei.AArmorRecipe;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.api.familiar.AbstractFamiliarHolder;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe;
import com.hollingsworth.arsnouveau.common.datagen.ApparatusRecipeBuilder;
import com.hollingsworth.arsnouveau.common.datagen.ApparatusRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.GlyphRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.ImbuementRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.*;
import com.hollingsworth.arsnouveau.common.items.ManipulationEssence;
import com.hollingsworth.arsnouveau.common.items.ModItem;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBurst;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectExchange;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLinger;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectWall;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.hollingsworth.arsnouveau.common.lib.LibItemNames;
import com.hollingsworth.arsnouveau.setup.registry.ModEntities;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.data.tags.EntityTypeTagsProvider;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;
import static com.hollingsworth.arsnouveau.setup.registry.RegistryHelper.getRegistryName;
import static net.minecraft.world.item.Items.*;

public class ArsProviders {

    static String root = AdamsArsPlus.MOD_ID;

    public static class CraftingTableProvider extends RecipeProvider {
        public CraftingTableProvider(DataGenerator pGenerator) {
            super(pGenerator.getPackOutput());
        }

        public Item getRitualItem(ResourceLocation id) {
            return RitualRegistry.getRitualItemMap().get(id);
        }

        public ShapelessRecipeBuilder shapelessBuilder(ItemLike result) {
            return shapelessBuilder(result, 1);
        }

        public ShapelessRecipeBuilder shapelessBuilder(ItemLike result, int resultCount) {
            return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, resultCount).unlockedBy("has_journal", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsRegistry.WORN_NOTEBOOK));
        }

        @Override
        protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
//            shapelessBuilder(getRitualItem(new ResourceLocation(ArsNouveau.MODID,LevelingRitual.ID)))
//                    .requires(GOLD_ESSENCE.get(),2)
//                    .requires(NETHERITE_INGOT,2)
//                    .requires(NETHER_STAR)
//                    .save(consumer, prefix("tablet_"+LevelingRitual.ID))
//            ;
        }
    }


    public static class GlyphProvider extends GlyphRecipeProvider {

        public GlyphProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void collectJsons(CachedOutput cache) {

            Path output = this.generator.getPackOutput().getOutputFolder();

            recipes.add(get(AugmentAccelerateThree.INSTANCE).withItem(AugmentAccelerateTwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(TRUE_ELEMENTAL_SOUL));
            recipes.add(get(AugmentAccelerateTwo.INSTANCE).withItem(AugmentAccelerate.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(ELEMENTAL_SOUL));
            recipes.add(get(AugmentAOEThree.INSTANCE).withItem(AugmentAOETwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(TRUE_ELEMENTAL_SOUL));
            recipes.add(get(AugmentAOETwo.INSTANCE).withItem(AugmentAOE.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(ELEMENTAL_SOUL));
            recipes.add(get(AugmentExtendTimeThree.INSTANCE).withItem(AugmentExtendTimeTwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(TRUE_ELEMENTAL_SOUL));
            recipes.add(get(AugmentExtendTimeTwo.INSTANCE).withItem(AugmentExtendTime.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(ELEMENTAL_SOUL));

            recipes.add(get(AugmentOpenDomain.INSTANCE).withItem(VOID_SOUL, 8).withItem(NETHER_STAR));

            recipes.add(get(SpellEfficiency.INSTANCE).withItem(MANA_DIAMOND, 8).withItem(NETHER_STAR));
            recipes.add(get(EffectDomain.INSTANCE).withItem(TRUE_ELEMENTAL_SOUL,4).withItem(NETHER_STAR).withItem(DRAGON_BREATH,4));
            recipes.add(get(EffectLimitless.INSTANCE).withItem(ItemsRegistry.ABJURATION_ESSENCE, 3).withItem(VOID_SOUL, 6));
            recipes.add(get(EffectSwapTarget.INSTANCE).withItem(EMERALD_BLOCK).withItem(EffectExchange.INSTANCE.getGlyph().asItem()).withItem(ENDER_EYE, 2).withItem(ItemsRegistry.MANIPULATION_ESSENCE));

            recipes.add(get(EffectSimpleDomain.INSTANCE).withItem(ELEMENTAL_SOUL,4).withItem(MANA_DIAMOND,4).withItem(ItemsRegistry.CONJURATION_ESSENCE));

            recipes.add(get(EffectEruption.INSTANCE).withItem(FLAME_SOUL,3).withItem(ItemsRegistry.FIRE_ESSENCE,2).withItem(FLINT_AND_STEEL));
            recipes.add(get(EffectIceburst.INSTANCE).withItem(FROST_SOUL,3).withItem(ItemsRegistry.WATER_ESSENCE,2).withItem(BLUE_ICE));
            recipes.add(get(EffectRaiseEarth.INSTANCE).withItem(EARTH_SOUL,3).withItem(ItemsRegistry.EARTH_ESSENCE,2).withItem(ANVIL));
            recipes.add(get(EffectDivineSmite.INSTANCE).withItem(LIGHTNING_SOUL,3).withItem(ItemsRegistry.AIR_ESSENCE,2).withItem(LIGHTNING_ROD));
            recipes.add(get(EffectMeteorSwarm.INSTANCE).withItem(HERO_SOUL,3).withItem(ItemsRegistry.CONJURATION_ESSENCE,2).withItem(FIRE_CHARGE));

            recipes.add(get(EffectAnnihilate.INSTANCE).withItem(VOID_SOUL,3).withItem(ItemsRegistry.ABJURATION_ESSENCE,2).withItem(CHORUS_FRUIT));

            recipes.add(get(MethodDetonate.INSTANCE).withItem(FLAME_SOUL).withItem(GUNPOWDER,2).withItem(ARROW, 2).withItem(ItemsRegistry.MANIPULATION_ESSENCE));
            recipes.add(get(PropagateDetonate.INSTANCE).withItem(MethodDetonate.INSTANCE.getGlyph().asItem()).withItem(ItemsRegistry.MANIPULATION_ESSENCE));




            for (GlyphRecipe recipe : recipes) {
                Path path = getScribeGlyphPath(output, recipe.output.getItem());
                saveStable(cache, recipe.asRecipe(), path);
            }

        }


        protected static Path getScribeGlyphPath(Path pathIn, Item glyph) {
            return pathIn.resolve("data/" + root + "/recipes/" + getRegistryName(glyph).getPath() + ".json");
        }

        @Override
        public String getName() {
            return "Example Glyph Recipes";
        }
    }

    public static class EnchantingAppProvider extends ApparatusRecipeProvider {

        public EnchantingAppProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void collectJsons(CachedOutput cache) {

            recipes.add(builder().withSourceCost(10000).withPedestalItem(8, ItemsRegistry.SOURCE_GEM).withReagent(DIAMOND).withResult(MANA_DIAMOND).build());
            recipes.add(builder().withSourceCost(10000).withPedestalItem(1, FLAME_SOUL).withPedestalItem(1, FROST_SOUL).withPedestalItem(1, EARTH_SOUL).withReagent(MANA_DIAMOND).withResult(ELEMENTAL_SOUL).build());
            recipes.add(builder().withSourceCost(10000).withPedestalItem(1, FLAME_SOUL).withPedestalItem(1, FROST_SOUL).withPedestalItem(1, EARTH_SOUL).withPedestalItem(1, HERO_SOUL).withPedestalItem(1, LIGHTNING_SOUL).withReagent(MANA_DIAMOND).withResult(TRUE_ELEMENTAL_SOUL).build());

            // Ryan Armor
            recipes.add(new AArmorRecipe(3,3,builder().withResult(RYAN_HOOD).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_HOOD)).withPedestalItem(4,FLAME_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(7000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,3,builder().withResult(RYAN_ROBES).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_ROBE)).withPedestalItem(4,FLAME_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(7000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,3,builder().withResult(RYAN_LEGGINGS).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_LEG)).withPedestalItem(4,FLAME_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(7000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,3,builder().withResult(RYAN_BOOTS).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_BOOT)).withPedestalItem(4,FLAME_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(7000).keepNbtOfReagent(true).build()));

            recipes.add(new AArmorRecipe(3,4,builder().withResult(RYAN_HOOD_A).withReagent(RYAN_HOOD).withPedestalItem(4,ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,4,builder().withResult(RYAN_ROBES_A).withReagent(RYAN_ROBES).withPedestalItem(4,ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,4,builder().withResult(RYAN_LEGGINGS_A).withReagent(RYAN_LEGGINGS).withPedestalItem(4,ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,4,builder().withResult(RYAN_BOOTS_A).withReagent(RYAN_BOOTS).withPedestalItem(4,ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build()));

            // Cade Armor
            recipes.add(new AArmorRecipe(3,3,builder().withResult(CADE_HOOD).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_HOOD)).withPedestalItem(4,FROST_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(7000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,3,builder().withResult(CADE_ROBES).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_ROBE)).withPedestalItem(4,FROST_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(7000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,3,builder().withResult(CADE_LEGGINGS).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_LEG)).withPedestalItem(4,FROST_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(7000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,3,builder().withResult(CADE_BOOTS).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_BOOT)).withPedestalItem(4,FROST_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(7000).keepNbtOfReagent(true).build()));

            recipes.add(new AArmorRecipe(3,4,builder().withResult(CADE_HOOD_A).withReagent(CADE_HOOD).withPedestalItem(4,ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,4,builder().withResult(CADE_ROBES_A).withReagent(CADE_ROBES).withPedestalItem(4,ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,4,builder().withResult(CADE_LEGGINGS_A).withReagent(CADE_LEGGINGS).withPedestalItem(4,ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,4,builder().withResult(CADE_BOOTS_A).withReagent(CADE_BOOTS).withPedestalItem(4,ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build()));

            // Nick Armor
            recipes.add(new AArmorRecipe(3,3,builder().withResult(NICK_HOOD).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_HOOD)).withPedestalItem(4,EARTH_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(7000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,3,builder().withResult(NICK_ROBES).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_ROBE)).withPedestalItem(4,EARTH_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(7000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,3,builder().withResult(NICK_LEGGINGS).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_LEG)).withPedestalItem(4,EARTH_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(7000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,3,builder().withResult(NICK_BOOTS).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_BOOT)).withPedestalItem(4,EARTH_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(7000).keepNbtOfReagent(true).build()));

            recipes.add(new AArmorRecipe(3,4,builder().withResult(NICK_HOOD_A).withReagent(NICK_HOOD).withPedestalItem(4,ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,4,builder().withResult(NICK_ROBES_A).withReagent(NICK_ROBES).withPedestalItem(4,ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,4,builder().withResult(NICK_LEGGINGS_A).withReagent(NICK_LEGGINGS).withPedestalItem(4,ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(3,4,builder().withResult(NICK_BOOTS_A).withReagent(NICK_BOOTS).withPedestalItem(4,ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build()));

            // Cam Armor
            recipes.add(new AArmorRecipe(4,4,builder().withResult(CAMR_HOOD).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_HOOD_A)).withPedestalItem(4,LIGHTNING_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(8000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(4,4,builder().withResult(CAMR_ROBES).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_ROBE_A)).withPedestalItem(4,LIGHTNING_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(8000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(4,4,builder().withResult(CAMR_LEGGINGS).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_LEG_A)).withPedestalItem(4,LIGHTNING_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(8000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(4,4,builder().withResult(CAMR_BOOTS).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_BOOT_A)).withPedestalItem(4,LIGHTNING_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(8000).keepNbtOfReagent(true).build()));

            recipes.add(new AArmorRecipe(4,5,builder().withResult(CAMR_HOOD_A).withReagent(CAMR_HOOD).withPedestalItem(4,TRUE_ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(4,5,builder().withResult(CAMR_ROBES_A).withReagent(CAMR_ROBES).withPedestalItem(4,TRUE_ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(4,5,builder().withResult(CAMR_LEGGINGS_A).withReagent(CAMR_LEGGINGS).withPedestalItem(4,TRUE_ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(4,5,builder().withResult(CAMR_BOOTS_A).withReagent(CAMR_BOOTS).withPedestalItem(4,TRUE_ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build()));

            // Matt Armor
            recipes.add(new AArmorRecipe(4,4,builder().withResult(MATT_HOOD).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_HOOD_A)).withPedestalItem(4,HERO_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(8000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(4,4,builder().withResult(MATT_ROBES).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_ROBE_A)).withPedestalItem(4,HERO_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(8000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(4,4,builder().withResult(MATT_LEGGINGS).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_LEG_A)).withPedestalItem(4,HERO_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(8000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(4,4,builder().withResult(MATT_BOOTS).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_BOOT_A)).withPedestalItem(4,HERO_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(8000).keepNbtOfReagent(true).build()));

            recipes.add(new AArmorRecipe(4,5,builder().withResult(MATT_HOOD_A).withReagent(MATT_HOOD).withPedestalItem(4,TRUE_ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(4,5,builder().withResult(MATT_ROBES_A).withReagent(MATT_ROBES).withPedestalItem(4,TRUE_ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(4,5,builder().withResult(MATT_LEGGINGS_A).withReagent(MATT_LEGGINGS).withPedestalItem(4,TRUE_ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(4,5,builder().withResult(MATT_BOOTS_A).withReagent(MATT_BOOTS).withPedestalItem(4,TRUE_ELEMENTAL_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build()));

            // Adam Armor
            recipes.add(new AArmorRecipe(5,5,builder().withResult(ADAM_HOOD).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_HOOD_B)).withPedestalItem(4,VOID_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(8500).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(5,5,builder().withResult(ADAM_ROBES).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_ROBE_B)).withPedestalItem(4,VOID_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(8500).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(5,5,builder().withResult(ADAM_LEGGINGS).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_LEG_B)).withPedestalItem(4,VOID_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(8500).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(5,5,builder().withResult(ADAM_BOOTS).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_BOOT_B)).withPedestalItem(4,VOID_SOUL).withPedestalItem(4,MANA_DIAMOND).withSourceCost(8500).keepNbtOfReagent(true).build()));

            recipes.add(new AArmorRecipe(5,6,builder().withResult(ADAM_HOOD_A).withReagent(ADAM_HOOD).withPedestalItem(4,VOID_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(10000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(5,6,builder().withResult(ADAM_ROBES_A).withReagent(ADAM_ROBES).withPedestalItem(4,VOID_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(10000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(5,6,builder().withResult(ADAM_LEGGINGS_A).withReagent(ADAM_LEGGINGS).withPedestalItem(4,VOID_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(10000).keepNbtOfReagent(true).build()));
            recipes.add(new AArmorRecipe(5,6,builder().withResult(ADAM_BOOTS_A).withReagent(ADAM_BOOTS).withPedestalItem(4,VOID_SOUL).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(10000).keepNbtOfReagent(true).build()));


            Path output = this.generator.getPackOutput().getOutputFolder();
            for (EnchantingApparatusRecipe g : recipes) {
                if (g != null) {
                    Path path = getRecipePath(output, g.getId().getPath());
                    saveStable(cache, g.asRecipe(), path);
                }
            }
        }

        protected static Path getRecipePath(Path pathIn, String str) {
            return pathIn.resolve("data/" + root + "/recipes/" + str + ".json");
        }

        @Override
        public String getName() {
            return "Example Apparatus";
        }
    }

    public static class ImbuementProvider extends ImbuementRecipeProvider {

        public ImbuementProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void collectJsons(CachedOutput cache) {
            System.out.println("started Imbument>collect jsons");

            Path output = generator.getPackOutput().getOutputFolder();
            for (ImbuementRecipe g : recipes) {
                Path path = getRecipePath(output, g.getId().getPath());
                saveStable(cache, g.asRecipe(), path);
            }

        }

        protected Path getRecipePath(Path pathIn, String str) {
            return pathIn.resolve("data/" + root + "/recipes/" + str + ".json");
        }

        @Override
        public String getName() {
            return "Example Imbuement";
        }

    }

    public static class PatchouliProvider extends com.hollingsworth.arsnouveau.common.datagen.PatchouliProvider {

        public PatchouliProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void collectJsons(CachedOutput cache) {

            for (AbstractSpellPart spell : ArsNouveauRegistry.registeredSpells) {
                addGlyphPage(spell);
            }

            //check the superclass for examples

            for (PatchouliPage patchouliPage : pages) {
                DataProvider.saveStable(cache, patchouliPage.build(), patchouliPage.path());
            }

        }

        @Override
        public PatchouliPage addBasicItem(ItemLike item, ResourceLocation category, IPatchouliPage recipePage) {
            PatchouliBuilder builder = new PatchouliBuilder(category, item.asItem().getDescriptionId())
                    .withIcon(item.asItem())
                    .withPage(new TextPage(root + ".page." + getRegistryName(item.asItem()).getPath()))
                    .withPage(recipePage);
            var page = new PatchouliPage(builder, getPath(category, getRegistryName(item.asItem()).getPath()));
            this.pages.add(page);
            return page;
        }

        public void addFamiliarPage(AbstractFamiliarHolder familiarHolder) {
            PatchouliBuilder builder = new PatchouliBuilder(FAMILIARS, "entity." + root + "." + familiarHolder.getRegistryName().getPath())
                    .withIcon(root + ":" + familiarHolder.getRegistryName().getPath())
                    .withTextPage(root + ".familiar_desc." + familiarHolder.getRegistryName().getPath())
                    .withPage(new EntityPage(familiarHolder.getRegistryName().toString()));
            this.pages.add(new PatchouliPage(builder, getPath(FAMILIARS, familiarHolder.getRegistryName().getPath())));
        }

        public void addRitualPage(AbstractRitual ritual) {
            PatchouliBuilder builder = new PatchouliBuilder(RITUALS, "item." + root + '.' + ritual.getRegistryName().getPath())
                    .withIcon(ritual.getRegistryName().toString())
                    .withTextPage(ritual.getDescriptionKey())
                    .withPage(new CraftingPage(root + ":tablet_" + ritual.getRegistryName().getPath()));

            this.pages.add(new PatchouliPage(builder, getPath(RITUALS, ritual.getRegistryName().getPath())));
        }

        public void addEnchantmentPage(Enchantment enchantment) {
            PatchouliBuilder builder = new PatchouliBuilder(ENCHANTMENTS, enchantment.getDescriptionId())
                    .withIcon(getRegistryName(Items.ENCHANTED_BOOK).toString())
                    .withTextPage(root + ".enchantment_desc." + getRegistryName(enchantment).getPath());

            for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); i++) {
                builder.withPage(new EnchantingPage("ars_nouveau:" + getRegistryName(enchantment).getPath() + "_" + i));
            }
            this.pages.add(new PatchouliPage(builder, getPath(ENCHANTMENTS, getRegistryName(enchantment).getPath())));
        }

        public void addGlyphPage(AbstractSpellPart spellPart) {
            ResourceLocation category = switch (spellPart.defaultTier().value) {
                case 1 -> GLYPHS_1;
                case 2 -> GLYPHS_2;
                default -> GLYPHS_3;
            };
            PatchouliBuilder builder = new PatchouliBuilder(category, spellPart.getName())
                    .withName(root + ".glyph_name." + spellPart.getRegistryName().getPath())
                    .withIcon(spellPart.getRegistryName().toString())
                    .withSortNum(spellPart instanceof AbstractCastMethod ? 1 : spellPart instanceof AbstractEffect ? 2 : 3)
                    .withPage(new TextPage(root + ".glyph_desc." + spellPart.getRegistryName().getPath()))
                    .withPage(new GlyphScribePage(spellPart));
            this.pages.add(new PatchouliPage(builder, getPath(category, spellPart.getRegistryName().getPath())));
        }

        /**
         * Gets a name for this provider, to use in logging.
         */
        @Override
        public String getName() {
            return "Example Patchouli Datagen";
        }

        @Override
        public Path getPath(ResourceLocation category, String fileName) {
            return this.generator.getPackOutput().getOutputFolder().resolve("data/" + root + "/patchouli_books/example/en_us/entries/" + category.getPath() + "/" + fileName + ".json");
        }

        ImbuementPage ImbuementPage(ItemLike item) {
            return new ImbuementPage(root + ":imbuement_" + getRegistryName(item.asItem()).getPath());
        }


    }

}
