package com.adamsmods.adamsarsplus.entities.ai;

import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.entities.custom.MysteriousMageEntity;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateTwo;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

public class MageCastingGoal_Det<T extends Mob & RangedAttackMob> extends Goal {
    MysteriousMageEntity mageEntity;

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

    Supplier<Integer> spellCooldown;
    Supplier<Spell> mageSpell;

    Supplier<Boolean> canUse;

    private int attackDelay = 15;
    private int ticksUntilNextAttack = 5;
    private boolean shouldCountTillNextAttack = false;

    public MageCastingGoal_Det(MysteriousMageEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks, Supplier<Integer> spellCooldown, Supplier<Spell> mageSpell) {
        this.mageEntity = entity;
        this.speedModifier = speed;
        this.attackRadiusSqr = attackRange * attackRange;
        this.canUse = canUse;
        this.animId = animId;
        this.delayTicks = delayTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        this.spellCooldown = spellCooldown;
        this.mageSpell = mageSpell;
    }

    public void summonProjectiles(Level world, LivingEntity shooter, Spell stats, SpellResolver resolver) {
        int numSplits = 1 + stats.getInstanceCount(AugmentSplit.INSTANCE);

        List<DetonateProjectile> projectiles = new ArrayList();

        for(int i = 0; i < numSplits; ++i) {
            DetonateProjectile spell = new DetonateProjectile(world, resolver);

            projectiles.add(spell);
        }

        float velocity = Math.max(0.1F, 0.75F + (stats.getInstanceCount(AugmentAccelerate.INSTANCE) + 2 * stats.getInstanceCount(AugmentAccelerateTwo.INSTANCE) + 4 * stats.getInstanceCount(AugmentAccelerateThree.INSTANCE)) / 2.0F);
        int opposite = -1;
        int counter = 0;

        for(DetonateProjectile proj : projectiles) {
            proj.shoot(shooter, shooter.getXRot(), shooter.getYRot() + (float)(Math.round((double)counter / (double)2.0F) * 10L * (long)opposite), 0.0F, velocity, 0.8F);
            opposite *= -1;
            ++counter;
            world.addFreshEntity(proj);
        }

    }

    void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell){
        if(spell != null) {
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(spell.color));

            summonProjectiles(entity.level(), entity, spell, resolver);
        }

        System.out.println(this.spellCooldown.get());

        this.mageEntity.castCooldown = this.spellCooldown.get() + random.nextInt(this.spellCooldown.get());
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.mageEntity.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mageEntity.getNavigation().isDone()) && !this.done;
    }

    public void start() {
        super.start();
        this.mageEntity.setAggressive(true);
        attackDelay = 15;
        ticksUntilNextAttack = 5;
    }

    public void stop() {
        super.stop();
        this.mageEntity.setCasting(false);
        this.mageEntity.setAggressive(false);
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
        LivingEntity livingentity = this.mageEntity.getTarget();
        if (livingentity != null) {
            double d0 = this.mageEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean canSeeEnemy = this.mageEntity.getSensing().hasLineOfSight(livingentity);
            if (canSeeEnemy != this.seeTime > 0) {
                this.seeTime = 0;
            }

            if (canSeeEnemy) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
                this.mageEntity.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.mageEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 10) {
                if ((double)this.mageEntity.getRandom().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.mageEntity.getRandom().nextFloat() < 0.3) {
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

                this.mageEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.mageEntity.lookAt(livingentity, 30.0F, 30.0F);
            } else {
                this.mageEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }

            if (this.seeTime >= 10 && !this.hasAnimated) {
                this.hasAnimated = true;
                Networking.sendToNearby(this.mageEntity.level(), this.mageEntity, new PacketAnimEntity(this.mageEntity.getId(), this.animId));
            }

            if (this.hasAnimated) {
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    mageEntity.setCasting(true);
                }

                if(isTimeToAttack()) {
                    performCastAttack(this.mageEntity, 1.0F, this.mageSpell.get());
                    mageEntity.setCasting(false);
                    this.done = true;
                }

            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                mageEntity.setCasting(false);
                mageEntity.castAnimationTimeout = 0;
            }

        }

        if(shouldCountTillNextAttack){
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }

    }

}
