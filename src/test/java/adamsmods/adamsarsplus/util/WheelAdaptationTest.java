package adamsmods.adamsarsplus.util;
import net.minecraft.nbt.CompoundTag;
public final class WheelAdaptationTest {
    private static void check(boolean ok) { if (!ok) throw new AssertionError(); }
    public static void main(String[] args) {
        CompoundTag state = new CompoundTag();
        check(WheelAdaptation.record(state, "minecraft:arrow", 0));
        for (int tick = 1; tick <= 100; tick++) check(!WheelAdaptation.complete(state, tick));
        check(!WheelAdaptation.record(state, "minecraft:arrow", 100));
        check(WheelAdaptation.record(state, "minecraft:mob_attack", 100));
        state = state.copy(); // Unequip/logout/reload: elapsed world time must not count.
        check(!WheelAdaptation.complete(state, 1000000));
        for (int tick = 102; tick < 600; tick++) check(!WheelAdaptation.complete(state, 1000000 + tick));
        check(WheelAdaptation.complete(state, 1000600));
        check(state.getString("saved").equals("minecraft:arrow"));
        check(!WheelAdaptation.record(state, "minecraft:arrow", 1000601));
        for (int tick = 601; tick < 700; tick++) check(!WheelAdaptation.complete(state, 1000000 + tick));
        check(WheelAdaptation.complete(state, 1000700));
        check(state.getString("saved").equals("minecraft:mob_attack"));
        check(state.getCompound("pending").isEmpty());
        CompoundTag old = new CompoundTag();
        CompoundTag pending = new CompoundTag();
        pending.putLong("minecraft:magic", 5);
        old.put("pending", pending);
        old.putString("saved", "minecraft:arrow");
        check(!WheelAdaptation.complete(old, 1000000));
        check(old.getCompound("pending").getLong("minecraft:magic") == 599);
        check(old.getString("saved").equals("minecraft:arrow"));
        System.out.println("PASS: 600 equipped ticks, offline pause, repeated hits, overlapping timers and legacy migration.");
    }
}
