package adamsmods.adamsarsplus.event;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.util.WheelAdaptation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public final class WheelAdaptationEvents {
    @SubscribeEvent
    public static void damaged(LivingDamageEvent.Post event) {
        if (event.getEntity().level().isClientSide() || event.getNewDamage() <= 0) return;
        event.getSource().typeHolder().unwrapKey().ifPresent(key ->
                WheelAdaptation.damaged(event.getEntity(), key.location().toString()));
    }
}
