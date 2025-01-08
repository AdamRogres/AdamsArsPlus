package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.custom.AdamEntity;
import com.adamsmods.adamsarsplus.entities.custom.CamEntity;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectAnnihilate;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLightning;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.EntityHitResult;

import java.util.function.Supplier;

public class AdamAttackAAGoal extends MeleeAttackGoal {
    private final AdamEntity entity;

    private int attackDelay = 8;
    private int ticksUntilNextAttack = 7;
    private boolean shouldCountTillNextAttack = false;

    Supplier<Boolean> canUse;
    boolean done;

    public AdamAttackAAGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((AdamEntity) pMob);
        this.canUse = canUse;
    }

    @Override
    public void start() {
        super.start();
        attackDelay = 8;
        ticksUntilNextAttack = 7;
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
    }

    private ParticleColor adamColor = new ParticleColor(0, 0, 0);

    public Spell adamAttackAASpell = new Spell()
            .add(EffectAnnihilate.INSTANCE)
            .add(AugmentAmplify.INSTANCE,3)

            .withColor(adamColor);

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;

            if(isTimeToStartAttackAnimation()) {
                entity.setAttackingAA(true);
            }

            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());

                if(pEnemy.isBlocking()){

                }
                else {
                    performSpellAttack(this.mob, 1.0F, adamAttackAASpell, adamColor, pEnemy);
                }
                performAttack(pEnemy);

                this.entity.attackABCooldown = 50;
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttackingAA(false);
            entity.attackAAAnimationTimeout = 0;
        }
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay + 10);
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
        return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 3.0F);
    }

    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
        this.done = true;
    }

    void performSpellAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color, LivingEntity enemy){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

        resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));

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
        entity.setAttackingAA(false);
        this.done = false;
        super.stop();
    }
}
