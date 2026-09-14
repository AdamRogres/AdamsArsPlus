package adamsmods.adamsarsplus.common.entity.ai;

import adamsmods.adamsarsplus.common.entity.custom.*;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.common.entity.IFollowingSummon;
import net.minecraft.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import java.util.EnumSet;
import java.util.function.BooleanSupplier;

/** Dynamic ownership checks: goals are constructed before summon owners are assigned. */
public final class TenShadowsTargeting {
    private TenShadowsTargeting() {}

    public static LivingEntity owner(Mob mob) {
        if (mob instanceof IFollowingSummon summon && summon.getSummoner() != null) return summon.getSummoner();
        LivingEntity cached = switch (mob) {
            case DivineDogEntity dog -> dog.getActualOwner();
            case NueEntity nue -> nue.getActualOwner();
            case RabbitEEntity rabbit -> rabbit.getActualOwner();
            case RDeerEntity deer -> deer.getActualOwner();
            case MahoragaEntity mahoraga -> mahoraga.getActualOwner();
            default -> null;
        };
        return mob instanceof ISummon summon && cached != null && cached.getUUID().equals(summon.getOwnerUUID()) ? cached : null;
    }

    public static boolean playerOwned(Mob mob) {
        if (!(mob instanceof ISummon summon)) return false;
        var id = summon.getOwnerUUID();
        if (id == null || id.equals(Util.NIL_UUID) || id.equals(mob.getUUID())) return false;
        var data = mob.getPersistentData();
        LivingEntity owner = owner(mob);
        if (owner != null) {
            data.putUUID("adamsarsplus_targeting_owner", id);
            data.putBoolean("adamsarsplus_player_owned", owner instanceof Player);
            return owner instanceof Player;
        }
        if (data.hasUUID("adamsarsplus_targeting_owner") && id.equals(data.getUUID("adamsarsplus_targeting_owner"))) {
            return data.getBoolean("adamsarsplus_player_owned");
        }
        // Older saves with an absent owner must not revert to hunting nearby players.
        return true;
    }

    public static boolean validTarget(Mob mob, LivingEntity target) {
        if (target == null || !target.isAlive() || target == mob || target == owner(mob) || mob.isAlliedTo(target)) return false;
        if (mob instanceof ISummon summon && target instanceof ISummon other
                && summon.getOwnerUUID() != null && summon.getOwnerUUID().equals(other.getOwnerUUID())) return false;
        return mob.canAttack(target);
    }

    public static Goal unownedOnly(Mob mob, Goal goal) {
        return new ConditionalGoal(goal, () -> !playerOwned(mob), () -> !playerOwned(mob));
    }

    public static Goal copyOwner(Mob mob, Goal goal) {
        return new ConditionalGoal(goal,
                () -> owner(mob) != null && validTarget(mob, owner(mob).getLastHurtMob()),
                () -> owner(mob) != null && validTarget(mob, mob.getTarget()));
    }

    private static final class ConditionalGoal extends Goal {
        private final Goal delegate;
        private final BooleanSupplier startAllowed;
        private final BooleanSupplier continueAllowed;
        private ConditionalGoal(Goal delegate, BooleanSupplier startAllowed, BooleanSupplier continueAllowed) {
            this.delegate = delegate;
            this.startAllowed = startAllowed;
            this.continueAllowed = continueAllowed;
            var flags = EnumSet.copyOf(delegate.getFlags());
            flags.add(Flag.TARGET);
            setFlags(flags);
        }
        @Override public boolean canUse() { return startAllowed.getAsBoolean() && delegate.canUse(); }
        @Override public boolean canContinueToUse() { return continueAllowed.getAsBoolean() && delegate.canContinueToUse(); }
        @Override public void start() { delegate.start(); }
        @Override public void stop() { delegate.stop(); }
        @Override public void tick() { delegate.tick(); }
        @Override public boolean isInterruptable() { return delegate.isInterruptable(); }
        @Override public boolean requiresUpdateEveryTick() { return delegate.requiresUpdateEveryTick(); }
    }
}
