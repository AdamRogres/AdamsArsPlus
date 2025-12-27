package com.adamsmods.adamsarsplus.client.patchouli.component;

import com.adamsmods.adamsarsplus.recipe.jei.AArmorRecipe;
import com.adamsmods.adamsarsplus.recipe.jei.JEIArsplusPlugin;
import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.IEnchantingRecipe;
import com.hollingsworth.arsnouveau.setup.registry.RecipeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import vazkii.patchouli.api.IVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import static com.adamsmods.adamsarsplus.registry.ModRegistry.A_ARMOR_UP;

public class AdamRotatingItemListComponent extends RotatingItemListComponentBase {
    @SerializedName("recipe_name")
    public String recipeName;

    @SerializedName("recipe_type")
    public String recipeType;

    @Override
    protected List<Ingredient> makeIngredients() {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) return new ArrayList<>();

        Map<ResourceLocation, ? extends Recipe<?>> map;
        if ("a_armor_upgrade".equals(recipeType)) {
            AArmorRecipe recipe = world.getRecipeManager().getAllRecipesFor(A_ARMOR_UP.get()).stream().filter(f -> f.id.toString().equals(recipeName)).findFirst().orElse(null);
            for(RecipeType type : ArsNouveauAPI.getInstance().getEnchantingRecipeTypes()){
                RecipeType<IEnchantingRecipe> enchantingRecipeRecipeType = (RecipeType<IEnchantingRecipe>) type;
                Recipe<?> recipe1 = world.getRecipeManager().getAllRecipesFor(enchantingRecipeRecipeType).stream().filter(f -> f.getId().toString().equals(recipeName)).findFirst().orElse(null);
                if(recipe1 instanceof AArmorRecipe aApparatusRecipe){
                    recipe = aApparatusRecipe;
                    break;
                }
            }
            return recipe == null ? ImmutableList.of() : recipe.pedestalItems;
        } else {
            throw new IllegalArgumentException("Type must be 'a_armor_upgrade'!");
        }
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        recipeName = lookup.apply(IVariable.wrap(recipeName)).asString();
        recipeType = lookup.apply(IVariable.wrap(recipeType)).asString();
    }
}