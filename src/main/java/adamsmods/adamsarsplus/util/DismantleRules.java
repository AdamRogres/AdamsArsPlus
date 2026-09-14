package adamsmods.adamsarsplus.util;

/** Per-target diminishing returns shared across all Dismantle casters. */
public final class DismantleRules {
    public static final int RESET_TICKS = 20;
    public static final double REDUCTION_PER_HIT = 0.20;

    private DismantleRules() {}

    public static int recentHits(boolean recorded, long now, long lastHit, int hits) {
        return recorded && now >= lastHit && now - lastHit < RESET_TICKS ? Math.max(0, hits) : 0;
    }

    public static float damage(float damage, int previousHits) {
        double multiplier = Math.max(0.0, 1.0 - REDUCTION_PER_HIT * Math.max(0, previousHits));
        return (float) (Math.max(0.0F, damage) * multiplier);
    }
}
