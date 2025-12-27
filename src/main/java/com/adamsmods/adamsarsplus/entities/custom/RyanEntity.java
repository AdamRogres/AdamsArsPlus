package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.entities.ai.pathfinding.AdvancedPathNavigate;
import com.adamsmods.adamsarsplus.entities.ai.RyanCastingGoal;
import com.adamsmods.adamsarsplus.entities.ai.RyanDomainGoal;
import com.adamsmods.adamsarsplus.entities.ai.RyanAttackGoal;

import com.adamsmods.adamsarsplus.entities.ai.pathfinding.GroundMoveHelper;
import com.hollingsworth.arsnouveau.common.entity.pathfinding.PathingStuckHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.RYAN_ENTITY;

public class RyanEntity extends Monster implements RangedAttackMob {
    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(RyanEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING =
            SynchedEntityData.defineId(RyanEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> USING_DOMAIN =
            SynchedEntityData.defineId(RyanEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HOVERING = SynchedEntityData.defineId(RyanEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(RyanEntity.class, EntityDataSerializers.BOOLEAN);


    public int attackCooldown;
    public int castCooldown;
    public int domainCooldown;

    public int navigatorType;

    private final ServerBossEvent bossEvent;

    public RyanEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        switchNavigator(1);
        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
    }

    public RyanEntity(Level pLevel){
        this(RYAN_ENTITY.get(), pLevel);
    }

    protected PathingStuckHandler createStuckHandler() {
        return PathingStuckHandler.createStuckHandler();
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level worldIn) {
        return createNavigator(worldIn, AdvancedPathNavigate.MovementType.WALKING);
    }


    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type) {
        return createNavigator(worldIn, type, createStuckHandler());
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type, PathingStuckHandler stuckHandler) {
        return createNavigator(worldIn, type, stuckHandler, 4f, 4f);
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type, PathingStuckHandler stuckHandler, float width, float height) {
        AdvancedPathNavigate newNavigator = new AdvancedPathNavigate(this, level(), type, width, height);
        this.navigation = newNavigator;
        newNavigator.setCanFloat(true);
        newNavigator.getNodeEvaluator().setCanOpenDoors(true);
        return newNavigator;
    }

    protected void switchNavigator(int navigatorType) {
        if (navigatorType == 0) {
            this.moveControl = new GroundMoveHelper(this);
            this.navigation = createNavigator(level(), AdvancedPathNavigate.MovementType.WALKING, createStuckHandler().withTeleportSteps(5));
            this.navigatorType = 0;
            this.setFlying(false);
            this.setHovering(false);
        } else if (navigatorType == 1) {
            this.moveControl = new FlyingMoveControl(this, 10, false);
            this.navigation = createNavigator(level(), AdvancedPathNavigate.MovementType.FLYING);
            this.navigatorType = 1;
        }
    }


    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    public final AnimationState castAnimationState = new AnimationState();
    public int castAnimationTimeout = 0;

    public final AnimationState domainAnimationState = new AnimationState();
    public int castDomainAnimationTimeout = 0;


    @Override
    public void tick() {
        super.tick();

        if(attackCooldown > 0) {
            attackCooldown--;
        }
        if(castCooldown > 0) {
            castCooldown--;
        }
        if(domainCooldown > 0) {
            domainCooldown--;
        }

        if(this.level().isClientSide()) {
            setupAnimationStates();
        }

        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if(this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 20;
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }
        if(!this.isAttacking()) {
            attackAnimationState.stop();
        }

        if(this.isCasting() && castAnimationTimeout <= 0) {
            castAnimationTimeout = 20;
            castAnimationState.start(this.tickCount);
        } else {
            --this.castAnimationTimeout;
        }
        if(!this.isCasting()) {
            castAnimationState.stop();
        }

        if(this.isUsingDomain() && castDomainAnimationTimeout <= 0) {
            castDomainAnimationTimeout = 20;
            domainAnimationState.start(this.tickCount);
        } else {
            --this.castDomainAnimationTimeout;
        }
        if(!this.isUsingDomain()) {
            domainAnimationState.stop();
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

        this.walkAnimation.update(f, 0.2f);
    }

    public void setAttacking(boolean attacking) { this.entityData.set(ATTACKING, attacking); }
    public boolean isAttacking() { return this.entityData.get(ATTACKING); }

    public void setCasting(boolean casting) { this.entityData.set(CASTING, casting); }
    public boolean isCasting(){ return this.entityData.get(CASTING); }

    public void setUsingDomain(boolean usingDomain) { this.entityData.set(USING_DOMAIN, usingDomain); }
    public boolean isUsingDomain(){ return this.entityData.get(USING_DOMAIN); }


    public boolean isHovering() {
        return this.entityData.get(HOVERING);
    }

    public void setHovering(boolean hovering) {
        this.entityData.set(HOVERING, hovering);
    }

    public boolean isFlying() {
        return this.entityData.get(FLYING);
    }

    public void setFlying(boolean flying) {
        this.entityData.set(FLYING, flying);
    }

    public boolean useFlyingPathFinder() {
        return isFlying() && this.getControllingPassenger() == null;
    }


    public void startSeenByPlayer(ServerPlayer pPlayer) {
        super.startSeenByPlayer(pPlayer);
        this.bossEvent.addPlayer(pPlayer);
    }

    public void stopSeenByPlayer(ServerPlayer pPlayer) {
        super.stopSeenByPlayer(pPlayer);
        this.bossEvent.removePlayer(pPlayer);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(CASTING, false);
        this.entityData.define(USING_DOMAIN, false);

        this.entityData.define(HOVERING, false);
        this.entityData.define(FLYING, false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("attack", attackCooldown);
        tag.putInt("cast", castCooldown);
        tag.putInt("domain", domainCooldown);

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.FALL) || source.is(DamageTypes.LAVA) || source.is(DamageTypes.IN_FIRE))
            return false;
        return super.hurt(source, amount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.attackCooldown = tag.getInt("attack");
        this.castCooldown = tag.getInt("cast");
        this.domainCooldown = tag.getInt( "domain");

        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    public void setCustomName(@javax.annotation.Nullable Component pName) {
        super.setCustomName(pName);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new RyanDomainGoal(this, 1.0D, 10f, () -> domainCooldown <= 0, 0, 15));
        this.goalSelector.addGoal(2, new RyanCastingGoal(this, 1.0D, 20f, () -> castCooldown <= 0, 0, 15));
        this.goalSelector.addGoal(3, new RyanAttackGoal(this, 1.0D, true, () -> true));

        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, (double)1.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, false));


    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 500D)
                .add(Attributes.ARMOR, 15D)
                .add(Attributes.ATTACK_DAMAGE, (double)12.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.5F)
                .add(Attributes.FLYING_SPEED, (double)0.5F)
                .add(Attributes.FOLLOW_RANGE, (double)70.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)1.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double)1.0F)
                .add(Attributes.ARMOR_TOUGHNESS, (double) 10D);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(pLevel.getRandom(), pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float p_82196_2_) {

    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.BLAZE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }
}
