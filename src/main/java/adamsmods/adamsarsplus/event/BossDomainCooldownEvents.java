package adamsmods.adamsarsplus.event;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.custom.*;
import adamsmods.adamsarsplus.util.DomainDamage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public final class BossDomainCooldownEvents {
    public static final int REDUCTION_TICKS = 20;

    @SubscribeEvent
    public static void damaged(LivingDamageEvent.Post event) {
        var target = event.getEntity();
        if (target.level().isClientSide() || event.getNewDamage() <= 0
                || !DomainDamage.isDomain(event.getSource())) return;

        // Adam's domainCooldown belongs to Domain A; domainbCooldown is untouched.
        if (target instanceof AdamEntity boss) boss.domainCooldown = reduce(boss.domainCooldown);
        else if (target instanceof JoshEntity boss) boss.domainCooldown = reduce(boss.domainCooldown);
        else if (target instanceof CadeEntity boss) boss.domainCooldown = reduce(boss.domainCooldown);
        else if (target instanceof CamEntity boss) boss.domainCooldown = reduce(boss.domainCooldown);
        else if (target instanceof MattEntity boss) boss.domainCooldown = reduce(boss.domainCooldown);
        else if (target instanceof NickEntity boss) boss.domainCooldown = reduce(boss.domainCooldown);
        else if (target instanceof RyanEntity boss) boss.domainCooldown = reduce(boss.domainCooldown);
    }

    private static int reduce(int cooldown) {
        return cooldown <= REDUCTION_TICKS ? 0 : cooldown - REDUCTION_TICKS;
    }
}
