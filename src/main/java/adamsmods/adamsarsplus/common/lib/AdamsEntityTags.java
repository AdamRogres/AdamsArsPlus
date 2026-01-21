package adamsmods.adamsarsplus.common.lib;

import adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class AdamsEntityTags {
     public static final TagKey<EntityType<?>> DOMAIN_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, AdamsArsPlus.prefix("domain_blacklist"));
}