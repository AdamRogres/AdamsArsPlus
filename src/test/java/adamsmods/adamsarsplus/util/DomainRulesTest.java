package adamsmods.adamsarsplus.util;

public class DomainRulesTest {
    private static void require(boolean value) {
        if (!value) throw new AssertionError("Domain timing/bounds regression");
    }

    public static void main(String[] args) {
        for (int acceleration : new int[]{-5, 0, 5, 100, Integer.MAX_VALUE}) {
            for (int age = 0; age < 30; age++) require(!DomainRules.isCastTick(age, 30, acceleration));
            require(DomainRules.isCastTick(30, 30, acceleration));
            require(!DomainRules.isCastTick(31, 30, acceleration));
        }
        require(DomainRules.isCastTick(50, 30, 0));
        require(!DomainRules.isCastTick(40, 30, 0));
        require(DomainRules.isCastTick(40, 30, 5));
        require(DomainRules.isCastTick(60, 30, -5));
        require(DomainRules.isCastTick(60, 60, 0));
        require(!DomainRules.isCastTick(30, 60, 0));
        require(DomainRules.contains(25, 5, false, 0, 64));
        require(!DomainRules.contains(25.01, 5, false, 64, 64));
        require(!DomainRules.contains(50, 5, false, 64, 64)); // AABB corner, outside sphere
        require(!DomainRules.contains(4, 5, true, 62, 64));
        require(DomainRules.contains(1, 5, true, 63, 64));
        require(DomainRules.suppresses(true, true, 1, 2)); // stronger counter-domain
        require(!DomainRules.suppresses(true, true, 2, 1));
        require(DomainRules.suppresses(true, true, 2, 2)); // equal domains both pause
        require(DomainRules.suppresses(true, true, 0, 0));
        require(!DomainRules.suppresses(false, true, 1, 2)); // resume after removal
        require(!DomainRules.suppresses(true, false, 1, 2)); // outside the radius
        require(DomainRules.simpleDomainDuration(100, 0) == 100);
        require(DomainRules.simpleDomainDuration(100, 1) == 80);
        require(DomainRules.simpleDomainDuration(100, 3) == 40);
        require(DomainRules.simpleDomainDuration(40, 2) == 0);
        require(DomainRules.simpleDomainDuration(4, 2) == 0);
        require(DomainRules.simpleDomainDuration(-1, 10) == -1); // infinite effects remain infinite
        require(DomainRules.simpleDomainDuration(100, -1) == 100);
        require(DomainRules.simpleDomainDuration(100, 0.5) == 90);
        require(DomainRules.simpleDomainDuration(100, Double.MAX_VALUE) == 0);
        System.out.println("PASS: domain reaction delay, acceleration limits, repeat intervals, spherical bounds dome exclusion, equal/stronger domain clashes, and Simple Domain duration erosion.");
    }
}
