package com.adamsmods.adamsarsplus.recipe.jei;

import com.adamsmods.adamsarsplus.item.armor.MageMagicArmor;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.ArmorUpgradeRecipe;
import com.hollingsworth.arsnouveau.api.perk.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.api.perk.IPerkHolder;
import com.hollingsworth.arsnouveau.api.perk.IPerkProvider;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.client.jei.EnchantingApparatusRecipeCategory;
import com.hollingsworth.arsnouveau.client.jei.JEIArsNouveauPlugin;
import com.hollingsworth.arsnouveau.client.jei.MultiInputCategory;
import com.hollingsworth.arsnouveau.common.armor.AnimatedMagicArmor;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AArmorRecipeCategory extends EnchantingApparatusRecipeCategory<AArmorRecipe> {
    public AArmorRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, AArmorRecipe recipe, IFocusGroup focuses) {
        MultiInputCategory.MultiProvider provider = (MultiInputCategory.MultiProvider)this.multiProvider.apply(recipe);
        List<Ingredient> inputs = provider.input();
        double angleBetweenEach = (double)360.0F / (double)inputs.size();
        List<ItemStack> stacks = PerkRegistry.getPerkProviderItems().stream().filter((item) -> {
            boolean var10000;
            if (item instanceof MageMagicArmor ama) {
                if (ama.getMinTier() < recipe.tier) {
                    var10000 = true;
                    return var10000;
                }
            }

            var10000 = false;
            return var10000;
        }).map(Item::getDefaultInstance).toList();
        List<ItemStack> outputs = new ArrayList();
        if (!focuses.isEmpty()) {
            List<ItemStack> list = focuses.getItemStackFocuses(RecipeIngredientRole.CATALYST).map((i) -> ((ItemStack)i.getTypedValue().getIngredient()).copy()).filter((i) -> i.getItem() instanceof MageMagicArmor).toList();
            List<ItemStack> list2 = focuses.getItemStackFocuses(RecipeIngredientRole.OUTPUT).map((i) -> ((ItemStack)i.getTypedValue().getIngredient()).copy()).filter((i) -> i.getItem() instanceof MageMagicArmor).toList();
            if (!list.isEmpty()) {
                stacks = list;
            } else if (!list2.isEmpty()) {
                stacks = list2;
            }
        }

        for(ItemStack stack : stacks) {
            ItemStack copy = stack.copy();
            IPerkProvider<ItemStack> perkProvider = PerkRegistry.getPerkProvider(stack.getItem());
            if (perkProvider != null) {
                IPerkHolder var15 = perkProvider.getPerkHolder(stack);
                if (var15 instanceof ArmorPerkHolder) {
                    ArmorPerkHolder armorPerkHolder = (ArmorPerkHolder)var15;
                    armorPerkHolder.setTier(recipe.tier - 1);
                }

                var15 = perkProvider.getPerkHolder(copy);
                if (var15 instanceof ArmorPerkHolder) {
                    ArmorPerkHolder armorPerkHolder = (ArmorPerkHolder)var15;
                    armorPerkHolder.setTier(recipe.tier);
                }
            }

            outputs.add(copy);
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 48, 45).addItemStacks(stacks);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 10).addItemStacks(outputs);

        for(Ingredient input : inputs) {
            builder.addSlot(RecipeIngredientRole.INPUT, (int)this.point.x, (int)this.point.y).addIngredients(input);
            this.point = rotatePointAbout(this.point, this.center, angleBetweenEach);
        }

    }

    public Component getTitle() {
        return Component.translatable("ars_nouveau.armor_upgrade");
    }

    public RecipeType<AArmorRecipe> getRecipeType() {
        return JEIArsplusPlugin.A_ARMOR_RECIPE_TYPE;
    }

    public void draw(AArmorRecipe recipe, @NotNull IRecipeSlotsView slotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        Font renderer = Minecraft.getInstance().font;
        graphics.drawString(renderer, Component.translatable("ars_nouveau.tier", new Object[]{1 + recipe.tier}), 0, 0, 10, false);
        if (recipe.consumesSource()) {
            graphics.drawString(renderer, Component.translatable("ars_nouveau.source", new Object[]{recipe.sourceCost}), 0, 100, 10, false);
        }

    }
}
