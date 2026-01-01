package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.entities.ai.*;
import com.adamsmods.adamsarsplus.entities.ai.pathfinding.AdvancedPathNavigate;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectBlueFlame;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectIceburst;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.entity.pathfinding.PathingStuckHandler;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import com.hollingsworth.arsnouveau.setup.registry.DamageTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.ai.control.MoveControl;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.WALKING_BLIZZARD_EFFECT;
import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.CADE_ENTITY;
import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

public class CadeEntity extends Monster implements RangedAttackMob {

    public static final EntityDataAccessor<Boolean> CASTING_A =
            SynchedEntityData.defineId(CadeEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_B =
            SynchedEntityData.defineId(CadeEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_DOMAIN =
            SynchedEntityData.defineId(CadeEntity.class, EntityDataSerializers.BOOLEAN);

    public int castACooldown;
    public int castBCooldown;
    public int castCCooldown;
    public int castDCooldown;
    public int castECooldown;
    public int domainCooldown;

    private final ServerBossEvent bossEvent;

    public CadeEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        this.moveControl = new CadeEntity.BossMoveControl(this);
    }

    public CadeEntity(Level pLevel){
        this(CADE_ENTITY.get(), pLevel);
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
        this.setNoGravity(true);

        if(!this.hasEffect(WALKING_BLIZZARD_EFFECT.get())){
            this.addEffect(new MobEffectInstance(WALKING_BLIZZARD_EFFECT.get(), 100, 1, false, false));
        }

        if(castACooldown > 0) {
            castACooldown--;
        }
        if(castBCooldown > 0) {
            castBCooldown--;
        }
        if(castCCooldown > 0) {
            castCCooldown--;
        }
        if(castDCooldown > 0) {
            castDCooldown--;
        }
        if(castECooldown > 0) {
            castECooldown--;
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
        tag.putInt("castc", castCCooldown);
        tag.putInt("castd", castDCooldown);
        tag.putInt("caste", castECooldown);
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
        this.castCCooldown = tag.getInt("castc");
        this.castDCooldown = tag.getInt("castd");
        this.castECooldown = tag.getInt("caste");
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
        this.goalSelector.addGoal(2, new CadeCastingGoalE<>(this, 1.3D, 20f, () -> castECooldown <= 0, 0, 15));
        this.goalSelector.addGoal(2, new CadeCastingGoalD<>(this, 1.3D, 20f, () -> castDCooldown <= 0, 0, 15));
        this.goalSelector.addGoal(2, new CadeCastingGoalC<>(this, 1.3D, 20f, () -> castCCooldown <= 0, 0, 15));
        this.goalSelector.addGoal(3, new CadeCastingGoalB<>(this, 1.0D, 20f, () -> castBCooldown <= 0, 0, 15));
        this.goalSelector.addGoal(3, new CadeCastingGoalA<>(this, 1.3D, 35f, () -> castACooldown <= 0, 0, 15));

        this.goalSelector.addGoal(4, new CadeEntity.CadeEntityAvoidMoveGoal());
        
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, false));

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

    // Goals and Movement
    class BossMoveControl extends MoveControl {
        public BossMoveControl(CadeEntity pCade) {
            super(pCade);
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 $$0 = new Vec3(this.wantedX - CadeEntity.this.getX(), this.wantedY - CadeEntity.this.getY(), this.wantedZ - CadeEntity.this.getZ());
                double $$1 = $$0.length();
                if ($$1 < CadeEntity.this.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                    CadeEntity.this.setDeltaMovement(CadeEntity.this.getDeltaMovement().scale((double)0.5F));
                } else {
                    CadeEntity.this.setDeltaMovement(CadeEntity.this.getDeltaMovement().add($$0.scale(this.speedModifier * 0.05 / $$1)));
                    if (CadeEntity.this.getTarget() == null) {
                        Vec3 $$2 = CadeEntity.this.getDeltaMovement();
                        CadeEntity.this.setYRot(-((float) Mth.atan2($$2.x, $$2.z)) * (180F / (float)Math.PI));
                        CadeEntity.this.yBodyRot = CadeEntity.this.getYRot();
                    } else {
                        double $$3 = CadeEntity.this.getTarget().getX() - CadeEntity.this.getX();
                        double $$4 = CadeEntity.this.getTarget().getZ() - CadeEntity.this.getZ();
                        CadeEntity.this.setYRot(-((float)Mth.atan2($$3, $$4)) * (180F / (float)Math.PI));
                        CadeEntity.this.yBodyRot = CadeEntity.this.getYRot();
                    }
                }
            }
        }
    }

    class CadeEntityAvoidMoveGoal extends Goal {
        public CadeEntityAvoidMoveGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !CadeEntity.this.getMoveControl().hasWanted() && CadeEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos $$0 = CadeEntity.this.blockPosition();

