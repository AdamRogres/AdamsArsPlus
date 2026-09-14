package adamsmods.adamsarsplus.util;

import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/** Provenance for sure-hit casts, their delayed spell contexts, and spawned entities. */
public final class DomainDamage {
    public static final String KEY = "adamsarsplus_domain_damage";
    private static final ThreadLocal<Integer> DEPTH = ThreadLocal.withInitial(() -> 0);
    private static final Set<DamageSource> SOURCES = Collections.synchronizedSet(
            Collections.newSetFromMap(new WeakHashMap<>()));
    private DomainDamage() {}

    public static void resolve(Runnable action) {
        int previous = DEPTH.get();
        DEPTH.set(previous + 1);
        try { action.run(); }
        finally { if (previous == 0) DEPTH.remove(); else DEPTH.set(previous); }
    }
    public static boolean resolving() { return DEPTH.get() > 0; }
    public static void mark(DamageSource source) { SOURCES.add(source); }
    public static boolean isDomain(SpellContext context) {
        for (var current = context; current != null; current = current.getPreviousContext()) {
            if (current.tag.getBoolean(KEY)) return true;
        }
        return false;
    }
    private static boolean tagged(Entity entity) {
        return entity != null && entity.getPersistentData().getBoolean(KEY);
    }
    public static boolean isDomain(DamageSource source) {
        return resolving() || SOURCES.contains(source)
                || tagged(source.getDirectEntity()) || tagged(source.getEntity());
    }
}
