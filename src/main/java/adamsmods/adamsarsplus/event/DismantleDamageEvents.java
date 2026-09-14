package adamsmods.adamsarsplus.event;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.util.DismantleRules;
import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public final class DismantleDamageEvents {
    private static final String LAST_HIT = "adamsarsplus_dismantle_last_hit";
    private static final String HIT_COUNT = "adamsarsplus_dismantle_hits";
    private static final ThreadLocal<Map<DamageSource, PendingHit>> PENDING = ThreadLocal.withInitial(IdentityHashMap::new);

    private static final class PendingHit {
        final int previousHits;
        boolean reducedToZero;
        PendingHit(int previousHits) { this.previousHits = previousHits; }
    }

    public static boolean attempt(LivingEntity target, DamageSource source, BooleanSupplier damage) {
        var data = target.getPersistentData();
        long now = target.level().getGameTime();
        int hits = DismantleRules.recentHits(data.contains(LAST_HIT), now, data.getLong(LAST_HIT), data.getInt(HIT_COUNT));
        var pending = PENDING.get();
        var hit = new PendingHit(hits);
        pending.put(source, hit);
        try {
            boolean success = damage.getAsBoolean();
            // A hit reduced to zero still keeps the chain active until attacks stop.
            if (success || hit.reducedToZero) {
                data.putLong(LAST_HIT, now);
                data.putInt(HIT_COUNT, hits == Integer.MAX_VALUE ? hits : hits + 1);
            }
            return success;
        } finally {
            pending.remove(source);
            if (pending.isEmpty()) PENDING.remove();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void damage(SpellDamageEvent.Pre event) {
        PendingHit hit = PENDING.get().get(event.damageSource);
        // Apply the percentage reduction after Ars adds Amplify, attributes and perk bonuses.
        if (hit != null) {
            float original = event.damage;
            event.damage = DismantleRules.damage(original, hit.previousHits);
            hit.reducedToZero = original > 0 && event.damage == 0;
        }
    }
}