            if(CadeEntity.this.getTarget() == null){
                for(int $$1 = 0; $$1 < 3; ++$$1) {
                    BlockPos $$2 = $$0.offset(CadeEntity.this.random.nextInt(15) - 7, CadeEntity.this.random.nextInt(11) - 5, CadeEntity.this.random.nextInt(15) - 7);
                    if (CadeEntity.this.level().isEmptyBlock($$2)) {
                        CadeEntity.this.moveControl.setWantedPosition((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, (double)0.25F);
                        CadeEntity.this.getLookControl().setLookAt((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, 180.0F, 20.0F);
                        
                        break;
                    }
                } 
            } else {
                BlockPos $$2 = $$0.offset(CadeEntity.this.random.nextInt(15) - 7, CadeEntity.this.random.nextInt(11) - 5, CadeEntity.this.random.nextInt(15) - 7);
                
                for(int $$1 = 0; $$1 < 3; ++$$1) {
                    BlockPos $$3 = $$0.offset(CadeEntity.this.random.nextInt(15) - 7, CadeEntity.this.random.nextInt(11) - 5, CadeEntity.this.random.nextInt(15) - 7);
                    if($$3.distToCenterSqr(CadeEntity.this.getTarget().position()) > $$2.distToCenterSqr(CadeEntity.this.getTarget().position())){
                        $$2 = $$3;
                    }
                    if (CadeEntity.this.level().isEmptyBlock($$2)) {
                        CadeEntity.this.moveControl.setWantedPosition((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, (double)0.25F);
                    }
                }
            }
        }
    }

    public class CadeCastingGoalA<T extends Mob & RangedAttackMob> extends Goal {
        CadeEntity CadeEntity;

        private final double speedModifier;
        boolean hasAnimated;
        int animatedTicks;
        int delayTicks;
        int animId;
        boolean done;

        Supplier<Boolean> canUse;

        private int attackDelay = 15;
        private int ticksUntilNextAttack = 15;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public CadeCastingGoalA(CadeEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.CadeEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor cadeColor = new ParticleColor(150, 150, 255);

        public Spell cadeCastASpell = new Spell()
                .add(AugmentAccelerateTwo.INSTANCE)

                .add(EffectFreeze.INSTANCE)
                .add(EffectColdSnap.INSTANCE)
                .add(AugmentAmplify.INSTANCE,8)
                .add(EffectFreeze.INSTANCE)

                .withColor(cadeColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            EntityProjectileSpell projectileSpell = new EntityProjectileSpell(entity.level(), resolver);
            projectileSpell.setColor(color);

            projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 1.0f, 0.8f);

            entity.level().addFreshEntity(projectileSpell);

            this.CadeEntity.castACooldown = random.nextInt(20) + 20;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.CadeEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.CadeEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.CadeEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 15;

            LivingEntity $$0 = this.CadeEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.CadeEntity.moveControl.setWantedPosition($$1.x + CadeEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + CadeEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.CadeEntity.setCastingA(false);
            this.CadeEntity.setAggressive(false);
            this.animatedTicks = 0;
            this.done = false;
            this.hasAnimated = false;
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

        public void tick() {
            LivingEntity livingentity = this.CadeEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.CadeEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        CadeEntity.setCastingA(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.CadeEntity, 1.0F, cadeCastASpell, cadeColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    CadeEntity.setCastingA(false);
                    CadeEntity.castAAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }

    }

    public class CadeCastingGoalB<T extends Mob & RangedAttackMob> extends Goal {
        CadeEntity CadeEntity;

        private final double speedModifier;
        boolean hasAnimated;
        int animatedTicks;
        int delayTicks;
        int animId;
        boolean done;

        Supplier<Boolean> canUse;

        private int attackDelay = 15;
        private int ticksUntilNextAttack = 15;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public CadeCastingGoalB(CadeEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.CadeEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor cadeColor = new ParticleColor(150, 150, 255);

        public Spell cadeCastBSpell = new Spell()
                .add(AugmentAccelerateThree.INSTANCE)

                .add(EffectFreeze.INSTANCE)
                .add(AugmentExtendTimeTwo.INSTANCE)
                .add(EffectColdSnap.INSTANCE)
                .add(AugmentAmplify.INSTANCE,8)

                .add(EffectBurst.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(EffectBreak.INSTANCE)
                .add(AugmentAmplifyTwo.INSTANCE)
                .add(EffectConjureWater.INSTANCE)
                .add(AugmentPierce.INSTANCE,2)
                .add(EffectFreeze.INSTANCE)
                .add(AugmentPierce.INSTANCE,2)

                .add(EffectDelay.INSTANCE)
                .add(EffectIceburst.INSTANCE)
                .add(AugmentAmplify.INSTANCE,3)
                .add(AugmentAOEThree.INSTANCE,2)

                .withColor(cadeColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            EntityProjectileSpell projectileSpell = new EntityProjectileSpell(entity.level(), resolver);
            projectileSpell.setColor(color);

            projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 1.0f, 0.8f);

            entity.level().addFreshEntity(projectileSpell);

            this.CadeEntity.castBCooldown = random.nextInt(80) + 80;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.CadeEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.CadeEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.CadeEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 15;

            LivingEntity $$0 = this.CadeEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.CadeEntity.moveControl.setWantedPosition($$1.x + CadeEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + CadeEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.CadeEntity.setCastingB(false);
            this.CadeEntity.setAggressive(false);
            this.animatedTicks = 0;
            this.done = false;
            this.hasAnimated = false;
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

        public void tick() {
            LivingEntity livingentity = this.CadeEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.CadeEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        CadeEntity.setCastingB(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.CadeEntity, 1.0F, cadeCastBSpell, cadeColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    CadeEntity.setCastingB(false);
                    CadeEntity.castBAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }

    }

    public class CadeCastingGoalC<T extends Mob & RangedAttackMob> extends Goal {
        CadeEntity CadeEntity;

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

        Supplier<Boolean> canUse;

        private int attackDelay = 15;
        private int ticksUntilNextAttack = 15;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public CadeCastingGoalC(CadeEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.CadeEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.attackRadiusSqr = attackRange * attackRange;

        }
        private ParticleColor cadeColor = new ParticleColor(240, 240, 255);

        public Spell cadeCastCSpell = new Spell()
                .add(AugmentAccelerateThree.INSTANCE)

                .add(EffectBurst.INSTANCE)
                .add(AugmentAOE.INSTANCE, 2)
                .add(AugmentSensitive.INSTANCE)
                .add(EffectConjureWater.INSTANCE)
                .add(AugmentPierce.INSTANCE,2)
                .add(EffectFreeze.INSTANCE)
                .add(AugmentPierce.INSTANCE,2)
                .add(EffectBurst.INSTANCE)
                .add(EffectFreeze.INSTANCE)
                .add(AugmentExtendTime.INSTANCE, 2)

                .withColor(cadeColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            this.CadeEntity.castCCooldown = random.nextInt(300) + 300;
            int time = 100;

            Vec3 pos = this.CadeEntity.position();

            AdamsArsPlus.setInterval(() -> {

                DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
                projectileSpell.setColor(color);
                projectileSpell.shoot(entity, 90, 0, 0.0F, 0.5f, 0.8f);
                projectileSpell.setPos(pos.add(CadeEntity.this.random.nextInt(19) - 9, 8, CadeEntity.this.random.nextInt(19) - 9));
                entity.level().addFreshEntity(projectileSpell);

            }, 10, time, () -> !this.CadeEntity.isAlive());
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.CadeEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.CadeEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.CadeEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 15;

            LivingEntity $$0 = this.CadeEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.CadeEntity.moveControl.setWantedPosition($$1.x + CadeEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + CadeEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.CadeEntity.setCastingB(false);
            this.CadeEntity.setAggressive(false);
            this.animatedTicks = 0;
            this.done = false;
            this.hasAnimated = false;
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

        public void tick() {
            LivingEntity livingentity = this.CadeEntity.getTarget();
            if (livingentity != null) {
                double d0 = this.CadeEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean canSeeEnemy = this.CadeEntity.getSensing().hasLineOfSight(livingentity);
                if (canSeeEnemy != this.seeTime > 0) {
                    this.seeTime = 0;
                }

                if (canSeeEnemy) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.CadeEntity.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.CadeEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 10) {
                    if ((double)this.CadeEntity.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.CadeEntity.getRandom().nextFloat() < 0.3) {
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

                    this.CadeEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.CadeEntity.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.CadeEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if (this.seeTime >= 20 && !this.hasAnimated) {
                    this.hasAnimated = true;
                    Networking.sendToNearby(this.CadeEntity.level(), this.CadeEntity, new PacketAnimEntity(this.CadeEntity.getId(), this.animId));
                }

                if (this.hasAnimated) {
                    shouldCountTillNextAttack = true;
                    this.CadeEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        CadeEntity.setCastingB(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.CadeEntity, 1.0F, cadeCastCSpell, cadeColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    CadeEntity.setCastingB(false);
                    CadeEntity.castBAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }

    }

    public class CadeCastingGoalD<T extends Mob & RangedAttackMob> extends Goal {
        CadeEntity CadeEntity;

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

        Supplier<Boolean> canUse;

        private int attackDelay = 15;
        private int ticksUntilNextAttack = 15;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public CadeCastingGoalD(CadeEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.CadeEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.attackRadiusSqr = attackRange * attackRange;

        }
        private ParticleColor cadeColor = new ParticleColor(240, 240, 255);

        public Spell cadeCastDSpell = new Spell()
                .add(AugmentAccelerateThree.INSTANCE)

                .add(EffectBurst.INSTANCE)
                .add(AugmentAOE.INSTANCE, 2)
                .add(AugmentSensitive.INSTANCE)
                .add(EffectIceburst.INSTANCE)
                .add(AugmentAOEThree.INSTANCE)
                .add(AugmentAmplify.INSTANCE, 4)

                .withColor(cadeColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            this.CadeEntity.castDCooldown = random.nextInt(300) + 300;
            int time = 150;

            Vec3 pos = this.CadeEntity.position();

            AdamsArsPlus.setInterval(() -> {

                DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
                projectileSpell.setColor(color);
                projectileSpell.shoot(entity, 90, 0, 0.0F, 0.5f, 0.8f);
                projectileSpell.setPos(pos.add(CadeEntity.this.random.nextInt(19) - 9, 8, CadeEntity.this.random.nextInt(19) - 9));
                entity.level().addFreshEntity(projectileSpell);

            }, 10, time, () -> !this.CadeEntity.isAlive());
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.CadeEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.CadeEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.CadeEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 15;

            LivingEntity $$0 = this.CadeEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.CadeEntity.moveControl.setWantedPosition($$1.x + CadeEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + CadeEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.CadeEntity.setCastingB(false);
            this.CadeEntity.setAggressive(false);
            this.animatedTicks = 0;
            this.done = false;
            this.hasAnimated = false;
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

        public void tick() {
            LivingEntity livingentity = this.CadeEntity.getTarget();
            if (livingentity != null) {
                double d0 = this.CadeEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean canSeeEnemy = this.CadeEntity.getSensing().hasLineOfSight(livingentity);
                if (canSeeEnemy != this.seeTime > 0) {
                    this.seeTime = 0;
                }

                if (canSeeEnemy) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.CadeEntity.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.CadeEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 10) {
                    if ((double)this.CadeEntity.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.CadeEntity.getRandom().nextFloat() < 0.3) {
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

                    this.CadeEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.CadeEntity.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.CadeEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if (this.seeTime >= 20 && !this.hasAnimated) {
                    this.hasAnimated = true;
                    Networking.sendToNearby(this.CadeEntity.level(), this.CadeEntity, new PacketAnimEntity(this.CadeEntity.getId(), this.animId));
                }

                if (this.hasAnimated) {
                    shouldCountTillNextAttack = true;
                    this.CadeEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        CadeEntity.setCastingB(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.CadeEntity, 1.0F, cadeCastDSpell, cadeColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    CadeEntity.setCastingB(false);
                    CadeEntity.castBAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }
    }

    public class CadeCastingGoalE<T extends Mob & RangedAttackMob> extends Goal {
        CadeEntity CadeEntity;

        private final double speedModifier;
        boolean hasAnimated;
        int animatedTicks;
        int delayTicks;
        int animId;
        boolean done;

        Supplier<Boolean> canUse;

        private int attackDelay = 15;
        private int ticksUntilNextAttack = 15;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public CadeCastingGoalE(CadeEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.CadeEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor cadeColor = new ParticleColor(150, 150, 255);

        public Spell cadeCastESpell = new Spell()
                .add(AugmentPierce.INSTANCE, 8)
                .add(AugmentDurationDown.INSTANCE, 2)

                .add(EffectDelay.INSTANCE)

                .add(EffectBurst.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(AugmentAOE.INSTANCE, 2)
                .add(EffectBreak.INSTANCE)
                .add(AugmentAmplifyTwo.INSTANCE)
                .add(EffectConjureWater.INSTANCE)
                .add(EffectFreeze.INSTANCE)

                .add(EffectDelay.INSTANCE)
                .add(EffectIceburst.INSTANCE)
                .add(AugmentAmplify.INSTANCE,3)
                .add(AugmentAOEThree.INSTANCE,2)

                .withColor(cadeColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
            projectileSpell.setColor(color);

            projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 0.3f, 0.8f);

            entity.level().addFreshEntity(projectileSpell);

            this.CadeEntity.castECooldown = random.nextInt(400) + 200;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.CadeEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.CadeEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.CadeEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 15;

            LivingEntity $$0 = this.CadeEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.CadeEntity.moveControl.setWantedPosition($$1.x + CadeEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + CadeEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.CadeEntity.setCastingA(false);
            this.CadeEntity.setAggressive(false);
            this.animatedTicks = 0;
            this.done = false;
            this.hasAnimated = false;
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

        public void tick() {
            LivingEntity livingentity = this.CadeEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.CadeEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        CadeEntity.setCastingA(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.CadeEntity, 1.0F, cadeCastESpell, cadeColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    CadeEntity.setCastingA(false);
                    CadeEntity.castAAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }

    }
    
}
