package com.adamsmods.adamsarsplus.recipe.jei;

import com.hollingsworth.arsnouveau.api.enchanting_apparatus.ArmorUpgradeRecipe;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantmentRecipe;
import com.hollingsworth.arsnouveau.client.container.IAutoFillTerminal;
import com.hollingsworth.arsnouveau.client.jei.*;
import com.hollingsworth.arsnouveau.common.crafting.recipes.CrushRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.DyeRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectCrush;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.RecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.List;

import static com.adamsmods.adamsarsplus.AdamsArsPlus.MOD_ID;

@JeiPlugin
public class JEIArsplusPlugin  implements IModPlugin {
    public static final RecipeType<AArmorRecipe> A_ARMOR_RECIPE_TYPE = RecipeType.create(MOD_ID, "a_armor_upgrade", AArmorRecipe.class);
    private static IJeiRuntime jeiRuntime;

    public JEIArsplusPlugin() {
    }

    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MOD_ID, "main");
    }

    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new IRecipeCategory[]{ new AArmorRecipeCategory(registry.getJeiHelpers().getGuiHelper())});
    }

    /*
    public void registerRecipes(IRecipeRegistration registry) {
        List<AArmorRecipe> aarmorUpgrades = new ArrayList();

        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        for(Recipe<?> i : manager.getRecipes()) {

            if (i instanceof AArmorRecipe upgradeRecipe) {
                aarmorUpgrades.add(upgradeRecipe);
            }
        }


        registry.addRecipes(A_ARMOR_RECIPE_TYPE, aarmorUpgrades);
    }*/

    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(BlockRegistry.ENCHANTING_APP_BLOCK), new RecipeType[]{A_ARMOR_RECIPE_TYPE});
    }

    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    }

    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        CraftingTerminalTransferHandler.registerTransferHandlers(registration);
    }

    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addCategoryExtension(DyeRecipe.class, DyeRecipeCategory::new);
    }

    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        JEIArsplusPlugin.jeiRuntime = jeiRuntime;
    }

    static {
        IAutoFillTerminal.updateSearch.add(new IAutoFillTerminal.ISearchHandler() {
            public void setSearch(String text) {
                if (JEIArsplusPlugin.jeiRuntime != null && JEIArsplusPlugin.jeiRuntime.getIngredientFilter() != null) {
                    JEIArsplusPlugin.jeiRuntime.getIngredientFilter().setFilterText(text);
                }

            }

            public String getSearch() {
                return JEIArsplusPlugin.jeiRuntime != null && JEIArsplusPlugin.jeiRuntime.getIngredientFilter() != null ? JEIArsplusPlugin.jeiRuntime.getIngredientFilter().getFilterText() : "";
            }

            public String getName() {
                return "JEI";
            }
        });
    }
}

