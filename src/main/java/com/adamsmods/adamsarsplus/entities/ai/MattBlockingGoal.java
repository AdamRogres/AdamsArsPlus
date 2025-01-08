package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.custom.MattEntity;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAOEThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateTwo;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectSummonUndead;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.phys.EntityHitResult;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

public class MattBlockingGoal<T extends Mob & RangedAttackMob> extends Goal {
    MattEntity mattEntity;

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

    private int ticksUntilNextAttack = 100;

    public MattBlockingGoal(MattEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
        this.mattEntity = entity;
        this.speedModifier = speed;
        this.attackRadiusSqr = attackRange * attackRange;
        this.canUse = canUse;
        this.animId = animId;
        this.delayTicks = delayTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

    }
    private ParticleColor mattColor = new ParticleColor(255, 255, 0);

    public Spell mattBlockSpell = new Spell()

            .withColor(mattColor);

    void performBlockAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

        resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));

        this.mattEntity.blockCooldown = 0;
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.mattEntity.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mattEntity.getNavigation().isDone()) && !this.done;
    }

    public void start() {
        super.start();
        this.mattEntity.setAggressive(true);
        ticksUntilNextAttack = 100;
    }

    public void stop() {
        super.stop();
        this.mattEntity.setBlocking(false);
        this.mattEntity.setAggressive(false);
        this.seeTime = 0;
        this.animatedTicks = 0;
        this.done = false;
        this.hasAnimated = false;
    }

    public void tick() {
        LivingEntity livingentity = this.mattEntity.getTarget();

        if (livingentity != null) {
            double d0 = this.mattEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean canSeeEnemy = this.mattEntity.getSensing().hasLineOfSight(livingentity);
            if (canSeeEnemy != this.seeTime > 0) {
                this.seeTime = 0;
            }

            if (canSeeEnemy) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
                this.mattEntity.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.mattEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 10) {
                if ((double)this.mattEntity.getRandom().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.mattEntity.getRandom().nextFloat() < 0.3) {
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

                this.mattEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.mattEntity.lookAt(livingentity, 30.0F, 30.0F);
            } else {
                this.mattEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }

            if (this.seeTime >= 10 && !this.hasAnimated) {
                this.hasAnimated = true;
                Networking.sendToNearby(this.mattEntity.level(), this.mattEntity, new PacketAnimEntity(this.mattEntity.getId(), this.animId));
            }
        }

        this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);

        if(this.ticksUntilNextAttack > 0){
            this.mattEntity.setBlocking(true);

        } else {
            this.mattEntity.setBlocking(false);

        }

    }

}
