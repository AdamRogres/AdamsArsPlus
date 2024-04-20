package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

public class FilterIsNotSelf extends AbstractEffect {
    public FilterIsNotSelf(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public static final FilterIsNotSelf INSTANCE = new FilterIsNotSelf(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_filter_not_self"), "Filter Not Self");

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nonnull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if ((rayTraceResult.getEntity().equals(shooter))) {
            spellContext.setCanceled(true);
        }
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return Collections.emptySet();
    }

    @Override
    public int getDefaultManaCost() {
        return 0;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.MANIPULATION);
    }
}





