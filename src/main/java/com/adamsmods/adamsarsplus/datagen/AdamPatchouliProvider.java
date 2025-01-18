package com.adamsmods.adamsarsplus.datagen;

import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectDomain;
import com.adamsmods.adamsarsplus.perk.*;
import com.adamsmods.adamsarsplus.ritual.RitualMageSummon;
import com.hollingsworth.arsnouveau.api.familiar.AbstractFamiliarHolder;
import com.hollingsworth.arsnouveau.api.perk.IPerk;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.datagen.PatchouliProvider;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.*;
import com.hollingsworth.arsnouveau.common.items.PerkItem;
import com.hollingsworth.arsnouveau.common.items.RitualTablet;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.registeredSpells;
import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;
import static net.minecraft.world.item.Items.ENCHANTED_BOOK;

public class AdamPatchouliProvider extends PatchouliProvider {
    public AdamPatchouliProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void collectJsons(CachedOutput cache) {

        for (AbstractSpellPart spell : registeredSpells) {
            addGlyphPage(spell);
        }

        addPage(new PatchouliBuilder(GETTING_STARTED, "adamsarsplus_tweaks")
                        .withIcon(EffectDomain.INSTANCE.glyphItem)
                        .withTextPage("adamsarsplus.page.adamsarsplus_tweaks")
                        .withTextPage("adamsarsplus.page.adamsarsplus_tweaks_2")
                , getPath(GETTING_STARTED, "adamsarsplus_tweaks"));

        addRitualPage(new RitualMageSummon());

        addBasicItem(ELEMENTAL_SOUL, RESOURCES, new ApparatusPage(ELEMENTAL_SOUL.get()));
        addBasicItem(TRUE_ELEMENTAL_SOUL, RESOURCES, new ApparatusPage(TRUE_ELEMENTAL_SOUL.get()));

        addPerkPage(SixeyesPerk.INSTANCE);
        addPerkPage(ImmortalPerk.INSTANCE);
        addPerkPage(CloudStepsPerk.INSTANCE);
        addPerkPage(DraconicHexPerk.INSTANCE);
        addPerkPage(AdrenalinePerk.INSTANCE);
        addPerkPage(InvinciblePerk.INSTANCE);

        addBasicItem(RYAN_HOOD, EQUIPMENT, new ApparatusPage(RYAN_HOOD.get()));
        addBasicItem(CADE_HOOD, EQUIPMENT, new ApparatusPage(CADE_HOOD.get()));
        addBasicItem(NICK_HOOD, EQUIPMENT, new ApparatusPage(NICK_HOOD.get()));
        addBasicItem(RYAN_HOOD_A, EQUIPMENT, new ApparatusPage(RYAN_HOOD_A.get()));
        addBasicItem(CADE_HOOD_A, EQUIPMENT, new ApparatusPage(CADE_HOOD_A.get()));
        addBasicItem(NICK_HOOD_A, EQUIPMENT, new ApparatusPage(NICK_HOOD_A.get()));

        addBasicItem(CAMR_HOOD, EQUIPMENT, new ApparatusPage(CAMR_HOOD.get()));
        addBasicItem(MATT_HOOD, EQUIPMENT, new ApparatusPage(MATT_HOOD.get()));
        addBasicItem(CAMR_HOOD_A, EQUIPMENT, new ApparatusPage(CAMR_HOOD_A.get()));
        addBasicItem(MATT_HOOD_A, EQUIPMENT, new ApparatusPage(MATT_HOOD_A.get()));

        addBasicItem(ADAM_HOOD, EQUIPMENT, new ApparatusPage(ADAM_HOOD.get()));
        addBasicItem(ADAM_HOOD_A, EQUIPMENT, new ApparatusPage(ADAM_HOOD_A.get()));

        for (PatchouliPage patchouliPage : pages) {
            saveStable(cache, patchouliPage.build(), patchouliPage.path());
        }

    }

