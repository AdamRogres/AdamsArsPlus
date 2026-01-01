package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.entities.ai.RyanDomainGoal;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAOEThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateTwo;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAmplifyTwo;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentExtendTimeTwo;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectBlueFlame;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectEruption;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
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
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.FLAME_DEITY_EFFECT;
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
    public int castBCooldown;
    public int castCCooldown;
    public int domainCooldown;

    private final ServerBossEvent bossEvent;

    public RyanEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.moveControl = new RyanEntity.BossMoveControl(this);
        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
    }

    public RyanEntity(Level pLevel){
        this(RYAN_ENTITY.get(), pLevel);
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
        if(!this.hasEffect(FLAME_DEITY_EFFECT.get())){
            this.addEffect(new MobEffectInstance(FLAME_DEITY_EFFECT.get(), 100, 0, false, false));
        }

        super.tick();
        this.setNoGravity(true);

        if(attackCooldown > 0) {
            attackCooldown--;
        }
        if(castCooldown > 0) {
            castCooldown--;
        }
        if(castBCooldown > 0) {
            castBCooldown--;
        }
        if(castCCooldown > 0) {
            castCCooldown--;
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
        tag.putInt("castb", castBCooldown);
        tag.putInt("castc", castCCooldown);
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
        this.castBCooldown = tag.getInt("castb");
        this.castCCooldown = tag.getInt("castc");
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

        this.goalSelector.addGoal(2, new RyanCastingGoalA(this, 1.3D, 20f, () -> castCooldown <= 0, 0, 15));
        this.goalSelector.addGoal(2, new RyanCastingGoalB(this, 1.3D, 20f, () -> castBCooldown <= 0, 0, 15));
        this.goalSelector.addGoal(2, new RyanCastingGoalC(this, 1.3D, 20f, () -> castCCooldown <= 0, 0, 15));

        this.goalSelector.addGoal(3, new RyanChargeAttackGoal(this, 1.5D, true, () -> true));

        this.goalSelector.addGoal(4, new RyanEntity.RyanEntityRandomMoveGoal());
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));

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
                .add(Attributes.ATTACK_KNOCKBACK, (double)0.7F)
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
    
    // Goals and Movement
    class BossMoveControl extends MoveControl {
        public BossMoveControl(RyanEntity pRyan) {
            super(pRyan);
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 $$0 = new Vec3(this.wantedX - RyanEntity.this.getX(), this.wantedY - RyanEntity.this.getY(), this.wantedZ - RyanEntity.this.getZ());
                double $$1 = $$0.length();
                if ($$1 < RyanEntity.this.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                    RyanEntity.this.setDeltaMovement(RyanEntity.this.getDeltaMovement().scale((double)0.5F));
                } else {
                    RyanEntity.this.setDeltaMovement(RyanEntity.this.getDeltaMovement().add($$0.scale(this.speedModifier * 0.05 / $$1)));
                    if (RyanEntity.this.getTarget() == null) {
                        Vec3 $$2 = RyanEntity.this.getDeltaMovement();
                        RyanEntity.this.setYRot(-((float) Mth.atan2($$2.x, $$2.z)) * (180F / (float)Math.PI));
                        RyanEntity.this.yBodyRot = RyanEntity.this.getYRot();
                    } else {
                        double $$3 = RyanEntity.this.getTarget().getX() - RyanEntity.this.getX();
                        double $$4 = RyanEntity.this.getTarget().getZ() - RyanEntity.this.getZ();
                        RyanEntity.this.setYRot(-((float)Mth.atan2($$3, $$4)) * (180F / (float)Math.PI));
                        RyanEntity.this.yBodyRot = RyanEntity.this.getYRot();
                    }
                }
            }
        }
    }
    
    class RyanChargeAttackGoal extends MeleeAttackGoal {
        private final RyanEntity entity;

        private int totalAnimation = 20;
        private int attackDelay = 12;
        private int ticksUntilNextAttack = 12;
        private boolean shouldCountTillNextAttack = false;

        Supplier<Boolean> canUse;
        boolean done;
        
        public RyanChargeAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((RyanEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 12;
            ticksUntilNextAttack = 12;
            
            LivingEntity $$0 = RyanEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                RyanEntity.this.moveControl.setWantedPosition($$1.x, $$1.y, $$1.z, (double)1.0F);
            }
        }

        private ParticleColor ryanColor = new ParticleColor(255, 0, 0);

        public Spell ryanAttackSpell = new Spell()
                .add(EffectDispel.INSTANCE)
                .add(EffectIgnite.INSTANCE)
                .add(AugmentExtendTimeTwo.INSTANCE)
                .add(EffectHex.INSTANCE)
                .add(EffectEruption.INSTANCE)

                .withColor(ryanColor);

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
                    entity.setAttacking(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    if(isEnemyWithinTrueAttackDistance(pEnemy, pDistToEnemySqr)) {
                        performAttack(pEnemy);
                        performSpellAttack(this.mob, 1.0F, ryanAttackSpell, ryanColor, pEnemy);
                    } else {
                        this.resetAttackLoopCooldown();
                        this.done = true;
                    }
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttacking(false);
                entity.attackAnimationTimeout = 0;
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

        void performSpellAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));

        }

        public void stop() {
            entity.setAttacking(false);
            this.done = false;
            super.stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity $$0 = RyanEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = RyanEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    RyanEntity.this.moveControl.setWantedPosition($$2.x, $$2.y, $$2.z, (double)1.0F);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    class RyanEntityRandomMoveGoal extends Goal {
        public RyanEntityRandomMoveGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !RyanEntity.this.getMoveControl().hasWanted() && RyanEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos $$0 = RyanEntity.this.blockPosition();

            for(int $$1 = 0; $$1 < 3; ++$$1) {
                BlockPos $$2 = $$0.offset(RyanEntity.this.random.nextInt(15) - 7, RyanEntity.this.random.nextInt(11) - 5, RyanEntity.this.random.nextInt(15) - 7);
                if (RyanEntity.this.level().isEmptyBlock($$2)) {
                    RyanEntity.this.moveControl.setWantedPosition((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, (double)0.25F);
                    if (RyanEntity.this.getTarget() == null) {
                        RyanEntity.this.getLookControl().setLookAt((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    public class RyanCastingGoalA<T extends Mob & RangedAttackMob> extends Goal {
        RyanEntity ryanEntity;

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

        public RyanCastingGoalA(RyanEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.ryanEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor  ryanColor = new ParticleColor(255, 0, 0);

        public Spell ryanCastSpell = new Spell()
                .add(AugmentAccelerateTwo.INSTANCE)
                .add(EffectIgnite.INSTANCE)
                .add(EffectFlare.INSTANCE)
                .add(AugmentAmplify.INSTANCE)

                .add(EffectExplosion.INSTANCE)
                .add(AugmentAmplify.INSTANCE,8)

                .add(EffectBurst.INSTANCE)
                .add(AugmentAOEThree.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(EffectBreak.INSTANCE)
                .add(AugmentAmplifyTwo.INSTANCE)
                .add(EffectIgnite.INSTANCE)
                .add(EffectEvaporate.INSTANCE)

                .withColor(ryanColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            EntityProjectileSpell projectileSpell = new EntityProjectileSpell(entity.level(), resolver);
            projectileSpell.setColor(color);

            projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 1.0f, 0.8f);

            entity.level().addFreshEntity(projectileSpell);

            this.ryanEntity.castCooldown = random.nextInt(200) + 80;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.ryanEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.ryanEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.ryanEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 15;

            LivingEntity $$0 = this.ryanEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.ryanEntity.moveControl.setWantedPosition($$1.x + RyanEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + RyanEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.ryanEntity.setCasting(false);
            this.ryanEntity.setAggressive(false);
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
            LivingEntity livingentity = this.ryanEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.ryanEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        ryanEntity.setCasting(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.ryanEntity, 1.0F, ryanCastSpell, ryanColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    ryanEntity.setCasting(false);
                    ryanEntity.castAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }

    }

    public class RyanCastingGoalB<T extends Mob & RangedAttackMob> extends Goal {
        RyanEntity ryanEntity;

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

        public RyanCastingGoalB(RyanEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.ryanEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.attackRadiusSqr = attackRange * attackRange;

        }
        private ParticleColor  ryanColor = new ParticleColor(255, 0, 0);

        public Spell ryanCastSpell = new Spell()
                .add(EffectBlueFlame.INSTANCE)
                .add(AugmentAOE.INSTANCE, 2)
                .add(AugmentAmplify.INSTANCE, 3)
                .add(AugmentExtendTime.INSTANCE, 4)

                .withColor(ryanColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            this.ryanEntity.castBCooldown = random.nextInt(400) + 160;
            int time = 100;

            Vec3 pos = this.ryanEntity.position();

            AdamsArsPlus.setInterval(() -> {

                DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
                projectileSpell.setColor(color);
                projectileSpell.shoot(entity, 90, 0, 0.0F, 0.5f, 0.8f);
                projectileSpell.setPos(pos.add(RyanEntity.this.random.nextInt(19) - 9, 8, RyanEntity.this.random.nextInt(19) - 9));
                entity.level().addFreshEntity(projectileSpell);

            }, 10, time, () -> !this.ryanEntity.isAlive());
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.ryanEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.ryanEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.ryanEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 15;

            LivingEntity $$0 = this.ryanEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.ryanEntity.moveControl.setWantedPosition($$1.x + RyanEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + RyanEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.ryanEntity.setCasting(false);
            this.ryanEntity.setAggressive(false);
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
            LivingEntity livingentity = this.ryanEntity.getTarget();
            if (livingentity != null) {
                double d0 = this.ryanEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean canSeeEnemy = this.ryanEntity.getSensing().hasLineOfSight(livingentity);
                if (canSeeEnemy != this.seeTime > 0) {
                    this.seeTime = 0;
                }

                if (canSeeEnemy) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.ryanEntity.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.ryanEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 10) {
                    if ((double)this.ryanEntity.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.ryanEntity.getRandom().nextFloat() < 0.3) {
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

                    this.ryanEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.ryanEntity.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.ryanEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if (this.seeTime >= 20 && !this.hasAnimated) {
                    this.hasAnimated = true;
                    Networking.sendToNearby(this.ryanEntity.level(), this.ryanEntity, new PacketAnimEntity(this.ryanEntity.getId(), this.animId));
                }

                if (this.hasAnimated) {
                    shouldCountTillNextAttack = true;
                    this.ryanEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        ryanEntity.setCasting(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.ryanEntity, 1.0F, ryanCastSpell, ryanColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    ryanEntity.setCasting(false);
                    ryanEntity.castAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }

    }

    public class RyanCastingGoalC<T extends Mob & RangedAttackMob> extends Goal {
        RyanEntity ryanEntity;

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

        public RyanCastingGoalC(RyanEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.ryanEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.attackRadiusSqr = attackRange * attackRange;

        }
        private ParticleColor  ryanColor = new ParticleColor(255, 0, 0);

        public Spell ryanCastSpell = new Spell()
                .add(AugmentAccelerateTwo.INSTANCE)
                .add(EffectIgnite.INSTANCE)
                .add(EffectFlare.INSTANCE)
                .add(AugmentAmplify.INSTANCE, 4)
                .add(EffectEruption.INSTANCE)

                .add(EffectBurst.INSTANCE)
                .add(AugmentAOE.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(EffectBreak.INSTANCE)
                .add(AugmentAmplifyTwo.INSTANCE)
                .add(EffectIgnite.INSTANCE)
                .add(EffectEvaporate.INSTANCE)

                .withColor(ryanColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            this.ryanEntity.castCCooldown = random.nextInt(400) + 160;

            int time = 50;

            Vec3 pos = this.ryanEntity.position();

            AdamsArsPlus.setInterval(() -> {
                if(ryanEntity.getTarget() != null){
                    this.ryanEntity.getLookControl().setLookAt(ryanEntity.getTarget());
                }

                DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
                projectileSpell.setColor(color);
                projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 1.0f, 0.8f);
                projectileSpell.setPos(pos);
                entity.level().addFreshEntity(projectileSpell);

            }, 10, time, () -> !this.ryanEntity.isAlive());

        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.ryanEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.ryanEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.ryanEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 15;

            LivingEntity $$0 = this.ryanEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.ryanEntity.moveControl.setWantedPosition($$1.x + RyanEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + RyanEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.ryanEntity.setCasting(false);
            this.ryanEntity.setAggressive(false);
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
            LivingEntity livingentity = this.ryanEntity.getTarget();
            if (livingentity != null) {
                if(true) {
                    shouldCountTillNextAttack = true;
                    this.ryanEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        ryanEntity.setCasting(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.ryanEntity, 1.0F, ryanCastSpell, ryanColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    ryanEntity.setCasting(false);
                    ryanEntity.castAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }

    }
}
