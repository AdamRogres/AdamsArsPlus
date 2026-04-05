package adamsmods.adamsarsplus.common.glyphs.effect_glyph;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static adamsmods.adamsarsplus.registry.ModPotions.ERUPTION_EFFECT;

public class EffectEruption extends AbstractEffect implements IPotionEffect {

    public EffectEruption() {
        super("glyph_effecteruption", "Eruption");
    }
    public static EffectEruption INSTANCE = new EffectEruption();

    @Override
    public void applyConfigPotion(LivingEntity entity, Holder<MobEffect> potionEffect, SpellStats spellStats) {
        this.applyConfigPotion(entity, potionEffect, spellStats, false);
    }

    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Entity var8 = rayTraceResult.getEntity();
        if (var8 instanceof LivingEntity living) {
            this.applyConfigPotion(living, ERUPTION_EFFECT, spellStats);
        }
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        this.addPotionConfig(builder, 7);
        this.addExtendTimeConfig(builder, 3);
    }

    public int getDefaultManaCost() {
        return 100;
    }

    public String getBookDescription() {
        return "Inflicts the target with Eruption. Entities with this effect take highly increased damage when burning.";
    }

    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    public @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return this.augmentSetOf(new AbstractAugment[]{AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE});
    }

    public @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(new SpellSchool[]{SpellSchools.ELEMENTAL_FIRE});
    }

    public int getBaseDuration() {
        return this.POTION_TIME == null ? 7 : 7;
    }

    public int getExtendTimeDuration() {
        return this.EXTEND_TIME == null ? 3 : 3;
    }
}

