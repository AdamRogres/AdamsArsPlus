package adamsmods.adamsarsplus.recipe;

import adamsmods.adamsarsplus.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.perk.IPerkHolder;
import com.hollingsworth.arsnouveau.api.util.PerkUtil;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ApparatusRecipeInput;
import com.hollingsworth.arsnouveau.common.crafting.recipes.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ITextOutput;
import com.hollingsworth.arsnouveau.common.items.data.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.common.util.ANCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AArmorRecipe extends EnchantingApparatusRecipe implements ITextOutput {

    public int tier = 3; // 0 indexed
    public int outTier;

    public int tier() {
        return tier;
    }

    public int outTier() {
        return outTier;
    }

    public AArmorRecipe(Ingredient reagent, ItemStack result, List<Ingredient> pedestalItems, int cost) {
        super(reagent, result, pedestalItems, cost, true);
    }

    public AArmorRecipe(Ingredient reagent, ItemStack result, List<Ingredient> pedestalItems, int cost, int tier, int outTier) {
        super(reagent, result, pedestalItems, cost, true);
        this.tier = tier;
        this.outTier = outTier;
    }

    @Override
    public boolean matches(ApparatusRecipeInput input, Level level) {
        ArmorPerkHolder perkHolder = PerkUtil.getPerkHolder(input.catalyst());
        if (!(perkHolder instanceof ArmorPerkHolder armorPerkHolder)) {
            return false;
        }
        return armorPerkHolder.getTier() == (tier - 1) && super.matches(input, level);
    }

    @Override
    public @NotNull ItemStack assemble(ApparatusRecipeInput input, HolderLookup.@NotNull Provider provider) {
        ItemStack result = super.assemble(input, provider);
        if (!input.catalyst().isComponentsPatchEmpty()) {
            result.applyComponents(input.catalyst().getComponentsPatch());
            result.setDamageValue(0);
        }

        ArmorPerkHolder perkHolder = PerkUtil.getPerkHolder(input.catalyst());
        if (perkHolder instanceof ArmorPerkHolder armorPerkHolder) {
            armorPerkHolder.setTier(this.outTier - 1);
        }

        return result;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRegistry.A_ARMOR_UP.get();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRegistry.A_ARMOR_UP_SERIALIZER.get();
    }

    /**
     * Returns the component that should be displayed in the output slot.
     */
    @Override
    public Component getOutputComponent() {
        return Component.translatable("ars_nouveau.armor_upgrade.book_desc", tier);
    }

    @Override
    public boolean excludeJei() {
        return false;
    }

    public static class Serializer implements RecipeSerializer<AArmorRecipe> {
        //CODEC
        public @NotNull MapCodec<AArmorRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, AArmorRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static MapCodec<AArmorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("reagent").forGetter(AArmorRecipe::reagent),
                ItemStack.CODEC.fieldOf("result").forGetter(AArmorRecipe::result),
                Ingredient.CODEC.listOf().fieldOf("pedestalItems").forGetter(AArmorRecipe::pedestalItems),
                Codec.INT.fieldOf("sourceCost").forGetter(AArmorRecipe::sourceCost),
                Codec.INT.optionalFieldOf("tier", 3).forGetter(recipe -> recipe.tier),
                Codec.INT.optionalFieldOf("outTier", 3).forGetter(recipe -> recipe.outTier)
        ).apply(instance, AArmorRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, AArmorRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,
                AArmorRecipe::reagent,
                ItemStack.STREAM_CODEC,
                AArmorRecipe::result,
                ANCodecs.INGREDIENT_LIST_STREAM,
                AArmorRecipe::pedestalItems,
                ByteBufCodecs.VAR_INT,
                AArmorRecipe::sourceCost,
                ByteBufCodecs.VAR_INT,
                AArmorRecipe::tier,
                ByteBufCodecs.VAR_INT,
                AArmorRecipe::outTier,
                AArmorRecipe::new
        );
    }

}