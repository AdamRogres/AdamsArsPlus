package adamsmods.adamsarsplus.util;

import adamsmods.adamsarsplus.common.entity.custom.MahoragaEntity;
import adamsmods.adamsarsplus.registry.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import top.theillusivec4.curios.api.CuriosApi;
import java.util.ArrayList;

/** Per-wheel state is saved and synchronized by the item's vanilla custom-data component. */
public final class WheelAdaptation {
    public static final int ADAPTATION_TICKS = 30 * 20;
    public static final int ROTATION_TICKS = 10;
    private static final String KEY = "adamsarsplus_wheel";

    public static CompoundTag state(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound(KEY);
    }

    private static void save(ItemStack stack, CompoundTag state) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.put(KEY, state));
    }

    public static boolean isSuppressed(LivingEntity wearer) {
        return wearer.hasEffect(TenShadowsState.effect(4));
    }

    private static void clear(ItemStack stack) {
        if (!state(stack).isEmpty()) {
            CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.remove(KEY));
        }
    }

    /** Called only after a successful summon has inherited the stored adaptation. */
    public static void consume(LivingEntity owner) {
        CuriosApi.getCuriosInventory(owner).ifPresent(inventory -> {
            for (var slot : inventory.findCurios(ModItems.GENERALS_WHEEL.get())) clear(slot.stack());
        });
    }

    // Hits of an already pending type do not postpone completion.
    public static boolean record(CompoundTag state, String type, long now) {
        CompoundTag pending = state.getCompound("pending");
        if (type.equals(state.getString("saved")) || pending.contains(type)) return false;
        pending.putLong(type, now + ADAPTATION_TICKS);
        state.put("pending", pending);
        return true;
    }

    public static boolean complete(CompoundTag state, long now) {
        CompoundTag pending = state.getCompound("pending");
        String latest = null;
        long latestTime = Long.MIN_VALUE;
        for (String type : new ArrayList<>(pending.getAllKeys())) {
            long deadline = pending.getLong(type);
            if (deadline > now) continue;
            // Stable tie-breaker for damage types recorded during the same tick.
            if (deadline > latestTime || (deadline == latestTime && (latest == null || type.compareTo(latest) > 0))) {
                latest = type;
                latestTime = deadline;
            }
            pending.remove(type);
        }
        if (latest == null) return false;
        state.put("pending", pending);
        state.putString("saved", latest);
        state.putLong("completed", now);
        state.putInt("turn", (state.getInt("turn") + 1) % 4);
        return true;
    }

    public static void damaged(LivingEntity wearer, String type) {
        if (wearer.level().isClientSide() || isSuppressed(wearer)) return;
        CuriosApi.getCuriosInventory(wearer).ifPresent(inventory -> {
            for (var slot : inventory.findCurios(ModItems.GENERALS_WHEEL.get())) {
                CompoundTag state = state(slot.stack());
                if (record(state, type, wearer.level().getGameTime())) save(slot.stack(), state);
            }
        });
    }

    public static void tick(LivingEntity wearer, ItemStack stack) {
        if (wearer.level().isClientSide()) return;
        if (isSuppressed(wearer)) {
            // Also discard timers on wheels equipped after Mahoraga was summoned.
            clear(stack);
            return;
        }
        CompoundTag state = state(stack);
        if (complete(state, wearer.level().getGameTime())) {
            save(stack, state);
            wearer.level().playSound(null, wearer.getX(), wearer.getY(), wearer.getZ(),
                    SoundEvents.IRON_DOOR_OPEN, SoundSource.PLAYERS, 1.5F, 1.0F);
        }
    }

    public static float rotation(ItemStack stack, long now, float partialTick) {
        CompoundTag state = state(stack);
        if (!state.contains("completed")) return 0;
        float progress = Math.max(0, Math.min(1, (now - state.getLong("completed") + partialTick) / ROTATION_TICKS));
        progress = progress * progress * (3 - 2 * progress);
        return (float) ((state.getInt("turn") - 1 + progress) * Math.PI / 2);
    }

    public static void inherit(LivingEntity owner, MahoragaEntity summon) {
        CuriosApi.getCuriosInventory(owner).ifPresent(inventory -> {
            // Normally only one wheel can be worn. If there are several, use the newest completion.
            CompoundTag newest = null;
            for (var slot : inventory.findCurios(ModItems.GENERALS_WHEEL.get())) {
                CompoundTag candidate = state(slot.stack());
                if (!candidate.getString("saved").isEmpty() && (newest == null
                        || candidate.getLong("completed") > newest.getLong("completed"))) newest = candidate;
            }
            if (newest != null) applyInherited(summon, newest.getString("saved"));
        });
    }

    public static void applyInherited(MahoragaEntity summon, String type) {
        ResourceLocation id = ResourceLocation.tryParse(type);
        if (id == null) return;
        var registry = summon.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        if (!registry.containsKey(id)) return;
        summon.adaptedDamageTypes[0] = registry.get(id);
        summon.adaptedDamageStage[0] = 3;
        summon.getPersistentData().putString("adamsarsplus_inherited_damage_type", type);
    }
}
