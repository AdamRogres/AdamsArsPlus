package adamsmods.adamsarsplus.datagen;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.glyphs.augment_glyph.*;
import adamsmods.adamsarsplus.common.glyphs.effect_glyph.*;
import adamsmods.adamsarsplus.common.glyphs.method_glyph.*;
import adamsmods.adamsarsplus.common.perk.*;
import adamsmods.adamsarsplus.common.rituals.*;
import adamsmods.adamsarsplus.recipe.AArmorRecipe;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.common.crafting.recipes.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe;
import com.hollingsworth.arsnouveau.common.datagen.*;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectExchange;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static adamsmods.adamsarsplus.AdamsArsPlus.prefix;
import static adamsmods.adamsarsplus.registry.ModBlocks.*;
import static adamsmods.adamsarsplus.registry.ModItems.*;
import static com.hollingsworth.arsnouveau.setup.registry.BlockRegistry.BASIC_SPELL_TURRET;
import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.*;
import static com.hollingsworth.arsnouveau.setup.registry.RegistryHelper.getRegistryName;
import static net.minecraft.world.item.Items.*;

public class ArsProviders {

    static String root = AdamsArsPlus.MODID;

    public static class CraftingTableProvider extends RecipeProvider {
        public RecipeOutput consumer;

        public CraftingTableProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
            super(pOutput, pRegistries);
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
        protected void buildRecipes(RecipeOutput pRecipeOutput) {
            this.consumer = pRecipeOutput;

            shapelessBuilder(getRitualItem(AdamsArsPlus.prefix( RitualMageSummon.ID)))
                    .requires(BlockRegistry.CASCADING_LOG)
                    .requires(MAGE_CLOTH.get(),2)
                    .requires(MANA_DIAMOND.get())
                    .save(consumer);

            shapelessBuilder(getRitualItem(AdamsArsPlus.prefix( RitualTenShadows.ID)))
                    .requires(BlockRegistry.VEXING_LOG)
                    .requires(SCULK,3)
                    .requires(FEATHER)
                    .requires(BONE)
                    .requires(RABBIT_FOOT)
                    .requires(BEEF)
                    .requires(ROTTEN_FLESH)
                    .save(consumer);
        }
    }

    public static class GlyphProvider extends GlyphRecipeProvider {

