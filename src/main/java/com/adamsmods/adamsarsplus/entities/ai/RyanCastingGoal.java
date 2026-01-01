package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.custom.CadeEntity;
import com.adamsmods.adamsarsplus.entities.custom.RyanEntity;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAOEThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateTwo;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAmplifyTwo;
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
import net.minecraft.world.phys.Vec3;

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
    private int ticksUntilNextAttack = 15;
    private int totalAnimation = 20;
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
    private ParticleColor  ryanColor = new ParticleColor(255, 0, 0);

    public Spell ryanCastSpell = new Spell()
            .add(AugmentAccelerateTwo.INSTANCE)
            .add(EffectIgnite.INSTANCE)
            .add(EffectFlare.INSTANCE)
            .add(AugmentAmplify.INSTANCE)

            .add(EffectExplosion.INSTANCE)
            .add(AugmentAmplify.INSTANCE,8)

            .add(EffectBurst.INSTANCE)
            .add(AugmentAOEThree.INSTANCE)
            .add(AugmentSensitive.INSTANCE)
            .add(EffectBreak.INSTANCE)
            .add(AugmentAmplifyTwo.INSTANCE)
            .add(EffectIgnite.INSTANCE)
            .add(EffectEvaporate.INSTANCE)

            .withColor(ryanColor);

    void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
        EntityProjectileSpell projectileSpell = new EntityProjectileSpell(entity.level(), resolver);
        projectileSpell.setColor(color);

        projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 1.0f, 0.8f);

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
        ticksUntilNextAttack = 15;

        LivingEntity $$0 = this.ryanEntity.getTarget();
        if ($$0 != null) {
            Vec3 $$1 = $$0.getEyePosition();
            //this.ryanEntity.moveControl.setWantedPosition($$1.x, $$1.y, $$1.z, (double)1.0F);
        }
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
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay);
    }

    protected void resetAttackLoopCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(totalAnimation);
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
            if(true){
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    ryanEntity.setCasting(true);
                }

                if(isTimeToAttack()) {
                    this.ryanEntity.getLookControl().setLookAt(livingentity);
                    performCastAttack(this.ryanEntity, 1.0F, ryanCastSpell, ryanColor);
                    this.done = true;
                    resetAttackLoopCooldown();
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
