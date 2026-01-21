package adamsmods.adamsarsplus.common.glyphs.effect_glyph;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static adamsmods.adamsarsplus.ConfigHandler.Common.DISCOUNT_BACKLASH;
import static adamsmods.adamsarsplus.ConfigHandler.Common.MAX_DISCOUNTS;
import static adamsmods.adamsarsplus.registry.ModPotions.MANA_EXHAUST_EFFECT;
import static adamsmods.adamsarsplus.registry.ModPotions.SIX_EYES_EFFECT;

public class SpellEfficiency extends AbstractEffect {
    public static final SpellEfficiency INSTANCE = new SpellEfficiency();

    public SpellEfficiency() {
        super("glyph_spellefficiency", "Spell Efficiency");
    }

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (DISCOUNT_BACKLASH.get() && !shooter.hasEffect(SIX_EYES_EFFECT)) {

            int X = spellContext.getSpell().getInstanceCount(SpellEfficiency.INSTANCE);
            int ticks = (int) (40.0 * X);

            shooter.addEffect(new MobEffectInstance(MANA_EXHAUST_EFFECT, ticks));
        }
    }

    @Override
    protected int getDefaultManaCost() {
        return 0;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        PER_SPELL_LIMIT = MAX_DISCOUNTS;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return setOf();
    }
}