        public GlyphProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void collectJsons(CachedOutput pOutput) {

            Path output = this.generator.getPackOutput().getOutputFolder();

            recipes.add(get(AugmentAmplifyThree.INSTANCE).withItem(AugmentAmplifyTwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND.get(), 4).withItem(TRUE_ELEMENTAL_SOUL.get()));
            recipes.add(get(AugmentAmplifyTwo.INSTANCE).withItem(AugmentAmplify.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND.get(), 4).withItem(ELEMENTAL_SOUL.get()));
            recipes.add(get(AugmentDampenThree.INSTANCE).withItem(AugmentDampenTwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND.get(), 4).withItem(TRUE_ELEMENTAL_SOUL.get()));
            recipes.add(get(AugmentDampenTwo.INSTANCE).withItem(AugmentDampen.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND.get(), 4).withItem(ELEMENTAL_SOUL.get()));
            recipes.add(get(AugmentAccelerateThree.INSTANCE).withItem(AugmentAccelerateTwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND.get(), 4).withItem(TRUE_ELEMENTAL_SOUL.get()));
            recipes.add(get(AugmentAccelerateTwo.INSTANCE).withItem(AugmentAccelerate.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND.get(), 4).withItem(ELEMENTAL_SOUL.get()));
            recipes.add(get(AugmentAOEThree.INSTANCE).withItem(AugmentAOETwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND.get(), 4).withItem(TRUE_ELEMENTAL_SOUL.get()));
            recipes.add(get(AugmentAOETwo.INSTANCE).withItem(AugmentAOE.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND.get(), 4).withItem(ELEMENTAL_SOUL.get()));
            recipes.add(get(AugmentLesserAOE.INSTANCE).withItem(GUNPOWDER,2).withItem(QUARTZ));
            recipes.add(get(AugmentExtendTimeThree.INSTANCE).withItem(AugmentExtendTimeTwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND.get(), 4).withItem(TRUE_ELEMENTAL_SOUL.get()));
            recipes.add(get(AugmentExtendTimeTwo.INSTANCE).withItem(AugmentExtendTime.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND.get(), 4).withItem(ELEMENTAL_SOUL.get()));
            recipes.add(get(AugmentDurationDownThree.INSTANCE).withItem(AugmentDurationDownTwo.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND.get(), 4).withItem(TRUE_ELEMENTAL_SOUL.get()));
            recipes.add(get(AugmentDurationDownTwo.INSTANCE).withItem(AugmentDurationDown.INSTANCE.getGlyph().asItem()).withItem(MANA_DIAMOND.get(), 4).withItem(ELEMENTAL_SOUL.get()));
            recipes.add(get(AugmentOpenDomain.INSTANCE).withItem(VOID_SOUL.get(), 8).withItem(NETHER_STAR));
            recipes.add(get(SpellEfficiency.INSTANCE).withItem(MANA_DIAMOND.get(), 8).withItem(NETHER_STAR));
            recipes.add(get(EffectDomain.INSTANCE).withItem(TRUE_ELEMENTAL_SOUL.get(),4).withItem(NETHER_STAR).withItem(DRAGON_BREATH,4));
            recipes.add(get(EffectSwapTarget.INSTANCE).withItem(EMERALD_BLOCK).withItem(EffectExchange.INSTANCE.getGlyph().asItem()).withItem(ENDER_EYE, 2).withItem(ItemsRegistry.MANIPULATION_ESSENCE));
            recipes.add(get(FilterNotSelf.INSTANCE).withItem(MethodSelf.INSTANCE.getGlyph().asItem()).withItem(ItemsRegistry.MANIPULATION_ESSENCE));
            recipes.add(get(EffectSimpleDomain.INSTANCE).withItem(ELEMENTAL_SOUL.get(),4).withItem(MANA_DIAMOND.get(),4).withItem(ItemsRegistry.CONJURATION_ESSENCE));

            recipes.add(get(EffectEruption.INSTANCE).withItem(FLAME_SOUL.get(),3).withItem(ItemsRegistry.FIRE_ESSENCE,2).withItem(FLINT_AND_STEEL));
            recipes.add(get(EffectIceburst.INSTANCE).withItem(FROST_SOUL.get(),3).withItem(ItemsRegistry.WATER_ESSENCE,2).withItem(BLUE_ICE));
            recipes.add(get(EffectRaiseEarth.INSTANCE).withItem(EARTH_SOUL.get(),3).withItem(ItemsRegistry.EARTH_ESSENCE,2).withItem(ANVIL));
            recipes.add(get(EffectDivineSmite.INSTANCE).withItem(LIGHTNING_SOUL.get(),3).withItem(AIR_ESSENCE,2).withItem(LIGHTNING_ROD));
            recipes.add(get(EffectMeteorSwarm.INSTANCE).withItem(HERO_SOUL.get(),3).withItem(ItemsRegistry.CONJURATION_ESSENCE,2).withItem(FIRE_CHARGE));

            recipes.add(get(EffectBlueFlame.INSTANCE).withItem(FLAME_SOUL.get(),3).withItem(SOUL_CAMPFIRE).withItem(LAVA_BUCKET).withItem(SOUL_LANTERN));
            recipes.add(get(EffectSoulRime.INSTANCE).withItem(FROST_SOUL.get(),3).withItem(BLUE_ICE).withItem(DRIPSTONE_BLOCK).withItem(CHAIN, 2));
            recipes.add(get(EffectFracture.INSTANCE).withItem(EARTH_SOUL.get(),3).withItem(ItemsRegistry.EARTH_ESSENCE,2).withItem(ANVIL));
            recipes.add(get(EffectDismantle.INSTANCE).withItem(LIGHTNING_SOUL.get(),3).withItem(SHEARS).withItem(ItemsRegistry.SPLIT_ARROW).withItem(NETHERITE_SWORD));
            recipes.add(get(EffectSummonUndead_boss.INSTANCE).withItem(HERO_SOUL.get(),3).withItem(ItemsRegistry.CONJURATION_ESSENCE).withItem(WITHER_SKELETON_SKULL).withItem(ItemsRegistry.ENCHANTERS_SWORD));
            recipes.add(get(EffectConjureBlade.INSTANCE).withItem(HERO_SOUL.get(),3).withItem(ItemsRegistry.CONJURATION_ESSENCE).withItem(DIAMOND_SWORD).withItem(DIAMOND_AXE));
            recipes.add(get(EffectConjureArrow.INSTANCE).withItem(ADVANCED_CLOCKWORK.get(),3).withItem(MANIPULATION_ESSENCE).withItem(ARROW).withItem(ELEMENTAL_SOUL.get()));
            recipes.add(get(EffectLimitless.INSTANCE).withItem(ItemsRegistry.ABJURATION_ESSENCE, 2).withItem(CHORUS_FRUIT).withItem(VOID_SOUL.get(), 3));
            recipes.add(get(EffectAnnihilate.INSTANCE).withItem(VOID_SOUL.get(),3).withItem(ItemsRegistry.ABJURATION_ESSENCE,1).withItem(END_CRYSTAL).withItem(DRAGON_HEAD));

            recipes.add(get(MethodDetonate.INSTANCE).withItem(FLAME_SOUL.get()).withItem(GUNPOWDER,2).withItem(ARROW, 2).withItem(ItemsRegistry.MANIPULATION_ESSENCE));
            recipes.add(get(PropagateDetonate.INSTANCE).withItem(MethodDetonate.INSTANCE.getGlyph().asItem()).withItem(ItemsRegistry.MANIPULATION_ESSENCE));

            for (GlyphRecipe recipe : recipes) {
                Path path = getScribeGlyphPath(output, recipe.output.getItem());
                saveStable(pOutput, GlyphRecipe.CODEC.encodeStart(JsonOps.INSTANCE, recipe).getOrThrow(), path);
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
        public void collectJsons(CachedOutput pOutput) {
            addEntries();
            for (ApparatusRecipeBuilder.RecipeWrapper<? extends EnchantingApparatusRecipe> wrapper : recipes) {
                Path path = getRecipePath(output, wrapper.id().getPath());
                saveStable(pOutput, wrapper.serialize(), path);
            }
        }

        public List<ApparatusRecipeBuilder.RecipeWrapper<? extends EnchantingApparatusRecipe>> recipes = new ArrayList<>();

        public ApparatusRecipeBuilder builder() {
            return ApparatusRecipeBuilder.builder();
        }

        public ApparatusRecipeBuilder.RecipeWrapper<AArmorRecipe> AAbuilder(AArmorRecipe recipe) {
            return new ApparatusRecipeBuilder.RecipeWrapper<AArmorRecipe>(AdamsArsPlus.prefix(getRegistryName(recipe.result().getItem()).getPath()), recipe, AArmorRecipe.CODEC);
        }

        public void addEntries() {
            // Basic
            recipes.add(builder().withSourceCost(500).withPedestalItem(8, ItemsRegistry.SOURCE_GEM).withReagent(DIAMOND).withResult(MANA_DIAMOND.get()).build());
            recipes.add(builder().withSourceCost(10000).withPedestalItem(1, FLAME_SOUL.get()).withPedestalItem(1, FROST_SOUL.get()).withPedestalItem(1, EARTH_SOUL.get()).withReagent(MANA_DIAMOND.get()).withResult(ELEMENTAL_SOUL.get()).build());
            recipes.add(builder().withSourceCost(10000).withPedestalItem(1, FLAME_SOUL.get()).withPedestalItem(1, FROST_SOUL.get()).withPedestalItem(1, EARTH_SOUL.get()).withPedestalItem(1, HERO_SOUL.get()).withPedestalItem(1, LIGHTNING_SOUL.get()).withReagent(MANA_DIAMOND.get()).withResult(TRUE_ELEMENTAL_SOUL.get()).build());
            recipes.add(builder().withSourceCost(500).withPedestalItem(1, BASIC_SPELL_TURRET.asItem()).withReagent(ADVANCED_CLOCKWORK.get()).withResult(AUTO_TURRET_BLOCK.asItem()).build());

            recipes.add(builder().withSourceCost(7000).withPedestalItem(3, GOLD_INGOT).withPedestalItem(2, REPEATER).withPedestalItem(1, MANA_DIAMOND.get()).withPedestalItem(2, ADVANCED_CLOCKWORK.get()).withReagent(CLOCK).withResult(ENCHANTERS_STOPWATCH.get()).build());
            recipes.add(builder().withSourceCost(2500).withPedestalItem(4, GOLD_INGOT).withPedestalItem(4, MANA_DIAMOND.get()).withReagent(SUMMONING_FOCUS).withResult(GENERALS_WHEEL.get()).build());

            recipes.add(builder().withSourceCost(500).withPedestalItem(1, FIRE_ESSENCE).withReagent(ENDER_EYE).withResult(EYE_OF_FLAME.get()).build());
            recipes.add(builder().withSourceCost(500).withPedestalItem(1, WATER_ESSENCE).withReagent(ENDER_EYE).withResult(EYE_OF_FROST.get()).build());
            recipes.add(builder().withSourceCost(500).withPedestalItem(1, EARTH_ESSENCE).withReagent(ENDER_EYE).withResult(EYE_OF_EARTH.get()).build());
            recipes.add(builder().withSourceCost(500).withPedestalItem(1, AIR_ESSENCE).withPedestalItem(ELEMENTAL_SOUL.get()).withReagent(ENDER_EYE).withResult(EYE_OF_LIGHTNING.get()).build());
            recipes.add(builder().withSourceCost(500).withPedestalItem(1, CONJURATION_ESSENCE).withPedestalItem(ELEMENTAL_SOUL.get()).withReagent(ENDER_EYE).withResult(EYE_OF_HOLY.get()).build());
            recipes.add(builder().withSourceCost(500).withPedestalItem(1, ABJURATION_ESSENCE).withPedestalItem(TRUE_ELEMENTAL_SOUL.get()).withReagent(ENDER_EYE).withResult(EYE_OF_VOID.get()).build());

            // Perks
            recipes.add(builder().withResult(getPerkItem(SixeyesPerk.INSTANCE.getRegistryName())).withReagent(ItemsRegistry.BLANK_THREAD).withPedestalItem(2, VOID_SOUL.get()).withPedestalItem(2, END_CRYSTAL).withPedestalItem(2, NETHERITE_BLOCK).build());
            recipes.add(builder().withResult(getPerkItem(CloudStepsPerk.INSTANCE.getRegistryName())).withReagent(ItemsRegistry.BLANK_THREAD).withPedestalItem(2, LIGHTNING_SOUL.get()).withPedestalItem(ELYTRA).withPedestalItem(3,AIR_ESSENCE).build());
            recipes.add(builder().withResult(getPerkItem(ImmortalPerk.INSTANCE.getRegistryName())).withReagent(ItemsRegistry.BLANK_THREAD).withPedestalItem(2, HERO_SOUL.get()).withPedestalItem(ENCHANTED_GOLDEN_APPLE).withPedestalItem(3, GHAST_TEAR).build());
            recipes.add(builder().withResult(getPerkItem(DraconicHexPerk.INSTANCE.getRegistryName())).withReagent(ItemsRegistry.BLANK_THREAD).withPedestalItem(2, FLAME_SOUL.get()).withPedestalItem(WITHER_SKELETON_SKULL).withPedestalItem(2, BLAZE_POWDER).build());
            recipes.add(builder().withResult(getPerkItem(AdrenalinePerk.INSTANCE.getRegistryName())).withReagent(ItemsRegistry.BLANK_THREAD).withPedestalItem(2, FROST_SOUL.get()).withPedestalItem(PUFFERFISH).withPedestalItem(HEART_OF_THE_SEA).withPedestalItem(POWDER_SNOW_BUCKET).build());
            recipes.add(builder().withResult(getPerkItem(InvinciblePerk.INSTANCE.getRegistryName())).withReagent(ItemsRegistry.BLANK_THREAD).withPedestalItem(2, EARTH_SOUL.get()).withPedestalItem(ANVIL).withPedestalItem(3, NETHERITE_SCRAP).build());

            // Ryan Armor
            recipes.add(AAbuilder(new AArmorRecipe(3,3,builder().withResult(RYAN_HOOD.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_HOOD)).withPedestalItem(4,FLAME_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(7000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,3,builder().withResult(RYAN_ROBES.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_ROBE)).withPedestalItem(4,FLAME_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(7000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,3,builder().withResult(RYAN_LEGGINGS.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_LEG)).withPedestalItem(4,FLAME_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(7000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,3,builder().withResult(RYAN_BOOTS.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_BOOT)).withPedestalItem(4,FLAME_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(7000).keepNbtOfReagent(true).build().recipe())));

            recipes.add(AAbuilder(new AArmorRecipe(3,4,builder().withResult(RYAN_HOOD_A.get()).withReagent(RYAN_HOOD.get()).withPedestalItem(4,ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,4,builder().withResult(RYAN_ROBES_A.get()).withReagent(RYAN_ROBES.get()).withPedestalItem(4,ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,4,builder().withResult(RYAN_LEGGINGS_A.get()).withReagent(RYAN_LEGGINGS.get()).withPedestalItem(4,ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,4,builder().withResult(RYAN_BOOTS_A.get()).withReagent(RYAN_BOOTS.get()).withPedestalItem(4,ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));

            // Cade Armor
            recipes.add(AAbuilder(new AArmorRecipe(3,3,builder().withResult(CADE_HOOD.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_HOOD)).withPedestalItem(4,FROST_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(7000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,3,builder().withResult(CADE_ROBES.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_ROBE)).withPedestalItem(4,FROST_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(7000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,3,builder().withResult(CADE_LEGGINGS.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_LEG)).withPedestalItem(4,FROST_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(7000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,3,builder().withResult(CADE_BOOTS.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_BOOT)).withPedestalItem(4,FROST_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(7000).keepNbtOfReagent(true).build().recipe())));

            recipes.add(AAbuilder(new AArmorRecipe(3,4,builder().withResult(CADE_HOOD_A.get()).withReagent(CADE_HOOD.get()).withPedestalItem(4,ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,4,builder().withResult(CADE_ROBES_A.get()).withReagent(CADE_ROBES.get()).withPedestalItem(4,ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,4,builder().withResult(CADE_LEGGINGS_A.get()).withReagent(CADE_LEGGINGS.get()).withPedestalItem(4,ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,4,builder().withResult(CADE_BOOTS_A.get()).withReagent(CADE_BOOTS.get()).withPedestalItem(4,ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));

            // Nick Armor
            recipes.add(AAbuilder(new AArmorRecipe(3,3,builder().withResult(NICK_HOOD.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_HOOD)).withPedestalItem(4,EARTH_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(7000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,3,builder().withResult(NICK_ROBES.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_ROBE)).withPedestalItem(4,EARTH_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(7000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,3,builder().withResult(NICK_LEGGINGS.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_LEG)).withPedestalItem(4,EARTH_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(7000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,3,builder().withResult(NICK_BOOTS.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGIC_BOOT)).withPedestalItem(4,EARTH_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(7000).keepNbtOfReagent(true).build().recipe())));

            recipes.add(AAbuilder(new AArmorRecipe(3,4,builder().withResult(NICK_HOOD_A.get()).withReagent(NICK_HOOD.get()).withPedestalItem(4,ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,4,builder().withResult(NICK_ROBES_A.get()).withReagent(NICK_ROBES.get()).withPedestalItem(4,ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,4,builder().withResult(NICK_LEGGINGS_A.get()).withReagent(NICK_LEGGINGS.get()).withPedestalItem(4,ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(3,4,builder().withResult(NICK_BOOTS_A.get()).withReagent(NICK_BOOTS.get()).withPedestalItem(4,ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));

            // Cam Armor
            recipes.add(AAbuilder(new AArmorRecipe(4,4,builder().withResult(CAMR_HOOD.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_HOOD_A)).withPedestalItem(4,LIGHTNING_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(8000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(4,4,builder().withResult(CAMR_ROBES.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_ROBE_A)).withPedestalItem(4,LIGHTNING_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(8000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(4,4,builder().withResult(CAMR_LEGGINGS.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_LEG_A)).withPedestalItem(4,LIGHTNING_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(8000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(4,4,builder().withResult(CAMR_BOOTS.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_BOOT_A)).withPedestalItem(4,LIGHTNING_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(8000).keepNbtOfReagent(true).build().recipe())));

            recipes.add(AAbuilder(new AArmorRecipe(4,5,builder().withResult(CAMR_HOOD_A.get()).withReagent(CAMR_HOOD.get()).withPedestalItem(4,TRUE_ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(4,5,builder().withResult(CAMR_ROBES_A.get()).withReagent(CAMR_ROBES.get()).withPedestalItem(4,TRUE_ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(4,5,builder().withResult(CAMR_LEGGINGS_A.get()).withReagent(CAMR_LEGGINGS.get()).withPedestalItem(4,TRUE_ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(4,5,builder().withResult(CAMR_BOOTS_A.get()).withReagent(CAMR_BOOTS.get()).withPedestalItem(4,TRUE_ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build().recipe())));

            // Matt Armor
            recipes.add(AAbuilder(new AArmorRecipe(4,4,builder().withResult(MATT_HOOD.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_HOOD_A)).withPedestalItem(4,HERO_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(8000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(4,4,builder().withResult(MATT_ROBES.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_ROBE_A)).withPedestalItem(4,HERO_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(8000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(4,4,builder().withResult(MATT_LEGGINGS.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_LEG_A)).withPedestalItem(4,HERO_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(8000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(4,4,builder().withResult(MATT_BOOTS.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_BOOT_A)).withPedestalItem(4,HERO_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(8000).keepNbtOfReagent(true).build().recipe())));

            recipes.add(AAbuilder(new AArmorRecipe(4,5,builder().withResult(MATT_HOOD_A.get()).withReagent(MATT_HOOD.get()).withPedestalItem(4,TRUE_ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(4,5,builder().withResult(MATT_ROBES_A.get()).withReagent(MATT_ROBES.get()).withPedestalItem(4,TRUE_ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(4,5,builder().withResult(MATT_LEGGINGS_A.get()).withReagent(MATT_LEGGINGS.get()).withPedestalItem(4,TRUE_ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(4,5,builder().withResult(MATT_BOOTS_A.get()).withReagent(MATT_BOOTS.get()).withPedestalItem(4,TRUE_ELEMENTAL_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(9000).keepNbtOfReagent(true).build().recipe())));

            // Adam Armor
            recipes.add(AAbuilder(new AArmorRecipe(5,5,builder().withResult(ADAM_HOOD.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_HOOD_B)).withPedestalItem(4,VOID_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(5,5,builder().withResult(ADAM_ROBES.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_ROBE_B)).withPedestalItem(4,VOID_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(5,5,builder().withResult(ADAM_LEGGINGS.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_LEG_B)).withPedestalItem(4,VOID_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(5,5,builder().withResult(ADAM_BOOTS.get()).withReagent(Ingredient.of(AdamsItemTagsProvider.MAGE_BOOT_B)).withPedestalItem(4,VOID_SOUL.get()).withPedestalItem(4,MANA_DIAMOND.get()).withSourceCost(8500).keepNbtOfReagent(true).build().recipe())));

            recipes.add(AAbuilder(new AArmorRecipe(5,6,builder().withResult(ADAM_HOOD_A.get()).withReagent(ADAM_HOOD.get()).withPedestalItem(4,VOID_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(10000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(5,6,builder().withResult(ADAM_ROBES_A.get()).withReagent(ADAM_ROBES.get()).withPedestalItem(4,VOID_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(10000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(5,6,builder().withResult(ADAM_LEGGINGS_A.get()).withReagent(ADAM_LEGGINGS.get()).withPedestalItem(4,VOID_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(10000).keepNbtOfReagent(true).build().recipe())));
            recipes.add(AAbuilder(new AArmorRecipe(5,6,builder().withResult(ADAM_BOOTS_A.get()).withReagent(ADAM_BOOTS.get()).withPedestalItem(4,VOID_SOUL.get()).withPedestalItem(4,NETHERITE_INGOT).withSourceCost(10000).keepNbtOfReagent(true).build().recipe())));
        }

        protected static Path getRecipePath(Path pathIn, String str) {
            return pathIn.resolve("data/" + root + "/recipes/" + str + ".json");
        }

        @Override
        public String getName() {
            return "Apparatus";
        }
    }

    public static class ImbuementProvider extends ImbuementRecipeProvider {

        public List<ImbuementRecipe> recipes = new ArrayList<>();

        public ImbuementProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public @NotNull CompletableFuture<?> run(@NotNull CachedOutput pOutput) {
            collectJsons(pOutput);
            List<CompletableFuture<?>> futures = new ArrayList<>();
            return ModDatagen.registries.thenCompose((registry) -> {
                for (ImbuementRecipe g : recipes) {
                    Path path = getRecipePath(output, g.id.getPath());
                    futures.add(DataProvider.saveStable(pOutput, registry, ImbuementRecipe.CODEC, g, path));
                }
                return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            });
        }

        @Override
        public void collectJsons(CachedOutput cache) {
            System.out.println("started Imbument>collect jsons");

            recipes.add(new ImbuementRecipe("fire_SOUL.get()_brick_block", Ingredient.of(STONE_BRICKS), new ItemStack(FIRE_SOUL_BRICK_BLOCK.get()), 50).withPedestalItem(FLAME_SOUL.get()));
            recipes.add(new ImbuementRecipe("fire_SOUL.get()_brick_slab", Ingredient.of(STONE_BRICK_SLAB), new ItemStack(FIRE_SOUL_BRICK_SLAB.get()), 50).withPedestalItem(FLAME_SOUL.get()));
            recipes.add(new ImbuementRecipe("fire_SOUL.get()_brick_stair", Ingredient.of(STONE_BRICK_STAIRS), new ItemStack(FIRE_SOUL_BRICK_STAIR.get()), 50).withPedestalItem(FLAME_SOUL.get()));

            recipes.add(new ImbuementRecipe("frost_SOUL.get()_brick_block", Ingredient.of(STONE_BRICKS), new ItemStack(FROST_SOUL_BRICK_BLOCK.get()), 50).withPedestalItem(FROST_SOUL.get()));
            recipes.add(new ImbuementRecipe("frost_SOUL.get()_brick_slab", Ingredient.of(STONE_BRICK_SLAB), new ItemStack(FROST_SOUL_BRICK_SLAB.get()), 50).withPedestalItem(FROST_SOUL.get()));
            recipes.add(new ImbuementRecipe("frost_SOUL.get()_brick_stair", Ingredient.of(STONE_BRICK_STAIRS), new ItemStack(FROST_SOUL_BRICK_STAIR.get()), 50).withPedestalItem(FROST_SOUL.get()));

            recipes.add(new ImbuementRecipe("earth_SOUL.get()_brick_block", Ingredient.of(STONE_BRICKS), new ItemStack(EARTH_SOUL_BRICK_BLOCK.get()), 50).withPedestalItem(EARTH_SOUL.get()));
            recipes.add(new ImbuementRecipe("earth_SOUL.get()_brick_slab", Ingredient.of(STONE_BRICK_SLAB), new ItemStack(EARTH_SOUL_BRICK_SLAB.get()), 50).withPedestalItem(EARTH_SOUL.get()));
            recipes.add(new ImbuementRecipe("earth_SOUL.get()_brick_stair", Ingredient.of(STONE_BRICK_STAIRS), new ItemStack(EARTH_SOUL_BRICK_STAIR.get()), 50).withPedestalItem(EARTH_SOUL.get()));

            recipes.add(new ImbuementRecipe("lightning_SOUL.get()_brick_block", Ingredient.of(STONE_BRICKS), new ItemStack(LIGHTNING_SOUL_BRICK_BLOCK.get()), 50).withPedestalItem(LIGHTNING_SOUL.get()));
            recipes.add(new ImbuementRecipe("lightning_SOUL.get()_brick_slab", Ingredient.of(STONE_BRICK_SLAB), new ItemStack(LIGHTNING_SOUL_BRICK_SLAB.get()), 50).withPedestalItem(LIGHTNING_SOUL.get()));
            recipes.add(new ImbuementRecipe("lightning_SOUL.get()_brick_stair", Ingredient.of(STONE_BRICK_STAIRS), new ItemStack(LIGHTNING_SOUL_BRICK_STAIR.get()), 50).withPedestalItem(LIGHTNING_SOUL.get()));

            recipes.add(new ImbuementRecipe("holy_SOUL.get()_brick_block", Ingredient.of(STONE_BRICKS), new ItemStack(HOLY_SOUL_BRICK_BLOCK.get()), 50).withPedestalItem(HERO_SOUL.get()));
            recipes.add(new ImbuementRecipe("holy_SOUL.get()_brick_slab", Ingredient.of(STONE_BRICK_SLAB), new ItemStack(HOLY_SOUL_BRICK_SLAB.get()), 50).withPedestalItem(HERO_SOUL.get()));
            recipes.add(new ImbuementRecipe("holy_SOUL.get()_brick_stair", Ingredient.of(STONE_BRICK_STAIRS), new ItemStack(HOLY_SOUL_BRICK_STAIR.get()), 50).withPedestalItem(HERO_SOUL.get()));

            recipes.add(new ImbuementRecipe("void_SOUL.get()_brick_block", Ingredient.of(STONE_BRICKS), new ItemStack(VOID_SOUL_BRICK_BLOCK.get()), 50).withPedestalItem(VOID_SOUL.get()));
            recipes.add(new ImbuementRecipe("void_SOUL.get()_brick_slab", Ingredient.of(STONE_BRICK_SLAB), new ItemStack(VOID_SOUL_BRICK_SLAB.get()), 50).withPedestalItem(VOID_SOUL.get()));
            recipes.add(new ImbuementRecipe("void_SOUL.get()_brick_stair", Ingredient.of(STONE_BRICK_STAIRS), new ItemStack(VOID_SOUL_BRICK_STAIR.get()), 50).withPedestalItem(VOID_SOUL.get()));
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
