package adamsmods.adamsarsplus.util;

import adamsmods.adamsarsplus.common.entity.EntityDomainSpell;
import adamsmods.adamsarsplus.common.glyphs.effect_glyph.EffectDomain;
import adamsmods.adamsarsplus.common.glyphs.augment_glyph.AugmentOpenDomain;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

/** Preview the self-targeted domain used by boss goals without casting the spell. */
public final class BossDomainRange {
    public static boolean containsTarget(LivingEntity caster, LivingEntity target, Spell spell) {
        if (target == null || !target.isAlive() || !(caster.level() instanceof ServerLevel level)) return false;
        int index = spell.indexOf(EffectDomain.INSTANCE);
        if (index < 0) return false;
        SpellContext context = new SpellContext(level, spell, caster, new LivingCaster(caster));
        context.setCurrentIndex(index + 1);
        SpellStats stats = new SpellStats.Builder().setAugments(spell.getAugments(index, caster))
                .addItemsFromEntity(caster).build(EffectDomain.INSTANCE, new EntityHitResult(caster), level, caster, context);
        var center = EntityDomainSpell.alignToExistingDomain(level, caster.position(), caster.position());
        int radius = 4 + Math.round((float) stats.getAoeMultiplier());
        // Closed domains place shell blocks around the rounded block origin. Stay inside
        // the inner shell surface rather than merely inside the sure-hit's outer range.
        var shellCenter = BlockPos.containing(center);
        boolean open = stats.hasBuff(AugmentOpenDomain.INSTANCE);
        double distance = target.position().distanceToSqr(open ? center : shellCenter.getCenter());
        return DomainRules.contains(distance, open ? radius + 0.5 : Math.max(0, radius - 0.5),
                stats.hasBuff(AugmentPierce.INSTANCE), target.getY(), shellCenter.getY());
    }
}
