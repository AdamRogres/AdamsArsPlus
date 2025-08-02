package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.registry.AdamCapabilityRegistry;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.common.entity.IFollowingSummon;
import com.hollingsworth.arsnouveau.common.entity.goal.FollowSummonerGoal;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.animal.Rabbit;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.TENSHADOWS_EFFECT;

public class RabbitEEntity extends Monster implements IFollowingSummon, ISummon {
    // Ten Shadows Reward
    public boolean ritualStatus;
    public LivingEntity[] attackersList = {null, null};
    public boolean isSummon;
    private LivingEntity owner;
    public int ticksLeft;

    public String color;
    public boolean isCopy;
    public int summonCooldown = 0;

    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int jumpDelayTicks;

    public static final EntityDataAccessor<Boolean> HOPPING =
            SynchedEntityData.defineId(RabbitEEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> COLOR =
            SynchedEntityData.defineId(RabbitEEntity.class, EntityDataSerializers.STRING);

    public RabbitEEntity(Level level, LivingEntity owner, boolean summon, String color, boolean copy) {
        super((EntityType) AdamsModEntities.RABBIT_ESCAPE.get(), level);

        this.owner = owner;
        this.setOwnerID(owner.getUUID());
        this.isSummon = summon;
        this.ritualStatus = false;

        this.isCopy = copy;
        this.setColor(color);

        this.jumpControl = new RabbitEJumpControl(this);
        this.moveControl = new RabbitEMoveControl(this);
        this.setSpeedModifier((double) 0.0F);
    }

    public RabbitEEntity(Level level, boolean summon, String color) {
        super((EntityType) AdamsModEntities.RABBIT_ESCAPE.get(), level);

        this.isSummon = summon;
        this.ritualStatus = false;
        this.setColor(color);

        this.jumpControl = new RabbitEJumpControl(this);
        this.moveControl = new RabbitEMoveControl(this);
        this.setSpeedModifier((double) 0.0F);
    }

    public RabbitEEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);

