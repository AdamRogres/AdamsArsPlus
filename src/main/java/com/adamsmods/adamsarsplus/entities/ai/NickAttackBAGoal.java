package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.custom.NickEntity;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAOEThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateTwo;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentExtendTimeTwo;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectDomain;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectSwapTarget;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtract;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.EntityHitResult;

import java.util.function.Supplier;

import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

public class NickAttackBAGoal extends MeleeAttackGoal {
    private final NickEntity entity;

    private int attackDelay = 12;
    private int ticksUntilNextAttack = 8;
    private boolean shouldCountTillNextAttack = false;

    Supplier<Boolean> canUse;
    boolean done;

    public NickAttackBAGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((NickEntity) pMob);
        this.canUse = canUse;
    }

    @Override
    public void start() {
        super.start();
        attackDelay = 12;
        ticksUntilNextAttack = 8;
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
    }

    private ParticleColor nickColor = new ParticleColor(0, 150, 0);

    public Spell nickAttackBASpell = new Spell()
            .add(EffectDispel.INSTANCE)
            .add(EffectLaunch.INSTANCE)
            .add(EffectLaunch.INSTANCE)
            .add(EffectSlowfall.INSTANCE)
            .add(AugmentDurationDown.INSTANCE,3)

            .add(EffectDelay.INSTANCE)
            .add(AugmentDurationDown.INSTANCE)
            .add(EffectSwapTarget.INSTANCE)

            .add(EffectLaunch.INSTANCE)

            .withColor(nickColor);

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;

            if(isTimeToStartAttackAnimation()) {
                entity.setAttackingBA(true);
            }

            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                performAttack(pEnemy);
                performSpellAttack(this.mob, 1.0F, nickAttackBASpell, nickColor, pEnemy);
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttackingBA(false);
            entity.attackBAAnimationTimeout = 0;
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
        return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 4.5F);
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

        this.entity.attackBACooldown = random.nextInt(120) + 80;
        this.entity.attackBBCooldown = 100;
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
        entity.setAttackingBA(false);
        this.done = false;
        super.stop();
    }
}
