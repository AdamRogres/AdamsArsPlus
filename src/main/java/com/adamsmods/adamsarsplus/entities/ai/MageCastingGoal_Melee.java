package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.custom.MysteriousMageEntity;
import com.adamsmods.adamsarsplus.entities.custom.SummonSkeleton_m;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.EntityHitResult;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

public class MageCastingGoal_Melee extends MeleeAttackGoal {
    MysteriousMageEntity mageEntity;

    private int attackDelay = 15;
    private int ticksUntilNextAttack = 5;
    private boolean shouldCountTillNextAttack = false;

    Supplier<Integer> spellCooldown;
    Supplier<Spell> mageSpell;
    Supplier<Boolean> canUse;

    boolean done;

    public MageCastingGoal_Melee(MysteriousMageEntity entity, double speed, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse, Supplier<Integer> spellCooldown, Supplier<Spell> mageSpell) {
        super(entity, speed, pFollowingTargetEvenIfNotSeen);

        this.mageEntity = entity;
        this.canUse = canUse;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        this.spellCooldown = spellCooldown;
        this.mageSpell = mageSpell;
    }

    @Override
    public void start() {
        super.start();
        attackDelay = 15;
        ticksUntilNextAttack = 5;
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
    }

    void performSpellAttack(LivingEntity entity, Spell spell, ParticleColor color, LivingEntity enemy){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

        resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));

        this.mageEntity.castCooldown = this.spellCooldown.get() + random.nextInt(this.spellCooldown.get());
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;

            if(isTimeToStartAttackAnimation()) {
                this.mageEntity.setAttacking(true);
            }

            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                performAttack(pEnemy);
                if(!pEnemy.isBlocking()){
                    performSpellAttack(this.mageEntity, mageSpell.get(), mageSpell.get().color, pEnemy);
                }
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            mageEntity.setAttacking(false);
            mageEntity.attackAnimationTimeout = 0;
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay + 8);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= attackDelay;
    }

    public int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected double getAttackReachSqr(LivingEntity pAttackTarget) {
        return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 1.0F);
    }

    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
        this.done = true;

    }

    @Override
    public void tick() {
        super.tick();
        if(shouldCountTillNextAttack){
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    public void stop() {
        mageEntity.setAttacking(false);
        this.done = false;
        super.stop();
    }
}


