package adamsmods.adamsarsplus.util;

/** Shared timing and spherical bounds for domain attacks, particles and announcements. */
public final class DomainRules {
    private DomainRules() {}

    public static boolean isCastTick(int age, int delay, int accelerates) {
        return age >= delay && (age - delay) % Math.max(20L - 2L * accelerates, 2L) == 0;
    }

    public static boolean suppresses(boolean active, boolean inRange, double ownAmplification, double otherAmplification) {
        return active && inRange && otherAmplification >= ownAmplification;
    }

    /** Finite Simple Domains lose twenty ticks per amplification; fractional ticks round down. */
    public static int simpleDomainDuration(int duration, double amplification) {
        if (duration <= 0 || !(amplification > 0)) return duration;
        int reduction = (int) Math.min(Integer.MAX_VALUE, 20.0 * amplification);
        return Math.max(0, duration - reduction);
    }

    /** Extra cooldown starts at half the base lifetime and changes by the duration augment delta. */
    public static int burnoutDuration(int lifetime, int baseLifetime) {
        long domainTicks = Math.max(1, lifetime);
        long extra = Math.max(0L, (long) Math.ceil(baseLifetime * 0.5) + (long) lifetime - baseLifetime);
        return (int) Math.min(Integer.MAX_VALUE, domainTicks + extra);
    }
    public static boolean contains(double distanceSquared, double radius, boolean dome, double y, int centerY) {
        return distanceSquared <= radius * radius && (!dome || y > centerY - 2);
    }
}
