package adamsmods.adamsarsplus.datagen;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.ArsNouveauRegistry;
import adamsmods.adamsarsplus.common.glyphs.effect_glyph.EffectDomain;
import adamsmods.adamsarsplus.common.perk.*;
import adamsmods.adamsarsplus.common.rituals.*;

import static adamsmods.adamsarsplus.ArsNouveauRegistry.registeredRituals;
import static adamsmods.adamsarsplus.registry.ModEntities.*;

import com.google.gson.JsonObject;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.familiar.AbstractFamiliarHolder;
import com.hollingsworth.arsnouveau.api.perk.IPerk;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.datagen.SimpleDataProvider;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.*;
import com.hollingsworth.arsnouveau.common.items.PerkItem;
import com.hollingsworth.arsnouveau.common.perk.EmptyPerk;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static adamsmods.adamsarsplus.ArsNouveauRegistry.registeredSpells;
import static adamsmods.adamsarsplus.registry.ModBlocks.AUTO_TURRET_BLOCK;
import static adamsmods.adamsarsplus.registry.ModItems.*;
import static com.hollingsworth.arsnouveau.common.datagen.PatchouliProvider.*;
import static com.hollingsworth.arsnouveau.setup.registry.Documentation.addPage;
import static com.hollingsworth.nuggets.common.registry.RegistryHelper.getRegistryName;
import static net.minecraft.world.item.Items.ENCHANTED_BOOK;

public class AdamPatchouliProvider extends SimpleDataProvider {
    public List<PatchouliPage> pages = new ArrayList<>();