    @Override
    public void addPerkPage(IPerk perk) {
        PerkItem perkItem = PerkRegistry.getPerkItemMap().get(perk.getRegistryName());
        PatchouliBuilder builder = new PatchouliBuilder(ARMOR, perkItem)
                .withIcon(perkItem)
                .withTextPage(perk.getDescriptionKey())
                .withPage(new ApparatusPage(perkItem)).withSortNum(99);
        this.pages.add(new PatchouliPage(builder, getPath(ARMOR, perk.getRegistryName().getPath() + ".json")));
    }

    @Override
    public PatchouliPage addBasicItem(ItemLike item, ResourceLocation category, IPatchouliPage recipePage) {
        PatchouliBuilder builder = new PatchouliBuilder(category, item.asItem().getDescriptionId())
                .withIcon(item.asItem())
                .withPage(new TextPage("adamsarsplus.page." + getRegistryName(item.asItem()).getPath()))
                .withPage(recipePage);
        var page = new PatchouliPage(builder, getPath(category, getRegistryName(item.asItem()).getPath()));
        this.pages.add(page);
        return page;
    }

    public void addFamiliarPage(AbstractFamiliarHolder familiarHolder) {
        PatchouliBuilder builder = new PatchouliBuilder(FAMILIARS, "entity.adamsarsplus." + familiarHolder.getRegistryName().getPath())
                .withIcon("adamsarsplus:" + familiarHolder.getRegistryName().getPath())
                .withTextPage("adamsarsplus.familiar_desc." + familiarHolder.getRegistryName().getPath())
                .withPage(new EntityPage(familiarHolder.getRegistryName().toString()));
        this.pages.add(new PatchouliPage(builder, getPath(FAMILIARS, familiarHolder.getRegistryName().getPath())));
    }

    public void addRitualPage(AbstractRitual ritual) {
        PatchouliBuilder builder = new PatchouliBuilder(RITUALS, "item.adamsarsplus." + ritual.getRegistryName().getPath())
                .withIcon(ritual.getRegistryName().toString())
                .withTextPage(ritual.getDescriptionKey())
                .withPage(new CraftingPage("adamsarsplus:tablet_" + ritual.getRegistryName().getPath()));

        this.pages.add(new PatchouliPage(builder, getPath(RITUALS, ritual.getRegistryName().getPath())));
    }

    public void addEnchantmentPage(Enchantment enchantment) {
        PatchouliBuilder builder = new PatchouliBuilder(ENCHANTMENTS, enchantment.getDescriptionId())
                .withIcon(getRegistryName(ENCHANTED_BOOK).toString())
                .withTextPage("adamsarsplus.enchantment_desc." + getRegistryName(enchantment).getPath());

        for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); i++) {
            builder.withPage(new EnchantingPage("ars_nouveau:" + getRegistryName(enchantment).getPath() + "_" + i));
        }
        this.pages.add(new PatchouliPage(builder, getPath(ENCHANTMENTS, getRegistryName(enchantment).getPath())));
    }

    public void  addGlyphPage(AbstractSpellPart spellPart) {
        ResourceLocation category = switch (spellPart.defaultTier().value) {
            case 1 -> GLYPHS_1;
            case 2 -> GLYPHS_2;
            default -> GLYPHS_3;
        };
        PatchouliBuilder builder = new PatchouliBuilder(category, spellPart.getName())
                .withName("adamsarsplus.glyph_name." + spellPart.getRegistryName().getPath())
                .withIcon(spellPart.getRegistryName().toString())
                .withSortNum(spellPart instanceof AbstractCastMethod ? 1 : spellPart instanceof AbstractEffect ? 2 : 3)
                .withPage(new TextPage("adamsarsplus.glyph_desc." + spellPart.getRegistryName().getPath()))
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

    ImbuementPage ImbuementPage(ItemLike item) {
        return new ImbuementPage("adamsarsplus:imbuement_" + getRegistryName(item.asItem()).getPath());
    }

    private ResourceLocation getRegistryName(Item asItem) {
        return ForgeRegistries.ITEMS.getKey(asItem);
    }

    private ResourceLocation getRegistryName(Enchantment enchantment) {
        return ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
    }

}