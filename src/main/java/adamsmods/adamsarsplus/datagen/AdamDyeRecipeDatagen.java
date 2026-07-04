package adamsmods.adamsarsplus.datagen;

import com.google.gson.JsonElement;
import com.hollingsworth.arsnouveau.common.crafting.recipes.DyeRecipe;
import com.hollingsworth.arsnouveau.common.datagen.SimpleDataProvider;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.NonNullList;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static adamsmods.adamsarsplus.registry.ModItems.*;
import static com.hollingsworth.arsnouveau.setup.registry.RegistryHelper.getRegistryName;

public class AdamDyeRecipeDatagen extends SimpleDataProvider {
    List<FileObj> files = new ArrayList<>();

    public AdamDyeRecipeDatagen(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void collectJsons(CachedOutput pOutput) {
        addDyeRecipe(CADE_BOOTS.get().asItem());
        addDyeRecipe(CADE_LEGGINGS.get().asItem());
        addDyeRecipe(CADE_ROBES.get().asItem());
        addDyeRecipe(CADE_HOOD.get().asItem());

        addDyeRecipe(CADE_BOOTS_A.get().asItem());
        addDyeRecipe(CADE_LEGGINGS_A.get().asItem());
        addDyeRecipe(CADE_ROBES_A.get().asItem());
        addDyeRecipe(CADE_HOOD_A.get().asItem());

        addDyeRecipe(RYAN_BOOTS.get().asItem());
        addDyeRecipe(RYAN_LEGGINGS.get().asItem());
        addDyeRecipe(RYAN_ROBES.get().asItem());
        addDyeRecipe(RYAN_HOOD.get().asItem());

        addDyeRecipe(RYAN_BOOTS_A.get().asItem());
        addDyeRecipe(RYAN_LEGGINGS_A.get().asItem());
        addDyeRecipe(RYAN_ROBES_A.get().asItem());
        addDyeRecipe(RYAN_HOOD_A.get().asItem());

        addDyeRecipe(NICK_BOOTS.get().asItem());
        addDyeRecipe(NICK_LEGGINGS.get().asItem());
        addDyeRecipe(NICK_ROBES.get().asItem());
        addDyeRecipe(NICK_HOOD.get().asItem());

        addDyeRecipe(NICK_BOOTS_A.get().asItem());
        addDyeRecipe(NICK_LEGGINGS_A.get().asItem());
        addDyeRecipe(NICK_ROBES_A.get().asItem());
        addDyeRecipe(NICK_HOOD_A.get().asItem());

        addDyeRecipe(CAMR_BOOTS.get().asItem());
        addDyeRecipe(CAMR_LEGGINGS.get().asItem());
        addDyeRecipe(CAMR_ROBES.get().asItem());
        addDyeRecipe(CAMR_HOOD.get().asItem());

        addDyeRecipe(CAMR_BOOTS_A.get().asItem());
        addDyeRecipe(CAMR_LEGGINGS_A.get().asItem());
        addDyeRecipe(CAMR_ROBES_A.get().asItem());
        addDyeRecipe(CAMR_HOOD_A.get().asItem());

        addDyeRecipe(MATT_BOOTS.get().asItem());
        addDyeRecipe(MATT_LEGGINGS.get().asItem());
        addDyeRecipe(MATT_ROBES.get().asItem());
        addDyeRecipe(MATT_HOOD.get().asItem());

        addDyeRecipe(MATT_BOOTS_A.get().asItem());
        addDyeRecipe(MATT_LEGGINGS_A.get().asItem());
        addDyeRecipe(MATT_ROBES_A.get().asItem());
        addDyeRecipe(MATT_HOOD_A.get().asItem());

        addDyeRecipe(ADAM_BOOTS.get().asItem());
        addDyeRecipe(ADAM_LEGGINGS.get().asItem());
        addDyeRecipe(ADAM_ROBES.get().asItem());
        addDyeRecipe(ADAM_HOOD.get().asItem());

        addDyeRecipe(ADAM_BOOTS_A.get().asItem());
        addDyeRecipe(ADAM_LEGGINGS_A.get().asItem());
        addDyeRecipe(ADAM_ROBES_A.get().asItem());
        addDyeRecipe(ADAM_HOOD_A.get().asItem());

        for (FileObj fileObj : files) {
            saveStable(pOutput, fileObj.element, fileObj.path);
        }
    }


    public void add(FileObj fileObj){
        files.add(fileObj);
    }

    public void addDyeRecipe(ItemLike inputItem){
        var dyeRecipe = new DyeRecipe("", CraftingBookCategory.MISC, inputItem.asItem().getDefaultInstance(), NonNullList.of(Ingredient.EMPTY, Ingredient.of(Tags.Items.DYES), Ingredient.of(inputItem)));
        files.add(new FileObj(resolvePath("data/ars_nouveau/recipe/dye_" + getRegistryName(inputItem.asItem()).getPath() + ".json"), DyeRecipe.CODEC.encodeStart(JsonOps.INSTANCE, dyeRecipe).getOrThrow()));
    }

    @Override
    public @NotNull String getName() {
        return "AdamsArsPlus: Json Datagen";
    }

    Path resolvePath(String path) {
        return this.generator.getPackOutput().getOutputFolder().resolve(path);
    }

    public record FileObj(Path path, JsonElement element){

    }
}