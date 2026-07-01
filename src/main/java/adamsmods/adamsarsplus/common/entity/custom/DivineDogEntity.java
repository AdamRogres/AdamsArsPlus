package adamsmods.adamsarsplus.common.entity.custom;

import adamsmods.adamsarsplus.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.common.entity.IFollowingSummon;
import com.hollingsworth.arsnouveau.common.entity.goal.FollowSummonerGoal;
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
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static adamsmods.adamsarsplus.registry.ModPotions.TENSHADOWS_EFFECT;

public class DivineDogEntity extends Monster implements IFollowingSummon, ISummon {
    private LivingEntity owner;
    private @Nullable BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;


    public static final EntityDataAccessor<Boolean> SPRINTING =
            SynchedEntityData.defineId(DivineDogEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> BITING =
            SynchedEntityData.defineId(DivineDogEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> LUNGING =
            SynchedEntityData.defineId(DivineDogEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> COLOR =
            SynchedEntityData.defineId(DivineDogEntity.class, EntityDataSerializers.STRING);

    public int ticksLeft;
    public boolean isSummon;
    public String color;

    public int sprintCooldown;

    public DivineDogEntity(Level level, LivingEntity owner, String color, boolean summon) {
        super((EntityType) ModEntities.DIVINE_DOG.get(), level);

        this.owner = owner;
        this.limitedLifespan = true;
        this.setOwnerID(owner.getUUID());
        this.setColor(color);
        this.isSummon = summon;
    }

    public DivineDogEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityType<?> getType() {
        return (EntityType)ModEntities.DIVINE_DOG.get();
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState sprintAnimationState = new AnimationState();
    public int sprintAnimationTimeout = 0;

    public final AnimationState biteAnimationState = new AnimationState();
    public int biteAnimationTimeout = 0;

    public final AnimationState lungeAnimationState = new AnimationState();
    public int lungeAnimationTimeout = 0;

    public void setSprinting(boolean sprinting) { this.entityData.set(SPRINTING, sprinting); }
    public boolean isSprinting(){ return this.entityData.get(SPRINTING); }

    public void setBiting(boolean biting) { this.entityData.set(BITING, biting); }
    public boolean isBiting(){ return this.entityData.get(BITING); }

    public void setLunging(boolean lunging) { this.entityData.set(LUNGING, lunging); }
    public boolean isLunging(){ return this.entityData.get(LUNGING); }

    public void setColor(String nColor) {this.entityData.set(COLOR, nColor); }
    public String getColor(){ return this.entityData.get(COLOR); }

    @Override
    public void tick() {
        super.tick();

        if(this.getSummoner() != null) {
            if (!this.level().isClientSide && this.isSummon && !this.getSummoner().hasEffect(TENSHADOWS_EFFECT)) {
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
        //Bite Animation control
        if(this.isBiting() && biteAnimationTimeout <= 0) {
            biteAnimationTimeout = 15;
            biteAnimationState.start(this.tickCount);
        } else {
            --this.biteAnimationTimeout;
        }
        if(!this.isBiting()) {
            biteAnimationState.stop();
        }
        //Lunge Animation control
        if(this.isLunging()) {
            if(this.lungeAnimationTimeout <= 0){
                lungeAnimationTimeout = 15;
                lungeAnimationState.start(this.tickCount);
            } else {
                --this.lungeAnimationTimeout;
                if (this.lungeAnimationTimeout <= 0) {
                    this.setLunging(false);
                }
            }
        }
        if(!this.isLunging()) {
            lungeAnimationState.stop();
        }
        // Sprint Animation control
        if(this.isSprinting()) {
            if(this.sprintAnimationTimeout <= 0){
                this.sprintAnimationTimeout = 10;
                sprintAnimationState.start(this.tickCount);
            } else {
                this.sprintAnimationTimeout--;
            }
        } else {
            sprintAnimationState.stop();
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
        if(this.isSprinting()){
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);

    }

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(DivineDogEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(OWNER_UUID, Optional.of(Util.NIL_UUID));
        pBuilder.define(SPRINTING, false);
        pBuilder.define(BITING, false);
        pBuilder.define(LUNGING, false);
        pBuilder.define(COLOR, "white");
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        onSummonDeath(level(), cause, false);
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

        return super.hurt(pSource, pAmount);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    public PlayerTeam getTeam() {
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
        this.goalSelector.addGoal(2, new DDogLungeAttackGoal(this, 1.4, false, () -> this.isLunging()));
        this.goalSelector.addGoal(3, new DDogSprintGoal(this, 1.4, false, () -> sprintCooldown > 70));
        this.goalSelector.addGoal(4, new FollowSummonerGoal(this, this.owner, (double) 1.0F, 25.0F, 3.0F));
        this.goalSelector.addGoal(6, new DDogAttackGoal(this, 0.9, false, () -> true));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, (double) 1.0F));
        this.goalSelector.addGoal(8, new FollowSummonerGoal(this, this.owner, (double) 1.0F, 9.0F, 3.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(3, new HurtByTargetGoal(this, new Class[]{DivineDogEntity.class}) {
            protected boolean canAttack(@Nullable LivingEntity pPotentialTarget, TargetingConditions pTargetPredicate) {
                return pPotentialTarget != null && super.canAttack(pPotentialTarget, pTargetPredicate) && !pPotentialTarget.getUUID().equals(DivineDogEntity.this.getOwnerUUID());
            }
        });
        this.targetSelector.addGoal(1, new CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Villager.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.ATTACK_DAMAGE, (double)7.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
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

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        if (this.isBiting()) {
            return SoundEvents.WOLF_GROWL;
        } else if (this.random.nextInt(3) != 0) {
            return SoundEvents.WOLF_AMBIENT;
        } else {
            return this.isSummon && this.getHealth() < 10.0F ? SoundEvents.WOLF_WHINE : SoundEvents.WOLF_PANT;
        }
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WOLF_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    public static void spawnShadowPoof(ServerLevel world, BlockPos pos) {
        for(int i = 0; i < 10; ++i) {
            double d0 = (double)pos.getX() + (double)0.5F;
            double d1 = (double)pos.getY() + 1.2;
            double d2 = (double)pos.getZ() + (double)0.5F;
            world.sendParticles(ParticleTypes.SQUID_INK, d0, d1, d2, 2, ((double)(world.random.nextFloat() * 1.0F) - (double)0.5F) / (double)3.0F, ((double)(world.random.nextFloat() * 1.0F) - (double)0.5F) / (double)3.0F, ((double)(world.random.nextFloat() * 1.0F) - (double)0.5F) / (double)3.0F, (double)0.1F);
        }

    }

    public class DDogAttackGoal extends MeleeAttackGoal {
        private final DivineDogEntity entity;

        private int attackDelay = 10;
        private int ticksUntilNextAttack = 10;
        private int totalAnimation = 15;
        private boolean shouldCountTillNextAttack = false;

        Supplier<Boolean> canUse;
        boolean done;

        public DDogAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((DivineDogEntity) pMob);
            this.canUse = canUse;
        }

        @Override
        public void start() {
            super.start();
            attackDelay = 10;
            ticksUntilNextAttack = 10;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy) {
            if (this.canPerformAttack(pEnemy)) {
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    entity.setBiting(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());

                    performAttack(pEnemy);
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setBiting(false);
                entity.biteAnimationTimeout = 0;
            }
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
            return (double)(this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() * 2.0F + 1.5F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;
            this.entity.sprintCooldown = 0;
        }


        @Override
        public void tick() {
            super.tick();
            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

            this.entity.sprintCooldown = Math.min(this.entity.sprintCooldown + 1, 80);
        }

        @Override
        public void stop() {
            entity.setBiting(false);
            this.done = false;
            super.stop();
        }
    }

    public class DDogLungeAttackGoal extends MeleeAttackGoal {
        private final DivineDogEntity entity;

        Supplier<Boolean> canUse;
        boolean done;

        public DDogLungeAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((DivineDogEntity) pMob);
            this.canUse = canUse;
        }

        @Override
        public void start() {
            super.start();
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy) {
            if (this.canPerformAttack(pEnemy)) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                performAttack(pEnemy);
                entity.setLunging(false);

            } else {
                resetAttackCooldown();
            }
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return (double)(this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() * 2.0F + 1.5F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            if(pEnemy instanceof Player playerEnemy){
                playerEnemy.getCooldowns().addCooldown(Items.SHIELD, 60);
                playerEnemy.disableShield();
            }

            this.done = true;
        }


        @Override
        public void tick() {
            super.tick();

        }

        @Override
        public void stop() {
            this.done = false;
            super.stop();
        }
    }

    public class DDogSprintGoal extends MeleeAttackGoal {
        private final DivineDogEntity entity;

        Supplier<Boolean> canUse;
        boolean done;

        public DDogSprintGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((DivineDogEntity) pMob);
            this.canUse = canUse;
        }

        @Override
        public void start() {
            super.start();
            entity.setSprinting(true);
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy) {
            if (this.canPerformAttack(pEnemy)) {

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());

                    performAttack(pEnemy);
                }
            }
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return (double)(this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() * 2.0F + 3.5F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackCooldown();

            Vec3 $$0 = this.mob.getDeltaMovement();
            Vec3 $$1 = new Vec3(pEnemy.getX() - this.mob.getX(), (double)0.0F, pEnemy.getZ() - this.mob.getZ());
            if ($$1.lengthSqr() > 1.0E-7) {
                $$1 = $$1.normalize().scale(0.5).add($$0.scale(0.2));
            }

            this.mob.setDeltaMovement($$1.x, 0.4, $$1.z);
            entity.lungeAnimationTimeout = 0;
            entity.setLunging(true);

            this.entity.sprintCooldown = 0;
            this.done = true;
        }

        @Override
        public void stop() {
            entity.setSprinting(false);
            this.done = false;
            super.stop();
        }
    }
}