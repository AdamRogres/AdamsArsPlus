package adamsmods.adamsarsplus.event;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.util.DomainDamage;
import adamsmods.adamsarsplus.util.LimitlessRules;
import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import static adamsmods.adamsarsplus.registry.ModPotions.LIMITLESS_EFFECT;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public final class LimitlessEvents {
    public static final String ACCELERATED_ARROW = "adamsarsplus_limitless_arrow";

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void spellDamage(SpellDamageEvent.Pre event) {
        if (DomainDamage.isDomain(event.context)) DomainDamage.mark(event.damageSource);
    }

    @SubscribeEvent
    public static void spawned(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && DomainDamage.resolving()) {
            event.getEntity().getPersistentData().putBoolean(DomainDamage.KEY, true);
        }
    }

    private static boolean cappedArrow(net.minecraft.world.damagesource.DamageSource source) {
        return source.getDirectEntity() instanceof AbstractArrow arrow
                && arrow.getPersistentData().getBoolean(ACCELERATED_ARROW);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void incoming(LivingIncomingDamageEvent event) {
        if (event.getEntity().hasEffect(LIMITLESS_EFFECT) && !DomainDamage.isDomain(event.getSource())) {
            event.setCanceled(true);
            return;
        }
        if (cappedArrow(event.getSource())) event.setAmount(Math.min(event.getAmount(), LimitlessRules.MAX_ARROW_DAMAGE));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void finalDamage(LivingDamageEvent.Pre event) {
        // Also catch enchantments or event modifiers that increase damage after the incoming check.
        var damage = event.getContainer();
        if (cappedArrow(damage.getSource())) damage.setNewDamage(Math.min(damage.getNewDamage(), LimitlessRules.MAX_ARROW_DAMAGE));
    }
}
