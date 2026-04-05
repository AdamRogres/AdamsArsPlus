package adamsmods.adamsarsplus.common.glyphs.effect_glyph;

import adamsmods.adamsarsplus.common.entity.EntityDivineSmite;
import adamsmods.adamsarsplus.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class EffectDivineSmite extends AbstractEffect implements IDamageEffect {

    public static final EffectDivineSmite INSTANCE = new EffectDivineSmite();
    public EffectDivineSmite() {
        super("glyph_effectdivinesmite", "Divine Smite");
    }

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Vec3 pos = this.safelyGetHitPos(rayTraceResult);
        EntityDivineSmite lightningBoltEntity = new EntityDivineSmite(ModEntities.DIVINE_SMITE.get(), world);
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
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDamageConfig(builder, 9);
        addAmpConfig(builder, 4);
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