        this.jumpControl = new RabbitEJumpControl(this);
        this.moveControl = new RabbitEMoveControl(this);
        this.setSpeedModifier((double) 0.0F);
    }

    public EntityType<?> getType() {
        return (EntityType) AdamsModEntities.RABBIT_ESCAPE.get();
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState hopAnimationState = new AnimationState();
    private int hopAnimationTimeout = 0;

    public void setHopping(boolean hopping) {
        this.entityData.set(HOPPING, hopping);
    }

    public boolean isHopping() {
        return this.entityData.get(HOPPING);
    }

    public void setColor(String nColor) {
        this.entityData.set(COLOR, nColor);
    }

    public String getColor() {
        return this.entityData.get(COLOR);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getSummoner() != null) {
            if (!this.level().isClientSide && this.isSummon && !this.getSummoner().hasEffect(TENSHADOWS_EFFECT.get())) {
                spawnShadowPoof((ServerLevel) this.level(), this.blockPosition());
                this.remove(RemovalReason.DISCARDED);
                this.onSummonDeath(this.level(), (DamageSource) null, true);
            }
        }

        if (!this.level().isClientSide && this.isSummon && this.isCopy && this.ticksLeft <= 0) {
            spawnShadowPoof((ServerLevel) this.level(), this.blockPosition());
            this.remove(RemovalReason.DISCARDED);
            this.onSummonDeath(this.level(), (DamageSource) null, true);
        }

        if (this.isCopy && this.ticksLeft > 0) {
            this.ticksLeft--;
        }

        if (summonCooldown > 0) {
            summonCooldown--;
        } else if (!this.level().isClientSide && !this.isCopy) {
            RabbitEEntity tsentity = new RabbitEEntity(this.level(), this, this.isSummon, "copy", true);
            if (this.isSummon && this.getOwner() != null) {
                tsentity = new RabbitEEntity(this.level(), this.getOwner(), this.isSummon, "copy", true);
            }

            tsentity.moveTo(this.blockPosition().offset(this.level().random.nextInt(0, 10) - 5, this.level().random.nextInt(0, 10) - 5, this.level().random.nextInt(0, 10) - 5), 0.0F, 0.0F);
            tsentity.finalizeSpawn((ServerLevelAccessor) this.level(), this.level().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
            tsentity.setTicksLeft(this.level().random.nextInt(100, 200));

            this.summon(tsentity, this.blockPosition());
            this.summonCooldown = this.level().random.nextInt(10,20);
        }

        if (this.level().isClientSide()) {
            setupAnimationStates();
        }

    }

    private void setupAnimationStates() {
        this.color = this.getColor();

        //Hop Animation control
        if (this.isHopping() && hopAnimationTimeout <= 0) {
            hopAnimationTimeout = 20;
            hopAnimationState.start(this.tickCount);
        } else {
            --this.hopAnimationTimeout;
        }
        if (!this.isHopping()) {
            hopAnimationState.stop();
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1F);
        } else {
            f = 0f;
        }
        if (this.isSprinting()) {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);

    }

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(RabbitEEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UUID, Optional.of(Util.NIL_UUID));
        this.entityData.define(HOPPING, false);
        this.entityData.define(COLOR, "main");
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        onSummonDeath(level(), cause, false);

        if (!this.ritualStatus && !this.isSummon && !this.isCopy) {
            if (this.attackersList[0] instanceof Player player) {
                AdamCapabilityRegistry.getTsTier(player).ifPresent((pRank) -> {
                    if (pRank.getTsTier() >= 1) {
                        pRank.setTsTier(Math.max(2, pRank.getTsTier()));
                        PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.tenshadows.rabbit_tamed"));
                    } else {
                        PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.tenshadows.tame_failed"));
                    }
                });
            }
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypes.MOB_ATTACK)) {
            Entity var4 = pSource.getEntity();
            if (var4 instanceof ISummon) {
                ISummon summon = (ISummon) var4;
                if (summon.getOwnerUUID() != null && summon.getOwnerUUID().equals(this.getOwnerUUID())) {
                    return false;
                }
            }
        }

        if (!this.ritualStatus && !this.isCopy) {
            this.ritualStatus = isRitualFailed(pSource.getEntity());
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
                ISummon summon = (ISummon) pEntity;
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
        compound.putBoolean("Summon", isSummon);
        compound.putBoolean("Failed", ritualStatus);
        compound.putString("color", this.getColor());
        compound.putBoolean("copy", isCopy);
        compound.putInt("cooldown", summonCooldown);

        if (this.getOwnerUUID() == null) {
            compound.putUUID("OwnerUUID", Util.NIL_UUID);
        } else {
            compound.putUUID("OwnerUUID", this.getOwnerUUID());
        }
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        this.setOwnerID(compound.getUUID("OwnerUUID"));
        this.owner = this.getOwnerFromID();
        this.isSummon = compound.getBoolean("Summon");
        this.ritualStatus = compound.getBoolean("Failed");
        this.color = compound.getString("color");
        this.setColor(this.color);
        this.isCopy = compound.getBoolean("copy");
        this.summonCooldown = compound.getInt("cooldown");
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
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RabbitEEntity.RabbitEPanicGoal(this, 2.2));
        this.goalSelector.addGoal(4, new FollowSummonerGoal(this, this.owner, (double) 1.0F, 25.0F, 3.0F));
        this.goalSelector.addGoal(4, new RabbitEEntity.RabbitEAvoidEntityGoal(this, Player.class, 8.0F, 2.2, 2.2));
        this.goalSelector.addGoal(5, new RabbitSwarmGoal(this, this.owner, (double) 1.0F, 1.0F, 0.0F, () -> (!this.isCopy || this.getTarget() == null)));
        this.goalSelector.addGoal(5, new RabbitEAttackGoal(this, () -> this.isCopy));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double) 1.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(3, new HurtByTargetGoal(this, new Class[]{RabbitEEntity.class}) {
            protected boolean canAttack(@Nullable LivingEntity pPotentialTarget, TargetingConditions pTargetPredicate) {
                return pPotentialTarget != null && super.canAttack(pPotentialTarget, pTargetPredicate) && !pPotentialTarget.getUUID().equals(RabbitEEntity.this.getOwnerUUID());
            }
        });
        this.targetSelector.addGoal(1, new IFollowingSummon.CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Villager.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40D)
                .add(Attributes.ATTACK_DAMAGE, (double) 5.0F)
                .add(Attributes.MOVEMENT_SPEED, (double) 1.0F)
                .add(Attributes.FOLLOW_RANGE, (double) 70.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double) 1.0F);
    }

    @org.jetbrains.annotations.Nullable
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
        for (int i = 0; i < 10; ++i) {
            double d0 = (double) pos.getX() + (double) 0.5F;
            double d1 = (double) pos.getY() + 1.2;
            double d2 = (double) pos.getZ() + (double) 0.5F;
            world.sendParticles(ParticleTypes.SQUID_INK, d0, d1, d2, 2, ((double) (world.random.nextFloat() * 1.0F) - (double) 0.5F) / (double) 3.0F, ((double) (world.random.nextFloat() * 1.0F) - (double) 0.5F) / (double) 3.0F, ((double) (world.random.nextFloat() * 1.0F) - (double) 0.5F) / (double) 3.0F, (double) 0.1F);
        }
    }

    // Ten Shadows Ritual Reward Control
    public boolean isRitualFailed(Entity attacker) {
        boolean isFailed = false;

        if (attacker instanceof LivingEntity leAttacker) {
            if (leAttacker instanceof ISummon) {
                ISummon summon = (ISummon) attacker;
                this.attackersList[checkDupes(summon.getOwner())] = summon.getOwner();
            } else {
                this.attackersList[checkDupes(leAttacker)] = leAttacker;
            }
        }

        if (this.attackersList[1] != null) {
            isFailed = true;
        }

        return isFailed;
    }

    public int checkDupes(LivingEntity newEntity) {
        if (this.attackersList[0] == null || this.attackersList[0] == newEntity) {
            return 0;
        }
        return 1;
    }

    // Rabbit Escape stuff

    protected SoundEvent getJumpSound() {
        return SoundEvents.RABBIT_JUMP;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.RABBIT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.RABBIT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.RABBIT_DEATH;
    }

    public boolean doHurtTarget(Entity pEntity) {
        if (this.isCopy) {
            this.playSound(SoundEvents.RABBIT_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            return pEntity.hurt(this.damageSources().mobAttack(this), 8.0F);
        } else {
            return pEntity.hurt(this.damageSources().mobAttack(this), 3.0F);
        }
    }

    public void summon(Mob mob, BlockPos pos) {
        mob.setPos((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
        mob.level().addFreshEntity(mob);
    }

    protected float getJumpPower() {
        float f = 0.3F;
        if (this.horizontalCollision || this.moveControl.hasWanted() && this.moveControl.getWantedY() > this.getY() + (double) 0.5F) {
            f = 1.0F;
        }

        Path path = this.navigation.getPath();
        if (path != null && !path.isDone()) {
            Vec3 vec3 = path.getNextEntityPos(this);
            if (vec3.y > this.getY() + (double) 0.5F) {
                f = 1.0F;
            }
        }

        if (this.moveControl.getSpeedModifier() <= 0.6) {
            f = 0.2F;
        }

        return f + this.getJumpBoostPower();
    }

    protected void jumpFromGround() {
        super.jumpFromGround();
        double d0 = this.moveControl.getSpeedModifier();
        if (d0 > (double) 0.0F) {
            double d1 = this.getDeltaMovement().horizontalDistanceSqr();
            if (d1 < 0.01) {
                this.moveRelative(0.1F, new Vec3((double) 0.0F, (double) 0.0F, (double) 1.0F));
            }
        }

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 1);
        }
    }

    public float getJumpCompletion(float pPartialTick) {
        return this.jumpDuration == 0 ? 0.0F : ((float) this.jumpTicks + pPartialTick) / (float) this.jumpDuration;
    }

    public void setSpeedModifier(double pSpeedModifier) {
        this.getNavigation().setSpeedModifier(pSpeedModifier);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), pSpeedModifier);
    }

    public void setJumping(boolean pJumping) {
        super.setJumping(pJumping);
        if (pJumping) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }
        this.setHopping(pJumping);
    }

    public void startJumping() {
        this.setJumping(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }

    public static class RabbitEJumpControl extends JumpControl {
        private final RabbitEEntity rabbit;
        private boolean canJump;

        public RabbitEJumpControl(RabbitEEntity pRabbit) {
            super(pRabbit);
            this.rabbit = pRabbit;
        }

        public boolean wantJump() {
            return this.jump;
        }

        public boolean canJump() {
            return this.canJump;
        }

        public void setCanJump(boolean pCanJump) {
            this.canJump = pCanJump;
        }

        public void tick() {
            if (this.jump) {
                this.rabbit.startJumping();
                this.jump = false;
            }

        }
    }

    static class RabbitEMoveControl extends MoveControl {
        private final RabbitEEntity rabbit;
        private double nextJumpSpeed;

        public RabbitEMoveControl(RabbitEEntity pRabbit) {
            super(pRabbit);
            this.rabbit = pRabbit;
        }

        public void tick() {
            if (this.rabbit.onGround() && !this.rabbit.jumping && !((RabbitEJumpControl) this.rabbit.jumpControl).wantJump()) {
                this.rabbit.setSpeedModifier((double) 0.0F);
            } else if (this.hasWanted()) {
                this.rabbit.setSpeedModifier(this.nextJumpSpeed);
            }

            super.tick();
        }

        public void setWantedPosition(double pX, double pY, double pZ, double pSpeed) {
            if (this.rabbit.isInWater()) {
                pSpeed = (double) 1.5F;
            }

            super.setWantedPosition(pX, pY, pZ, pSpeed);
            if (pSpeed > (double) 0.0F) {
                this.nextJumpSpeed = pSpeed;
            }

        }
    }

    static class RabbitEAttackGoal extends MeleeAttackGoal {
        Supplier<Boolean> canUse;

        public RabbitEAttackGoal(RabbitEEntity pRabbit, Supplier<Boolean> canUse) {
            super(pRabbit, 1.4, true);

            this.canUse = canUse;
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return (double) (4.0F + pAttackTarget.getBbWidth());
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            double d0 = this.getAttackReachSqr(pEnemy);
            if (pDistToEnemySqr <= d0) {
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(pEnemy);
            }
        }

        public boolean canUse() {
            super.canUse();

            return this.canUse.get();
        }
    }

    static class RabbitEAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final RabbitEEntity rabbit;

        public RabbitEAvoidEntityGoal(RabbitEEntity pRabbit, Class<T> pEntityClassToAvoid, float pMaxDist, double pWalkSpeedModifier, double pSprintSpeedModifier) {
            super(pRabbit, pEntityClassToAvoid, pMaxDist, pWalkSpeedModifier, pSprintSpeedModifier);
            this.rabbit = pRabbit;
        }

        public boolean canUse() {
            return this.rabbit.isCopy && super.canUse();
        }
    }

    static class RabbitEPanicGoal extends PanicGoal {
        private final RabbitEEntity rabbit;

        public RabbitEPanicGoal(RabbitEEntity pRabbit, double pSpeedModifier) {
            super(pRabbit, pSpeedModifier);
            this.rabbit = pRabbit;
        }

        public void tick() {
            super.tick();
            this.rabbit.setSpeedModifier(this.speedModifier);
        }
    }

    public void customServerAiStep() {
        if (this.jumpDelayTicks > 0) {
            --this.jumpDelayTicks;
        }

        if (this.onGround()) {
            if (!this.wasOnGround) {
                this.setJumping(false);
                this.checkLandingDelay();
            }

            if (this.isCopy && this.jumpDelayTicks == 0) {
                LivingEntity livingentity = this.getTarget();
                if (livingentity != null && this.distanceToSqr(livingentity) < (double) 16.0F) {
                    this.facePoint(livingentity.getX(), livingentity.getZ());
                    this.moveControl.setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), this.moveControl.getSpeedModifier());
                    this.startJumping();
                    this.wasOnGround = true;
                }
            }

            RabbitEJumpControl rabbit$rabbitjumpcontrol = (RabbitEJumpControl) this.jumpControl;
            if (!rabbit$rabbitjumpcontrol.wantJump()) {
                if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
                    Path path = this.navigation.getPath();
                    Vec3 vec3 = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
                    if (path != null && !path.isDone()) {
                        vec3 = path.getNextEntityPos(this);
                    }

                    this.facePoint(vec3.x, vec3.z);
                    this.startJumping();
                }
            } else if (!rabbit$rabbitjumpcontrol.canJump()) {
                this.enableJumpControl();
            }
        }

        this.wasOnGround = this.onGround();
    }

    public boolean canSpawnSprintParticle() {
        return false;
    }

    private void facePoint(double pX, double pZ) {
        this.setYRot((float) (Mth.atan2(pZ - this.getZ(), pX - this.getX()) * (double) (180F / (float) Math.PI)) - 90.0F);
    }

    private void enableJumpControl() {
        ((RabbitEEntity.RabbitEJumpControl) this.jumpControl).setCanJump(true);
    }

    private void disableJumpControl() {
        ((RabbitEEntity.RabbitEJumpControl) this.jumpControl).setCanJump(false);
    }

    private void setLandingDelay() {
        if (this.moveControl.getSpeedModifier() < 2.2) {
            this.jumpDelayTicks = 10;
        } else {
            this.jumpDelayTicks = 1;
        }

    }

    private void checkLandingDelay() {
        this.setLandingDelay();
        this.disableJumpControl();
    }

    public void aiStep() {
        super.aiStep();
        if (this.jumpTicks != this.jumpDuration) {
            ++this.jumpTicks;
        } else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }

    }

    static class RabbitSwarmGoal extends Goal {
        protected final IFollowingSummon summon;
        protected final LevelReader world;
        private final double followSpeed;
        private final PathNavigation navigator;
        private int timeToRecalcPath;
        private final float maxDist;
        private final float minDist;
        private float oldWaterCost;
        private Supplier<Boolean> canUse;

        public RabbitSwarmGoal(IFollowingSummon mobEntity, LivingEntity owner, double followSpeedIn, float minDistIn, float maxDistIn, Supplier<Boolean> canUse) {
            this.summon = mobEntity;
            this.world = mobEntity.getWorld();
            this.followSpeed = followSpeedIn;
            this.navigator = mobEntity.getPathNav();
            this.minDist = minDistIn;
            this.maxDist = maxDistIn;
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            if (!(mobEntity.getPathNav() instanceof GroundPathNavigation) && !(mobEntity.getPathNav() instanceof FlyingPathNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        public boolean canUse() {
            LivingEntity livingentity = this.summon.getSummoner();
            if (livingentity == null) {
                return false;
            } else if (livingentity instanceof Player && livingentity.isSpectator()) {
                return false;
            } else if (this.summon instanceof TamableAnimal && ((TamableAnimal) this.summon).isOrderedToSit()) {
                return false;
            } else {
                return (this.canUse.get());
            }
        }

        public boolean canContinueToUse() {
            boolean flag = true;
            if (this.summon instanceof TamableAnimal) {
                flag = !((TamableAnimal) this.summon).isOrderedToSit();
            }

            if (this.summon.getSummoner() == null) {
                return false;
            } else {
                return !this.navigator.isDone() && this.summon.getSelfEntity().distanceToSqr(this.summon.getSummoner()) > (double) (this.maxDist * this.maxDist) && flag;
            }
        }

        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.summon.getSelfEntity().getPathfindingMalus(BlockPathTypes.WATER);
            this.summon.getSelfEntity().setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        }

        public void stop() {
            this.navigator.stop();
            this.summon.getSelfEntity().setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
        }

        public void tick() {
            if (this.summon.getSummoner() != null) {
                this.summon.getSelfEntity().getLookControl().setLookAt(this.summon.getSummoner(), 10.0F, (float) this.summon.getSelfEntity().getMaxHeadXRot());
                if (!(this.summon instanceof TamableAnimal) || !((TamableAnimal) this.summon).isOrderedToSit()) {
                    if (--this.timeToRecalcPath <= 0) {
                        this.timeToRecalcPath = 10;
                        if (!this.navigator.moveTo(this.summon.getSummoner(), this.followSpeed) && !(this.summon.getSelfEntity().distanceToSqr(this.summon.getSummoner()) < (double) 144.0F)) {
                            int i = Mth.floor(this.summon.getSummoner().getX()) - 2;
                            int j = Mth.floor(this.summon.getSummoner().getZ()) - 2;
                            int k = Mth.floor(this.summon.getSummoner().getBoundingBox().minY + 6);

                            for (int l = 0; l <= 4; ++l) {
                                for (int i1 = 0; i1 <= 4; ++i1) {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.canTeleportToBlock(new BlockPos(i + l, k - 1, j + i1))) {
                                        this.summon.getSelfEntity().moveTo((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.summon.getSelfEntity().getYRot(), this.summon.getSelfEntity().getXRot());
                                        this.navigator.stop();
                                        return;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        protected boolean canTeleportToBlock(BlockPos pos) {
            BlockState blockstate = this.world.getBlockState(pos);
            return blockstate.isValidSpawn(this.world, pos, this.summon.getSelfEntity().getType()) && this.world.isEmptyBlock(pos.above()) && this.world.isEmptyBlock(pos.above(2));
        }
    }
}
