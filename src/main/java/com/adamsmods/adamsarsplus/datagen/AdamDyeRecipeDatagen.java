package com.adamsmods.adamsarsplus.datagen;

import com.google.gson.JsonElement;
import com.hollingsworth.arsnouveau.common.crafting.recipes.DyeRecipe;
import com.hollingsworth.arsnouveau.common.datagen.SimpleDataProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.ItemLike;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;
import static com.hollingsworth.arsnouveau.setup.registry.RegistryHelper.getRegistryName;


public class AdamDyeRecipeDatagen extends SimpleDataProvider {
    List<FileObj> files = new ArrayList<>();

    public AdamDyeRecipeDatagen(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void collectJsons(CachedOutput pOutput) {
        addDyeRecipe(CADE_BOOTS.get());
        addDyeRecipe(CADE_LEGGINGS.get());
        addDyeRecipe(CADE_ROBES.get());
        addDyeRecipe(CADE_HOOD.get());

        addDyeRecipe(CADE_BOOTS_A.get());
        addDyeRecipe(CADE_LEGGINGS_A.get());
        addDyeRecipe(CADE_ROBES_A.get());
        addDyeRecipe(CADE_HOOD_A.get());

        addDyeRecipe(RYAN_BOOTS.get());
        addDyeRecipe(RYAN_LEGGINGS.get());
        addDyeRecipe(RYAN_ROBES.get());
        addDyeRecipe(RYAN_HOOD.get());

        addDyeRecipe(RYAN_BOOTS_A.get());
        addDyeRecipe(RYAN_LEGGINGS_A.get());
        addDyeRecipe(RYAN_ROBES_A.get());
        addDyeRecipe(RYAN_HOOD_A.get());

        addDyeRecipe(NICK_BOOTS.get());
        addDyeRecipe(NICK_LEGGINGS.get());
        addDyeRecipe(NICK_ROBES.get());
        addDyeRecipe(NICK_HOOD.get());

        addDyeRecipe(NICK_BOOTS_A.get());
        addDyeRecipe(NICK_LEGGINGS_A.get());
        addDyeRecipe(NICK_ROBES_A.get());
        addDyeRecipe(NICK_HOOD_A.get());

        addDyeRecipe(CAMR_BOOTS.get());
        addDyeRecipe(CAMR_LEGGINGS.get());
        addDyeRecipe(CAMR_ROBES.get());
        addDyeRecipe(CAMR_HOOD.get());

        addDyeRecipe(CAMR_BOOTS_A.get());
        addDyeRecipe(CAMR_LEGGINGS_A.get());
        addDyeRecipe(CAMR_ROBES_A.get());
        addDyeRecipe(CAMR_HOOD_A.get());

        addDyeRecipe(MATT_BOOTS.get());
        addDyeRecipe(MATT_LEGGINGS.get());
        addDyeRecipe(MATT_ROBES.get());
        addDyeRecipe(MATT_HOOD.get());

        addDyeRecipe(MATT_BOOTS_A.get());
        addDyeRecipe(MATT_LEGGINGS_A.get());
        addDyeRecipe(MATT_ROBES_A.get());
        addDyeRecipe(MATT_HOOD_A.get());

        addDyeRecipe(ADAM_BOOTS.get());
        addDyeRecipe(ADAM_LEGGINGS.get());
        addDyeRecipe(ADAM_ROBES.get());
        addDyeRecipe(ADAM_HOOD.get());

        addDyeRecipe(ADAM_BOOTS_A.get());
        addDyeRecipe(ADAM_LEGGINGS_A.get());
        addDyeRecipe(ADAM_ROBES_A.get());
        addDyeRecipe(ADAM_HOOD_A.get());

        for (FileObj fileObj : files) {
            saveStable(pOutput, fileObj.element, fileObj.path);
        }
    }


    public void add(FileObj fileObj){
        files.add(fileObj);
    }

    public void addDyeRecipe(ItemLike inputItem){
        JsonElement dyeRecipe = DyeRecipe.asRecipe(inputItem.asItem());
        add(new FileObj(output.resolve("data/adamsarsplus/recipes/dye_" + getRegistryName(inputItem.asItem()).getPath() + ".json"), dyeRecipe));
    }

    @Override
    public String getName() {
        return "AdamsArsPlus: Json Datagen";
    }
    public record FileObj(Path path, JsonElement element){

    }
}