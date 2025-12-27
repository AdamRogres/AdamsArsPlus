package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.entities.ai.DDogAttackGoal;
import com.adamsmods.adamsarsplus.entities.ai.DDogLungeAttackGoal;
import com.adamsmods.adamsarsplus.entities.ai.DDogSprintGoal;
import com.adamsmods.adamsarsplus.entities.ai.pathfinding.AdvancedPathNavigate;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.IFollowingSummon;
import com.hollingsworth.arsnouveau.common.entity.goal.FollowSummonerGoal;
import com.hollingsworth.arsnouveau.common.entity.pathfinding.PathingStuckHandler;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.HOLY_LEGION_EFFECT;
import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.TENSHADOWS_EFFECT;

public class TerraprismaEntity extends Monster implements IFollowingSummon, ISummon {
    private LivingEntity owner;
    private @Nullable BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;

    public static final EntityDataAccessor<Boolean> ATTACKING_A =
            SynchedEntityData.defineId(TerraprismaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING_B =
            SynchedEntityData.defineId(TerraprismaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING_C =
            SynchedEntityData.defineId(TerraprismaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> COLOR =
            SynchedEntityData.defineId(TerraprismaEntity.class, EntityDataSerializers.STRING);

    public int ticksLeft;
    public boolean isSummon;
    public String color;
    public int iTime = 0;

    public TerraprismaEntity(Level level, LivingEntity owner, boolean summon) {
        super((EntityType) AdamsModEntities.TERRA_ENTITY.get(), level);

        this.owner = owner;
        this.limitedLifespan = true;
        this.setOwnerID(owner.getUUID());
        this.isSummon = summon;

        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    public TerraprismaEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);

        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    public EntityType<?> getType() {
        return (EntityType)AdamsModEntities.TERRA_ENTITY.get();
    }

    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAAnimationState = new AnimationState();
    public int attackAAnimationTimeout = 0;

    public final AnimationState attackBAnimationState = new AnimationState();
    public int attackBAnimationTimeout = 0;

    public final AnimationState attackCAnimationState = new AnimationState();
    public int attackCAnimationTimeout = 0;

    public void setAttackingA(boolean AttackingA) { this.entityData.set(ATTACKING_A, AttackingA); }
    public boolean isAttackingA(){ return this.entityData.get(ATTACKING_A); }

    public void setAttackingB(boolean AttackingB) { this.entityData.set(ATTACKING_B, AttackingB); }
    public boolean isAttackingB(){ return this.entityData.get(ATTACKING_B); }

    public void setAttackingC(boolean AttackingC) { this.entityData.set(ATTACKING_C, AttackingC); }
    public boolean isAttackingC(){ return this.entityData.get(ATTACKING_C); }

    public void setColor(String nColor) {this.entityData.set(COLOR, nColor); }
    public String getColor(){ return this.entityData.get(COLOR); }

    @Override
    public void tick() {
        super.tick();

        iTime++;

        // Sword Color Animation Control
        switch(iTime % 18){
            case 1   -> this.setColor("one");
            case 2   -> this.setColor("two");
            case 3   -> this.setColor("three");
            case 4   -> this.setColor("four");
            case 5   -> this.setColor("five");
            case 6   -> this.setColor("six");
            case 7   -> this.setColor("seven");
            case 8   -> this.setColor("eight");
            case 9   -> this.setColor("nine");
            case 10  -> this.setColor("ten");
            case 11  -> this.setColor("eleven");
            case 12  -> this.setColor("twelve");
            case 13  -> this.setColor("thirteen");
            case 14  -> this.setColor("fourteen");
            case 15  -> this.setColor("fifteen");
            case 16  -> this.setColor("sixteen");
            case 17  -> this.setColor("seventeen");
            default  -> this.setColor("eighteen");
        }

        if(this.getSummoner() != null) {
            if (!this.level().isClientSide && this.isSummon && !this.getSummoner().hasEffect(HOLY_LEGION_EFFECT.get())) {
                spawnShadowPoof((ServerLevel) this.level(), this.blockPosition());
                this.remove(RemovalReason.DISCARDED);
                this.onSummonDeath(this.level(), (DamageSource) null, true);
            }
        }

        if(this.level().isClientSide()) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        this.color = this.getColor();

        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        //Attack A Animation control
        if(this.isAttackingA() && attackAAnimationTimeout <= 0) {
            attackAAnimationTimeout = 12;
            attackAAnimationState.start(this.tickCount);
        } else {
            --this.attackAAnimationTimeout;
        }
        if(!this.isAttackingA()) {
            attackAAnimationState.stop();
        }
        //Attack B Animation control
        if(this.isAttackingB() && attackBAnimationTimeout <= 0) {
            attackBAnimationTimeout = 12;
            attackBAnimationState.start(this.tickCount);
        } else {
            --this.attackBAnimationTimeout;
        }
        if(!this.isAttackingB()) {
            attackBAnimationState.stop();
        }
        //Attack C Animation control
        if(this.isAttackingC() && attackCAnimationTimeout <= 0) {
            attackCAnimationTimeout = 12;
            attackCAnimationState.start(this.tickCount);
        } else {
            --this.attackCAnimationTimeout;
        }
        if(!this.isAttackingC()) {
            attackCAnimationState.stop();
        }

    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1F);
        } else {
            f = 0f;
        }
        if(this.isAttackingA() || this.isAttackingB() || this.isAttackingC()){
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(TerraprismaEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UUID, Optional.of(Util.NIL_UUID));
        this.entityData.define(ATTACKING_A, false);
        this.entityData.define(ATTACKING_B, false);
        this.entityData.define(ATTACKING_C, false);
        this.entityData.define(COLOR, "white");
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        onSummonDeath(level(), cause, false);
        this.getOwner().removeEffect(HOLY_LEGION_EFFECT.get());
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypes.MOB_ATTACK)) {
            Entity var4 = pSource.getEntity();
            if (var4 instanceof ISummon) {
                ISummon summon = (ISummon)var4;
                if (summon.getOwnerUUID() != null && summon.getOwnerUUID().equals(this.getOwnerUUID())) {
                    return false;
                }
            }
        }
        if(pSource.is(DamageTypes.FALL)){
            return false;
        }

        return super.hurt(pSource, pAmount);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    public Team getTeam() {
        return this.getSummoner() != null ? this.getSummoner().getTeam() : super.getTeam();
    }

    public boolean isAlliedTo(Entity pEntity) {
        LivingEntity summoner = this.getSummoner();
        if (summoner != null) {
            if (pEntity instanceof ISummon) {
                ISummon summon = (ISummon)pEntity;
                if (summon.getOwnerUUID() != null && summon.getOwnerUUID().equals(this.getOwnerUUID())) {
                    return true;
                }
            }

            return pEntity == summoner || summoner.isAlliedTo(pEntity);
        } else {
            return super.isAlliedTo(pEntity);
        }
    }

    public Level getWorld() {
        return this.level();
    }

    public PathNavigation getPathNav() {
        return this.navigation;
    }

    public Mob getSelfEntity() {
        return this;
    }

    public LivingEntity getSummoner() {
        return this.getOwnerFromID();
    }

    public LivingEntity getActualOwner() {
        return this.owner;
    }

    public int getExperienceReward() {
        return 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerUUID() == null) {
            compound.putUUID("OwnerUUID", Util.NIL_UUID);
        } else {
            compound.putUUID("OwnerUUID", this.getOwnerUUID());
        }

        compound.putInt("left", ticksLeft);
        compound.putBoolean("Summon", isSummon);
        compound.putString("color", this.getColor());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        this.setOwnerID(compound.getUUID("OwnerUUID"));
        this.owner = this.getOwnerFromID();

        this.ticksLeft = compound.getInt("left");
        this.isSummon = compound.getBoolean("Summon");
        this.color = compound.getString("color");
        this.setColor(this.color);
    }

