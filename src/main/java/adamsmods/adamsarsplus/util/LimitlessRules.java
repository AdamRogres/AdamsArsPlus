package adamsmods.adamsarsplus.util;

public final class LimitlessRules {
    private LimitlessRules() {}
    public static final double MAX_SPEED = 8.0; // blocks per tick
    public static final float MAX_ARROW_DAMAGE = 100.0F;

    public static double speedLimit(double arrowBaseDamage, boolean critical) {
        if (!(arrowBaseDamage > 0)) return MAX_SPEED;
        // Critical arrows add up to floor(damage / 2) + 1: 66 + 34 = 100.
        // Margin prevents floating-point rounding from raising ceil(speed * baseDamage).
        return Math.min(MAX_SPEED, ((critical ? 66.0 : 100.0) - 0.001) / arrowBaseDamage);
    }
}
