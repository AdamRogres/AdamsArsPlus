package adamsmods.adamsarsplus.client.example.caster_tools;

import adamsmods.adamsarsplus.common.items.example.caster_tools.SpellHorn;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static adamsmods.adamsarsplus.AdamsArsPlus.prefix;

public class SpellHornModel extends GeoModel<SpellHorn> {

    ResourceLocation MODEL =  prefix("geo/spell_horn.geo.json");
    ResourceLocation TEXTURE = prefix("textures/item/spell_horn.png");
    ResourceLocation ANIM = prefix("animations/item/spell_horn.animation.json");

    @Override
    public ResourceLocation getModelResource(SpellHorn object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SpellHorn object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(SpellHorn animatable) {
        return ANIM;
    }

}