    public void setLimitedLife(int lifeTicks) {
        this.limitedLifeTicks = lifeTicks;
    }

    public LivingEntity getOwnerFromID() {
        try {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : this.level().getPlayerByUUID(uuid);
        } catch (IllegalArgumentException var21) {
            return null;
        }
    }

    @Override
    public int getTicksLeft() {
        return ticksLeft;
    }

    @Override
    public void setTicksLeft(int ticks) {
        this.ticksLeft = ticks;
    }

    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(1, new FollowSummonerGoal(this, this.getOwner(), (double) 1.0F, 25.0F, 3.0F));
        this.goalSelector.addGoal(2, new TerraprismaAttackGoal(this, (double) 1.0F, true, () -> true));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, (double) 1.0F));
        this.goalSelector.addGoal(4, new FollowSummonerGoal(this, this.getOwner(), (double) 1.0F, 2.0F, 1.0F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(1, new CopyOwnerTargetGoal<>(this));
        this.targetSelector.addGoal(2, new CopyOwnerHurtByGoal<>(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this, new Class[]{TerraprismaEntity.class}) {
            protected boolean canAttack(@Nullable LivingEntity pPotentialTarget, TargetingConditions pTargetPredicate) {
                return pPotentialTarget != null && super.canAttack(pPotentialTarget, pTargetPredicate) && !pPotentialTarget.getUUID().equals(TerraprismaEntity.this.getOwnerUUID());
            }
        });
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 2D)
                .add(Attributes.ATTACK_DAMAGE, (double)10.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.5F)
                .add(Attributes.FLYING_SPEED, (double)0.7F)
                .add(Attributes.FOLLOW_RANGE, (double)70.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)1.0F);
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.getEntityData().get(OWNER_UUID).isEmpty() ? this.getUUID() : this.getEntityData().get(OWNER_UUID).get();
    }

    @Override
    public void setOwnerID(UUID uuid) {
        this.getEntityData().set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    public static void spawnShadowPoof(ServerLevel world, BlockPos pos) {
        for(int i = 0; i < 10; ++i) {
            double d0 = (double)pos.getX() + (double)0.5F;
            double d1 = (double)pos.getY() + 1.2;
            double d2 = (double)pos.getZ() + (double)0.5F;
            world.sendParticles(ParticleTypes.END_ROD, d0, d1, d2, 2, ((double)(world.random.nextFloat() * 1.0F) - (double)0.5F) / (double)3.0F, ((double)(world.random.nextFloat() * 1.0F) - (double)0.5F) / (double)3.0F, ((double)(world.random.nextFloat() * 1.0F) - (double)0.5F) / (double)3.0F, (double)0.1F);
        }

    }

    public class TerraprismaAttackGoal extends MeleeAttackGoal {
        private final TerraprismaEntity entity;

        private int attackDelay = 8;
        private int ticksUntilNextAttack = 8;
        private int totalAnimation = 12;
        private boolean shouldCountTillNextAttack = false;
        private int attackType = 0;

        Supplier<Boolean> canUse;
        boolean done;

        public TerraprismaAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((TerraprismaEntity) pMob);
            this.canUse = canUse;
        }

        @Override
        public void start() {
            super.start();
            attackDelay = 8;
            ticksUntilNextAttack = 8;
            attackType = 0;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
                shouldCountTillNextAttack = true;
                if(this.attackType == 0){
                    this.attackType = this.entity.getRandom().nextInt(1, 3);
                }

                if(isTimeToStartAttackAnimation() && this.attackType == 1) {
                    entity.setAttackingA(true);
                }
                if(isTimeToStartAttackAnimation() && this.attackType == 2) {
                    entity.setAttackingB(true);
                }
                if(isTimeToStartAttackAnimation() && this.attackType == 3) {
                    entity.setAttackingC(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());

                    performAttack(pEnemy);

                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttackingA(false);
                entity.attackAAnimationTimeout = 0;
                entity.setAttackingB(false);
                entity.attackBAnimationTimeout = 0;
                entity.setAttackingC(false);
                entity.attackCAnimationTimeout = 0;
            }
        }

        private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
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

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return (double)(this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() * 2.0F + 4.5F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;
            this.attackType = 0;
        }


        @Override
        public void tick() {
            super.tick();
            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }

        @Override
        public void stop() {
            entity.setAttackingA(false);
            entity.setAttackingB(false);
            entity.setAttackingC(false);
            this.done = false;
            this.attackType = 0;
            super.stop();
        }
    }

    public static class CopyOwnerHurtByGoal<I extends PathfinderMob & IFollowingSummon> extends TargetGoal {
        public CopyOwnerHurtByGoal(I creature) {
            super(creature, false);
        }

        public boolean canUse() {
            Mob var2 = this.mob;
            if (!(var2 instanceof IFollowingSummon summon)) {
                return false;
            } else {
                return summon.getSummoner() != null && summon.getSummoner().getLastHurtByMob() != null;
            }
        }

        public void start() {
            Mob var2 = this.mob;
            if (var2 instanceof IFollowingSummon summon) {
                if (summon.getSummoner() != null) {
                    this.mob.setTarget(summon.getSummoner().getLastHurtByMob());
                }
            }

            super.start();
        }
    }
}