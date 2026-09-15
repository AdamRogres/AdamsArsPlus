package adamsmods.adamsarsplus.event;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.custom.MahoragaEntity;
import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public final class MahoragaAdaptationEvents {
    // Ars can attribute damage to a fake player; retain the actual spell caster for this hit.
    private static final Map<DamageSource, MahoragaEntity> SPELL_CASTERS = Collections.synchronizedMap(new WeakHashMap<>());

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void spell(SpellDamageEvent.Pre event) {
        if (event.caster instanceof MahoragaEntity mahoraga) SPELL_CASTERS.put(event.damageSource, mahoraga);
    }

    @SubscribeEvent
    public static void damaged(LivingDamageEvent.Post event) {
        var target = event.getEntity();
        if (target.level().isClientSide() || event.getNewDamage() <= 0 || !target.isAlive()
                || target.getHealth() <= target.getMaxHealth() * 0.5F) return;
        MahoragaEntity attacker = SPELL_CASTERS.get(event.getSource());
        if (attacker == null && event.getSource().getEntity() instanceof MahoragaEntity mahoraga) attacker = mahoraga;
        if (attacker != null && attacker != target) attacker.adaptOffense();
    }
}
