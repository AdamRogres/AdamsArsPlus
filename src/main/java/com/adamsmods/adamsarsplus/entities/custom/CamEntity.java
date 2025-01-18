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
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBlink;
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
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.ADAM_ENTITY;
import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.CAM_ENTITY;

public class CamEntity extends Monster implements RangedAttackMob {


    public static final EntityDataAccessor<Boolean> ATTACKING_AA =
            SynchedEntityData.defineId(CamEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING_AB =
            SynchedEntityData.defineId(CamEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING_B =
            SynchedEntityData.defineId(CamEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING =
            SynchedEntityData.defineId(CamEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_DOMAIN =
            SynchedEntityData.defineId(CamEntity.class, EntityDataSerializers.BOOLEAN);

    public int attackAACooldown;
    public int attackABCooldown;
    public int attackBCooldown;
    public int castingCooldown;
    public int domainCooldown;

    public int blinkCooldown;

    public int navigatorType;

    private final ServerBossEvent bossEvent;

    public CamEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.navigation = createNavigator(level(), AdvancedPathNavigate.MovementType.FLYING);
        this.navigatorType = 1;
    }

    public CamEntity(Level pLevel){
        this(CAM_ENTITY.get(), pLevel);
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

    public final AnimationState attackAAAnimationState = new AnimationState();
    public int attackAAAnimationTimeout = 0;

    public final AnimationState attackABAnimationState = new AnimationState();
    public int attackABAnimationTimeout = 0;

    public final AnimationState attackBAnimationState = new AnimationState();
    public int attackBAnimationTimeout = 0;

    public final AnimationState castingAnimationState = new AnimationState();
    public int castingAnimationTimeout = 0;

    public final AnimationState castDomainAnimationState = new AnimationState();
    public int castDomainAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if(attackAACooldown > 0) {
            attackAACooldown--;
        }
        if(attackABCooldown > 0) {
            attackABCooldown--;
        }
        if(attackBCooldown > 0) {
            attackBCooldown--;
        }
        if(castingCooldown > 0) {
            castingCooldown--;
        }
        if(domainCooldown > 0) {
            domainCooldown--;
        }
        if(blinkCooldown > 0) {
            blinkCooldown--;
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

        //Attack AA Animation control
        if(this.isAttackingAA() && attackAAAnimationTimeout <= 0) {
            attackAAAnimationTimeout = 20;
            attackAAAnimationState.start(this.tickCount);
        } else {
            --this.attackAAAnimationTimeout;
        }
        if(!this.isAttackingAA()) {
            attackAAAnimationState.stop();
        }

        //Attack AB Animation control
        if(this.isAttackingAB() && attackABAnimationTimeout <= 0) {
            attackABAnimationTimeout = 20;
            attackABAnimationState.start(this.tickCount);
        } else {
            --this.attackABAnimationTimeout;
        }
        if(!this.isAttackingAB()) {
            attackABAnimationState.stop();
        }

        //Attack B Animation control
        if(this.isAttackingB() && attackBAnimationTimeout <= 0) {
            attackBAnimationTimeout = 15;
            attackBAnimationState.start(this.tickCount);
        } else {
            --this.attackBAnimationTimeout;
        }
        if(!this.isAttackingB()) {
            attackBAnimationState.stop();
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


    public void setAttackingAA(boolean attackingAA) { this.entityData.set(ATTACKING_AA, attackingAA); }
    public boolean isAttackingAA(){ return this.entityData.get(ATTACKING_AA); }

    public void setAttackingAB(boolean attackingAB) { this.entityData.set(ATTACKING_AB, attackingAB); }
    public boolean isAttackingAB(){ return this.entityData.get(ATTACKING_AB); }

    public void setAttackingB(boolean attackingB) { this.entityData.set(ATTACKING_B, attackingB); }
    public boolean isAttackingB(){ return this.entityData.get(ATTACKING_B); }

    public void setCasting(boolean casting) { this.entityData.set(CASTING, casting); }
    public boolean isCasting(){ return this.entityData.get(CASTING); }

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
        this.entityData.define(ATTACKING_AA, false);
        this.entityData.define(ATTACKING_AB, false);
        this.entityData.define(ATTACKING_B, false);
        this.entityData.define(CASTING, false);

        this.entityData.define(CASTING_DOMAIN, false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("attackaa", attackAACooldown);
        tag.putInt("attackab", attackABCooldown);
        tag.putInt("attackb", attackBCooldown);
        tag.putInt("casting", castingCooldown);
        tag.putInt("domain", domainCooldown);
        tag.putInt("blink", blinkCooldown);

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.LAVA) || source.is(DamageTypes.FALL) || source.is(DamageTypes.LIGHTNING_BOLT) || source.is(DamageTypes.ON_FIRE))
            return false;

        if(this.blinkCooldown <= 0){
            if(source.is(DamageTypes.IN_WALL)){
                this.teleportRelative(0,8,0);
            } else if(!this.level().isClientSide()) {
                performSpellSelf(this, 1.0F, camBlinkSpell, camColor);
            }

            this.blinkCooldown = random.nextInt(160) + 40;

            return super.hurt(source, amount);
        }

        return super.hurt(source, amount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.attackAACooldown = tag.getInt("attackaa");
        this.attackABCooldown = tag.getInt("attackab");
        this.attackBCooldown = tag.getInt("attackb");
        this.castingCooldown = tag.getInt("casting");

        this.domainCooldown = tag.getInt( "domain");

        this.blinkCooldown = tag.getInt( "blink");

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

        this.goalSelector.addGoal(1, new CamAttackABGoal(this, 1.0D, true, () -> attackABCooldown != 0));

        this.goalSelector.addGoal(2, new CamDomainGoal(this, 1.0D, 20f, () -> domainCooldown <= 0, ModAnimationsDefinition.CAM_DOMAIN.hashCode(), 13));
        this.goalSelector.addGoal(2, new CamCastingGoal<>(this, 1.0D, 35f, () -> castingCooldown <= 0, ModAnimationsDefinition.CAM_CAST.hashCode(), 12));
        this.goalSelector.addGoal(2, new CamAttackBGoal(this, 1.0D, true, () -> attackBCooldown <= 0));

        this.goalSelector.addGoal(3, new CamAttackAAGoal(this, 1.0D, true, () -> true));

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
                .add(Attributes.MAX_HEALTH, 925D)
                .add(Attributes.ARMOR, 20D)
                .add(Attributes.ATTACK_DAMAGE, (double)15.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.75F)
                .add(Attributes.FLYING_SPEED, (double)0.75F)
                .add(Attributes.FOLLOW_RANGE, (double)70.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)1.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double)0.5F)
                .add(Attributes.ARMOR_TOUGHNESS, (double) 12D);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(pLevel.getRandom(), pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
    }

    private ParticleColor camColor = new ParticleColor(255, 255, 255);

    public Spell camBlinkSpell = new Spell()
            .add(EffectBlink.INSTANCE)

            .withColor(camColor);


    @Override
    public void performRangedAttack(LivingEntity entity, float p_82196_2_) {

    }

    public void performSpellSelf(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

        resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));
    }

}
