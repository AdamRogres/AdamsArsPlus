package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.custom.NickEntity;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectRaiseEarth;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectSwapTarget;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.EntityHitResult;

import java.util.function.Supplier;

import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

public class NickAttackCGoal extends MeleeAttackGoal {
    private final NickEntity entity;

    private int attackDelay = 4;
    private int attackDelay2 = 20;

    private int ticksUntilNextAttack = 3;
    private boolean shouldCountTillNextAttack = false;

    Supplier<Boolean> canUse;
    boolean done;

    public NickAttackCGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((NickEntity) pMob);
        this.canUse = canUse;
    }

    @Override
    public void start() {
        super.start();
        attackDelay = 4;
        attackDelay2 = 20;
        ticksUntilNextAttack = 3;
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
    }

    private ParticleColor nickColor = new ParticleColor(0, 150, 0);

    public Spell nickAttackC1Spell = new Spell()
            .add(EffectBurst.INSTANCE)
            .add(AugmentAOE.INSTANCE,3)
            .add(EffectFangs.INSTANCE)

            .withColor(nickColor);

    public Spell nickAttackC2Spell = new Spell()
            .add(EffectBurst.INSTANCE)
            .add(AugmentSensitive.INSTANCE)
            .add(EffectRaiseEarth.INSTANCE)
            .add(EffectDelay.INSTANCE)
            .add(EffectBreak.INSTANCE)
            .add(AugmentAmplify.INSTANCE,2)

            .withColor(nickColor);

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;

            if(isTimeToStartAttackAnimation()) {
                entity.setAttackingC(true);
            }
            if(isTimeToStartAttack2()){
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(pEnemy);
            }
            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                performAttack(pEnemy);
                performSpellAttack(this.mob, 1.0F, nickAttackC1Spell, nickAttackC2Spell, nickColor, pEnemy);
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttackingC(false);
            entity.attackCAnimationTimeout = 0;
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(27);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= attackDelay;
    }

    protected boolean isTimeToStartAttack2() {
        return this.ticksUntilNextAttack <= attackDelay2;
    }
    public int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected double getAttackReachSqr(LivingEntity pAttackTarget) {
        return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 4.5F);
    }

    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);

        this.done = true;

    }

    void performSpellAttack(LivingEntity entity, float p_82196_2_, Spell spell1, Spell spell2, ParticleColor color, LivingEntity enemy){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell1, entity, new LivingCaster(entity)).withColors(color));
        resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));

        EntitySpellResolver resolver2 = new EntitySpellResolver(new SpellContext(entity.level(), spell2, entity, new LivingCaster(entity)).withColors(color));
        resolver2.onResolveEffect(entity.level(), new EntityHitResult(enemy));

        this.entity.attackCCooldown = random.nextInt(120) + 200;
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
        entity.setAttackingC(false);
        this.done = false;
        super.stop();
    }
}
