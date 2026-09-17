package adamsmods.adamsarsplus.util;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.EntityDomainSpell;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import java.util.*;

/** Server-thread-only index, maintained as domains enter and leave loaded levels. */
@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public final class LoadedDomains {
    private static final Map<ServerLevel, Set<EntityDomainSpell>> DOMAINS = new IdentityHashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void joined(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel level && event.getEntity() instanceof EntityDomainSpell domain) {
            DOMAINS.computeIfAbsent(level, ignored -> new HashSet<>()).add(domain);
        }
    }
    @SubscribeEvent
    public static void left(EntityLeaveLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel level && event.getEntity() instanceof EntityDomainSpell domain) {
            var domains = DOMAINS.get(level);
            if (domains != null) {
                domains.remove(domain);
                if (domains.isEmpty()) DOMAINS.remove(level);
            }
        }
    }
    @SubscribeEvent
    public static void unloaded(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel level) DOMAINS.remove(level);
    }
    @SubscribeEvent
    public static void stopped(ServerStoppedEvent event) { DOMAINS.clear(); }

    public static Iterable<EntityDomainSpell> in(ServerLevel level) {
        return DOMAINS.getOrDefault(level, Collections.emptySet());
    }
}
