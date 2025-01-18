package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.entities.ai.*;
import com.adamsmods.adamsarsplus.entities.ai.pathfinding.AdvancedPathNavigate;
import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.pathfinding.PathingStuckHandler;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBlink;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectDispel;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectHeal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.ADAM_ENTITY;
import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.MATT_ENTITY;
import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

public class MattEntity extends Monster implements RangedAttackMob {


    public static final EntityDataAccessor<Boolean> ATTACKING_A =
            SynchedEntityData.defineId(MattEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING_B =
            SynchedEntityData.defineId(MattEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> BLOCKING =
            SynchedEntityData.defineId(MattEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING =
            SynchedEntityData.defineId(MattEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_B =
            SynchedEntityData.defineId(MattEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_DOMAIN =
            SynchedEntityData.defineId(MattEntity.class, EntityDataSerializers.BOOLEAN);

    public int attackACooldown;
    public int attackBCooldown;
    public int blockCooldown;
    public int castingCooldown;
    public int castingBCooldown;
    public int domainCooldown;

    public int recoverCooldown;

    public int navigatorType;

    private final ServerBossEvent bossEvent;

    public MattEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.navigation = createNavigator(level(), AdvancedPathNavigate.MovementType.FLYING);
        this.navigatorType = 1;
    }

    public MattEntity(Level pLevel){
        this(MATT_ENTITY.get(), pLevel);
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

    public final AnimationState attackBAnimationState = new AnimationState();
    public int attackBAnimationTimeout = 0;

    public final AnimationState blockAnimationState = new AnimationState();
    public int blockAnimationTimeout = 0;

    public final AnimationState castingAnimationState = new AnimationState();
    public int castingAnimationTimeout = 0;

    public final AnimationState castingBAnimationState = new AnimationState();
    public int castingBAnimationTimeout = 0;

    public final AnimationState castDomainAnimationState = new AnimationState();
    public int castDomainAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if(attackACooldown > 0) {
            attackACooldown--;
        }
        if(attackBCooldown > 0) {
            attackBCooldown--;
        }
        if(blockCooldown > 0) {
            blockCooldown--;
        }
        if(castingCooldown > 0) {
            castingCooldown--;
        }
        if(castingBCooldown > 0) {
            castingBCooldown--;
        }
        if(domainCooldown > 0) {
            domainCooldown--;
        }
        if(recoverCooldown > 0) {
            recoverCooldown--;
        }
        else{
            performSpellSelf(this,1.0F, mattRecoverSpell, mattColor);
            recoverCooldown = random.nextInt(200) + 200;
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

        //Attack B Animation control
        if(this.isAttackingB() && attackBAnimationTimeout <= 0) {
            attackBAnimationTimeout = 20;
            attackBAnimationState.start(this.tickCount);
        } else {
            --this.attackBAnimationTimeout;
        }
        if(!this.isAttackingB()) {
            attackBAnimationState.stop();
        }

        //Block Animation control
        if(this.isBlock() && blockAnimationTimeout <= 0) {
            blockAnimationTimeout = 15;
            blockAnimationState.start(this.tickCount);
        } else {
            --this.blockAnimationTimeout;
        }
        if(!this.isBlock()) {
            blockAnimationState.stop();
        }

        //Casting Animation control
        if(this.isCasting() && castingAnimationTimeout <= 0) {
            castingAnimationTimeout = 27;
            castingAnimationState.start(this.tickCount);
        } else {
            --this.castingAnimationTimeout;
        }
        if(!this.isCasting()) {
            castingAnimationState.stop();
        }

        //Casting B Animation control
        if(this.isCastingB() && castingBAnimationTimeout <= 0) {
            castingBAnimationTimeout = 27;
            castingBAnimationState.start(this.tickCount);
        } else {
            --this.castingBAnimationTimeout;
        }
        if(!this.isCastingB()) {
            castingBAnimationState.stop();
        }

        //Domain Animation Control
        if(this.isUsingDomain() && castDomainAnimationTimeout <= 0) {
            castDomainAnimationTimeout = 20;
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

    private ParticleColor mattColor = new ParticleColor(255, 150, 0);

    public Spell mattRecoverSpell = new Spell()
            .add(EffectDispel.INSTANCE)
            .add(EffectHeal.INSTANCE)
            .add(AugmentAmplify.INSTANCE,3)

            .withColor(mattColor);

    public void performSpellSelf(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

        resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));
    }

    public void setAttackingA(boolean attackingA) { this.entityData.set(ATTACKING_A, attackingA); }
    public boolean isAttackingA(){ return this.entityData.get(ATTACKING_A); }

    public void setAttackingB(boolean attackingB) { this.entityData.set(ATTACKING_B, attackingB); }
    public boolean isAttackingB(){ return this.entityData.get(ATTACKING_B); }

    public void setBlocking(boolean blocking) { this.entityData.set(BLOCKING, blocking); }
    public boolean isBlock(){ return this.entityData.get(BLOCKING); }

    public void setCasting(boolean casting) { this.entityData.set(CASTING, casting); }
    public boolean isCasting(){ return this.entityData.get(CASTING); }

    public void setCastingB(boolean castingB) { this.entityData.set(CASTING_B, castingB); }
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
        this.entityData.define(ATTACKING_A, false);
        this.entityData.define(ATTACKING_B, false);
        this.entityData.define(BLOCKING, false);
        this.entityData.define(CASTING, false);
        this.entityData.define(CASTING_B, false);
        this.entityData.define(CASTING_DOMAIN, false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("attacka", attackACooldown);
        tag.putInt("attackb", attackBCooldown);
        tag.putInt("block", blockCooldown);
        tag.putInt("casting", castingCooldown);
        tag.putInt("castingb", castingBCooldown);
        tag.putInt("domain", domainCooldown);
        tag.putInt("heal", recoverCooldown);

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.LAVA) || source.is(DamageTypes.FALL)  || source.is(DamageTypes.ON_FIRE))
            return false;

        if(this.isBlock()){
            this.attackBCooldown = 0;
            this.blockCooldown = random.nextInt(100) + 30;

            return false;
        }

        return super.hurt(source, amount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.attackACooldown = tag.getInt("attacka");
        this.attackBCooldown = tag.getInt("attackb");
        this.blockCooldown = tag.getInt("block");
        this.castingCooldown = tag.getInt("casting");
        this.castingBCooldown = tag.getInt("castingb");
        this.domainCooldown = tag.getInt( "domain");
        this.recoverCooldown = tag.getInt( "heal");

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

        this.goalSelector.addGoal(1, new MattDomainGoal(this, 1.0D, 20f, () -> domainCooldown <= 0, ModAnimationsDefinition.MATT_DOMAIN.hashCode(), 15));
        this.goalSelector.addGoal(2, new MattCastingGoal<>(this, 1.0D, 35f, () -> castingCooldown <= 0, ModAnimationsDefinition.MATT_CAST.hashCode(), 12));
        this.goalSelector.addGoal(2, new MattCastingBGoal<>(this, 1.0D, 35f, () -> castingBCooldown <= 0, ModAnimationsDefinition.MATT_CAST.hashCode(), 12));
        this.goalSelector.addGoal(2, new MattAttackBGoal(this, 1.0D, true, () -> attackBCooldown <= 0));

        this.goalSelector.addGoal(3, new MattBlockingGoal<>(this, 1.0D, 70f, () -> blockCooldown <= 0, ModAnimationsDefinition.MATT_CAST.hashCode(), 12));

        this.goalSelector.addGoal(4, new MattAttackAGoal(this, 1.0D, true, () -> true));

        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, (double)1.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1225D)
                .add(Attributes.ARMOR, 23D)
                .add(Attributes.ATTACK_DAMAGE, (double)20.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.4F)
                .add(Attributes.FLYING_SPEED, (double)0.4F)
                .add(Attributes.FOLLOW_RANGE, (double)70.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)1.5F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double)0.8F)
                .add(Attributes.ARMOR_TOUGHNESS, (double) 15D);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(pLevel.getRandom(), pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
    }
    @Override
    public void performRangedAttack(LivingEntity entity, float p_82196_2_) {

    }


}
