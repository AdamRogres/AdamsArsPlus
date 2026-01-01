package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.entities.ai.*;
import com.adamsmods.adamsarsplus.entities.ai.pathfinding.AdvancedPathNavigate;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAOEThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateTwo;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAmplifyTwo;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentExtendTimeTwo;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectEruption;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectFracture;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectRaiseEarth;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectSwapTarget;
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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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

import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.NICK_ENTITY;
import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

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
    public int castCooldown;
    public int domainCooldown;
    public int leapCooldown;

    private final ServerBossEvent bossEvent;

    public NickEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        this.moveControl = new NickEntity.BossMoveControl(this);
    }

    public NickEntity(Level pLevel){
        this(NICK_ENTITY.get(), pLevel);
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
        this.setNoGravity(true);

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
        if(castCooldown > 0) {
            castCooldown--;
        }
        if(domainCooldown > 0) {
            domainCooldown--;
        }
        if(leapCooldown > 0) {
            leapCooldown--;
        } else if(this.getTarget() != null && this.getTarget().position().distanceToSqr(this.position()) > 30D){
            performSpellSelf(this, nickLeapSpell, nickColor);
            leapCooldown = random.nextInt(200) + 200;
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

    public void performSpellSelf(LivingEntity entity, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

        resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));
    }

    private ParticleColor nickColor = new ParticleColor(0, 255, 0);

    public Spell nickLeapSpell = new Spell()
            .add(EffectLeap.INSTANCE)
            .add(AugmentAmplify.INSTANCE, 2)

            .withColor(nickColor);


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
        tag.putInt("cast", castCooldown);
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

        this.goalSelector.addGoal(1, new NickAttackGoalBB(this, 1.5D, true, () -> attackBBCooldown != 0));

        this.goalSelector.addGoal(2, new NickDomainGoal(this, 1.0D, 20f, () -> domainCooldown <= 0, 0, 24));
        this.goalSelector.addGoal(2, new NickCastingGoalA<>(this, 1.3D, 20f, () -> castCooldown <= 0, 0, 12));
        this.goalSelector.addGoal(3, new NickAttackGoalC(this, 1.0D, true, () -> attackCCooldown <= 0));
        this.goalSelector.addGoal(3, new NickAttackGoalBA(this, 1.0D, true, () -> attackBACooldown <= 0));
        this.goalSelector.addGoal(4, new NickAttackGoalA(this, 1.2D, true, () -> true));

        this.goalSelector.addGoal(7, new NickEntity.NickEntityRandomMoveGoal());
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, false));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 900D)
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

    // Goals and Movement
    class BossMoveControl extends MoveControl {
        public BossMoveControl(NickEntity pNick) {
            super(pNick);
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 $$0 = new Vec3(this.wantedX - NickEntity.this.getX(), this.wantedY - NickEntity.this.getY(), this.wantedZ - NickEntity.this.getZ());
                double $$1 = $$0.length();
                if ($$1 < NickEntity.this.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                    NickEntity.this.setDeltaMovement(NickEntity.this.getDeltaMovement().scale((double)0.5F));
                } else {
                    NickEntity.this.setDeltaMovement(NickEntity.this.getDeltaMovement().add($$0.scale(this.speedModifier * 0.05 / $$1)));
                    if (NickEntity.this.getTarget() == null) {
                        Vec3 $$2 = NickEntity.this.getDeltaMovement();
                        NickEntity.this.setYRot(-((float) Mth.atan2($$2.x, $$2.z)) * (180F / (float)Math.PI));
                        NickEntity.this.yBodyRot = NickEntity.this.getYRot();
                    } else {
                        double $$3 = NickEntity.this.getTarget().getX() - NickEntity.this.getX();
                        double $$4 = NickEntity.this.getTarget().getZ() - NickEntity.this.getZ();
                        NickEntity.this.setYRot(-((float)Mth.atan2($$3, $$4)) * (180F / (float)Math.PI));
                        NickEntity.this.yBodyRot = NickEntity.this.getYRot();
                    }
                }
            }
        }
    }

    class NickEntityRandomMoveGoal extends Goal {
        public NickEntityRandomMoveGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !NickEntity.this.getMoveControl().hasWanted() && NickEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos $$0 = NickEntity.this.blockPosition();

            for(int $$1 = 0; $$1 < 3; ++$$1) {
                BlockPos $$2 = $$0.offset(NickEntity.this.random.nextInt(15) - 7, NickEntity.this.random.nextInt(11) - 5, NickEntity.this.random.nextInt(15) - 7);
                if (NickEntity.this.level().isEmptyBlock($$2)) {
                    NickEntity.this.moveControl.setWantedPosition((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, (double)0.25F);
                    if (NickEntity.this.getTarget() == null) {
                        NickEntity.this.getLookControl().setLookAt((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    class NickAttackGoalA extends MeleeAttackGoal {
        private final NickEntity entity;

        private int totalAnimation = 20;
        private int attackDelay = 10;
        private int ticksUntilNextAttack = 10;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public NickAttackGoalA(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((NickEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 10;
            ticksUntilNextAttack = 10;

            LivingEntity $$0 = NickEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                NickEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, this.speedModifier);
            }
        }

        private ParticleColor NickColor = new ParticleColor(0, 255, 0);

        public Spell NickAttackSpell = new Spell()
                .add(EffectFracture.INSTANCE)
                .add(AugmentDurationDown.INSTANCE, 2)

                .add(EffectKnockback.INSTANCE)
                .add(AugmentAmplify.INSTANCE, 2)

                .withColor(NickColor);

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
                    entity.setAttackingA(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
                        performAttack(pEnemy);
                        if(!pEnemy.isBlocking()){
                            performSpellAttack(this.mob, 1.0F, NickAttackSpell, NickColor, pEnemy);
                        }
                    } else {
                        this.resetAttackLoopCooldown();
                        this.done = true;
                    }
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttackingA(false);
                entity.attackAAnimationTimeout = 0;
            }
        }

        private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
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
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 8.0F);
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
            entity.setAttackingA(false);
            this.done = false;
            super.stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity $$0 = NickEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = NickEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    NickEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, (double)this.speedModifier);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    class NickAttackGoalBA extends MeleeAttackGoal {
        private final NickEntity entity;

        private int totalAnimation = 20;
        private int attackDelay = 12;
        private int ticksUntilNextAttack = 12;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public NickAttackGoalBA(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((NickEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 12;
            ticksUntilNextAttack = 12;

            LivingEntity $$0 = NickEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                NickEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, this.speedModifier);
            }
        }

        private ParticleColor nickColor = new ParticleColor(0, 150, 0);

        public Spell nickAttackBASpell = new Spell()
                .add(EffectDispel.INSTANCE)
                .add(EffectLaunch.INSTANCE)
                .add(EffectLaunch.INSTANCE)
                .add(EffectSlowfall.INSTANCE)
                .add(AugmentDurationDown.INSTANCE,3)

                .add(EffectDelay.INSTANCE)
                .add(AugmentDurationDown.INSTANCE)
                .add(EffectSwapTarget.INSTANCE)

                .add(EffectLaunch.INSTANCE)

                .withColor(nickColor);

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
                    entity.setAttackingBA(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
                        performAttack(pEnemy);
                        if(!pEnemy.isBlocking()){
                            performSpellAttack(this.mob, 1.0F, nickAttackBASpell, nickColor, pEnemy);
                        }
                    } else {
                        this.resetAttackLoopCooldown();
                        this.done = true;
                    }
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttackingBA(false);
                entity.attackBAAnimationTimeout = 0;
            }
        }

        private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
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
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 8.0F);
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

            this.entity.attackBACooldown = random.nextInt(120) + 80;
            this.entity.attackBBCooldown = 100;
        }

        public void stop() {
            entity.setAttackingBA(false);
            this.done = false;
            super.stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity $$0 = NickEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = NickEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    NickEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, (double)this.speedModifier);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    class NickAttackGoalBB extends MeleeAttackGoal {
        private final NickEntity entity;

        private int totalAnimation = 15;
        private int attackDelay = 8;
        private int ticksUntilNextAttack = 8;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public NickAttackGoalBB(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((NickEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 8;
            ticksUntilNextAttack = 8;

            LivingEntity $$0 = NickEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                NickEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, this.speedModifier);
            }
        }

        private ParticleColor nickColor = new ParticleColor(0, 150, 0);

        public Spell nickAttackBBSpell = new Spell()
                .add(EffectDispel.INSTANCE)
                .add(EffectGravity.INSTANCE)
                .add(AugmentExtendTime.INSTANCE)
                .add(EffectSnare.INSTANCE)

                .add(EffectSwapTarget.INSTANCE)
                .add(EffectDispel.INSTANCE)

                .withColor(nickColor);

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
                    entity.setAttackingBB(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
                        performAttack(pEnemy);
                        if(!pEnemy.isBlocking()){
                            performSpellAttack(this.mob, 1.0F, nickAttackBBSpell, nickColor, pEnemy);
                        }
                    } else {
                        this.resetAttackLoopCooldown();
                        this.done = true;
                    }
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttackingBB(false);
                entity.attackBBAnimationTimeout = 0;
            }
        }

        private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
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
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 8.0F);
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

            this.entity.attackBBCooldown = 0;
        }

        public void stop() {
            entity.setAttackingBB(false);
            this.done = false;
            super.stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity $$0 = NickEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = NickEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    NickEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, (double)this.speedModifier);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    class NickAttackGoalC extends MeleeAttackGoal {
        private final NickEntity entity;

        private int totalAnimation = 26;
        private int attackDelay = 21;
        private int attackDelay2 = 13;
        private int ticksUntilNextAttack = 21;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public NickAttackGoalC(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((NickEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 21;
            attackDelay2 = 13;
            ticksUntilNextAttack = 21;

            LivingEntity $$0 = NickEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                NickEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, this.speedModifier);
            }
        }

        private ParticleColor nickColor = new ParticleColor(0, 150, 0);

        public Spell nickAttackCSpell(int AOE) {
            return new Spell()
                    .add(EffectBurst.INSTANCE)
                    .add(AugmentSensitive.INSTANCE)
                    .add(AugmentDampen.INSTANCE)
                    .add(AugmentAOE.INSTANCE, AOE + 1)
                    .add(EffectRaiseEarth.INSTANCE)
                    .add(AugmentSensitive.INSTANCE)
                    .add(AugmentAmplify.INSTANCE, 4)

                    .withColor(nickColor);
        }

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
                    entity.setAttackingC(true);
                }
                if(isTimeToStartAttack2()){
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
                        performAttack(pEnemy);
                        performSpellAttack(this.mob, nickColor);
                    }
                }
                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
                        performAttack(pEnemy);
                    }
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttackingC(false);
                entity.attackCAnimationTimeout = 0;
            }
        }

        private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
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

        protected boolean isTimeToStartAttack2() {
            return this.ticksUntilNextAttack <= attackDelay2;
        }

        protected boolean isTimeToStartAttackAnimation() {
            return this.ticksUntilNextAttack <= attackDelay;
        }

        public int getTicksUntilNextAttack() {
            return this.ticksUntilNextAttack;
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 8.0F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;

        }

        void performSpellAttack(LivingEntity entity, ParticleColor color){
            final int[] AOE = {1};
            int time = 50;

            AdamsArsPlus.setInterval(() -> {
                Spell spell = nickAttackCSpell(AOE[0]);
                EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
                resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));

                AOE[0] = AOE[0] + 1;
            }, 10, time, () -> !this.entity.isAlive());

            this.entity.attackCCooldown = random.nextInt(120) + 200;
        }

        public void stop() {
            entity.setAttackingC(false);
            this.done = false;
            super.stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity $$0 = NickEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = NickEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    NickEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, (double)this.speedModifier);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    public class NickCastingGoalA<T extends Mob & RangedAttackMob> extends Goal {
        NickEntity NickEntity;

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

        public NickCastingGoalA(NickEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.NickEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor  NickColor = new ParticleColor(0, 255, 0);

        public Spell NickCastSpell = new Spell()
                .add(AugmentAccelerateTwo.INSTANCE)

                .add(EffectBurst.INSTANCE)
                .add(AugmentAOEThree.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(EffectBreak.INSTANCE)
                .add(AugmentAmplifyTwo.INSTANCE)

                .add(EffectBurst.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(EffectRaiseEarth.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(AugmentAmplify.INSTANCE, 4)

                .withColor(NickColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            EntityProjectileSpell projectileSpell = new EntityProjectileSpell(entity.level(), resolver);
            projectileSpell.setColor(color);

            projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 1.0f, 0.8f);

            entity.level().addFreshEntity(projectileSpell);

            this.NickEntity.castCooldown = random.nextInt(200) + 80;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.NickEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.NickEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.NickEntity.setAggressive(true);
            attackDelay = 12;
            ticksUntilNextAttack = 12;

            LivingEntity $$0 = this.NickEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.NickEntity.moveControl.setWantedPosition($$1.x + NickEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + NickEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.NickEntity.setAttackingA(false);
            this.NickEntity.setAggressive(false);
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
            LivingEntity livingentity = this.NickEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.NickEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        NickEntity.setAttackingA(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.NickEntity, 1.0F, NickCastSpell, NickColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    NickEntity.setAttackingA(false);
                    NickEntity.attackAAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }

    }
}
