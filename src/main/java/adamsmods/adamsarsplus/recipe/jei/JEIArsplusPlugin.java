package adamsmods.adamsarsplus.recipe.jei;

import adamsmods.adamsarsplus.recipe.AArmorRecipe;
import adamsmods.adamsarsplus.recipe.AArmorRecipeCategory;
import com.alexthw.sauce.registry.ModRegistry;
import com.hollingsworth.arsnouveau.client.container.IAutoFillTerminal;
import com.hollingsworth.arsnouveau.client.jei.CraftingTerminalTransferHandler;
import com.hollingsworth.arsnouveau.client.jei.DyeRecipeCategory;
import com.hollingsworth.arsnouveau.common.crafting.recipes.DyeRecipe;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static adamsmods.adamsarsplus.AdamsArsPlus.MODID;
import static adamsmods.adamsarsplus.registry.ModRegistry.A_ARMOR_UP;
import static mezz.jei.api.recipe.RecipeType.createFromDeferredVanilla;

@JeiPlugin
public class JEIArsplusPlugin implements IModPlugin {
    public static final Supplier<RecipeType<RecipeHolder<AArmorRecipe>>> A_ARMOR_RECIPE_TYPE = createFromDeferredVanilla(A_ARMOR_UP);

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(MODID, "main");
    }

    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new AArmorRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    // May need to comment this out
    @SuppressWarnings("unchecked")
    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registry) {
        assert Minecraft.getInstance().level != null;
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        List<RecipeHolder<AArmorRecipe>> armorRecipes = new ArrayList<>();
        for (RecipeHolder<?> i : manager.getRecipes()) {
            switch (i.value()) {
                case AArmorRecipe recipe -> armorRecipes.add((RecipeHolder<AArmorRecipe>) i);
                default -> {
                }
            }
        }
        registry.addRecipes(A_ARMOR_RECIPE_TYPE.get(), armorRecipes);
    }
    // =======

    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(BlockRegistry.ENCHANTING_APP_BLOCK), A_ARMOR_RECIPE_TYPE.get());
    }
}

