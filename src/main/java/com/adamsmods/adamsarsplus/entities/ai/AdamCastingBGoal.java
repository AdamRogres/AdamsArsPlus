package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.entities.custom.AdamEntity;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.*;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class AdamCastingBGoal<T extends Mob & RangedAttackMob> extends Goal {
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

    int randomSpell = 0;

    Supplier<Boolean> canUse;

    private int attackDelay = 15;
    private int ticksUntilNextAttack = 5;
    private boolean shouldCountTillNextAttack = false;

    public AdamCastingBGoal(AdamEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
        this.adamEntity = entity;
        this.speedModifier = speed;
        this.attackRadiusSqr = attackRange * attackRange;
        this.canUse = canUse;
        this.animId = animId;
        this.delayTicks = delayTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

    }
    private ParticleColor ryanColor = new ParticleColor(255, 0, 0);
    private ParticleColor cadeColor = new ParticleColor(150, 150, 255);
    private ParticleColor nickColor = new ParticleColor(0, 150, 0);
    private ParticleColor camColor = new ParticleColor(255, 255, 255);
    private ParticleColor mattColor = new ParticleColor(255, 255, 0);

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

    public Spell cadeCastSpell = new Spell()
            .add(AugmentAccelerateThree.INSTANCE)

            .add(EffectFreeze.INSTANCE)
            .add(AugmentExtendTimeTwo.INSTANCE)
            .add(EffectColdSnap.INSTANCE)
            .add(AugmentAmplify.INSTANCE,8)

            .add(EffectBurst.INSTANCE)
            .add(AugmentSensitive.INSTANCE)
            .add(EffectConjureWater.INSTANCE)
            .add(AugmentPierce.INSTANCE,2)
            .add(EffectFreeze.INSTANCE)
            .add(AugmentPierce.INSTANCE,2)

            .add(EffectDelay.INSTANCE)
            .add(EffectIceburst.INSTANCE)
            .add(AugmentAmplify.INSTANCE,3)
            .add(AugmentAOEThree.INSTANCE,2)

            .withColor(cadeColor);

    public Spell nickCastSpell = new Spell()
            .add(AugmentAccelerateThree.INSTANCE)

            .add(EffectBurst.INSTANCE)
            .add(AugmentSensitive.INSTANCE)
            .add(AugmentAOETwo.INSTANCE)
            .add(EffectRaiseEarth.INSTANCE)

            .add(EffectBurst.INSTANCE)
            .add(EffectGravity.INSTANCE)
            .add(AugmentExtendTimeTwo.INSTANCE)
            .add(EffectSnare.INSTANCE)

            .add(EffectDelay.INSTANCE)
            .add(EffectKnockback.INSTANCE)

            .withColor(nickColor);

    public Spell camCastSpell = new Spell()
            .add(AugmentAccelerateTwo.INSTANCE)
            .add(AugmentPierce.INSTANCE,3)
            .add(AugmentDurationDown.INSTANCE,1)

            .add(EffectLightning.INSTANCE)
            .add(AugmentAmplify.INSTANCE,6)

            .add(EffectDelay.INSTANCE)
            .add(AugmentDurationDown.INSTANCE)

            .add(EffectDivineSmite.INSTANCE)
            .add(AugmentAmplify.INSTANCE,6)

            .withColor(camColor);

    public Spell mattCastSpell = new Spell()
            .add(AugmentAccelerateTwo.INSTANCE)
            .add(AugmentDurationDown.INSTANCE)

            .add(EffectSummonUndead_boss.INSTANCE)
            .add(AugmentSplit.INSTANCE, 2)
            .add(AugmentAmplify.INSTANCE,4)
            .add(AugmentPierce.INSTANCE)

            .add(EffectSummonUndead_boss.INSTANCE)
            .add(AugmentSplit.INSTANCE, 2)
            .add(AugmentAmplify.INSTANCE,4)

            .withColor(mattColor);

    void performCastBAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
        DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
        projectileSpell.setColor(color);

        projectileSpell.shoot(entity, entity.getXRot(), entity.getYRot(), 0.0F, 1.0f, 0.8f);

        entity.level().addFreshEntity(projectileSpell);

        this.adamEntity.castingBCooldown = random.nextInt(100) + 200;
    }

    void performChargeParticle(LivingEntity entity) {
        Level world = entity.level();

        Vec3 livingEyes = entity.getEyePosition();
        double x = livingEyes.x;
        double y = livingEyes.y;
        double z = livingEyes.z;

        double xOff = cos(entity.getXRot());
        double zOff = sin(entity.getXRot());

        if (world instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x + xOff, y, z + zOff, 1, 0, 0, 0, 0.2);

        }
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
        attackDelay = 15;
        ticksUntilNextAttack = 5;
    }

    public void stop() {
        super.stop();
        this.adamEntity.setCastingB(false);
        this.adamEntity.setAggressive(false);
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

            if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
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

            if (this.seeTime >= 10 && !this.hasAnimated) {
                this.hasAnimated = true;
                Networking.sendToNearby(this.adamEntity.level(), this.adamEntity, new PacketAnimEntity(this.adamEntity.getId(), this.animId));
            }

            if (this.hasAnimated) {
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    adamEntity.setCastingB(true);
                }

                if(isTimeToAttack()) {
                    randomSpell = random.nextInt(100);
                    if(randomSpell > 80){
                        performCastBAttack(this.adamEntity, 1.0F, ryanCastSpell, ryanColor);
                    } else if (randomSpell > 60) {
                        performCastBAttack(this.adamEntity, 1.0F, cadeCastSpell, cadeColor);
                    } else if (randomSpell > 40){
                        performCastBAttack(this.adamEntity, 1.0F, nickCastSpell, nickColor);
                    } else if (randomSpell > 20){
                        performCastBAttack(this.adamEntity, 1.0F, camCastSpell, camColor);
                    } else {
                        performCastBAttack(this.adamEntity, 1.0F, mattCastSpell, mattColor);
                    }

                    adamEntity.setCastingB(false);
                    this.done = true;
                }

            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                adamEntity.setCastingB(false);
                adamEntity.castAAnimationTimeout = 0;
            }

        }

        if(shouldCountTillNextAttack){
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }

    }

}
