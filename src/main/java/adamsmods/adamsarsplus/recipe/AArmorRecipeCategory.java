package adamsmods.adamsarsplus.recipe;

import adamsmods.adamsarsplus.common.items.armor.MageMagicArmor;
import adamsmods.adamsarsplus.recipe.jei.JEIArsplusPlugin;
import com.hollingsworth.arsnouveau.api.perk.IPerkHolder;
import com.hollingsworth.arsnouveau.api.perk.IPerkProvider;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.util.PerkUtil;
import com.hollingsworth.arsnouveau.client.jei.EnchantingApparatusRecipeCategory;
import com.hollingsworth.arsnouveau.common.items.data.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AArmorRecipeCategory extends EnchantingApparatusRecipeCategory<AArmorRecipe> {
    public AArmorRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<AArmorRecipe> holder, IFocusGroup focuses) {
        MultiProvider provider = multiProvider.apply(holder.value());
        List<Ingredient> inputs = provider.input();
        double angleBetweenEach = 360.0 / inputs.size();
        if (provider.optionalCenter() != null) {
            var stacks = provider.optionalCenter().getItems();
            var list = Arrays.stream(stacks).map(stack -> {
                var newStack = stack;
                ArmorPerkHolder armorPerkHolder = PerkUtil.getPerkHolder(stack);
                if (armorPerkHolder != null) {
                    newStack = stack.copy();
                    newStack.set(DataComponentRegistry.ARMOR_PERKS, armorPerkHolder.setTier(holder.value().tier - 1));
                }
                return newStack;
            }).toList();
            builder.addSlot(RecipeIngredientRole.INPUT, 48, 45).addItemStacks(list);
        }
        for (Ingredient input : inputs) {
            builder.addSlot(RecipeIngredientRole.INPUT, (int) point.x, (int) point.y).addIngredients(input);
            point = rotatePointAbout(point, center, angleBetweenEach);
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 10).addItemStacks(Collections.singletonList(provider.output()));
    }

    public Component getTitle() {
        return Component.translatable("ars_nouveau.armor_upgrade");
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<AArmorRecipe>> getRecipeType() {
        return JEIArsplusPlugin.A_ARMOR_RECIPE_TYPE.get();
    }

    @Override
    public void draw(RecipeHolder<AArmorRecipe> holder, @NotNull IRecipeSlotsView slotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        AArmorRecipe recipe = holder.value();
        Font renderer = Minecraft.getInstance().font;
        guiGraphics.drawString(renderer, Component.translatable("ars_nouveau.tier.prerequired", recipe.tier), 0, 0, 10, false);

        if (recipe.consumesSource())
            guiGraphics.drawString(renderer, Component.translatable("ars_nouveau.source", recipe.sourceCost()), 0, 100, 10, false);
    }
}
