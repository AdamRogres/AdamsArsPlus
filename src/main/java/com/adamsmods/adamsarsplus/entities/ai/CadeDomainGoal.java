package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.custom.CadeEntity;
import com.adamsmods.adamsarsplus.entities.custom.RyanEntity;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectDomain;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtract;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.phys.EntityHitResult;

import java.util.EnumSet;
import java.util.function.Supplier;

public class CadeDomainGoal<T extends Mob & RangedAttackMob> extends Goal {
    CadeEntity cadeEntity;

    private final double speedModifier;
    private final float attackRadiusSqr;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    boolean hasAnimated;
    int animatedTicks;
    int delayTicks;
    int animId;
    boolean done;

    Supplier<Boolean> canUse;

    private int attackDelay = 15;
    private int ticksUntilNextAttack = 10;
    private boolean shouldCountTillNextAttack = false;

    public CadeDomainGoal(CadeEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
        this.cadeEntity = entity;
        this.speedModifier = speed;
        this.attackRadiusSqr = attackRange * attackRange;
        this.canUse = canUse;
        this.animId = animId;
        this.delayTicks = delayTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

    }
    private ParticleColor cadeColor = new ParticleColor(150, 150, 255);

    public Spell cadeDomainSpell = new Spell()
            .add(AugmentAccelerateThree.INSTANCE)
            .add(EffectDomain.INSTANCE)
            .add(AugmentExtendTimeThree.INSTANCE)
            .add(AugmentAOEThree.INSTANCE, 2)
            .add(AugmentExtract.INSTANCE)
            .add(AugmentAccelerateTwo.INSTANCE)

            .add(EffectFreeze.INSTANCE)

            .add(EffectBurst.INSTANCE)
            .add(AugmentAOE.INSTANCE)
            .add(AugmentSensitive.INSTANCE)
            .add(EffectConjureWater.INSTANCE)
            .add(EffectFreeze.INSTANCE)

            .withColor(cadeColor);


    void performDomainAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

        resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));

        this.cadeEntity.domainCooldown = 2000;
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.cadeEntity.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.cadeEntity.getNavigation().isDone()) && !this.done;
    }

    public void start() {
        super.start();
        this.cadeEntity.setAggressive(true);
        attackDelay = 15;
        ticksUntilNextAttack = 10;
    }

    public void stop() {
        super.stop();
        this.cadeEntity.setUsingDomain(false);
        this.cadeEntity.setAggressive(false);
        this.seeTime = 0;
        this.animatedTicks = 0;
        this.done = false;
        this.hasAnimated = false;
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

    public void tick() {
        LivingEntity livingentity = this.cadeEntity.getTarget();
        if (livingentity != null) {
            double d0 = this.cadeEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean canSeeEnemy = this.cadeEntity.getSensing().hasLineOfSight(livingentity);
            if (canSeeEnemy != this.seeTime > 0) {
                this.seeTime = 0;
            }

            if (canSeeEnemy) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                this.cadeEntity.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.cadeEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.cadeEntity.getRandom().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.cadeEntity.getRandom().nextFloat() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (d0 > (double)(this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double)(this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.cadeEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.cadeEntity.lookAt(livingentity, 30.0F, 30.0F);
            } else {
                this.cadeEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }

            if (this.seeTime >= 40 && !this.hasAnimated) {
                this.hasAnimated = true;
                Networking.sendToNearby(this.cadeEntity.level(), this.cadeEntity, new PacketAnimEntity(this.cadeEntity.getId(), this.animId));
            }

            if (this.hasAnimated) {
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    cadeEntity.setUsingDomain(true);
                }

                if(isTimeToAttack()) {
                    performDomainAttack(this.cadeEntity, 1.0F, cadeDomainSpell, cadeColor);
                    cadeEntity.setUsingDomain(false);
                    this.done = true;
                }

            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                cadeEntity.setUsingDomain(false);
                cadeEntity.castDomainAnimationTimeout = 0;
            }

        }

        if(shouldCountTillNextAttack){
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }

    }

}
