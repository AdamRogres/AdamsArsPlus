package com.adamsmods.adamsarsplus.glyphs.augment_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import net.minecraft.resources.ResourceLocation;


public class AugmentAccelerateTwo extends AbstractAugment {

    public static final AugmentAccelerateTwo INSTANCE = new AugmentAccelerateTwo(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_accelerate_two"), "Accelerate II");

    public AugmentAccelerateTwo(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentAccelerate.INSTANCE.getDefaultManaCost() * 4;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAccelerationModifier(2.0F);
        return super.applyModifiers(builder, spellPart);
    }
    @Override
    public String getBookDescription() {
        return "Greatly increases the speed of projectile spells.";
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }
}