    public AdamPatchouliProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    public void addEntries() {

        for (AbstractRitual r : registeredRituals) {
            if(r.getRegistryName().getNamespace().equals(AdamsArsPlus.MODID))
                addRitualPage(r);
        }

        for (AbstractSpellPart s : registeredSpells) {
            if(s.getRegistryName().getNamespace().equals(AdamsArsPlus.MODID)) {
                addGlyphPage(s);
            }
        }

        for (IPerk perk : PerkRegistry.getPerkMap().values()) {
            if(perk.getRegistryName().getNamespace().equals(AdamsArsPlus.MODID) && !(perk instanceof EmptyPerk))
                addPerkPage(perk);
        }

        addPage(new PatchouliBuilder(GETTING_STARTED, "adamsarsplus_tweaks")
                .withIcon(EffectDomain.INSTANCE.glyphItem)
                .withTextPage("adamsarsplus.page.adamsarsplus_tweaks")
                .withTextPage("adamsarsplus.page.adamsarsplus_tweaks_2")
                , getPath(GETTING_STARTED, "adamsarsplus_tweaks"));

        addRitualPage(new RitualMageSummon());
        addRitualPage(new RitualTenShadows());

        addBasicItem(ELEMENTAL_SOUL.get(), RESOURCES, new ApparatusPage(ELEMENTAL_SOUL.get()));
        addBasicItem(TRUE_ELEMENTAL_SOUL.get(), RESOURCES, new ApparatusPage(TRUE_ELEMENTAL_SOUL.get()));
        addBasicItem(FLAME_SOUL.get(), RESOURCES, new EntityPage(RYAN_ENTITY.get()));
        addBasicItem(FROST_SOUL.get(), RESOURCES, new EntityPage(CADE_ENTITY.get()));
        addBasicItem(EARTH_SOUL.get(), RESOURCES, new EntityPage(NICK_ENTITY.get()));
        addBasicItem(LIGHTNING_SOUL.get(), RESOURCES, new EntityPage(CAM_ENTITY.get()));
        addBasicItem(HERO_SOUL.get(), RESOURCES, new EntityPage(MATT_ENTITY.get()));
        addBasicItem(VOID_SOUL.get(), RESOURCES, new EntityPage(ADAM_ENTITY.get()));
        addBasicItem(MAGE_CLOTH.get(), RESOURCES, new EntityPage(MAGE_ENTITY.get()));

        addBasicItem(EYE_OF_FLAME.get(), RITUALS, new ApparatusPage(EYE_OF_FLAME.get()));
        addBasicItem(EYE_OF_FROST.get(), RITUALS, new ApparatusPage(EYE_OF_FROST.get()));
        addBasicItem(EYE_OF_EARTH.get(), RITUALS, new ApparatusPage(EYE_OF_EARTH.get()));
        addBasicItem(EYE_OF_LIGHTNING.get(), RITUALS, new ApparatusPage(EYE_OF_LIGHTNING.get()));
        addBasicItem(EYE_OF_HOLY.get(), RITUALS, new ApparatusPage(EYE_OF_HOLY.get()));
        addBasicItem(EYE_OF_VOID.get(), RITUALS, new ApparatusPage(EYE_OF_VOID.get()));

        addPerkPage(SixeyesPerk.INSTANCE);
        addPerkPage(ImmortalPerk.INSTANCE);
        addPerkPage(CloudStepsPerk.INSTANCE);
        addPerkPage(DraconicHexPerk.INSTANCE);
        addPerkPage(AdrenalinePerk.INSTANCE);
        addPerkPage(InvinciblePerk.INSTANCE);

        addBasicItem(ENCHANTERS_STOPWATCH.get(), EQUIPMENT, new ApparatusPage(ENCHANTERS_STOPWATCH.get()));
        addBasicItem(GENERALS_WHEEL.get(), EQUIPMENT, new ApparatusPage(GENERALS_WHEEL.get()));
        addBasicItem(MAGE_TOME.get(), EQUIPMENT, new EntityPage(MAGE_KNIGHT.get()));

        addBasicItem(RYAN_HOOD.get(), EQUIPMENT, new MageArmorPage(RYAN_HOOD.get()));
        addBasicItem(CADE_HOOD.get(), EQUIPMENT, new MageArmorPage(CADE_HOOD.get()));
        addBasicItem(NICK_HOOD.get(), EQUIPMENT, new MageArmorPage(NICK_HOOD.get()));
        addBasicItem(RYAN_HOOD_A.get(), EQUIPMENT, new MageArmorPage(RYAN_HOOD_A.get()));
        addBasicItem(CADE_HOOD_A.get(), EQUIPMENT, new MageArmorPage(CADE_HOOD_A.get()));
        addBasicItem(NICK_HOOD_A.get(), EQUIPMENT, new MageArmorPage(NICK_HOOD_A.get()));
        addBasicItem(CAMR_HOOD.get(), EQUIPMENT, new MageArmorPage(CAMR_HOOD.get()));
        addBasicItem(MATT_HOOD.get(), EQUIPMENT, new MageArmorPage(MATT_HOOD.get()));
        addBasicItem(CAMR_HOOD_A.get(), EQUIPMENT, new MageArmorPage(CAMR_HOOD_A.get()));
        addBasicItem(MATT_HOOD_A.get(), EQUIPMENT, new MageArmorPage(MATT_HOOD_A.get()));
        addBasicItem(ADAM_HOOD.get(), EQUIPMENT, new MageArmorPage(ADAM_HOOD.get()));
        addBasicItem(ADAM_HOOD_A.get(), EQUIPMENT, new MageArmorPage(ADAM_HOOD_A.get()));

        addBasicItem(AUTO_TURRET_BLOCK, AUTOMATION, new ApparatusPage(AUTO_TURRET_BLOCK.get()));
    }

    public String getLangPath(String name, int count) {
        return "ars_nouveau.page" + count + "." + name;
    }

    public String getLangPath(String name) {
        return "ars_nouveau.page." + name;
    }

    public PatchouliPage addPage(PatchouliBuilder builder, Path path) {
        return addPage(new PatchouliPage(builder, path));
    }

    public PatchouliPage addPage(PatchouliPage patchouliPage){
        this.pages.add(patchouliPage);
        return patchouliPage;
    }

    public PatchouliBuilder buildBasicItem(ItemLike item, ResourceLocation category, IPatchouliPage recipePage) {
        PatchouliBuilder builder = new PatchouliBuilder(category, item.asItem().getDescriptionId())
                .withIcon(item.asItem())
                .withPage(new TextPage("ars_nouveau.page." + getRegistryName(item.asItem()).getPath()));
        if (recipePage != null) {
            builder.withPage(recipePage);
        }
        return builder;
    }

    public PatchouliPage addBasicItem(ItemLike item, ResourceLocation category, IPatchouliPage recipePage) {
        PatchouliBuilder builder = buildBasicItem(item, category, recipePage);
        return addPage(new PatchouliPage(builder, getPath(category, getRegistryName(item.asItem()))));
    }

    public Path getPath(ResourceLocation category, ResourceLocation fileName) {
        return this.output.resolve("assets/ars_nouveau/patchouli_books/worn_notebook/en_us/entries/" + category.getPath() + "/" + fileName.getPath() + ".json");
    }

