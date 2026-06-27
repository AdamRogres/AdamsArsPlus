package adamsmods.adamsarsplus.datagen;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.lib.AdamsEntityTags;
import com.hollingsworth.arsnouveau.common.lib.EntityTags;
import com.hollingsworth.arsnouveau.setup.registry.ModEntities;
import static adamsmods.adamsarsplus.registry.ModEntities.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AdamsEntityTagProvider extends EntityTypeTagsProvider {
    public AdamsEntityTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, AdamsArsPlus.MODID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(AdamsEntityTags.DOMAIN_BLACKLIST).add(
                ModEntities.LIGHTNING_ENTITY.get(), 
                ModEntities.LINGER_SPELL.get(), 
                ModEntities.WALL_SPELL.get(), 
                DOMAIN_SPELL.get(),
                FIRE_ENTITY.get(),
                METEOR_SPELL.get());

        this.tag(Tags.EntityTypes.BOSSES).add(
                RYAN_ENTITY.get(),
                CADE_ENTITY.get(),
                NICK_ENTITY.get(),
                CAM_ENTITY.get(),
                MATT_ENTITY.get(),
                JOSH_ENTITY.get(),
                ADAM_ENTITY.get());

        this.tag(EntityTags.JAR_BLACKLIST).add(
                RYAN_ENTITY.get(),
                CADE_ENTITY.get(),
                NICK_ENTITY.get(),
                CAM_ENTITY.get(),
                MATT_ENTITY.get(),
                JOSH_ENTITY.get(),
                ADAM_ENTITY.get());

        this.tag(EntityTags.DISINTEGRATION_BLACKLIST).add(
                RYAN_ENTITY.get(),
                CADE_ENTITY.get(), 
                NICK_ENTITY.get(), 
                CAM_ENTITY.get(), 
                MATT_ENTITY.get(),
                JOSH_ENTITY.get(),
                ADAM_ENTITY.get());

        this.tag(EntityTags.MAGIC_FIND).add(
                RYAN_ENTITY.get(), 
                CADE_ENTITY.get(), 
                NICK_ENTITY.get(), 
                CAM_ENTITY.get(), 
                MATT_ENTITY.get(),
                JOSH_ENTITY.get(),
                ADAM_ENTITY.get(), 
                MAGE_ENTITY.get(),
                MAGE_KNIGHT.get(),
                FLAME_MAGE_ENTITY.get(),
                FLAME_KNIGHT.get(),
                FROST_MAGE_ENTITY.get(),
                FROST_KNIGHT.get(),
                EARTH_MAGE_ENTITY.get(),
                EARTH_KNIGHT.get(),
                LIGHTNING_MAGE_ENTITY.get(),
                LIGHTNING_KNIGHT.get(),
                HOLY_MAGE_ENTITY.get(),
                HOLY_KNIGHT.get(),
                VOID_MAGE_ENTITY.get(),
                VOID_KNIGHT.get());

        this.tag(EntityTags.HOSTILE_MOBS).add(
                MAGE_ENTITY.get(),
                MAGE_KNIGHT.get(),
                FLAME_MAGE_ENTITY.get(),
                FLAME_KNIGHT.get(),
                FROST_MAGE_ENTITY.get(),
                FROST_KNIGHT.get(),
                EARTH_MAGE_ENTITY.get(),
                EARTH_KNIGHT.get(),
                LIGHTNING_MAGE_ENTITY.get(),
                LIGHTNING_KNIGHT.get(),
                HOLY_MAGE_ENTITY.get(),
                HOLY_KNIGHT.get(),
                VOID_MAGE_ENTITY.get(),
                VOID_KNIGHT.get());

        this.tag(EntityTags.LINGERING_BLACKLIST).add(
                DOMAIN_SPELL.get(),
                FIRE_ENTITY.get());

    }

    private static TagKey<EntityType<?>> create(ResourceLocation pName) {
        return TagKey.create(Registries.ENTITY_TYPE, pName);
    }
}