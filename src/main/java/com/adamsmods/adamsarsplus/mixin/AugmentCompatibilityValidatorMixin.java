package com.adamsmods.adamsarsplus.mixin;

import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.validation.AugmentCompatibilityValidator;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAOEThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAOETwo;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateTwo;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentExtendTimeThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentExtendTimeTwo;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Mixin(value = AugmentCompatibilityValidator.class, remap = false)
public class AugmentCompatibilityValidatorMixin {
    @Redirect(method = "*",
            at = @At(value = "FIELD",
                    target = "Lcom/hollingsworth/arsnouveau/api/spell/AbstractSpellPart;compatibleAugments",
                    opcode = Opcodes.GETFIELD))
    private static Set<AbstractAugment> adjustCompatibleAugments(@Nonnull AbstractSpellPart instance) {
        Set<AbstractAugment> compatibleAugments = new HashSet<>(instance.compatibleAugments);
        if (compatibleAugments.contains(AugmentAOE.INSTANCE)) {
            compatibleAugments.add(AugmentAOETwo.INSTANCE);
            compatibleAugments.add(AugmentAOEThree.INSTANCE);
        }
        if (compatibleAugments.contains(AugmentAccelerate.INSTANCE)) {
            compatibleAugments.add(AugmentAccelerateTwo.INSTANCE);
            compatibleAugments.add(AugmentAccelerateThree.INSTANCE);
        }
        if (compatibleAugments.contains(AugmentExtendTime.INSTANCE)) {
            compatibleAugments.add(AugmentExtendTimeTwo.INSTANCE);
            compatibleAugments.add(AugmentExtendTimeThree.INSTANCE);
        }
        return Collections.unmodifiableSet(compatibleAugments);
    }
}
