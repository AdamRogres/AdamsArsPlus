package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.custom.DivineDogEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class DDogSprintGoal extends MeleeAttackGoal {
    private final DivineDogEntity entity;

    Supplier<Boolean> canUse;
    boolean done;

    public DDogSprintGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((DivineDogEntity) pMob);
        this.canUse = canUse;
    }

    @Override
    public void start() {
        super.start();
        entity.setSprinting(true);
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {

            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());

                performAttack(pEnemy);
            }
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
    }

    protected double getAttackReachSqr(LivingEntity pAttackTarget) {
        return (double)(this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() * 2.0F + 3.5F);
    }

    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();

        Vec3 $$0 = this.mob.getDeltaMovement();
        Vec3 $$1 = new Vec3(pEnemy.getX() - this.mob.getX(), (double)0.0F, pEnemy.getZ() - this.mob.getZ());
        if ($$1.lengthSqr() > 1.0E-7) {
            $$1 = $$1.normalize().scale(0.5).add($$0.scale(0.2));
        }

        this.mob.setDeltaMovement($$1.x, 0.4, $$1.z);
        entity.setLunging(true);

        this.entity.sprintCooldown = 0;
        this.done = true;
    }

    @Override
    public void stop() {
        entity.setSprinting(false);
        this.done = false;
        super.stop();
    }
}