    public Path getPath(ResourceLocation category, String fileName) {
        return this.output.resolve("assets/ars_nouveau/patchouli_books/worn_notebook/en_us/entries/" + category.getPath() + "/" + fileName + ".json");
    }

    @Override
    public void collectJsons(CachedOutput pOutput) {
        addEntries();
        for (PatchouliPage patchouliPage : pages) {
            saveStable(pOutput, patchouliPage.build(), patchouliPage.path);
        }
    }

    public record PatchouliPage(PatchouliBuilder builder, Path path) {
        @Override
        public Path path() {
            return path;
        }

        public JsonObject build() {
            return builder.build();
        }

        public String relationPath(){
            String fileName = path.getFileName().toString();
            fileName = FilenameUtils.removeExtension(fileName);
            return builder.category.toString() + "/" + fileName;
        }
    }

    public void addPerkPage(IPerk perk){
        PerkItem perkItem = PerkRegistry.getPerkItemMap().get(perk.getRegistryName());
        PatchouliBuilder builder = new PatchouliBuilder(ARMOR, perkItem)
                .withIcon(perkItem)
                .withTextPage(perk.getDescriptionKey())
                .withPage(new ApparatusPage(perkItem)).withSortNum(99);
        this.pages.add(new PatchouliPage(builder, this.output.resolve("assets/" + perk.getRegistryName().getNamespace() + "/patchouli_books/worn_notebook/en_us/entries/armor/" + perk.getRegistryName().getPath() + ".json")));
    }

    public void addFamiliarPage(AbstractFamiliarHolder familiarHolder) {
        PatchouliBuilder builder = new PatchouliBuilder(FAMILIARS, "entity.ars_nouveau." + familiarHolder.getRegistryName().getPath())
                .withIcon("ars_nouveau:" + familiarHolder.getRegistryName().getPath())
                .withTextPage("ars_nouveau.familiar_desc." + familiarHolder.getRegistryName().getPath())
                .withPage(new EntityPage(familiarHolder.getRegistryName().toString()));
        this.pages.add(new PatchouliPage(builder, this.output.resolve("assets/" + familiarHolder.getRegistryName().getNamespace() + "/patchouli_books/worn_notebook/en_us/entries/familiars/" + familiarHolder.getRegistryName().getPath() + ".json")));
    }

    public void addRitualPage(AbstractRitual ritual) {
        PatchouliBuilder builder = new PatchouliBuilder(RITUALS, "item." + ritual.getRegistryName().getNamespace() + "." + ritual.getRegistryName().getPath())
                .withIcon(ritual.getRegistryName().toString())
                .withTextPage(ritual.getDescriptionKey())
                .withPage(new CraftingPage(ritual.getRegistryName().toString()));

        this.pages.add(new PatchouliPage(builder, this.output.resolve("assets/" + ritual.getRegistryName().getNamespace() + "/patchouli_books/worn_notebook/en_us/entries/rituals/" + ritual.getRegistryName().getPath() + ".json")));
    }

    public void addGlyphPage(AbstractSpellPart spellPart) {
        ResourceLocation category = switch (spellPart.defaultTier().value) {
            case 1 -> GLYPHS_1;
            case 2 -> GLYPHS_2;
            default -> GLYPHS_3;
        };
        PatchouliBuilder builder = new PatchouliBuilder(category, spellPart.getName())
                .withName("ars_nouveau.glyph_name." + spellPart.getRegistryName().getPath())
                .withIcon(spellPart.getRegistryName().toString())
                .withSortNum(spellPart instanceof AbstractCastMethod ? 1 : spellPart instanceof AbstractEffect ? 2 : 3)
                .withPage(new TextPage("ars_nouveau.glyph_desc." + spellPart.getRegistryName().getPath()))
                .withPage(new GlyphScribePage(spellPart));
        this.pages.add(new PatchouliPage(builder, getPath(category, spellPart.getRegistryName().getPath())));
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    @Override
    public String getName() {
        return "Ars Plus Patchouli Datagen";
    }

    static class MageArmorPage extends ApparatusPage {
        public MageArmorPage(ItemLike itemLike) {
            super(itemLike);
        }

        @Override
        public ResourceLocation getType() {
            return ArsNouveau.prefix("a_armor_upgrade_recipe");
        }
    }

}