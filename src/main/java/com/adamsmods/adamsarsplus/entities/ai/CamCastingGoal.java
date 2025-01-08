package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.entities.custom.CadeEntity;
import com.adamsmods.adamsarsplus.entities.custom.CamEntity;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateTwo;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectDivineSmite;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectColdSnap;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectDelay;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFreeze;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLightning;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

public class CamCastingGoal<T extends Mob & RangedAttackMob> extends Goal {
    CamEntity camEntity;

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

    private int attackDelay = 12;
    private int ticksUntilNextAttack = 8;
    private boolean shouldCountTillNextAttack = false;

    public CamCastingGoal(CamEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
        this.camEntity = entity;
        this.speedModifier = speed;
        this.attackRadiusSqr = attackRange * attackRange;
        this.canUse = canUse;
        this.animId = animId;
        this.delayTicks = delayTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

    }
    private ParticleColor camColor = new ParticleColor(255, 255, 255);

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

    void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
        DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
        projectileSpell.setColor(color);

        projectileSpell.shoot(entity, entity.getXRot(), entity.getYRot(), 0.0F, 1.0f, 0.8f);

        entity.level().addFreshEntity(projectileSpell);

        this.camEntity.castingCooldown = random.nextInt(180) + 120;
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.camEntity.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.camEntity.getNavigation().isDone()) && !this.done;
    }

    public void start() {
        super.start();
        this.camEntity.setAggressive(true);
        attackDelay = 12;
        ticksUntilNextAttack = 8;
    }

    public void stop() {
        super.stop();
        this.camEntity.setCasting(false);
        this.camEntity.setAggressive(false);
        this.seeTime = 0;
        this.animatedTicks = 0;
        this.done = false;
        this.hasAnimated = false;
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

    public void tick() {
        LivingEntity livingentity = this.camEntity.getTarget();
        if (livingentity != null) {
            double d0 = this.camEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean canSeeEnemy = this.camEntity.getSensing().hasLineOfSight(livingentity);
            if (canSeeEnemy != this.seeTime > 0) {
                this.seeTime = 0;
            }

            if (canSeeEnemy) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
                this.camEntity.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.camEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 10) {
                if ((double)this.camEntity.getRandom().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.camEntity.getRandom().nextFloat() < 0.3) {
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

                this.camEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.camEntity.lookAt(livingentity, 30.0F, 30.0F);
            } else {
                this.camEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }

            if (this.seeTime >= 10 && !this.hasAnimated) {
                this.hasAnimated = true;
                Networking.sendToNearby(this.camEntity.level(), this.camEntity, new PacketAnimEntity(this.camEntity.getId(), this.animId));
            }

            if (this.hasAnimated) {
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    camEntity.setCasting(true);
                }

                if(isTimeToAttack()) {
                    performCastAttack(this.camEntity, 1.0F, camCastSpell, camColor);
                    camEntity.setCasting(false);
                    this.done = true;
                }

            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                camEntity.setCasting(false);
                camEntity.castingAnimationTimeout = 0;
            }

        }

        if(shouldCountTillNextAttack){
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }

    }

}
