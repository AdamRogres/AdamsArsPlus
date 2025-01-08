package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.entities.ai.*;
import com.adamsmods.adamsarsplus.entities.ai.pathfinding.AdvancedPathNavigate;
import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NickEntity extends Monster implements RangedAttackMob {


    public static final EntityDataAccessor<Boolean> ATTACKING_A =
            SynchedEntityData.defineId(NickEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING_BA =
            SynchedEntityData.defineId(NickEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING_BB =
            SynchedEntityData.defineId(NickEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING_C =
            SynchedEntityData.defineId(NickEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_DOMAIN =
            SynchedEntityData.defineId(NickEntity.class, EntityDataSerializers.BOOLEAN);

    public int attackACooldown;
    public int attackBACooldown;
    public int attackBBCooldown;
    public int attackCCooldown;
    public int domainCooldown;

    public int navigatorType;

    private final ServerBossEvent bossEvent;

    public NickEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.navigation = createNavigator(level(), AdvancedPathNavigate.MovementType.FLYING);
        this.navigatorType = 1;
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

    public final AnimationState attackAAnimationState = new AnimationState();
    public int attackAAnimationTimeout = 0;

    public final AnimationState attackBAAnimationState = new AnimationState();
    public int attackBAAnimationTimeout = 0;

    public final AnimationState attackBBAnimationState = new AnimationState();
    public int attackBBAnimationTimeout = 0;

    public final AnimationState attackCAnimationState = new AnimationState();
    public int attackCAnimationTimeout = 0;

    public final AnimationState castDomainAnimationState = new AnimationState();
    public int castDomainAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if(attackACooldown > 0) {
            attackACooldown--;
        }
        if(attackBACooldown > 0) {
            attackBACooldown--;
        }
        if(attackBBCooldown > 0) {
            attackBBCooldown--;
        }
        if(attackCCooldown > 0) {
            attackCCooldown--;
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

        //Attack A Animation control
        if(this.isAttackingA() && attackAAnimationTimeout <= 0) {
            attackAAnimationTimeout = 20;
            attackAAnimationState.start(this.tickCount);
        } else {
            --this.attackAAnimationTimeout;
        }
        if(!this.isAttackingA()) {
            attackAAnimationState.stop();
        }

        //Attack BA Animation control
        if(this.isAttackingBA() && attackBAAnimationTimeout <= 0) {
            attackBAAnimationTimeout = 20;
            attackBAAnimationState.start(this.tickCount);
        } else {
            --this.attackBAAnimationTimeout;
        }
        if(!this.isAttackingBA()) {
            attackBAAnimationState.stop();
        }

        //Attack BB Animation control
        if(this.isAttackingBB() && attackBBAnimationTimeout <= 0) {
            attackBBAnimationTimeout = 15;
            attackBBAnimationState.start(this.tickCount);
        } else {
            --this.attackBBAnimationTimeout;
        }
        if(!this.isAttackingBB()) {
            attackBBAnimationState.stop();
        }

        //Attack C Animation control
        if(this.isAttackingC() && attackCAnimationTimeout <= 0) {
            attackCAnimationTimeout = 27;
            attackCAnimationState.start(this.tickCount);
        } else {
            --this.attackCAnimationTimeout;
        }
        if(!this.isAttackingC()) {
            attackCAnimationState.stop();
        }

        if(this.isUsingDomain() && castDomainAnimationTimeout <= 0) {
            castDomainAnimationTimeout = 48;
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


    public void setAttackingA(boolean casting) { this.entityData.set(ATTACKING_A, casting); }
    public boolean isAttackingA(){ return this.entityData.get(ATTACKING_A); }

    public void setAttackingBA(boolean casting) { this.entityData.set(ATTACKING_BA, casting); }
    public boolean isAttackingBA(){ return this.entityData.get(ATTACKING_BA); }

    public void setAttackingBB(boolean casting) { this.entityData.set(ATTACKING_BB, casting); }
    public boolean isAttackingBB(){ return this.entityData.get(ATTACKING_BB); }

    public void setAttackingC(boolean casting) { this.entityData.set(ATTACKING_C, casting); }
    public boolean isAttackingC(){ return this.entityData.get(ATTACKING_C); }

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
        this.entityData.define(ATTACKING_A, false);
        this.entityData.define(ATTACKING_BA, false);
        this.entityData.define(ATTACKING_BB, false);
        this.entityData.define(ATTACKING_C, false);

        this.entityData.define(CASTING_DOMAIN, false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("attacka", attackACooldown);
        tag.putInt("attackba", attackBACooldown);
        tag.putInt("attackbb", attackBBCooldown);
        tag.putInt("attackc", attackCCooldown);
        tag.putInt("domain", domainCooldown);

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.LAVA) || source.is(DamageTypes.FALL))
            return false;

        return super.hurt(source, amount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.attackACooldown = tag.getInt("attacka");
        this.attackBACooldown = tag.getInt("attackba");
        this.attackBBCooldown = tag.getInt("attackbb");
        this.attackCCooldown = tag.getInt("attackc");

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

        this.goalSelector.addGoal(1, new NickAttackBBGoal(this, 1.0D, true, () -> attackBBCooldown != 0));

        this.goalSelector.addGoal(2, new NickDomainGoal(this, 1.0D, 20f, () -> domainCooldown <= 0, ModAnimationsDefinition.NICK_CAST.hashCode(), 24));
        this.goalSelector.addGoal(2, new NickAttackCGoal(this, 1.0D, true, () -> attackCCooldown <= 0));
        this.goalSelector.addGoal(2, new NickAttackBAGoal(this, 1.0D, true, () -> attackBACooldown <= 0));
        this.goalSelector.addGoal(3, new NickAttackGoal(this, 1.0D, true, () -> true));

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
                .add(Attributes.MAX_HEALTH, 800D)
                .add(Attributes.ARMOR, 17D)
                .add(Attributes.ATTACK_DAMAGE, (double)15.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.4F)
                .add(Attributes.FLYING_SPEED, (double)0.4F)
                .add(Attributes.FOLLOW_RANGE, (double)70.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)1.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double)0.9F)
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
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }
}
