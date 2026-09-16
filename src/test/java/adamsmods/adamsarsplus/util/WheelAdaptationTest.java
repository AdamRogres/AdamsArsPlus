package adamsmods.adamsarsplus.util;
import net.minecraft.nbt.CompoundTag;
public final class WheelAdaptationTest {
    private static void check(boolean ok) { if (!ok) throw new AssertionError(); }
    public static void main(String[] args) {
        CompoundTag state = new CompoundTag();
        check(WheelAdaptation.record(state, "minecraft:arrow", 0));
        check(!WheelAdaptation.record(state, "minecraft:arrow", 100));
        check(WheelAdaptation.record(state, "minecraft:mob_attack", 100));
        check(!WheelAdaptation.complete(state, 599));
        check(WheelAdaptation.complete(state, 600));
        check(state.getString("saved").equals("minecraft:arrow"));
        check(!WheelAdaptation.record(state, "minecraft:arrow", 601));
        state = state.copy(); // Saved item state retains the other pending timer.
        check(!WheelAdaptation.complete(state, 699));
        check(WheelAdaptation.complete(state, 700));
        check(state.getString("saved").equals("minecraft:mob_attack"));
        check(state.getCompound("pending").isEmpty());
        check(!WheelAdaptation.complete(state, 701));
        check(WheelAdaptation.record(state, "minecraft:arrow", 800));
        check(WheelAdaptation.record(state, "minecraft:magic", 900));
        check(WheelAdaptation.complete(state, 2000));
        check(state.getString("saved").equals("minecraft:magic"));
        System.out.println("PASS: wheel 600-tick timer, repeated hits, independent timers, saved replacement and overdue completion.");
    }
}
