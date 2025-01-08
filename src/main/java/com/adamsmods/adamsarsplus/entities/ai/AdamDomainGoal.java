package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.custom.AdamEntity;
import com.adamsmods.adamsarsplus.entities.custom.CamEntity;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAOEThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentExtendTimeThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentOpenDomain;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectAnnihilate;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectDomain;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtract;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBurst;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLightning;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.phys.EntityHitResult;

import java.util.EnumSet;
import java.util.function.Supplier;

public class AdamDomainGoal<T extends Mob & RangedAttackMob> extends Goal {
    AdamEntity adamEntity;

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

    private int attackDelay = 10;
    private int ticksUntilNextAttack = 10;
    private boolean shouldCountTillNextAttack = false;

    public AdamDomainGoal(AdamEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
        this.adamEntity = entity;
        this.speedModifier = speed;
        this.attackRadiusSqr = attackRange * attackRange;
        this.canUse = canUse;
        this.animId = animId;
        this.delayTicks = delayTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

    }
    private ParticleColor adamColor = new ParticleColor(0, 0, 0);

    public Spell adamDomainSpell = new Spell()
            .add(EffectDomain.INSTANCE)
            .add(AugmentExtendTimeThree.INSTANCE)
            .add(AugmentAOEThree.INSTANCE, 4)
            .add(AugmentExtract.INSTANCE)
            .add(AugmentOpenDomain.INSTANCE)

            .add(EffectAnnihilate.INSTANCE)
            .add(AugmentAmplify.INSTANCE,2)
            .add(EffectBurst.INSTANCE)
            .add(EffectAnnihilate.INSTANCE)

            .withColor(adamColor);


    void performDomainAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

        resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));

        this.adamEntity.domainCooldown = 2000;
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.adamEntity.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.adamEntity.getNavigation().isDone()) && !this.done;
    }

    public void start() {
        super.start();
        this.adamEntity.setAggressive(true);
        attackDelay = 10;
        ticksUntilNextAttack = 10;
    }

    public void stop() {
        super.stop();
        this.adamEntity.setUsingDomain(false);
        this.adamEntity.setAggressive(false);
        this.seeTime = 0;
        this.animatedTicks = 0;
        this.done = false;
        this.hasAnimated = false;
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay + 24);
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
        LivingEntity livingentity = this.adamEntity.getTarget();
        if (livingentity != null) {
            double d0 = this.adamEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean canSeeEnemy = this.adamEntity.getSensing().hasLineOfSight(livingentity);
            if (canSeeEnemy != this.seeTime > 0) {
                this.seeTime = 0;
            }

            if (canSeeEnemy) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                this.adamEntity.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.adamEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 10) {
                if ((double)this.adamEntity.getRandom().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.adamEntity.getRandom().nextFloat() < 0.3) {
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

               // this.adamEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.adamEntity.lookAt(livingentity, 30.0F, 30.0F);
            } else {
                this.adamEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }

            if (this.seeTime >= 20 && !this.hasAnimated) {
                this.hasAnimated = true;
                Networking.sendToNearby(this.adamEntity.level(), this.adamEntity, new PacketAnimEntity(this.adamEntity.getId(), this.animId));
            }

            if (this.hasAnimated) {
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    adamEntity.setUsingDomain(true);
                }

                if(isTimeToAttack()) {
                    performDomainAttack(this.adamEntity, 1.0F, adamDomainSpell, adamColor);
                    adamEntity.setUsingDomain(false);
                    this.done = true;
                }

            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                adamEntity.setUsingDomain(false);
                adamEntity.castDomainAnimationTimeout = 0;
            }

        }

        if(shouldCountTillNextAttack){
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }

    }

}
