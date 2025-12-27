package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class FilterNotSelf extends AbstractEffect {
    public FilterNotSelf(ResourceLocation tag, String description) {
        super(tag, description);
    }
    public static final FilterNotSelf INSTANCE = new FilterNotSelf(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_filternotself"), "Filter: Not Self");

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if ((rayTraceResult.getEntity().equals(shooter))) {
            spellContext.setCanceled(true);
        }
    }

    @Override
    protected int getDefaultManaCost() {
        return 0;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return setOf();
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.MANIPULATION);
    }
}
