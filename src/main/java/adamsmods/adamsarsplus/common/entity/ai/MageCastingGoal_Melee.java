package adamsmods.adamsarsplus.common.entity.ai;

import adamsmods.adamsarsplus.common.entity.custom.MysteriousMageEntity;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.EntityHitResult;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

public class MageCastingGoal_Melee extends MeleeAttackGoal {
        // Reach in blocks (entity-position distance). Edit independently for this goal.
        public double meleeReach = Math.sqrt((double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + 0.6F + 1.0F));

        @Override
        protected boolean canPerformAttack(LivingEntity target) {
            return target.isAlive()
                    && this.mob.distanceToSqr(target) <= this.getAttackReachSqr(target)
                    && this.mob.getSensing().hasLineOfSight(target);
        }

    MysteriousMageEntity mageEntity;

    private int attackDelay = 15;
    private int ticksUntilNextAttack = 15;
    private int totalAnimation = 20;
    private boolean swinging;
        private int attackTicks;
        private LivingEntity attackTarget;

    Supplier<Integer> spellCooldown;
    Supplier<Spell> mageSpell;
    Supplier<Boolean> canUse;

    boolean done;

    public MageCastingGoal_Melee(MysteriousMageEntity entity, double speed, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse, Supplier<Integer> spellCooldown, Supplier<Spell> mageSpell) {
        super(entity, speed, pFollowingTargetEvenIfNotSeen);

        this.mageEntity = entity;
        this.canUse = canUse;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        this.spellCooldown = spellCooldown;
        this.mageSpell = mageSpell;
    }

    @Override
    public void start() {
            this.swinging = false;
            this.attackTicks = 0;
            this.attackTarget = null;
            this.done = false;
        super.start();
        attackDelay = 15;
        ticksUntilNextAttack = 15;
    }

    public boolean canUse() {
        return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
    }

    public boolean canContinueToUse() {
            return !this.done && this.mob.getTarget() != null
                    && this.mob.getTarget().isAlive() && (this.swinging || this.canUse());
        }

    void performSpellAttack(LivingEntity entity, Spell spell, LivingEntity enemy){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)));

        resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));

        this.mageEntity.castCooldown = 10 + random.nextInt(this.spellCooldown.get());
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy) {
            if (this.done) {
                return;
            }
            if (!this.swinging) {
                if (!this.canPerformAttack(pEnemy)) {
                    return;
                }
                this.swinging = true;
                this.attackTarget = pEnemy;
                this.attackTicks = 0;
            }
            pEnemy = this.attackTarget;
            this.attackTicks++;
            this.ticksUntilNextAttack = this.attackDelay - this.attackTicks;
            // Resolve each hit once; losing reach does not cancel the swing.


            if(isTimeToStartAttackAnimation()) {
                this.mageEntity.setAttacking(true);
            }

            if(isTimeToAttack() && this.canPerformAttack(pEnemy)) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                performAttack(pEnemy);
                if(!pEnemy.isBlocking()){
                    performSpellAttack(this.mageEntity, mageSpell.get(), pEnemy);
                }
            }

            if (this.attackTicks >= this.totalAnimation) {
                this.done = true;
            }
        }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.attackDelay;
    }

    protected boolean isTimeToAttack() {
        return this.attackTicks == this.attackDelay;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.attackTicks == 1;
    }

    public int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected double getAttackReachSqr(LivingEntity pAttackTarget) {
        return meleeReach * meleeReach;
    }

    protected void performAttack(LivingEntity pEnemy) {
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void stop() {
            this.swinging = false;
            this.attackTicks = 0;
            this.attackTarget = null;
        mageEntity.setAttacking(false);
        this.done = false;
        super.stop();
    }
        @Override
        public boolean isInterruptable() {
            return !this.swinging;
        }
    }


