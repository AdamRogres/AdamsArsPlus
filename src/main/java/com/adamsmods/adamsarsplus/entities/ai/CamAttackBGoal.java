package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.custom.CamEntity;
import com.adamsmods.adamsarsplus.entities.custom.NickEntity;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.EntityHitResult;

import java.util.function.Supplier;

import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

public class CamAttackBGoal extends MeleeAttackGoal {
    private final CamEntity entity;

    private int attackDelay = 5;
    private int ticksUntilNextAttack = 10;
    private boolean shouldCountTillNextAttack = false;

    Supplier<Boolean> canUse;
    boolean done;

    public CamAttackBGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((CamEntity) pMob);
        this.canUse = canUse;
    }

    @Override
    public void start() {
        super.start();
        attackDelay = 5;
        ticksUntilNextAttack = 10;
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
    }

    private ParticleColor camColor = new ParticleColor(255, 255, 255);

    public Spell camAttackB1Spell = new Spell()
            .add(EffectBlink.INSTANCE)

            .withColor(camColor);

    public Spell camAttackB2Spell = new Spell()
            .add(EffectLaunch.INSTANCE)

            .add(EffectDelay.INSTANCE)
            .add(EffectWindshear.INSTANCE)
            .add(AugmentAmplify.INSTANCE, 9)

            .withColor(camColor);

    public Spell camAttackB3Spell = new Spell()
            .add(EffectKnockback.INSTANCE)

            .withColor(camColor);

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;

            if(isTimeToStartAttackAnimation()) {
                entity.setAttackingB(true);
            }
            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                performAttack(pEnemy);
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttackingB(false);
            entity.attackBAnimationTimeout = 0;
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(15);
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
        return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 15F);
    }

    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);

        this.done = true;

        if(pEnemy.isBlocking()){
            performSpellAttack(this.mob, 1.0F, camAttackB1Spell, camAttackB3Spell, camColor, pEnemy);
        }
        else{
            performSpellAttack(this.mob, 1.0F, camAttackB1Spell, camAttackB2Spell, camColor, pEnemy);
        }
    }

    void performSpellAttack(LivingEntity entity, float p_82196_2_, Spell spell1, Spell spell2, ParticleColor color, LivingEntity enemy){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell1, entity, new LivingCaster(entity)).withColors(color));
        resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));

        EntitySpellResolver resolver2 = new EntitySpellResolver(new SpellContext(entity.level(), spell2, entity, new LivingCaster(entity)).withColors(color));
        resolver2.onResolveEffect(entity.level(), new EntityHitResult(enemy));

        this.entity.attackBCooldown = random.nextInt(200) + 200;
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
        entity.setAttackingB(false);
        this.done = false;
        super.stop();
    }
}
