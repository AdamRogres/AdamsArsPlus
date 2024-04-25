package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.ArsNouveauRegistry;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.entity.EnchantedFallingBlock;
import com.hollingsworth.arsnouveau.common.items.curios.ShapersFocus;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class EffectLimitless extends AbstractEffect implements IPotionEffect {
    public static EffectLimitless INSTANCE = new EffectLimitless(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectlimitless"), "Limitless");

    public EffectLimitless(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world,@NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (rayTraceResult.getEntity() instanceof LivingEntity living) {
            if (spellStats.hasBuff(AugmentExtendTime.INSTANCE)) {

                this.applyConfigPotion(living, ArsNouveauRegistry.LIMITLESS_EFFECT.get(), spellStats);
            }
                Entity entity = rayTraceResult.getEntity();
                entity.setDeltaMovement(entity.getDeltaMovement().scale(0));
                entity.hurtMarked = true;

        }
    }

    @Override
    public int getDefaultManaCost() {
        return 90;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addPotionConfig(builder, 2);
        addExtendTimeConfig(builder, 1);
    }

   @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentExtendTime.INSTANCE,
                AugmentDurationDown.INSTANCE
        );
    }

    @Override
    public String getBookDescription() {
        return "Causes blocks and entities to fall. When augmented with Extend Time, players will have their flight disabled and will obtain the Gravity effect. While afflicted with Gravity, entities will rapidly fall and take double falling damage.";
    }

   @NotNull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.MANIPULATION);
    }

    @Override
    public int getBaseDuration() {
        return POTION_TIME == null ? 2 : POTION_TIME.get();
    }

    @Override
    public int getExtendTimeDuration() {
        return EXTEND_TIME == null ? 1 : EXTEND_TIME.get();
    }
}
