package com.adamsmods.adamsarsplus.glyphs.augment_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import net.minecraft.resources.ResourceLocation;


public class AugmentAccelerateThree extends AbstractAugment {

    public static final AugmentAccelerateThree INSTANCE = new AugmentAccelerateThree(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_accelerate_three"), "Accelerate III");

    public AugmentAccelerateThree(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public int getDefaultManaCost() {
        return AugmentAccelerate.INSTANCE.getDefaultManaCost() * 9;
    }

    @Override
    public SpellStats.Builder applyModifiers(SpellStats.Builder builder, AbstractSpellPart spellPart) {
        builder.addAccelerationModifier(4.0F);
        return super.applyModifiers(builder, spellPart);
    }
    @Override
    public String getBookDescription() {
        return "Immensely increases the speed of projectile spells.";
    }
}
