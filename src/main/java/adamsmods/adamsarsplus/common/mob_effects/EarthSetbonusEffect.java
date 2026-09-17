package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.effect.MobEffects.HEALTH_BOOST;

public class EarthSetbonusEffect extends MobEffect {
    public static final String SACRIFICED_MANA = "adamsarsplus_earthen_heart_sacrificed_mana";
    private static final ResourceLocation HEALTH_ID = AdamsArsPlus.prefix("earthen_heart_health");

    public EarthSetbonusEffect() {
        super(MobEffectCategory.NEUTRAL, 2039587);
        // Register the ID so vanilla effect removal also removes our dynamic bonus.
        addAttributeModifier(Attributes.MAX_HEALTH, HEALTH_ID, 0, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(com.hollingsworth.arsnouveau.api.perk.PerkAttributes.MAX_MANA,
                AdamsArsPlus.prefix("earthen_heart_mana"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide() || !(entity instanceof Player player)) return true;
        var mana = CapabilityRegistry.getMana(player);
        var calculated = com.hollingsworth.arsnouveau.api.util.ManaUtil.calcMaxMana(player);
        int maxMana = calculated.getRealMax();
        boolean changed = mana.getMaxMana() != maxMana || mana.getReserve() != calculated.Reserve();
        mana.setMaxMana(maxMana);
        mana.setReserve(calculated.Reserve());
        if (mana.getCurrentMana() > maxMana) {
            mana.setMana(maxMana);
            changed = true;
        }
        if (changed && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) mana.syncToClient(serverPlayer);
        var health = player.getAttribute(Attributes.MAX_HEALTH);
        if (health == null) return true;

        // Migrate the identifiable infinite, hidden vanilla boost created by the old implementation.
        var legacy = player.getEffect(HEALTH_BOOST);
        if (legacy != null && legacy.isInfiniteDuration() && !legacy.isVisible() && !legacy.showIcon()
                && legacy.getAmplifier() == maxMana) player.removeEffect(HEALTH_BOOST);

        // Convert only the mana sacrificed by the maximum-mana calculation (24 mana per health point).
        double bonus = Math.max(0, maxMana) / 24.0;
        var existing = health.getModifier(HEALTH_ID);
        if (existing == null || existing.amount() != bonus) {
            health.removeModifier(HEALTH_ID);
            health.addTransientModifier(new AttributeModifier(HEALTH_ID, bonus, AttributeModifier.Operation.ADD_VALUE));
            if (player.getHealth() > player.getMaxHealth()) player.setHealth(player.getMaxHealth());
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
