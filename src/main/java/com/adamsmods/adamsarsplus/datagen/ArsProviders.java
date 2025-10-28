package com.adamsmods.adamsarsplus.datagen;

import com.adamsmods.adamsarsplus.ArsNouveauRegistry;
import com.adamsmods.adamsarsplus.AdamsArsPlus;

import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.method_glyph.MethodDetonate;
import com.adamsmods.adamsarsplus.glyphs.method_glyph.PropagateDetonate;
import com.adamsmods.adamsarsplus.perk.*;
import com.adamsmods.adamsarsplus.recipe.jei.AArmorRecipe;
import com.adamsmods.adamsarsplus.ritual.RitualMageSummon;
import com.adamsmods.adamsarsplus.ritual.RitualTenShadows;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.api.familiar.AbstractFamiliarHolder;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe;
import com.hollingsworth.arsnouveau.common.datagen.ApparatusRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.GlyphRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.ImbuementRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.*;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectExchange;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;

import net.minecraft.world.level.block.SkullBlock;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.function.Consumer;

import static com.adamsmods.adamsarsplus.AdamsArsPlus.prefix;
import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;
import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.AIR_ESSENCE;
import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.SUMMONING_FOCUS;
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

            shapelessBuilder(getRitualItem(new ResourceLocation(AdamsArsPlus.MOD_ID, RitualMageSummon.ID)))
                    .requires(BlockRegistry.CASCADING_LOG)
                    .requires(MAGE_CLOTH.get(),2)
                    .requires(MANA_DIAMOND.get())
                    .save(consumer, prefix("tablet_" + RitualMageSummon.ID));

            shapelessBuilder(getRitualItem(new ResourceLocation(AdamsArsPlus.MOD_ID, RitualTenShadows.ID)))
                    .requires(BlockRegistry.VEXING_LOG)
                    .requires(SCULK,3)
                    .requires(FEATHER)
                    .requires(BONE)
                    .requires(RABBIT_FOOT)
                    .requires(BEEF)
                    .requires(ROTTEN_FLESH)
                    .save(consumer, prefix("tablet_" + RitualTenShadows.ID));
        }
    }

    public static class GlyphProvider extends GlyphRecipeProvider {

        public GlyphProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void collectJsons(CachedOutput cache) {

            Path output = this.generator.getPackOutput().getOutputFolder();

            recipes.add(get(AugmentAmplifyThree.INSTANCE).withItem(AugmentAmplifyTwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(TRUE_ELEMENTAL_SOUL));
            recipes.add(get(AugmentAmplifyTwo.INSTANCE).withItem(AugmentAmplify.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(ELEMENTAL_SOUL));
            recipes.add(get(AugmentDampenThree.INSTANCE).withItem(AugmentDampenTwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(TRUE_ELEMENTAL_SOUL));
            recipes.add(get(AugmentDampenTwo.INSTANCE).withItem(AugmentDampen.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(ELEMENTAL_SOUL));
            recipes.add(get(AugmentAccelerateThree.INSTANCE).withItem(AugmentAccelerateTwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(TRUE_ELEMENTAL_SOUL));
            recipes.add(get(AugmentAccelerateTwo.INSTANCE).withItem(AugmentAccelerate.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(ELEMENTAL_SOUL));
            recipes.add(get(AugmentAOEThree.INSTANCE).withItem(AugmentAOETwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(TRUE_ELEMENTAL_SOUL));
            recipes.add(get(AugmentAOETwo.INSTANCE).withItem(AugmentAOE.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(ELEMENTAL_SOUL));
            recipes.add(get(AugmentLesserAOE.INSTANCE).withItem(GUNPOWDER,2).withItem(QUARTZ));
            recipes.add(get(AugmentExtendTimeThree.INSTANCE).withItem(AugmentExtendTimeTwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(TRUE_ELEMENTAL_SOUL));
            recipes.add(get(AugmentExtendTimeTwo.INSTANCE).withItem(AugmentExtendTime.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(ELEMENTAL_SOUL));
            recipes.add(get(AugmentDurationDownThree.INSTANCE).withItem(AugmentDurationDownTwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(TRUE_ELEMENTAL_SOUL));
            recipes.add(get(AugmentDurationDownTwo.INSTANCE).withItem(AugmentDurationDown.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND, 4).withItem(ELEMENTAL_SOUL));
            recipes.add(get(AugmentOpenDomain.INSTANCE).withItem(VOID_SOUL, 8).withItem(NETHER_STAR));
            recipes.add(get(SpellEfficiency.INSTANCE).withItem(MANA_DIAMOND, 8).withItem(NETHER_STAR));
            recipes.add(get(EffectDomain.INSTANCE).withItem(TRUE_ELEMENTAL_SOUL,4).withItem(NETHER_STAR).withItem(DRAGON_BREATH,4));
            recipes.add(get(EffectSwapTarget.INSTANCE).withItem(EMERALD_BLOCK).withItem(EffectExchange.INSTANCE.getGlyph().asItem()).withItem(ENDER_EYE, 2).withItem(ItemsRegistry.MANIPULATION_ESSENCE));
            recipes.add(get(FilterNotSelf.INSTANCE).withItem(MethodSelf.INSTANCE.getGlyph().asItem()).withItem(ItemsRegistry.MANIPULATION_ESSENCE));
            recipes.add(get(EffectSimpleDomain.INSTANCE).withItem(ELEMENTAL_SOUL,4).withItem(MANA_DIAMOND,4).withItem(ItemsRegistry.CONJURATION_ESSENCE));

            recipes.add(get(EffectEruption.INSTANCE).withItem(FLAME_SOUL,3).withItem(ItemsRegistry.FIRE_ESSENCE,2).withItem(FLINT_AND_STEEL));
            recipes.add(get(EffectIceburst.INSTANCE).withItem(FROST_SOUL,3).withItem(ItemsRegistry.WATER_ESSENCE,2).withItem(BLUE_ICE));
            recipes.add(get(EffectRaiseEarth.INSTANCE).withItem(EARTH_SOUL,3).withItem(ItemsRegistry.EARTH_ESSENCE,2).withItem(ANVIL));
            recipes.add(get(EffectDivineSmite.INSTANCE).withItem(LIGHTNING_SOUL,3).withItem(AIR_ESSENCE,2).withItem(LIGHTNING_ROD));
            recipes.add(get(EffectMeteorSwarm.INSTANCE).withItem(HERO_SOUL,3).withItem(ItemsRegistry.CONJURATION_ESSENCE,2).withItem(FIRE_CHARGE));

            recipes.add(get(EffectBlueFlame.INSTANCE).withItem(FLAME_SOUL,3).withItem(SOUL_CAMPFIRE).withItem(LAVA_BUCKET).withItem(SOUL_LANTERN));

            recipes.add(get(EffectFracture.INSTANCE).withItem(EARTH_SOUL,3).withItem(ItemsRegistry.EARTH_ESSENCE,2).withItem(ANVIL));
            recipes.add(get(EffectDismantle.INSTANCE).withItem(LIGHTNING_SOUL,3).withItem(SHEARS).withItem(ItemsRegistry.SPLIT_ARROW).withItem(NETHERITE_SWORD));
            recipes.add(get(EffectSummonUndead_boss.INSTANCE).withItem(HERO_SOUL,3).withItem(ItemsRegistry.CONJURATION_ESSENCE).withItem(WITHER_SKELETON_SKULL).withItem(ItemsRegistry.ENCHANTERS_SWORD));
            recipes.add(get(EffectConjureBlade.INSTANCE).withItem(HERO_SOUL,3).withItem(ItemsRegistry.CONJURATION_ESSENCE).withItem(DIAMOND_SWORD).withItem(DIAMOND_AXE));

            recipes.add(get(EffectLimitless.INSTANCE).withItem(ItemsRegistry.ABJURATION_ESSENCE, 2).withItem(CHORUS_FRUIT).withItem(VOID_SOUL, 3));
            recipes.add(get(EffectAnnihilate.INSTANCE).withItem(VOID_SOUL,3).withItem(ItemsRegistry.ABJURATION_ESSENCE,1).withItem(END_CRYSTAL).withItem(DRAGON_HEAD));

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
            // Basic
            recipes.add(builder().withSourceCost(10000).withPedestalItem(8, ItemsRegistry.SOURCE_GEM).withReagent(DIAMOND).withResult(MANA_DIAMOND).build());
            recipes.add(builder().withSourceCost(10000).withPedestalItem(1, FLAME_SOUL).withPedestalItem(1, FROST_SOUL).withPedestalItem(1, EARTH_SOUL).withReagent(MANA_DIAMOND).withResult(ELEMENTAL_SOUL).build());
            recipes.add(builder().withSourceCost(10000).withPedestalItem(1, FLAME_SOUL).withPedestalItem(1, FROST_SOUL).withPedestalItem(1, EARTH_SOUL).withPedestalItem(1, HERO_SOUL).withPedestalItem(1, LIGHTNING_SOUL).withReagent(MANA_DIAMOND).withResult(TRUE_ELEMENTAL_SOUL).build());

            recipes.add(builder().withSourceCost(7000).withPedestalItem(3, GOLD_INGOT).withPedestalItem(2, REPEATER).withPedestalItem(1, MANA_DIAMOND).withPedestalItem(2, LIGHTNING_SOUL).withReagent(CLOCK).withResult(ENCHANTERS_STOPWATCH).build());
            recipes.add(builder().withSourceCost(2500).withPedestalItem(4, GOLD_INGOT).withPedestalItem(4, MANA_DIAMOND).withReagent(SUMMONING_FOCUS).withResult(GENERALS_WHEEL).build());

            // Perks
            recipes.add(builder().withResult(getPerkItem(SixeyesPerk.INSTANCE.getRegistryName())).withReagent(ItemsRegistry.BLANK_THREAD).withPedestalItem(2, VOID_SOUL).withPedestalItem(2, END_CRYSTAL).withPedestalItem(2, NETHERITE_BLOCK).build());
            recipes.add(builder().withResult(getPerkItem(CloudStepsPerk.INSTANCE.getRegistryName())).withReagent(ItemsRegistry.BLANK_THREAD).withPedestalItem(2, LIGHTNING_SOUL).withPedestalItem(ELYTRA).withPedestalItem(3,AIR_ESSENCE).build());
            recipes.add(builder().withResult(getPerkItem(ImmortalPerk.INSTANCE.getRegistryName())).withReagent(ItemsRegistry.BLANK_THREAD).withPedestalItem(2, HERO_SOUL).withPedestalItem(ENCHANTED_GOLDEN_APPLE).withPedestalItem(3, GHAST_TEAR).build());
            recipes.add(builder().withResult(getPerkItem(DraconicHexPerk.INSTANCE.getRegistryName())).withReagent(ItemsRegistry.BLANK_THREAD).withPedestalItem(2, FLAME_SOUL).withPedestalItem(WITHER_SKELETON_SKULL).withPedestalItem(2, BLAZE_POWDER).build());
            recipes.add(builder().withResult(getPerkItem(AdrenalinePerk.INSTANCE.getRegistryName())).withReagent(ItemsRegistry.BLANK_THREAD).withPedestalItem(2, FROST_SOUL).withPedestalItem(PUFFERFISH).withPedestalItem(HEART_OF_THE_SEA).withPedestalItem(POWDER_SNOW_BUCKET).build());
            recipes.add(builder().withResult(getPerkItem(InvinciblePerk.INSTANCE.getRegistryName())).withReagent(ItemsRegistry.BLANK_THREAD).withPedestalItem(2, EARTH_SOUL).withPedestalItem(ANVIL).withPedestalItem(3, NETHERITE_SCRAP).build());

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


}
