package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.entities.ai.*;
import com.adamsmods.adamsarsplus.entities.ai.pathfinding.AdvancedPathNavigate;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.*;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.*;
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
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.HOLY_LEGION_EFFECT;
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
    public int castingCCooldown;
    public int domainCooldown;

    public int recoverCooldown;

    private final ServerBossEvent bossEvent;

    public MattEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        this.moveControl = new MattEntity.BossMoveControl(this);
    }

    public MattEntity(Level pLevel){
        this(MATT_ENTITY.get(), pLevel);
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
        this.setNoGravity(true);

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
        if(castingCCooldown > 0) {
            castingCCooldown--;
        }
        if(domainCooldown > 0) {
            domainCooldown--;
        }
        if(recoverCooldown > 0) {
            recoverCooldown--;
        }
        else{
            performSpellSelf(this, mattRecoverSpell, mattColor);
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

    public void performSpellSelf(LivingEntity entity, Spell spell, ParticleColor color){
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
        tag.putInt("castingc", castingCCooldown);
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

            this.playSound(SoundEvents.SHIELD_BLOCK, 1.5F, 1F);

            if(source.getEntity() instanceof LivingEntity enemy && (source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK))){
                knockback(enemy, this, 0.3f);
            }

            return false;
        }

        return super.hurt(source, amount);
    }

    public void knockback(Entity target, LivingEntity shooter, float strength) {
        this.knockback(target, (double)strength, (double) Mth.sin(target.yRotO * ((float)Math.PI / 180F)), (double)(-Mth.cos(target.yRotO * ((float)Math.PI / 180F))));
    }

    public void knockback(Entity entity, double strength, double xRatio, double zRatio) {
        if (entity instanceof LivingEntity living) {
            strength *= (double)1.0F - living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            strength = Math.max(strength, 0.5);
        }

        if (strength > (double)0.0F) {
            entity.hasImpulse = true;
            Vec3 vec3 = entity.getDeltaMovement();
            Vec3 vec31 = (new Vec3(xRatio, (double)0.0F, zRatio)).normalize().scale(strength * -1);
            entity.setDeltaMovement(vec3.x / (double)2.0F - vec31.x, 0.4, vec3.z / (double)2.0F - vec31.z);
        }

        entity.hurtMarked = true;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.attackACooldown = tag.getInt("attacka");
        this.attackBCooldown = tag.getInt("attackb");
        this.blockCooldown = tag.getInt("block");
        this.castingCooldown = tag.getInt("casting");
        this.castingBCooldown = tag.getInt("castingb");
        this.castingCCooldown = tag.getInt("castingc");
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

        this.goalSelector.addGoal(1, new MattDomainGoal<>(this, 1.0D, 20f, () -> domainCooldown <= 0, 0, 15));
        this.goalSelector.addGoal(2, new MattCastingGoalA<>(this, 1.0D, 35f, () -> castingCooldown <= 0, 0, 12));
        this.goalSelector.addGoal(2, new MattCastingGoalB<>(this, 1.0D, 35f, () -> castingBCooldown <= 0, 0, 12));
        this.goalSelector.addGoal(2, new MattCastingGoalC<>(this, 1.0D, 35f, () -> castingCCooldown <= 0, 0, 12));

        this.goalSelector.addGoal(3, new MattAttackGoalB(this, 1.0D, true, () -> attackBCooldown <= 0));
        this.goalSelector.addGoal(3, new MattBlockGoal(this, 0.7D, true, () -> blockCooldown <= 0));

        this.goalSelector.addGoal(4, new MattAttackGoalA(this, 1.3D, true, () -> true));

        this.goalSelector.addGoal(7, new MattEntity.MattEntityRandomMoveGoal());
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, false));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1225D)
                .add(Attributes.ARMOR, 23D)
                .add(Attributes.ATTACK_DAMAGE, (double)20.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.4F)
                .add(Attributes.FLYING_SPEED, (double)0.4F)
                .add(Attributes.FOLLOW_RANGE, (double)150.0F)
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

    // Goals and Movement
    class BossMoveControl extends MoveControl {
        public BossMoveControl(MattEntity pMatt) {
            super(pMatt);
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 $$0 = new Vec3(this.wantedX - MattEntity.this.getX(), this.wantedY - MattEntity.this.getY(), this.wantedZ - MattEntity.this.getZ());
                double $$1 = $$0.length();
                if ($$1 < MattEntity.this.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                    MattEntity.this.setDeltaMovement(MattEntity.this.getDeltaMovement().scale((double)0.5F));
                } else {
                    MattEntity.this.setDeltaMovement(MattEntity.this.getDeltaMovement().add($$0.scale(this.speedModifier * 0.05 / $$1)));
                    if (MattEntity.this.getTarget() == null) {
                        Vec3 $$2 = MattEntity.this.getDeltaMovement();
                        MattEntity.this.setYRot(-((float) Mth.atan2($$2.x, $$2.z)) * (180F / (float)Math.PI));
                        MattEntity.this.yBodyRot = MattEntity.this.getYRot();
                    } else {
                        double $$3 = MattEntity.this.getTarget().getX() - MattEntity.this.getX();
                        double $$4 = MattEntity.this.getTarget().getZ() - MattEntity.this.getZ();
                        MattEntity.this.setYRot(-((float)Mth.atan2($$3, $$4)) * (180F / (float)Math.PI));
                        MattEntity.this.yBodyRot = MattEntity.this.getYRot();
                    }
                }
            }
        }
    }

    class MattEntityRandomMoveGoal extends Goal {
        public MattEntityRandomMoveGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !MattEntity.this.getMoveControl().hasWanted() && MattEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos $$0 = MattEntity.this.blockPosition();

            for(int $$1 = 0; $$1 < 3; ++$$1) {
                BlockPos $$2 = $$0.offset(MattEntity.this.random.nextInt(15) - 7, MattEntity.this.random.nextInt(11) - 5, MattEntity.this.random.nextInt(15) - 7);
                if (MattEntity.this.level().isEmptyBlock($$2)) {
                    MattEntity.this.moveControl.setWantedPosition((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, (double)0.25F);
                    if (MattEntity.this.getTarget() == null) {
                        MattEntity.this.getLookControl().setLookAt((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }

    class MattAttackGoalA extends MeleeAttackGoal {
        private final MattEntity entity;

        private int totalAnimation = 25;
        private int attackDelay = 18;
        private int ticksUntilNextAttack = 18;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public MattAttackGoalA(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((MattEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 18;
            ticksUntilNextAttack = 18;

            LivingEntity $$0 = MattEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                MattEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, speedModifier);
            }
        }

        private ParticleColor MattColor = new ParticleColor(255, 255, 0);

        public Spell MattAttackSpell = new Spell()
                .add(EffectConjureBlade.INSTANCE)
                .add(AugmentAOEThree.INSTANCE)
                .add(AugmentAmplifyThree.INSTANCE, 2)

                .withColor(MattColor);

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
                        //performSpellAttack(this.mob, MattAttackSpell, MattColor, pEnemy);
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
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 7.0F);
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
            entity.setAttackingA(false);
            this.done = false;
            super.stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity $$0 = MattEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = MattEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    MattEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, speedModifier);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    class MattAttackGoalB extends MeleeAttackGoal {
        private final MattEntity entity;

        private int totalAnimation = 20;
        private int attackDelay = 16;
        private int ticksUntilNextAttack = 16;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public MattAttackGoalB(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((MattEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 16;
            ticksUntilNextAttack = 16;

            LivingEntity $$0 = MattEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                MattEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, speedModifier);
            }
        }

        private ParticleColor MattColor = new ParticleColor(255, 255, 0);

        public Spell MattAttackSpell = new Spell()
                .add(EffectWither.INSTANCE)
                .add(AugmentAmplify.INSTANCE,3)
                .add(EffectHarm.INSTANCE)
                .add(AugmentAmplify.INSTANCE,3)
                .add(AugmentExtendTimeTwo.INSTANCE)

                .add(EffectHex.INSTANCE)
                .add(AugmentExtendTimeTwo.INSTANCE)

                .withColor(MattColor);

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
                    if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
                        performAttack(pEnemy);
                        if(!pEnemy.isBlocking()){
                            performSpellAttack(this.mob, MattAttackSpell, MattColor, pEnemy);
                        }
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
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 9.0F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;

            this.entity.attackBCooldown = random.nextInt(200) + 100;
        }

        void performSpellAttack(LivingEntity entity, Spell spell, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));
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
            LivingEntity $$0 = MattEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = MattEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    MattEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, speedModifier);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    class MattBlockGoal extends Goal {
        private int ticksUntilNextAttack = 100;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public MattBlockGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
            this.canUse = canUse;
        }

        public boolean canUse() {
            return this.canUse.get();
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !MattEntity.this.getNavigation().isDone()) && !this.done;
        }

        public void tick() {
            BlockPos $$0 = MattEntity.this.blockPosition();

            for(int $$1 = 0; $$1 < 3; ++$$1) {
                BlockPos $$2 = $$0.offset(MattEntity.this.random.nextInt(15) - 7, MattEntity.this.random.nextInt(11) - 5, MattEntity.this.random.nextInt(15) - 7);
                if (MattEntity.this.level().isEmptyBlock($$2)) {
                    MattEntity.this.moveControl.setWantedPosition((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, (double)0.25F);
                    if (MattEntity.this.getTarget() == null) {
                        MattEntity.this.getLookControl().setLookAt((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, 180.0F, 20.0F);
                    }
                    break;
                }
            }

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);

            if(this.ticksUntilNextAttack > 0){
                MattEntity.this.setBlocking(true);
            } else {
                MattEntity.this.setBlocking(false);
                this.done = true;
                MattEntity.this.blockCooldown = random.nextInt(100) + 30;
            }
        }

        public void start() {
            super.start();
            MattEntity.this.setAggressive(true);
        }

        public void stop() {
            super.stop();
            MattEntity.this.setBlocking(false);
            MattEntity.this.setAggressive(false);
            this.done = false;
        }
    }

    public class MattCastingGoalA<T extends Mob & RangedAttackMob> extends Goal {
        MattEntity MattEntity;

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

        public MattCastingGoalA(MattEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.MattEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor  MattColor = new ParticleColor(255, 255, 0);

        public Spell MattCastSpell = new Spell()
                .add(EffectConjureBlade.INSTANCE)
                .add(AugmentAOEThree.INSTANCE, 4)
                .add(AugmentAmplifyThree.INSTANCE, 16)

                .add(EffectBurst.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(AugmentAOE.INSTANCE, 2)
                .add(AugmentAmplifyThree.INSTANCE, 1)

                .withColor(MattColor);

        void performCastAttack(LivingEntity entity, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            EntityProjectileSpell projectileSpell = new EntityProjectileSpell(entity.level(), resolver);
            projectileSpell.setColor(color);

            projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 1.0f, 0.8f);

            entity.level().addFreshEntity(projectileSpell);

            this.MattEntity.castingCooldown = random.nextInt(180) + 200;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.MattEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.MattEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.MattEntity.setAggressive(true);
            attackDelay = 12;
            ticksUntilNextAttack = 12;

            LivingEntity $$0 = this.MattEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.MattEntity.moveControl.setWantedPosition($$1.x + MattEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + MattEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.MattEntity.setCasting(false);
            this.MattEntity.setAggressive(false);
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
            LivingEntity livingentity = this.MattEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.MattEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        MattEntity.setCasting(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.MattEntity, MattCastSpell, MattColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    MattEntity.setCasting(false);
                    MattEntity.castingAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }
    }

    public class MattCastingGoalB<T extends Mob & RangedAttackMob> extends Goal {
        MattEntity MattEntity;

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

        public MattCastingGoalB(MattEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.MattEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor  MattColor = new ParticleColor(255, 255, 0);

        public Spell MattCastSpell = new Spell()
                .add(EffectDelay.INSTANCE)
                .add(AugmentExtendTime.INSTANCE, 2)

                .add(EffectSummonUndead_boss.INSTANCE)
                .add(AugmentSplit.INSTANCE, 2)
                .add(AugmentAmplify.INSTANCE,4)

                .add(EffectWither.INSTANCE)
                .add(AugmentExtendTime.INSTANCE, 2)
                .add(EffectHex.INSTANCE)
                .add(AugmentExtendTime.INSTANCE, 2)

                .add(EffectFangs.INSTANCE)
                .add(AugmentAmplifyThree.INSTANCE)

                .withColor(MattColor);

        void performCastAttack(LivingEntity entity, Spell spell, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new BlockHitResult(enemy.position(), entity.getDirection(), enemy.blockPosition(), true));

            this.MattEntity.castingBCooldown = random.nextInt(280) + 300;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.MattEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.MattEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.MattEntity.setAggressive(true);
            attackDelay = 12;
            ticksUntilNextAttack = 12;

            LivingEntity $$0 = this.MattEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.MattEntity.moveControl.setWantedPosition($$1.x + MattEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + MattEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.MattEntity.setCastingB(false);
            this.MattEntity.setAggressive(false);
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
            LivingEntity livingentity = this.MattEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.MattEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        MattEntity.setCastingB(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.MattEntity, MattCastSpell, MattColor, livingentity);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    MattEntity.setCastingB(false);
                    MattEntity.castingBAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }
    }

    public class MattCastingGoalC<T extends Mob & RangedAttackMob> extends Goal {
        MattEntity MattEntity;

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

        public MattCastingGoalC(MattEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.MattEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.attackRadiusSqr = attackRange * attackRange;

        }
        private ParticleColor  MattColor = new ParticleColor(255, 255, 0);

        public Spell MattCastSpell = new Spell()
                .add(EffectMeteorSwarm.INSTANCE)
                .add(AugmentAOEThree.INSTANCE)

                .add(EffectDivineSmite.INSTANCE)
                .add(AugmentAmplify.INSTANCE, 4)

                .withColor(MattColor);

        void performCastAttack(LivingEntity entity, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            this.MattEntity.castingCCooldown = random.nextInt(400) + 260;
            int time = 100;

            Vec3 pos = this.MattEntity.position();

            AdamsArsPlus.setInterval(() -> {

                DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
                projectileSpell.setColor(color);
                projectileSpell.shoot(entity, 90, 0, 0.0F, 0.5f, 0.8f);
                projectileSpell.setPos(pos.add(MattEntity.this.random.nextInt(19) - 9, 0, MattEntity.this.random.nextInt(19) - 9));
                entity.level().addFreshEntity(projectileSpell);

            }, 10, time, () -> !this.MattEntity.isAlive());
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.MattEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.MattEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.MattEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 15;

            LivingEntity $$0 = this.MattEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.MattEntity.moveControl.setWantedPosition($$1.x + MattEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + MattEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.MattEntity.setUsingDomain(false);
            this.MattEntity.setAggressive(false);
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
            LivingEntity livingentity = this.MattEntity.getTarget();
            if (livingentity != null) {
                double d0 = this.MattEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean canSeeEnemy = this.MattEntity.getSensing().hasLineOfSight(livingentity);
                if (canSeeEnemy != this.seeTime > 0) {
                    this.seeTime = 0;
                }

                if (canSeeEnemy) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.MattEntity.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.MattEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 10) {
                    if ((double)this.MattEntity.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.MattEntity.getRandom().nextFloat() < 0.3) {
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

                    this.MattEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.MattEntity.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.MattEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if (this.seeTime >= 20 && !this.hasAnimated) {
                    this.hasAnimated = true;
                    Networking.sendToNearby(this.MattEntity.level(), this.MattEntity, new PacketAnimEntity(this.MattEntity.getId(), this.animId));
                }

                if (this.hasAnimated) {
                    shouldCountTillNextAttack = true;
                    this.MattEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        MattEntity.setUsingDomain(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.MattEntity, MattCastSpell, MattColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    MattEntity.setUsingDomain(false);
                    MattEntity.castDomainAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }


}
