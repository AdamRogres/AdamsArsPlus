package adamsmods.adamsarsplus.datagen;

public class CommunityMagesTest {
    public static void main(String[] args) {
        CommunityMages.mages.clear();
        check(CommunityMages.colorOrDefault(0, "red").equals("red"), "Empty list must retain the mage color");
        CommunityMages.initialize();
        int count = CommunityMages.mages.size();
        check(count > 13, "Built-in and bundled community definitions must load");
        check(CommunityMages.mages.get(7).tier.contains("flame"), "Flame default index");
        check(CommunityMages.colorOrDefault(7, "white").equals("red"), "Flame color");
        check(CommunityMages.colorOrDefault(-1, "white").equals("white"), "Negative index");
        check(CommunityMages.colorOrDefault(count, "white").equals("white"), "Stale index");
        CommunityMages.initialize();
        check(CommunityMages.mages.size() == count, "Repeated initialization must not duplicate definitions");
        System.out.println("PASS: " + count + " mage definitions loaded; default indices, empty/stale lookups and repeated initialization.");
    }
    private static void check(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }
}
