package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.EntityDivineSmite;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.DIVINE_SMITE;

public class EffectDivineSmite extends AbstractEffect implements IDamageEffect {
    public EffectDivineSmite(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public static final EffectDivineSmite INSTANCE = new EffectDivineSmite(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectdivinesmite"), "Divine Smite");

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Vec3 pos = this.safelyGetHitPos(rayTraceResult);
        EntityDivineSmite lightningBoltEntity = new EntityDivineSmite(DIVINE_SMITE.get(), world);
        lightningBoltEntity.setPos(pos.x(), pos.y(), pos.z());
        lightningBoltEntity.setCause(shooter instanceof ServerPlayer ? (ServerPlayer)shooter : null);
        lightningBoltEntity.setAoe((float)spellStats.getAoeMultiplier());
        lightningBoltEntity.setSensitive(spellStats.hasBuff(AugmentSensitive.INSTANCE));
        lightningBoltEntity.setDamage(this.DAMAGE.get().floatValue() + (float)((this.AMP_VALUE.get()) * spellStats.getAmpMultiplier()));
        lightningBoltEntity.amps = (float)spellStats.getAmpMultiplier();
        lightningBoltEntity.extendTimes = (int)spellStats.getDurationMultiplier();
        lightningBoltEntity.ampScalar = ((Double)this.AMP_VALUE.get()).floatValue();

        world.addFreshEntity(lightningBoltEntity);
    }


    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        this.addDamageConfig(builder, 9.0);
        this.addAmpConfig(builder, 4.0);
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentSensitive.INSTANCE,
                AugmentAOE.INSTANCE,
                AugmentAmplify.INSTANCE
        );
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public int getDefaultManaCost() {
        return 700;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.ELEMENTAL_AIR);
    }
    @Override
    public void addDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(),4);
        defaults.put(AugmentAOE.INSTANCE.getRegistryName(),4);

    }
}
