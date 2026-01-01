package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.entities.ai.*;
import com.adamsmods.adamsarsplus.entities.ai.pathfinding.AdvancedPathNavigate;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectDismantle;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectDivineSmite;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectEruption;
import com.adamsmods.adamsarsplus.glyphs.method_glyph.PropagateDetonate;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.entity.pathfinding.PathingStuckHandler;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.*;
import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.CAM_ENTITY;
import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

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
    public int castingBCooldown;
    public int castingCCooldown;
    public int domainCooldown;

    public int blinkCooldown;

    private final ServerBossEvent bossEvent;

    public CamEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        this.moveControl = new CamEntity.BossMoveControl(this);
    }

    public CamEntity(Level pLevel){
        this(CAM_ENTITY.get(), pLevel);
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
        this.setNoGravity(true);

        if(this.hasEffect(LEAP_FATIGUE_EFFECT.get())){
            this.addEffect(new MobEffectInstance(LIGHTNING_STEPS_EFFECT.get(), 100, 0, false, false));
        }

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
        if(castingBCooldown > 0) {
            castingBCooldown--;
        }
        if(castingCCooldown > 0) {
            castingCCooldown--;
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
        tag.putInt("castingb", castingBCooldown);
        tag.putInt("castingc", castingCCooldown);
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
                performSpellSelf(this, camBlinkSpell, camColor);
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
        this.castingBCooldown = tag.getInt("castingb");
        this.castingCCooldown = tag.getInt("castingc");
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

        this.goalSelector.addGoal(1, new CamAttackGoalAB(this, 1.5D, true, () -> attackABCooldown != 0));
        this.goalSelector.addGoal(2, new CamDomainGoal(this, 1.0D, 20f, () -> domainCooldown <= 0, 0, 13));
        this.goalSelector.addGoal(2, new CamCastingGoalB(this, 1.0D, 35f, () -> castingBCooldown <= 0, 0, 12));
        this.goalSelector.addGoal(2, new CamCastingGoalC(this, 1.0D, 35f, () -> castingCCooldown <= 0, 0, 12));

        this.goalSelector.addGoal(3, new CamCastingGoalA(this, 1.0D, 35f, () -> castingCooldown <= 0, 0, 12));
        this.goalSelector.addGoal(3, new CamAttackGoalB(this, 1.0D, true, () -> attackBCooldown <= 0));
        this.goalSelector.addGoal(4, new CamAttackGoalAA(this, 1.3D, true, () -> true));

        this.goalSelector.addGoal(7, new CamEntity.CamEntityRandomMoveGoal());
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, false));

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

    public void performSpellSelf(LivingEntity entity, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

        resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));
    }

    // Goals and Movement
    class BossMoveControl extends MoveControl {
        public BossMoveControl(CamEntity pCam) {
            super(pCam);
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 $$0 = new Vec3(this.wantedX - CamEntity.this.getX(), this.wantedY - CamEntity.this.getY(), this.wantedZ - CamEntity.this.getZ());
                double $$1 = $$0.length();
                if ($$1 < CamEntity.this.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                    CamEntity.this.setDeltaMovement(CamEntity.this.getDeltaMovement().scale((double)0.5F));
                } else {
                    CamEntity.this.setDeltaMovement(CamEntity.this.getDeltaMovement().add($$0.scale(this.speedModifier * 0.05 / $$1)));
                    if (CamEntity.this.getTarget() == null) {
                        Vec3 $$2 = CamEntity.this.getDeltaMovement();
                        CamEntity.this.setYRot(-((float) Mth.atan2($$2.x, $$2.z)) * (180F / (float)Math.PI));
                        CamEntity.this.yBodyRot = CamEntity.this.getYRot();
                    } else {
                        double $$3 = CamEntity.this.getTarget().getX() - CamEntity.this.getX();
                        double $$4 = CamEntity.this.getTarget().getZ() - CamEntity.this.getZ();
                        CamEntity.this.setYRot(-((float)Mth.atan2($$3, $$4)) * (180F / (float)Math.PI));
                        CamEntity.this.yBodyRot = CamEntity.this.getYRot();
                    }
                }
            }
        }
    }

    class CamEntityRandomMoveGoal extends Goal {
        public CamEntityRandomMoveGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !CamEntity.this.getMoveControl().hasWanted() && CamEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos $$0 = CamEntity.this.blockPosition();

            for(int $$1 = 0; $$1 < 3; ++$$1) {
                BlockPos $$2 = $$0.offset(CamEntity.this.random.nextInt(15) - 7, CamEntity.this.random.nextInt(11) - 5, CamEntity.this.random.nextInt(15) - 7);
                if (CamEntity.this.level().isEmptyBlock($$2)) {
                    CamEntity.this.moveControl.setWantedPosition((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, (double)0.25F);
                    if (CamEntity.this.getTarget() == null) {
                        CamEntity.this.getLookControl().setLookAt((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }

    class CamAttackGoalAA extends MeleeAttackGoal {
        private final CamEntity entity;

        private int totalAnimation = 15;
        private int attackDelay = 10;
        private int ticksUntilNextAttack = 10;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public CamAttackGoalAA(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((CamEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 10;
            ticksUntilNextAttack = 10;

            LivingEntity $$0 = CamEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                CamEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, speedModifier);
            }
        }

        private ParticleColor CamColor = new ParticleColor(255, 255, 255);

        public Spell CamAttackSpell = new Spell()
                .add(EffectDismantle.INSTANCE)
                .add(AugmentAccelerateThree.INSTANCE)
                .add(AugmentAmplify.INSTANCE, 3)

                .withColor(CamColor);

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

                if(isTimeToStartAttackAnimation()) {
                    entity.setAttackingAA(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    if(isEnemyWithinTrueAttackDistance(pEnemy, pDistToEnemySqr)) {
                        performAttack(pEnemy);
                        performSpellAttack(this.mob, CamAttackSpell, CamColor, pEnemy);

                        this.entity.attackABCooldown = 50;
                    } else {
                        this.resetAttackLoopCooldown();
                        this.done = true;
                    }
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttackingAA(false);
                entity.attackAAAnimationTimeout = 0;
            }
        }

        private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
        }

        private boolean isEnemyWithinTrueAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackTrueReachSqr(pEnemy);
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
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 5.0F);
        }

        protected double getAttackTrueReachSqr(LivingEntity pAttackTarget) {
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 5.0F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;

        }

        void performSpellAttack(LivingEntity entity, Spell spell, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));

        }

        public void stop() {
            entity.setAttackingAA(false);
            this.done = false;
            super.stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity $$0 = CamEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = CamEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    CamEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, speedModifier);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    class CamAttackGoalAB extends MeleeAttackGoal {
        private final CamEntity entity;

        private int totalAnimation = 15;
        private int attackDelay = 10;
        private int ticksUntilNextAttack = 10;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public CamAttackGoalAB(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((CamEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 10;
            ticksUntilNextAttack = 10;

            LivingEntity $$0 = CamEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                CamEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, speedModifier);
            }
        }

        private ParticleColor CamColor = new ParticleColor(255, 255, 255);

        public Spell camAttackABSpell = new Spell()
                .add(EffectLightning.INSTANCE)
                .add(AugmentAmplify.INSTANCE,8)

                .withColor(CamColor);

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

                if(isTimeToStartAttackAnimation()) {
                    entity.setAttackingAB(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    if(isEnemyWithinTrueAttackDistance(pEnemy, pDistToEnemySqr)) {
                        performAttack(pEnemy);
                        performSpellAttack(this.mob, camAttackABSpell, CamColor, pEnemy);
                    } else {
                        this.resetAttackLoopCooldown();
                        this.done = true;
                    }
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttackingAB(false);
                entity.attackABAnimationTimeout = 0;
            }
        }

        private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
        }

        private boolean isEnemyWithinTrueAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackTrueReachSqr(pEnemy);
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
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 6.0F);
        }

        protected double getAttackTrueReachSqr(LivingEntity pAttackTarget) {
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 6.0F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;

        }

        void performSpellAttack(LivingEntity entity, Spell spell, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));
            this.entity.attackABCooldown = 0;
        }

        public void stop() {
            entity.setAttackingAB(false);
            this.done = false;
            super.stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity $$0 = CamEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = CamEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    CamEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, speedModifier);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    class CamAttackGoalB extends MeleeAttackGoal {
        private final CamEntity entity;

        private int totalAnimation = 15;
        private int attackDelay = 6;
        private int ticksUntilNextAttack = 6;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public CamAttackGoalB(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((CamEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 6;
            ticksUntilNextAttack = 6;

            LivingEntity $$0 = CamEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                CamEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, speedModifier);
            }
        }

        private ParticleColor camColor = new ParticleColor(255, 255, 255);

        public Spell camAttackB1Spell = new Spell()
                .add(EffectBlink.INSTANCE)

                .withColor(camColor);

        public Spell camAttackB2Spell = new Spell()
                .add(EffectLaunch.INSTANCE)

                .add(EffectDelay.INSTANCE)
                .add(EffectWindshear.INSTANCE)
                .add(AugmentAmplify.INSTANCE, 9)

                .withColor(camColor);

        public Spell camAttackB3Spell = new Spell()
                .add(EffectKnockback.INSTANCE)

                .withColor(camColor);

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

                if(isTimeToStartAttackAnimation()) {
                    entity.setAttackingB(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    if(isEnemyWithinTrueAttackDistance(pEnemy, pDistToEnemySqr)) {
                        performAttack(pEnemy);

                    } else {
                        this.resetAttackLoopCooldown();
                        this.done = true;
                    }
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttackingB(false);
                entity.attackBAnimationTimeout = 0;
            }
        }

        private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
        }

        private boolean isEnemyWithinTrueAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackTrueReachSqr(pEnemy);
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
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 5.0F);
        }

        protected double getAttackTrueReachSqr(LivingEntity pAttackTarget) {
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 5.0F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);

            this.done = true;

            if(pEnemy.isBlocking()){
                performSpellAttack(this.mob, camAttackB1Spell, camAttackB3Spell, camColor, pEnemy);
            }
            else{
                performSpellAttack(this.mob, camAttackB1Spell, camAttackB2Spell, camColor, pEnemy);
            }
        }

        void performSpellAttack(LivingEntity entity, Spell spell1, Spell spell2, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell1, entity, new LivingCaster(entity)).withColors(color));
            resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));

            EntitySpellResolver resolver2 = new EntitySpellResolver(new SpellContext(entity.level(), spell2, entity, new LivingCaster(entity)).withColors(color));
            resolver2.onResolveEffect(entity.level(), new EntityHitResult(enemy));

            this.entity.attackBCooldown = random.nextInt(200) + 200;
        }

        public void stop() {
            entity.setAttackingB(false);
            this.done = false;
            super.stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity $$0 = CamEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = CamEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    CamEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, speedModifier);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    public class CamCastingGoalA<T extends Mob & RangedAttackMob> extends Goal {
        CamEntity CamEntity;

        private final double speedModifier;
        boolean hasAnimated;
        int animatedTicks;
        int delayTicks;
        int animId;
        boolean done;

        Supplier<Boolean> canUse;

        private int attackDelay = 12;
        private int ticksUntilNextAttack = 12;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public CamCastingGoalA(CamEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.CamEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor camColor = new ParticleColor(255, 255, 255);

        public Spell camCastSpell = new Spell()
                .add(AugmentAccelerateTwo.INSTANCE)
                .add(AugmentPierce.INSTANCE,3)
                .add(AugmentDurationDown.INSTANCE,1)

                .add(EffectLightning.INSTANCE)
                .add(AugmentAmplify.INSTANCE,6)

                .add(EffectDelay.INSTANCE)
                .add(AugmentDurationDown.INSTANCE)

                .add(EffectDivineSmite.INSTANCE)
                .add(AugmentAmplify.INSTANCE,6)

                .add(EffectBurst.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(EffectBreak.INSTANCE)
                .add(AugmentAmplifyThree.INSTANCE)

                .withColor(camColor);

        void performCastAttack(LivingEntity entity, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
            projectileSpell.setColor(color);

            projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 1.0f, 0.8f);

            entity.level().addFreshEntity(projectileSpell);

            this.CamEntity.castingCooldown = random.nextInt(180) + 120;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.CamEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.CamEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.CamEntity.setAggressive(true);
            attackDelay = 12;
            ticksUntilNextAttack = 12;

            LivingEntity $$0 = this.CamEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.CamEntity.moveControl.setWantedPosition($$1.x + CamEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + CamEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.CamEntity.setCasting(false);
            this.CamEntity.setAggressive(false);
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
            LivingEntity livingentity = this.CamEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.CamEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        CamEntity.setCasting(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.CamEntity, camCastSpell, camColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    CamEntity.setCasting(false);
                    CamEntity.castingAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }

    }

    public class CamCastingGoalB<T extends Mob & RangedAttackMob> extends Goal {
        CamEntity CamEntity;

        private final double speedModifier;
        boolean hasAnimated;
        int animatedTicks;
        int delayTicks;
        int animId;
        boolean done;

        Supplier<Boolean> canUse;

        private int attackDelay = 12;
        private int ticksUntilNextAttack = 12;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public CamCastingGoalB(CamEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.CamEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor camColor = new ParticleColor(255, 255, 255);

        public Spell camCastSpell = new Spell()
                .add(AugmentAccelerateTwo.INSTANCE)

                .add(EffectLightning.INSTANCE)
                .add(AugmentAmplify.INSTANCE,6)

                .withColor(camColor);

        void performCastAttack(LivingEntity entity, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            this.CamEntity.castingBCooldown = random.nextInt(300) + 300;

            int time = 75;

            Vec3 pos = this.CamEntity.position();

            AdamsArsPlus.setInterval(() -> {
                if(CamEntity.getTarget() != null){
                    this.CamEntity.getLookControl().setLookAt(CamEntity.getTarget());
                }

                EntityProjectileSpell projectileSpell = new EntityProjectileSpell(entity.level(), resolver);
                projectileSpell.setColor(color);
                projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 1.0f, 0.8f);
                projectileSpell.setPos(pos);
                entity.level().addFreshEntity(projectileSpell);

            }, 15, time, () -> !this.CamEntity.isAlive());
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.CamEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.CamEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.CamEntity.setAggressive(true);
            attackDelay = 12;
            ticksUntilNextAttack = 12;

            LivingEntity $$0 = this.CamEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.CamEntity.moveControl.setWantedPosition($$1.x + CamEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + CamEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.CamEntity.setCasting(false);
            this.CamEntity.setAggressive(false);
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
            LivingEntity livingentity = this.CamEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.CamEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        CamEntity.setCasting(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.CamEntity, camCastSpell, camColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    CamEntity.setCasting(false);
                    CamEntity.castingAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    public class CamCastingGoalC<T extends Mob & RangedAttackMob> extends Goal {
        CamEntity CamEntity;

        private final double speedModifier;
        boolean hasAnimated;
        int animatedTicks;
        int delayTicks;
        int animId;
        boolean done;

        Supplier<Boolean> canUse;

        private int attackDelay = 12;
        private int ticksUntilNextAttack = 12;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public CamCastingGoalC(CamEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.CamEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor camColor = new ParticleColor(255, 255, 255);

        public Spell camCastSpell = new Spell()
                .add(EffectBurst.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(AugmentAOE.INSTANCE)

                .add(PropagateDetonate.INSTANCE)
                .add(AugmentAccelerate.INSTANCE)
                .add(EffectDismantle.INSTANCE)
                .add(AugmentAccelerateThree.INSTANCE)
                .add(AugmentAmplify.INSTANCE,2)

                .withColor(camColor);

        void performCastAttack(LivingEntity entity, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));

            this.CamEntity.castingCCooldown = random.nextInt(280) + 220;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.CamEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.CamEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.CamEntity.setAggressive(true);
            attackDelay = 12;
            ticksUntilNextAttack = 12;

            LivingEntity $$0 = this.CamEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.CamEntity.moveControl.setWantedPosition($$1.x + CamEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + CamEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.CamEntity.setCasting(false);
            this.CamEntity.setAggressive(false);
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
            LivingEntity livingentity = this.CamEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.CamEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        CamEntity.setCasting(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.CamEntity, camCastSpell, camColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    CamEntity.setCasting(false);
                    CamEntity.castingAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }
}
