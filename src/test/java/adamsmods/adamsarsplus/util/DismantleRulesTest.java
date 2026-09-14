package adamsmods.adamsarsplus.util;

public final class DismantleRulesTest {
    private static void check(boolean ok) { if (!ok) throw new AssertionError("Dismantle diminishing damage"); }
    public static void main(String[] args) {
        for (int hit = 0; hit <= 5; hit++) {
            check(Math.abs(DismantleRules.damage(100, hit) - (100 - 20 * hit)) < 0.0001F);
            check(Math.abs(DismantleRules.damage(4, hit) - (4 - 0.8F * hit)) < 0.0001F);
        }
        check(DismantleRules.damage(4, 100) == 0);
        check(DismantleRules.damage(1000, 5) == 0);
        check(DismantleRules.damage(4.5F, 5) == 0);
        check(DismantleRules.damage(10, 3) == 4);
        check(DismantleRules.recentHits(false, 0, 0, 2) == 0);
        check(DismantleRules.recentHits(true, 100, 100, 2) == 2);
        check(DismantleRules.recentHits(true, 119, 100, 2) == 2);
        check(DismantleRules.recentHits(true, 120, 100, 2) == 0);
        check(DismantleRules.recentHits(true, 99, 100, 2) == 0);
        System.out.println("PASS: Dismantle first hit, successive hits, damage floor and reset window.");
    }
}
