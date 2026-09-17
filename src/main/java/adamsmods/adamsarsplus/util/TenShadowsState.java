package adamsmods.adamsarsplus.util;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.custom.*;
import adamsmods.adamsarsplus.registry.ModPotions;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import java.util.UUID;

/** Separate effect, cooldown and generation for each shikigami group. */
@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public final class TenShadowsState {
    public static final String SESSION = "adamsarsplus_shikigami_session";
    private TenShadowsState() {}
    private static String key(int rank) { return "adamsarsplus_shikigami_" + rank; }
    public static Holder<MobEffect> effect(int rank) {
        return switch (rank) {
            case 1 -> ModPotions.TS_NUE_EFFECT;
            case 2 -> ModPotions.TS_RABBIT_ESCAPE_EFFECT;
            case 3 -> ModPotions.TS_ROUND_DEER_EFFECT;
            case 4 -> ModPotions.TS_MAHORAGA_EFFECT;
            default -> ModPotions.TS_DIVINE_DOGS_EFFECT;
        };
    }
    public static boolean coolingDown(LivingEntity owner, int rank) {
        if (rank == 4) upgradeMahoraga(owner);
        return owner.getPersistentData().getLong(key(rank) + "_cooldown") > owner.level().getGameTime();
    }
    public static void cooldown(LivingEntity owner, int rank, int ticks) {
        String key = key(rank) + "_cooldown";
        owner.getPersistentData().putLong(key, Math.max(owner.getPersistentData().getLong(key), owner.level().getGameTime() + ticks));
    }
    public static void activate(LivingEntity owner, int rank, int duration) {
        owner.getPersistentData().putUUID(key(rank), UUID.randomUUID());
        owner.getPersistentData().putInt(key(rank) + "_members", 0);
        owner.addEffect(new MobEffectInstance(effect(rank), duration));
    }
    private static void upgradeMahoraga(LivingEntity owner) {
        // One-time migration of the former long cooldown and finite effect on existing saves.
        var data = owner.getPersistentData();
        if (data.getBoolean("adamsarsplus_mahoraga_unlimited")) return;
        data.putBoolean("adamsarsplus_mahoraga_unlimited", true);
        data.remove(key(4) + "_cooldown");
        var current = owner.getEffect(effect(4));
        if (current != null && !current.isInfiniteDuration()) {
            owner.addEffect(new MobEffectInstance(effect(4), -1, current.getAmplifier(),
                    current.isAmbient(), current.isVisible(), current.showIcon()));
        }
    }
    public static void track(LivingEntity owner, Mob summon, int rank) {
        summon.getPersistentData().putUUID(SESSION, owner.getPersistentData().getUUID(key(rank)));
        summon.getPersistentData().putBoolean("adamsarsplus_session_player_owned", owner instanceof net.minecraft.world.entity.player.Player);
        owner.getPersistentData().putInt(key(rank) + "_members", owner.getPersistentData().getInt(key(rank) + "_members") + 1);
    }
    public static void finishSummoning(LivingEntity owner, int rank, int duration) {
        if (owner.getPersistentData().getInt(key(rank) + "_members") == 0) {
            owner.removeEffect(effect(rank));

        }
    }
    public static void dismiss(LivingEntity owner, int rank) {
        owner.removeEffect(effect(rank));
        // Invalidate unloaded summons too, even if this type is resummoned before they load.
        owner.getPersistentData().putUUID(key(rank), UUID.randomUUID());
        owner.getPersistentData().putInt(key(rank) + "_members", 0);
        cooldown(owner, rank, 200);
    }
    public static boolean active(Mob summon, LivingEntity owner, int rank) {
        if (rank == 4) upgradeMahoraga(owner);
        var data = summon.getPersistentData();
        return owner.hasEffect(effect(rank)) && data.hasUUID(SESSION)
                && owner.getPersistentData().hasUUID(key(rank))
                && data.getUUID(SESSION).equals(owner.getPersistentData().getUUID(key(rank)));
    }
    public static void inheritSession(Mob parent, Mob copy) {
        if (parent.getPersistentData().hasUUID(SESSION)) {
            copy.getPersistentData().putUUID(SESSION, parent.getPersistentData().getUUID(SESSION));
            if (parent.getPersistentData().contains("adamsarsplus_session_player_owned"))
                copy.getPersistentData().putBoolean("adamsarsplus_session_player_owned", parent.getPersistentData().getBoolean("adamsarsplus_session_player_owned"));
        }
    }
    private static int rank(LivingEntity entity) {
        if (entity instanceof DivineDogEntity e && e.isSummon) return 0;
        if (entity instanceof NueEntity e && e.isSummon) return 1;
        if (entity instanceof RabbitEEntity e && e.isSummon) return 2;
        if (entity instanceof RDeerEntity e && e.isSummon) return 3;
        if (entity instanceof MahoragaEntity e && e.isSummon) return 4;
        return -1;
    }
    /** Convert currently loaded legacy summons before a cast toggles any of them. */
    public static void migrateLegacy(LivingEntity owner) {
        var legacy = owner.getEffect(ModPotions.TENSHADOWS_EFFECT);
        if (legacy == null || !(owner.level() instanceof ServerLevel level)) return;
        for (ServerLevel dimension : level.getServer().getAllLevels()) {
            for (var entity : dimension.getAllEntities()) {
                if (!(entity instanceof Mob mob) || !(entity instanceof ISummon summon)
                        || !owner.getUUID().equals(summon.getOwnerUUID()) || mob.getPersistentData().hasUUID(SESSION)) continue;
                int rank = rank(mob);
                if (rank < 0) continue;
                if (!owner.hasEffect(effect(rank))) activate(owner, rank, legacy.getDuration());
                if (mob instanceof RabbitEEntity rabbit && rabbit.isCopy) {
                    mob.getPersistentData().putUUID(SESSION, owner.getPersistentData().getUUID(key(rank)));
                } else track(owner, mob, rank);
            }
        }
        owner.removeEffect(ModPotions.TENSHADOWS_EFFECT);
    }
    /** Dismiss on departure rather than leave an unattended summon fighting in loaded chunks. */
    private static void resetPlayer(net.minecraft.world.entity.player.Player player) {
        if (!(player.level() instanceof ServerLevel level)) return;
        for (int rank = 0; rank <= 4; rank++) {
            if (player.hasEffect(effect(rank)) || player.getPersistentData().getInt(key(rank) + "_members") > 0) {
                dismiss(player, rank);
            }
        }
        player.removeEffect(ModPotions.TENSHADOWS_EFFECT);
        // This scan only runs on player lifecycle events, never every tick.
        for (ServerLevel dimension : level.getServer().getAllLevels()) {
            var remove = new java.util.ArrayList<Mob>();
            for (var entity : dimension.getAllEntities()) {
                if (entity instanceof Mob mob && entity instanceof ISummon summon
                        && rank(mob) >= 0 && player.getUUID().equals(summon.getOwnerUUID())) remove.add(mob);
            }
            remove.forEach(Mob::discard);
        }
    }

    @SubscribeEvent
    public static void loggedOut(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent event) {
        resetPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void changedDimension(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent event) {
        resetPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void loggedIn(net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        // Reconcile older saves and sessions interrupted by a server crash/restart.
        resetPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void validateSummon(net.neoforged.neoforge.event.tick.EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof Mob mob) || !(mob instanceof ISummon summon)
                || !(mob.level() instanceof ServerLevel level) || rank(mob) < 0) return;
        // Only manage tracked player summons; leave ritual bosses and NPC-owned summons alone.
        if (!mob.getPersistentData().hasUUID(SESSION) || summon.getOwnerUUID() == null) return;
        boolean playerOwned = mob.getPersistentData().contains("adamsarsplus_session_player_owned")
                ? mob.getPersistentData().getBoolean("adamsarsplus_session_player_owned")
                : adamsmods.adamsarsplus.common.entity.ai.TenShadowsTargeting.playerOwned(mob);
        if (!playerOwned) return;
        var owner = level.getServer().getPlayerList().getPlayer(summon.getOwnerUUID());
        if (owner == null || owner.level() != level || !active(mob, owner, rank(mob))) {
            mob.discard();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void died(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Mob mob) || !(mob instanceof ISummon summon)
                || !(mob.level() instanceof ServerLevel level)) return;
        int rank = rank(mob);
        if (rank < 0 || (mob instanceof RabbitEEntity rabbit && rabbit.isCopy)) return;
        if (summon.getOwnerUUID() == null) return;
        LivingEntity owner = level.getServer().getPlayerList().getPlayer(summon.getOwnerUUID());
        if (owner == null || !active(mob, owner, rank)) return;
        int remaining = Math.max(0, owner.getPersistentData().getInt(key(rank) + "_members") - 1);
        owner.getPersistentData().putInt(key(rank) + "_members", remaining);
        if (remaining == 0) dismiss(owner, rank);
    }
}
