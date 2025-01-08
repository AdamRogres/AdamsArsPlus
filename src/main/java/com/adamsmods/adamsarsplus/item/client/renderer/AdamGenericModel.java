package com.adamsmods.adamsarsplus.item.client.renderer;

import com.hollingsworth.arsnouveau.client.renderer.tile.GenericModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class AdamGenericModel<T extends GeoAnimatable> extends GeoModel<T> {
    public String path;
    public ResourceLocation modelLocation;
    public ResourceLocation textLoc;
    public ResourceLocation animationLoc;
    public String textPathRoot;
    public String name;

    public AdamGenericModel(String name) {
        this.textPathRoot = "item/armor";
        this.modelLocation = new ResourceLocation("adamsarsplus", "geo/" + name + ".geo.json");
        this.textLoc = new ResourceLocation("adamsarsplus", "textures/" + this.textPathRoot + "/" + name + ".png");
        this.animationLoc = new ResourceLocation("adamsarsplus", "animations/" + name + "_animations.json");
        this.name = name;
    }

    public AdamGenericModel(String name, String textPath) {
        this(name);
        this.textPathRoot = textPath;
        this.textLoc = new ResourceLocation("adamsarsplus", "textures/" + this.textPathRoot + "/" + name + ".png");
    }

    public AdamGenericModel withEmptyAnim() {
        this.animationLoc = new ResourceLocation("adamsarsplus", "animations/empty.json");
        return this;
    }

    public ResourceLocation getModelResource(T GeoAnimatable) {
        return this.modelLocation;
    }

    public ResourceLocation getTextureResource(T GeoAnimatable) {
        return this.textLoc;
    }

    public ResourceLocation getAnimationResource(T GeoAnimatable) {
        return this.animationLoc;
    }
}