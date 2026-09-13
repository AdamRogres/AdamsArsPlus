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

    public static boolean contains(double distanceSquared, double radius, boolean dome, double y, int centerY) {
        return distanceSquared <= radius * radius && (!dome || y > centerY - 2);
    }
}
