package adamsmods.adamsarsplus.util;

public final class LimitlessRulesTest {
    public static void main(String[] args) {
        for (double base : new double[]{0.1, 2, 10, 50, 1000}) {
            for (boolean crit : new boolean[]{false, true}) {
                double speed = LimitlessRules.speedLimit(base, crit);
                int damage = (int) Math.ceil((float)speed * base);
                int worst = damage + (crit ? damage / 2 + 1 : 0);
                if (speed > 8 || worst > 100) throw new AssertionError("Arrow damage/speed cap");
            }
        }
        if (LimitlessRules.speedLimit(0, true) != 8) throw new AssertionError("Zero base damage");
        System.out.println("PASS: Limitless speed ceiling and critical/noncritical arrow damage bounds.");
    }
}
