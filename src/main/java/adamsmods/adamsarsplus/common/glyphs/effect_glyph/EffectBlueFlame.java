package adamsmods.adamsarsplus.common.glyphs.effect_glyph;

import adamsmods.adamsarsplus.common.entity.FireEntity;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class EffectBlueFlame extends AbstractEffect {

    public static final EffectBlueFlame INSTANCE = new EffectBlueFlame();

    public EffectBlueFlame() {
        super("glyph_effectblueflame", "Cremation");
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDamageConfig(builder, 4);
        addAmpConfig(builder, 2);
    }

    public void summonFlame(Level world, HitResult rayTraceResult, @Nullable LivingEntity shooter, SpellContext spellContext, SpellStats stats){
        FireEntity flame = new FireEntity(world, (float) stats.getAoeMultiplier() + 1, (int) stats.getAmpMultiplier(), (double) stats.getAccMultiplier(), (int)stats.getDurationMultiplier(), stats.isSensitive());
        Vec3 pos = new Vec3(rayTraceResult.getLocation().x, rayTraceResult.getLocation().y + 1.1, rayTraceResult.getLocation().z);
        flame.setPos(pos);
        if(!flame.level().isClientSide){
            flame.level().addFreshEntity(flame);
        }
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.summonFlame(world, rayTraceResult, shooter, spellContext,spellStats);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        this.summonFlame(world, rayTraceResult, shooter, spellContext,spellStats);
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentAOE.INSTANCE,
                AugmentAccelerate.INSTANCE,
                AugmentAmplify.INSTANCE,
                AugmentExtendTime.INSTANCE,
                AugmentSensitive.INSTANCE
        );
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public int getDefaultManaCost() {
        return 200;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.ELEMENTAL_FIRE);
    }
}
