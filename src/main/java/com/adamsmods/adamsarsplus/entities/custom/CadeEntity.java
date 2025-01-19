package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.entities.ai.*;
import com.adamsmods.adamsarsplus.entities.ai.pathfinding.AdvancedPathNavigate;
import com.hollingsworth.arsnouveau.common.entity.pathfinding.PathingStuckHandler;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import com.hollingsworth.arsnouveau.setup.registry.DamageTypesRegistry;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.CADE_ENTITY;

public class CadeEntity extends Monster implements RangedAttackMob {


    public static final EntityDataAccessor<Boolean> CASTING_A =
            SynchedEntityData.defineId(CadeEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_B =
            SynchedEntityData.defineId(CadeEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_DOMAIN =
            SynchedEntityData.defineId(CadeEntity.class, EntityDataSerializers.BOOLEAN);

    public int castACooldown;
    public int castBCooldown;
    public int domainCooldown;

    public int navigatorType;

    private final ServerBossEvent bossEvent;

    public CadeEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.navigation = createNavigator(level(), AdvancedPathNavigate.MovementType.FLYING);
        this.navigatorType = 1;
    }

    public CadeEntity(Level pLevel){
        this(CADE_ENTITY.get(), pLevel);
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

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState castAAnimationState = new AnimationState();
    public int castAAnimationTimeout = 0;

    public final AnimationState castBAnimationState = new AnimationState();
    public int castBAnimationTimeout = 0;

    public final AnimationState castDomainAnimationState = new AnimationState();
    public int castDomainAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if(castACooldown > 0) {
            castACooldown--;
        }
        if(castBCooldown > 0) {
            castBCooldown--;
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

        //Cast A Animation control
        if(this.isCastingA() && castAAnimationTimeout <= 0) {
            castAAnimationTimeout = 20;
            castAAnimationState.start(this.tickCount);
        } else {
            --this.castAAnimationTimeout;
        }
        if(!this.isCastingA()) {
            castAAnimationState.stop();
        }

        if(this.isCastingB() && castBAnimationTimeout <= 0) {
            castBAnimationTimeout = 20;
            castBAnimationState.start(this.tickCount);
        } else {
            --this.castBAnimationTimeout;
        }
        if(!this.isCastingB()) {
            castBAnimationState.stop();
        }

        if(this.isUsingDomain() && castDomainAnimationTimeout <= 0) {
            castDomainAnimationTimeout = 25;
            castDomainAnimationState.start(this.tickCount);
        } else {
            --this.castDomainAnimationTimeout;
        }
        if(!this.isUsingDomain()) {
            castDomainAnimationState.stop();
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


    public void setCastingA(boolean casting) { this.entityData.set(CASTING_A, casting); }
    public boolean isCastingA(){ return this.entityData.get(CASTING_A); }

    public void setCastingB(boolean casting) { this.entityData.set(CASTING_B, casting); }
    public boolean isCastingB(){ return this.entityData.get(CASTING_B); }

    public void setUsingDomain(boolean usingDomain) { this.entityData.set(CASTING_DOMAIN, usingDomain); }
    public boolean isUsingDomain(){ return this.entityData.get(CASTING_DOMAIN); }


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
        this.entityData.define(CASTING_A, false);
        this.entityData.define(CASTING_B, false);
        this.entityData.define(CASTING_DOMAIN, false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("casta", castACooldown);
        tag.putInt("castb", castBCooldown);
        tag.putInt("domain", domainCooldown);

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.DROWN) || source.is(DamageTypes.FALL) || source.is(DamageTypesRegistry.COLD_SNAP))
            return false;
        if (source.is(DamageTypes.IN_WALL)){
            teleportRelative(0,0.7,0);
        }
        return super.hurt(source, amount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.castACooldown = tag.getInt("casta");
        this.castBCooldown = tag.getInt("castb");
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

        this.goalSelector.addGoal(1, new CadeDomainGoal(this, 1.0D, 20f, () -> domainCooldown <= 0, 0, 15));
        this.goalSelector.addGoal(2, new CadeCastingBGoal<>(this, 1.0D, 20f, () -> castBCooldown <= 0, 0, 15));
        this.goalSelector.addGoal(2, new CadeCastingAGoal<>(this, 1.0D, 35f, () -> castACooldown <= 0, 0, 15));

        this.goalSelector.addGoal(4, new CadeEntity.CadeAvoidEntityGoal(this, Player.class, 15.0F, 1, 1.2));
        this.goalSelector.addGoal(4, new CadeEntity.CadeAvoidEntityGoal(this, IronGolem.class, 15.0F, 1, 1.2));

        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, (double)1.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 325D)
                .add(Attributes.ARMOR, 12D)
                .add(Attributes.ATTACK_DAMAGE, (double)6.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.5F)
                .add(Attributes.FLYING_SPEED, (double)0.5F)
                .add(Attributes.FOLLOW_RANGE, (double)70.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)1.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double)0.2F)
                .add(Attributes.ARMOR_TOUGHNESS, (double) 5D)
                .add(Attributes.FLYING_SPEED, 0.2F);
    }

    static class CadeAvoidEntityGoal<T extends CadeEntity> extends AvoidEntityGoal<T> {
        private final CadeEntity cade;

        public CadeAvoidEntityGoal(CadeEntity pCade, Class<T> pEntityClassToAvoid, float pMaxDist, double pWalkSpeedModifier, double pSprintSpeedModifier) {
            super(pCade, pEntityClassToAvoid, pMaxDist, pWalkSpeedModifier, pSprintSpeedModifier);
            this.cade = pCade;
        }

        public void start() {
            super.start();

            if(cade.onGround()){
                cade.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 2, 1, false, false, true));
            }
        }

        public boolean canUse() {
            return super.canUse();
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(pLevel.getRandom(), pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {

    }
    @Override
    public void performRangedAttack(LivingEntity entity, float p_82196_2_) {

    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.AMETHYST_BLOCK_BREAK;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.AMETHYST_CLUSTER_BREAK;
    }
}
