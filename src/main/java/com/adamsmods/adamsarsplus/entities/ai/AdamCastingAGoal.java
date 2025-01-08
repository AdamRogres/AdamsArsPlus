package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.entities.custom.AdamEntity;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAOEThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateThree;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectAnnihilate;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBurst;
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

public class AdamCastingAGoal<T extends Mob & RangedAttackMob> extends Goal {
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

    private int attackDelay = 13;
    private int ticksUntilNextAttack = 7;
    private boolean shouldCountTillNextAttack = false;

    public AdamCastingAGoal(AdamEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
        this.adamEntity = entity;
        this.speedModifier = speed;
        this.attackRadiusSqr = attackRange * attackRange;
        this.canUse = canUse;
        this.animId = animId;
        this.delayTicks = delayTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

    }
    private ParticleColor adamColor = new ParticleColor(150, 0, 255);

    public Spell adamCastASpell = new Spell()
            .add(AugmentAccelerateThree.INSTANCE)
            .add(AugmentPierce.INSTANCE,15)
            .add(AugmentDurationDown.INSTANCE,8)

            .add(EffectBurst.INSTANCE)
            .add(AugmentSensitive.INSTANCE)
            .add(AugmentAOEThree.INSTANCE)
            .add(EffectAnnihilate.INSTANCE)
            .add(AugmentAmplify.INSTANCE,2)

            .add(EffectBurst.INSTANCE)
            .add(EffectAnnihilate.INSTANCE)
            .add(AugmentAmplify.INSTANCE,4)

            .withColor(adamColor);

    void performCastAAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
        DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
        projectileSpell.setColor(color);

        projectileSpell.shoot(entity, entity.getXRot(), entity.getYRot(), 0.0F, 1.0f, 0.8f);

        entity.level().addFreshEntity(projectileSpell);

        this.adamEntity.castingACooldown = random.nextInt(100) + 200;
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
        attackDelay = 13;
        ticksUntilNextAttack = 7;
    }

    public void stop() {
        super.stop();
        this.adamEntity.setCastingA(false);
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

                this.adamEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
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

                if(isTimeToStartAttackAnimation() && !isTimeToAttack()){
                    performChargeParticle(this.adamEntity);
                }

                if(isTimeToStartAttackAnimation()) {
                    adamEntity.setCastingA(true);
                }

                if(isTimeToAttack()) {
                    performCastAAttack(this.adamEntity, 1.0F, adamCastASpell, adamColor);
                    adamEntity.setCastingA(false);
                    this.done = true;
                }

            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                adamEntity.setCastingA(false);
                adamEntity.castAAnimationTimeout = 0;
            }

        }

        if(shouldCountTillNextAttack){
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }

    }

}
