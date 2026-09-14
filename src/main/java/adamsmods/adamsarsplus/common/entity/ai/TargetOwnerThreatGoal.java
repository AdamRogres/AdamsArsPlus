package adamsmods.adamsarsplus.common.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

/** Defend a player owner from mobs currently targeting them, regardless of mob category. */
public final class TargetOwnerThreatGoal extends NearestAttackableTargetGoal<Mob> {
    public TargetOwnerThreatGoal(Mob summon) {
        super(summon, Mob.class, 10, true, false, candidate ->
                candidate instanceof Mob threat && threatensOwner(summon, threat));
    }

    private static boolean threatensOwner(Mob summon, Mob threat) {
        return TenShadowsTargeting.owner(summon) instanceof Player owner
                && threat.getTarget() == owner && TenShadowsTargeting.validTarget(summon, threat);
    }

    @Override public boolean canUse() {
        return TenShadowsTargeting.playerOwned(mob) && super.canUse();
    }

    @Override public boolean canContinueToUse() {
        return mob.getTarget() instanceof Mob threat && threatensOwner(mob, threat) && super.canContinueToUse();
    }
}
