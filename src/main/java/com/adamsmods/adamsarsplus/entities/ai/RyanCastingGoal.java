package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.custom.CadeEntity;
import com.adamsmods.adamsarsplus.entities.custom.RyanEntity;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAOEThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateTwo;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentExtendTimeTwo;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

public class RyanCastingGoal<T extends Mob & RangedAttackMob> extends Goal {
    RyanEntity ryanEntity;

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
    private int ticksUntilNextAttack = 5;
    private boolean shouldCountTillNextAttack = false;

    public RyanCastingGoal(RyanEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
        this.ryanEntity = entity;
        this.speedModifier = speed;
        this.attackRadiusSqr = attackRange * attackRange;
        this.canUse = canUse;
        this.animId = animId;
        this.delayTicks = delayTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

    }
    private ParticleColor ryanColor = new ParticleColor(255, 0, 0);

    public Spell ryanCastSpell = new Spell()
            .add(AugmentAccelerateTwo.INSTANCE)
            .add(EffectIgnite.INSTANCE)
            .add(EffectFlare.INSTANCE)
            .add(AugmentAmplify.INSTANCE)

            .add(EffectExplosion.INSTANCE)
            .add(AugmentAmplify.INSTANCE,4)

            .add(EffectBurst.INSTANCE)
            .add(AugmentAOEThree.INSTANCE)
            .add(AugmentSensitive.INSTANCE)
            .add(EffectIgnite.INSTANCE)
            .add(EffectEvaporate.INSTANCE)

            .withColor(ryanColor);

    void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
        EntityProjectileSpell projectileSpell = new EntityProjectileSpell(entity.level(), resolver);
        projectileSpell.setColor(color);

        projectileSpell.shoot(entity, entity.getXRot(), entity.getYRot(), 0.0F, 1.0f, 0.8f);

        entity.level().addFreshEntity(projectileSpell);

        this.ryanEntity.castCooldown = random.nextInt(200) + 80;
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.ryanEntity.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.ryanEntity.getNavigation().isDone()) && !this.done;
    }

    public void start() {
        super.start();
        this.ryanEntity.setAggressive(true);
        attackDelay = 15;
        ticksUntilNextAttack = 5;
    }

    public void stop() {
        super.stop();
        this.ryanEntity.setCasting(false);
        this.ryanEntity.setAggressive(false);
        this.seeTime = 0;
        this.animatedTicks = 0;
        this.done = false;
        this.hasAnimated = false;
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay + 5);
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
        LivingEntity livingentity = this.ryanEntity.getTarget();
        if (livingentity != null) {
            double d0 = this.ryanEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean canSeeEnemy = this.ryanEntity.getSensing().hasLineOfSight(livingentity);

            if (canSeeEnemy != this.seeTime > 0) {
                this.seeTime = 0;
            }

            if (canSeeEnemy) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
                this.ryanEntity.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.ryanEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 10) {
                if ((double)this.ryanEntity.getRandom().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.ryanEntity.getRandom().nextFloat() < 0.3) {
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

                this.ryanEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.ryanEntity.lookAt(livingentity, 30.0F, 30.0F);
            } else {
                this.ryanEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }

            if (this.seeTime >= 10 && !this.hasAnimated) {
                this.hasAnimated = true;
                Networking.sendToNearby(this.ryanEntity.level(), this.ryanEntity, new PacketAnimEntity(this.ryanEntity.getId(), this.animId));
            }

            if(true){
            //if (this.hasAnimated) {
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    ryanEntity.setCasting(true);
                }

                if(isTimeToAttack()) {
                    performCastAttack(this.ryanEntity, 1.0F, ryanCastSpell, ryanColor);
                    ryanEntity.setCasting(false);
                    this.done = true;
                }

            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                ryanEntity.setCasting(false);
                ryanEntity.castAnimationTimeout = 0;
            }

        }

        if(shouldCountTillNextAttack){
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }

    }

}
