package adamsmods.adamsarsplus.common.entity.custom;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.entity.DetonateProjectile;
import adamsmods.adamsarsplus.common.glyphs.augment_glyph.*;
import adamsmods.adamsarsplus.common.glyphs.effect_glyph.*;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
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

import static adamsmods.adamsarsplus.registry.ModEntities.JOSH_ENTITY;

public class JoshEntity extends Monster implements RangedAttackMob {

    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(JoshEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> BLOCKING =
            SynchedEntityData.defineId(JoshEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING =
            SynchedEntityData.defineId(JoshEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> NOT_CASTING =
            SynchedEntityData.defineId(JoshEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_DOMAIN =
            SynchedEntityData.defineId(JoshEntity.class, EntityDataSerializers.BOOLEAN);

    public int attackCooldown;
    public int blockCooldown;
    public int castingCooldown;
    public int castingBCooldown;
    public int castingCCooldown;
    public int domainCooldown;

    public int shooting;
    public int blockCount;

    private final ServerBossEvent bossEvent;

    public JoshEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        this.moveControl = new BossMoveControl(this);
    }

    public JoshEntity(Level pLevel){
        this(JOSH_ENTITY.get(), pLevel);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    public final AnimationState blockAnimationState = new AnimationState();
    public int blockAnimationTimeout = 0;

    public final AnimationState castingAnimationState = new AnimationState();
    public int castingAnimationTimeout = 0;

    public final AnimationState notCastingAnimationState = new AnimationState();
    public int notCastingAnimationTimeout = 0;

    public final AnimationState castDomainAnimationState = new AnimationState();
    public int castDomainAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();
        this.setNoGravity(true);

        if(attackCooldown > 0) {
            attackCooldown--;
        }
        if(blockCooldown > 0) {
            blockCooldown--;
        } else {
            if(blockCount < 5){
                blockCount++;
                this.blockCooldown = random.nextInt(100) + 30;
            }
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
        if(shooting > 0){
            shooting--;
        }

        if(this.level().isClientSide()) {
            setupAnimationStates();
        } else {
            // Animation conditions
            this.setBlocking(this.blockCount > 0 && !this.isAttacking());
            this.setCasting((this.shooting > 0 || this.castingCooldown <= 0 || (this.castingBCooldown <= 0) || (this.castingCCooldown <= 0)) && !this.isAttacking());
            this.setNotCasting(!this.isCasting() && !this.isAttacking());
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

        //Attack Animation control
        if(this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 20;
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }
        if(!this.isAttacking()) {
            attackAnimationState.stop();
        }

        //Block Animation control
        if(this.isBlock()) {
            blockAnimationState.start(this.tickCount);
        }
        if(!this.isBlock()) {
            blockAnimationState.stop();
        }

        //Casting Animation control
        if(this.isCasting()) {
            castingAnimationState.start(this.tickCount);
        }
        if(!this.isCasting()) {
            castingAnimationState.stop();
        }

        //Not Casting Animation control
        if(this.isNotCasting()) {
            notCastingAnimationState.start(this.tickCount);
        }
        if(!this.isNotCasting()) {
            notCastingAnimationState.stop();
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
        } else if(this.isAttacking()){
            f = 0f;
        } else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    private ParticleColor joshColor = new ParticleColor(0, 75, 0);

    public void setAttacking(boolean attackingA) { this.entityData.set(ATTACKING, attackingA); }
    public boolean isAttacking(){ return this.entityData.get(ATTACKING); }

    public void setBlocking(boolean blocking) { this.entityData.set(BLOCKING, blocking); }
    public boolean isBlock(){ return this.entityData.get(BLOCKING); }

    public void setCasting(boolean casting) { this.entityData.set(CASTING, casting); }
    public boolean isCasting(){ return this.entityData.get(CASTING); }

    public void setNotCasting(boolean casting) { this.entityData.set(CASTING, casting); }
    public boolean isNotCasting(){ return this.entityData.get(CASTING); }

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
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(ATTACKING, false);
        pBuilder.define(BLOCKING, false);
        pBuilder.define(CASTING, false);
        pBuilder.define(NOT_CASTING, false);
        pBuilder.define(CASTING_DOMAIN, false);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.LAVA) || source.is(DamageTypes.FALL))
            return false;

        if(this.isBlock()){
            blockCount--;

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

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("attack", attackCooldown);
        tag.putInt("block", blockCooldown);
        tag.putInt("blockC", blockCount);
        tag.putInt("casting", castingCooldown);
        tag.putInt("castingb", castingBCooldown);
        tag.putInt("castingc", castingCCooldown);
        tag.putInt("domain", domainCooldown);

    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.attackCooldown = tag.getInt("attacka");
        this.blockCooldown = tag.getInt("block");
        this.blockCount = tag.getInt("blockC");
        this.castingCooldown = tag.getInt("casting");
        this.castingBCooldown = tag.getInt("castingb");
        this.castingCCooldown = tag.getInt("castingc");
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

        this.goalSelector.addGoal(1, new JoshDomainGoal<>(this, 0.9d, 50f, ()-> this.domainCooldown <= 0, 0, 0));

        this.goalSelector.addGoal(2, new JoshCastingGoalC<>(this, 0.9d, 50f, ()-> this.castingCCooldown <= 0, 0, 0));
        this.goalSelector.addGoal(3, new JoshCastingGoalB<>(this, 0.9d, 50f, ()-> this.castingBCooldown <= 0, 0, 0));
        this.goalSelector.addGoal(4, new JoshCastingGoalA<>(this, 0.9d, 50f, ()-> this.castingCooldown <= 0, 0, 0));

        this.goalSelector.addGoal(5, new JoshAttackGoal(this, 1.2d, true, ()-> !this.isCasting()));

        this.goalSelector.addGoal(7, new JoshEntityRandomMoveGoal());
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, false));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1800D)
                .add(Attributes.ARMOR, 23D)
                .add(Attributes.ATTACK_DAMAGE, (double)20.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.4F)
                .add(Attributes.FLYING_SPEED, (double)0.3F)
                .add(Attributes.FOLLOW_RANGE, (double)150.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)1.5F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double)1F)
                .add(Attributes.ARMOR_TOUGHNESS, (double) 15D);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(pLevel.getRandom(), pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
    }
    @Override
    public void performRangedAttack(LivingEntity entity, float p_82196_2_) {

    }

