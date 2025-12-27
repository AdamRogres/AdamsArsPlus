package com.adamsmods.adamsarsplus.client.patchouli;

import com.adamsmods.adamsarsplus.recipe.jei.AArmorRecipe;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AArmourUpgradeProcessor implements IComponentProcessor {
    AArmorRecipe recipe;

    @Override
    public void setup(Level level, IVariableProvider variables) {
        RecipeManager manager = level.getRecipeManager();
        String recipeID = variables.get("recipe").asString();
        if (manager.byKey(new ResourceLocation(recipeID)).orElse(null) instanceof AArmorRecipe ear)
            recipe = ear;
    }

    @Override
    public IVariable process(Level level, String key) {
        if (recipe == null)
            return null;
        if (key.equals("reagent"))
            return IVariable.wrapList(Arrays.stream(recipe.reagent.getItems()).map(IVariable::from).collect(Collectors.toList()));

        if (key.equals("recipe")) {
            return IVariable.wrap(recipe.getId().toString());
        }
        if (key.equals("tier")) {
            return IVariable.wrap(recipe.getOutputComponent().getString());
        }
        if (key.equals("output")) {
            return IVariable.from(recipe.result);
        }
        if (key.equals("footer")) {
            return IVariable.wrap(recipe.result.getItem().getDescriptionId());
        }

        return IVariable.empty();
    }
}