    // Goals and Movement
    class BossMoveControl extends MoveControl {
        public BossMoveControl(JoshEntity pJosh) {
            super(pJosh);
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 $$0 = new Vec3(this.wantedX - JoshEntity.this.getX(), this.wantedY - JoshEntity.this.getY(), this.wantedZ - JoshEntity.this.getZ());
                double $$1 = $$0.length();
                if ($$1 < JoshEntity.this.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                    JoshEntity.this.setDeltaMovement(JoshEntity.this.getDeltaMovement().scale((double)0.5F));
                } else {
                    JoshEntity.this.setDeltaMovement(JoshEntity.this.getDeltaMovement().add($$0.scale(this.speedModifier * 0.05 / $$1)));
                    if (JoshEntity.this.getTarget() == null) {
                        Vec3 $$2 = JoshEntity.this.getDeltaMovement();
                        JoshEntity.this.setYRot(-((float) Mth.atan2($$2.x, $$2.z)) * (180F / (float)Math.PI));
                        JoshEntity.this.yBodyRot = JoshEntity.this.getYRot();
                    } else {
                        double $$3 = JoshEntity.this.getTarget().getX() - JoshEntity.this.getX();
                        double $$4 = JoshEntity.this.getTarget().getZ() - JoshEntity.this.getZ();
                        JoshEntity.this.setYRot(-((float)Mth.atan2($$3, $$4)) * (180F / (float)Math.PI));
                        JoshEntity.this.yBodyRot = JoshEntity.this.getYRot();
                    }
                }
            }
        }
    }

    class JoshEntityRandomMoveGoal extends Goal {
        public JoshEntityRandomMoveGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !JoshEntity.this.getMoveControl().hasWanted() && JoshEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos $$0 = JoshEntity.this.blockPosition();

            for(int $$1 = 0; $$1 < 3; ++$$1) {
                BlockPos $$2 = $$0.offset(JoshEntity.this.random.nextInt(15) - 7, JoshEntity.this.random.nextInt(11) - 5, JoshEntity.this.random.nextInt(15) - 7);
                if (JoshEntity.this.level().isEmptyBlock($$2)) {
                    JoshEntity.this.moveControl.setWantedPosition((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, (double)0.25F);
                    if (JoshEntity.this.getTarget() == null) {
                        JoshEntity.this.getLookControl().setLookAt((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }

    class JoshAttackGoal extends MeleeAttackGoal {
        private final JoshEntity entity;

        private int totalAnimation = 20;
        private int attackDelay = 14;
        private int ticksUntilNextAttack = 14;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public JoshAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((JoshEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 14;
            ticksUntilNextAttack = 14;

            LivingEntity $$0 = JoshEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                JoshEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, speedModifier);
            }
        }

        private ParticleColor JoshColor = new ParticleColor(0, 100, 0);

        public Spell JoshAttackSpell = new Spell()
                .add(EffectKnockback.INSTANCE)
                .add(AugmentAmplifyThree.INSTANCE, 1)
                .add(EffectFracture.INSTANCE)

                .withColor(JoshColor);

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy) {
            if (this.canPerformAttack(pEnemy)) {
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    entity.setAttacking(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    if (this.canPerformAttack(pEnemy)) {
                        performJoshAttack(pEnemy);
                        performSpellAttack(this.mob, JoshAttackSpell, JoshColor, pEnemy);
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

        protected void performJoshAttack(LivingEntity pEnemy) {
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
            entity.setAttacking(false);
            this.done = false;
            super.stop();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity $$0 = JoshEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = JoshEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    JoshEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, speedModifier);
                }

                this.checkAndPerformAttack($$0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    public class JoshCastingGoalA<T extends Mob & RangedAttackMob> extends Goal {
        JoshEntity JoshEntity;

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

        public JoshCastingGoalA(JoshEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.JoshEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor  JoshColor = new ParticleColor(0, 100, 0);

        public Spell JoshCastSpell = new Spell()
                .add(EffectConjureArrow.INSTANCE)
                .add(AugmentAccelerateThree.INSTANCE)
                .add(AugmentAmplify.INSTANCE, 10)
                .add(AugmentSplit.INSTANCE, 2)

                .withColor(JoshColor);

        void performCastAttack(LivingEntity entity, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            this.JoshEntity.castingCooldown = 300;
            int time = 150;

            AdamsArsPlus.setInterval(() -> {

                JoshEntity.shooting = 10;

                if(JoshEntity.getTarget() != null){
                    this.JoshEntity.getLookControl().setLookAt(JoshEntity.getTarget());
                }

                if(!JoshEntity.isAttacking()){
                    resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));
                }

            }, 5, time, () -> !this.JoshEntity.isAlive());
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.JoshEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.JoshEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.JoshEntity.setAggressive(true);
            attackDelay = 12;
            ticksUntilNextAttack = 12;

            LivingEntity $$0 = this.JoshEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.JoshEntity.moveControl.setWantedPosition($$1.x + JoshEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + JoshEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.JoshEntity.setAggressive(false);
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
            LivingEntity livingentity = this.JoshEntity.getTarget();
            if (livingentity != null) {
                shouldCountTillNextAttack = true;
                this.JoshEntity.getLookControl().setLookAt(livingentity);

                if(isTimeToAttack()) {
                    performCastAttack(this.JoshEntity, JoshCastSpell, JoshColor);
                    this.done = true;
                }

            }
            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    public class JoshCastingGoalB<T extends Mob & RangedAttackMob> extends Goal {
        JoshEntity JoshEntity;

        private final double speedModifier;
        boolean hasAnimated;
        int animatedTicks;
        int delayTicks;
        int animId;
        boolean done;

        Supplier<Boolean> canUse;

        private int attackDelay = 20;
        private int ticksUntilNextAttack = 20;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public JoshCastingGoalB(JoshEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.JoshEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor  JoshColor = new ParticleColor(255, 0, 0);

        public Spell JoshCastSpell = new Spell()
                .add(EffectIgnite.INSTANCE)
                .add(EffectFlare.INSTANCE)
                .add(AugmentAmplify.INSTANCE)

                .add(EffectExplosion.INSTANCE)
                .add(AugmentAmplify.INSTANCE,16)
                .add(AugmentAOEThree.INSTANCE)

                .add(EffectBurst.INSTANCE)
                .add(AugmentAOEThree.INSTANCE, 2)
                .add(AugmentSensitive.INSTANCE)
                .add(EffectBreak.INSTANCE)
                .add(AugmentAmplifyTwo.INSTANCE)
                .add(EffectIgnite.INSTANCE)
                .add(EffectEvaporate.INSTANCE)

                .withColor(JoshColor);

        void performCastAttack(LivingEntity entity, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);

            projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 1.0f, 0.8f);

            entity.level().addFreshEntity(projectileSpell);

            this.JoshEntity.castingBCooldown = random.nextInt(300) + 150;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.JoshEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.JoshEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.JoshEntity.setAggressive(true);
            attackDelay = 20;
            ticksUntilNextAttack = 20;

            LivingEntity $$0 = this.JoshEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.JoshEntity.moveControl.setWantedPosition($$1.x + JoshEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + JoshEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.JoshEntity.setAggressive(false);
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
            LivingEntity livingentity = this.JoshEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.JoshEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {

                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.JoshEntity, JoshCastSpell, JoshColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    JoshEntity.castingAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }
    }

    public class JoshCastingGoalC<T extends Mob & RangedAttackMob> extends Goal {
        JoshEntity JoshEntity;

        private final double speedModifier;
        boolean hasAnimated;
        int animatedTicks;
        int delayTicks;
        int animId;
        boolean done;

        Supplier<Boolean> canUse;

        private int attackDelay = 20;
        private int ticksUntilNextAttack = 20;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public JoshCastingGoalC(JoshEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.JoshEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor  JoshColor = new ParticleColor(255, 255, 255);

        public Spell JoshCastSpell = new Spell()
                .add(EffectBurst.INSTANCE)
                .add(AugmentAOE.INSTANCE)

                .add(EffectFirework.INSTANCE)
                .add(AugmentAmplifyThree.INSTANCE, 8)

                .withColor(JoshColor);

        void performCastAttack(LivingEntity entity, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);

            projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 2.0f, 0.8f);

            entity.level().addFreshEntity(projectileSpell);

            this.JoshEntity.castingCCooldown = random.nextInt(300) + 300;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.JoshEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.JoshEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.JoshEntity.setAggressive(true);
            attackDelay = 20;
            ticksUntilNextAttack = 20;

            LivingEntity $$0 = this.JoshEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.JoshEntity.moveControl.setWantedPosition($$1.x + JoshEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + JoshEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.JoshEntity.setAggressive(false);
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
            LivingEntity livingentity = this.JoshEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.JoshEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {

                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.JoshEntity, JoshCastSpell, JoshColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    JoshEntity.castingAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }
    }

    public class JoshDomainGoal<T extends Mob & RangedAttackMob> extends Goal {
        JoshEntity JoshEntity;

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

        private int attackDelay = 10;
        private int ticksUntilNextAttack = 10;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public JoshDomainGoal(JoshEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.JoshEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.attackRadiusSqr = attackRange * attackRange;

        }
        private ParticleColor JoshColor = new ParticleColor(255, 150, 0);

        public Spell JoshDomainSpell = new Spell()
                .add(AugmentAccelerateThree.INSTANCE)
                .add(EffectDomain.INSTANCE)
                .add(AugmentExtendTimeThree.INSTANCE)
                .add(AugmentAOEThree.INSTANCE, 3)
                .add(AugmentExtract.INSTANCE)
                .add(AugmentAccelerateThree.INSTANCE, 2)

                .add(EffectSwapTarget.INSTANCE)
                .add(EffectHeal.INSTANCE)

                .withColor(JoshColor);

        void performDomainAttack(LivingEntity entity, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));

            this.JoshEntity.domainCooldown = random.nextInt(2000) + 1000;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.JoshEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.JoshEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.JoshEntity.setAggressive(true);
            attackDelay = 10;
            ticksUntilNextAttack = 10;

            LivingEntity $$0 = this.JoshEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.JoshEntity.moveControl.setWantedPosition($$1.x + JoshEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + JoshEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.JoshEntity.setUsingDomain(false);
            this.JoshEntity.setAggressive(false);
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
            LivingEntity livingentity = this.JoshEntity.getTarget();
            if (livingentity != null) {
                double d0 = this.JoshEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean canSeeEnemy = this.JoshEntity.getSensing().hasLineOfSight(livingentity);
                if (canSeeEnemy != this.seeTime > 0) {
                    this.seeTime = 0;
                }

                if (canSeeEnemy) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.JoshEntity.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.JoshEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 10) {
                    if ((double)this.JoshEntity.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.JoshEntity.getRandom().nextFloat() < 0.3) {
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

                    this.JoshEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.JoshEntity.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.JoshEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if (this.seeTime >= 20 && !this.hasAnimated) {
                    this.hasAnimated = true;
                }

                if (this.hasAnimated) {
                    shouldCountTillNextAttack = true;
                    this.JoshEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        JoshEntity.setUsingDomain(true);
                    }

                    if(isTimeToAttack()) {
                        performDomainAttack(this.JoshEntity, JoshDomainSpell, JoshColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    JoshEntity.setUsingDomain(false);
                    JoshEntity.castDomainAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }
}
