package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.registry.AdamCapabilityRegistry;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.IFollowingSummon;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectDelay;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLightning;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.monster.Phantom;

import java.util.*;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.TENSHADOWS_EFFECT;
import static java.lang.Math.PI;

public class NueEntity extends FlyingMob implements IFollowingSummon, ISummon {
    private LivingEntity owner;
    // Ten Shadows Reward
    public boolean ritualStatus;
    public LivingEntity[] attackersList = {null, null};

    public static final EntityDataAccessor<Boolean> IDLE =
            SynchedEntityData.defineId(NueEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> FLYING =
            SynchedEntityData.defineId(NueEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(NueEntity.class, EntityDataSerializers.BOOLEAN);

    public int ticksLeft;
    public boolean isSummon;

    Vec3 moveTargetPoint;
    BlockPos anchorPoint;
    AttackPhase attackPhase;

    public NueEntity(Level level, LivingEntity owner, boolean summon) {
        super((EntityType) AdamsModEntities.NUE.get(), level);

        this.owner = owner;
        this.setOwnerID(owner.getUUID());
        this.isSummon = summon;
        this.ritualStatus = false;

        this.moveTargetPoint = Vec3.ZERO;
        this.anchorPoint = BlockPos.ZERO;
        this.attackPhase = NueEntity.AttackPhase.CIRCLE;
        this.moveControl = new NueMoveControl(this);
        this.lookControl = new NueLookControl(this);
    }

    public NueEntity(Level level, boolean summon) {
        super((EntityType) AdamsModEntities.NUE.get(), level);

        this.isSummon = summon;
        this.ritualStatus = false;

        this.moveTargetPoint = Vec3.ZERO;
        this.anchorPoint = BlockPos.ZERO;
        this.attackPhase = NueEntity.AttackPhase.CIRCLE;
        this.moveControl = new NueMoveControl(this);
        this.lookControl = new NueLookControl(this);
    }

    public NueEntity(EntityType<? extends FlyingMob> type, Level worldIn) {
        super(type, worldIn);

        this.moveTargetPoint = Vec3.ZERO;
        this.anchorPoint = BlockPos.ZERO;
        this.attackPhase = NueEntity.AttackPhase.CIRCLE;
        this.moveControl = new NueMoveControl(this);
        this.lookControl = new NueLookControl(this);
    }

    public EntityType<?> getType() {
        return (EntityType)AdamsModEntities.NUE.get();
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState flyAnimationState = new AnimationState();
    public int flyAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    public void setIdle(boolean idle) { this.entityData.set(IDLE, idle); }
    public boolean isIdle(){ return this.entityData.get(IDLE); }

    public void setFlying(boolean flying) { this.entityData.set(FLYING, flying); }
    public boolean isFlying(){ return this.entityData.get(FLYING); }

    public void setAttacking(boolean attacking) { this.entityData.set(ATTACKING, attacking); }
    public boolean isAttacking(){ return this.entityData.get(ATTACKING); }

    @Override
    public void tick() {
        super.tick();

        if(this.getSummoner() != null) {
            if (!this.level().isClientSide && this.isSummon && !this.getSummoner().hasEffect(TENSHADOWS_EFFECT.get())) {
                spawnShadowPoof((ServerLevel) this.level(), this.blockPosition());
                this.remove(RemovalReason.DISCARDED);
                this.onSummonDeath(this.level(), (DamageSource) null, true);
            }
        }

        if(this.level().isClientSide()) {
            setupAnimationStates();
        }

    }

    private void setupAnimationStates() {

        this.setIdle(false);

        if (this.isIdle() && this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        if(!this.isIdle()){
            this.idleAnimationState.stop();
        }
        //Attack Animation control
        if(this.isAttacking()){
            if(this.attackAnimationTimeout <= 0){
                attackAnimationState.start(this.tickCount);
                this.attackAnimationTimeout = 25;
            } else {
                this.attackAnimationTimeout = Math.max(1, this.attackAnimationTimeout - 1);

                if(this.attackAnimationTimeout == 1){
                    this.setAttacking(false);
                    this.attackAnimationTimeout = 0;
                }
            }
        } else {
            attackAnimationState.stop();
        }

        // Flying Animation control
        if(this.isFlying() && !this.isAttacking()) {
            if(this.flyAnimationTimeout <= 0){
                this.flyAnimationTimeout = 20;
                flyAnimationState.start(this.tickCount);
            } else {
                this.flyAnimationTimeout--;
            }

        } else {
            flyAnimationState.stop();
        }

    }

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(NueEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UUID, Optional.of(Util.NIL_UUID));
        this.entityData.define(FLYING, false);
        this.entityData.define(ATTACKING, false);
        this.entityData.define(IDLE, false);
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        onSummonDeath(level(), cause, false);

        if(!this.ritualStatus && !this.isSummon){
            if(this.attackersList[0] instanceof Player player){
                AdamCapabilityRegistry.getTsTier(player).ifPresent((pRank) -> {
                    pRank.setTsTier(Math.max(1, pRank.getTsTier()));
                });

                PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.tenshadows.nue_tamed"));
            }
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if(pSource.is(DamageTypes.LIGHTNING_BOLT)){
            this.clearFire();
            return false;
        }

        if (pSource.is(DamageTypes.MOB_ATTACK)) {
            Entity var4 = pSource.getEntity();
            if (var4 instanceof ISummon) {
                ISummon summon = (ISummon)var4;
                if (summon.getOwnerUUID() != null && summon.getOwnerUUID().equals(this.getOwnerUUID())) {
                    return false;
                }
            }
        }


        if(!this.ritualStatus){ this.ritualStatus = isRitualFailed(pSource.getEntity()); }

        return super.hurt(pSource, pAmount);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    public Team getTeam() {
        return this.getSummoner() != null ? this.getSummoner().getTeam() : super.getTeam();
    }

    public boolean isAlliedTo(Entity pEntity) {
        LivingEntity summoner = this.getSummoner();
        if (summoner != null) {
            if (pEntity instanceof ISummon) {
                ISummon summon = (ISummon)pEntity;
                if (summon.getOwnerUUID() != null && summon.getOwnerUUID().equals(this.getOwnerUUID())) {
                    return true;
                }
            }

            return pEntity == summoner || summoner.isAlliedTo(pEntity);
        } else {
            return super.isAlliedTo(pEntity);
        }
    }

    public Level getWorld() {
        return this.level();
    }

    public PathNavigation getPathNav() {
        return this.navigation;
    }

    public Mob getSelfEntity() {
        return this;
    }

    public LivingEntity getSummoner() {
        return this.getOwnerFromID();
    }

    public LivingEntity getActualOwner() {
        return this.owner;
    }

    public int getExperienceReward() {
        return 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("left", ticksLeft);
        compound.putBoolean("Summon", isSummon);
        compound.putBoolean("Failed", ritualStatus);

        if (this.getOwnerUUID() == null) {
            compound.putUUID("OwnerUUID", Util.NIL_UUID);
        } else {
            compound.putUUID("OwnerUUID", this.getOwnerUUID());
        }
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        this.setOwnerID(compound.getUUID("OwnerUUID"));
        this.owner = this.getOwnerFromID();
        this.ticksLeft = compound.getInt("left");
        this.isSummon = compound.getBoolean("Summon");
        this.ritualStatus = compound.getBoolean("Failed");
    }

    public LivingEntity getOwnerFromID() {
        try {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : this.level().getPlayerByUUID(uuid);
        } catch (IllegalArgumentException var21) {
            return null;
        }
    }

    @Override
    public int getTicksLeft() {
        return ticksLeft;
    }

    @Override
    public void setTicksLeft(int ticks) {
        this.ticksLeft = ticks;
    }

    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(1, new NueAttackStrategyGoal());
        this.goalSelector.addGoal(2, new NueSweepAttackGoal());
        this.goalSelector.addGoal(3, new NueFollowSummonerGoal());
        this.goalSelector.addGoal(4, new NueCircleAroundAnchorGoal());

        this.targetSelector.addGoal(1, new NueAttackPlayerTargetGoal());
        this.targetSelector.addGoal(1, new CopyOwnerTargetGoalFlying<>(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Villager.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 35D)
                .add(Attributes.ATTACK_DAMAGE, (double)7.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.FLYING_SPEED, (double)0.5F)
                .add(Attributes.FOLLOW_RANGE, (double)70.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double)1.0F);
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.getEntityData().get(OWNER_UUID).isEmpty() ? this.getUUID() : this.getEntityData().get(OWNER_UUID).get();
    }

    @Override
    public void setOwnerID(UUID uuid) {
        this.getEntityData().set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.PHANTOM_FLAP, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
            return SoundEvents.PHANTOM_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PHANTOM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PHANTOM_DEATH;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    public static void spawnShadowPoof(ServerLevel world, BlockPos pos) {
        for(int i = 0; i < 10; ++i) {
            double d0 = (double)pos.getX() + (double)0.5F;
            double d1 = (double)pos.getY() + 1.2;
            double d2 = (double)pos.getZ() + (double)0.5F;
            world.sendParticles(ParticleTypes.SQUID_INK, d0, d1, d2, 2, ((double)(world.random.nextFloat() * 1.0F) - (double)0.5F) / (double)3.0F, ((double)(world.random.nextFloat() * 1.0F) - (double)0.5F) / (double)3.0F, ((double)(world.random.nextFloat() * 1.0F) - (double)0.5F) / (double)3.0F, (double)0.1F);
        }

    }

    protected BodyRotationControl createBodyControl() {
        return new NueBodyRotationControl(this);
    }

    class NueBodyRotationControl extends BodyRotationControl {
        public NueBodyRotationControl(Mob pMob) {
            super(pMob);
        }

        public void clientTick() {
            NueEntity.this.yHeadRot = NueEntity.this.yBodyRot;
            NueEntity.this.yBodyRot = NueEntity.this.getYRot();
        }
    }

    public static class CopyOwnerTargetGoalFlying<I extends FlyingMob & IFollowingSummon> extends TargetGoal {
        public CopyOwnerTargetGoalFlying(I creature) {
            super(creature, false);
        }

        public boolean canUse() {
            Mob var2 = this.mob;
            if (!(var2 instanceof IFollowingSummon summon)) {
                return false;
            } else {
                return summon.getSummoner() != null && summon.getSummoner().getLastHurtMob() != null;
            }
        }

        public void start() {
            Mob var2 = this.mob;
            if (var2 instanceof IFollowingSummon summon) {
                if (summon.getSummoner() != null) {
                    this.mob.setTarget(summon.getSummoner().getLastHurtMob());
                }
            }

            super.start();
        }
    }

    static enum AttackPhase {
        CIRCLE,
        SWOOP;

        private AttackPhase() {
        }
    }

    class NueMoveControl extends MoveControl {
        private float speed = 0.1F;

        public NueMoveControl(Mob pMob) {
            super(pMob);
        }

        public void tick() {
            if (NueEntity.this.horizontalCollision) {
                NueEntity.this.setYRot(NueEntity.this.getYRot() + 180.0F);
                this.speed = 0.1F;
            }

            double $$0 = NueEntity.this.moveTargetPoint.x - NueEntity.this.getX();
            double $$1 = NueEntity.this.moveTargetPoint.y - NueEntity.this.getY();
            double $$2 = NueEntity.this.moveTargetPoint.z - NueEntity.this.getZ();
            double $$3 = Math.sqrt($$0 * $$0 + $$2 * $$2);
            if (Math.abs($$3) > (double)1.0E-5F) {
                double $$4 = (double)1.0F - Math.abs($$1 * (double)0.7F) / $$3;
                $$0 *= $$4;
                $$2 *= $$4;
                $$3 = Math.sqrt($$0 * $$0 + $$2 * $$2);
                double $$5 = Math.sqrt($$0 * $$0 + $$2 * $$2 + $$1 * $$1);
                float $$6 = NueEntity.this.getYRot();
                float $$7 = (float) Mth.atan2($$2, $$0);
                float $$8 = Mth.wrapDegrees(NueEntity.this.getYRot() + 90.0F);
                float $$9 = Mth.wrapDegrees($$7 * (180F / (float) PI));
                NueEntity.this.setYRot(Mth.approachDegrees($$8, $$9, 4.0F) - 90.0F);
                NueEntity.this.yBodyRot = NueEntity.this.getYRot();
                if (Mth.degreesDifferenceAbs($$6, NueEntity.this.getYRot()) < 3.0F) {
                    this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
                } else {
                    this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
                }

                float $$10 = (float)(-(Mth.atan2(-$$1, $$3) * (double)(180F / (float) PI)));
                NueEntity.this.setXRot($$10);
                float $$11 = NueEntity.this.getYRot() + 90.0F;
                double $$12 = (double)(this.speed * Mth.cos($$11 * ((float) PI / 180F))) * Math.abs($$0 / $$5);
                double $$13 = (double)(this.speed * Mth.sin($$11 * ((float) PI / 180F))) * Math.abs($$2 / $$5);
                double $$14 = (double)(this.speed * Mth.sin($$10 * ((float) PI / 180F))) * Math.abs($$1 / $$5);
                Vec3 $$15 = NueEntity.this.getDeltaMovement();
                NueEntity.this.setDeltaMovement($$15.add((new Vec3($$12, $$14, $$13)).subtract($$15).scale(0.2)));
            }

        }
    }

    class NueLookControl extends LookControl {
        public NueLookControl(Mob pMob) {
            super(pMob);
        }

        public void tick() {
        }
    }

    abstract class NueMoveTargetGoal extends Goal {
        public NueMoveTargetGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return NueEntity.this.moveTargetPoint.distanceToSqr(NueEntity.this.getX(), NueEntity.this.getY(), NueEntity.this.getZ()) < (double)4.0F;
        }
    }

    class NueFollowSummonerGoal extends NueEntity.NueCircleAroundAnchorGoal{

        private int height;

        NueFollowSummonerGoal() {
        }

        public boolean canUse(){
            return NueEntity.this.getOwner() != null;
        }

        public void start(){
            super.start();

            this.height = NueEntity.this.random.nextInt(5);
            NueEntity.this.setFlying(true);
        }

        public void stop(){
            super.stop();

            NueEntity.this.setFlying(false);
        }

        public void tick(){
            super.tick();

            this.setAnchorAboveTarget();
        }

        private void setAnchorAboveTarget() {
            NueEntity.this.anchorPoint = NueEntity.this.getOwner().blockPosition().above(7 + this.height);
        }

    }

    class NueCircleAroundAnchorGoal extends NueEntity.NueMoveTargetGoal {
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        NueCircleAroundAnchorGoal() {
        }

        public boolean canUse() {
            return NueEntity.this.getTarget() == null || NueEntity.this.attackPhase == NueEntity.AttackPhase.CIRCLE;
        }

        public void start() {
            this.distance = 5.0F + NueEntity.this.random.nextFloat() * 10.0F;
            this.height = -4.0F + NueEntity.this.random.nextFloat() * 9.0F;
            this.clockwise = NueEntity.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();

            NueEntity.this.setFlying(true);
        }

        public void stop(){
            super.stop();

            NueEntity.this.setFlying(false);
        }

        public void tick() {
            if (NueEntity.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
                this.height = -4.0F + NueEntity.this.random.nextFloat() * 9.0F;
            }

            if (NueEntity.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
                ++this.distance;
                if (this.distance > 15.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }

            if (NueEntity.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
                this.angle = NueEntity.this.random.nextFloat() * 2.0F * (float) PI;
                this.selectNext();
            }

            if (this.touchingTarget()) {
                this.selectNext();
            }

            if (NueEntity.this.moveTargetPoint.y < NueEntity.this.getY() && !NueEntity.this.level().isEmptyBlock(NueEntity.this.blockPosition().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (NueEntity.this.moveTargetPoint.y > NueEntity.this.getY() && !NueEntity.this.level().isEmptyBlock(NueEntity.this.blockPosition().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }

        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(NueEntity.this.anchorPoint)) {
                NueEntity.this.anchorPoint = NueEntity.this.blockPosition();
            }

            this.angle += this.clockwise * 15.0F * ((float) PI / 180F);
            NueEntity.this.moveTargetPoint = Vec3.atLowerCornerOf(NueEntity.this.anchorPoint).add((double)(this.distance * Mth.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * Mth.sin(this.angle)));
        }
    }

    class NueSweepAttackGoal extends NueEntity.NueMoveTargetGoal {
        private boolean isScaredOfCat;
        private int catSearchTick;

        NueSweepAttackGoal() {
        }

        public boolean canUse() {
            return NueEntity.this.getTarget() != null && NueEntity.this.attackPhase == NueEntity.AttackPhase.SWOOP;
        }

        public boolean canContinueToUse() {
            LivingEntity $$0 = NueEntity.this.getTarget();
            if ($$0 == null) {
                return false;
            } else if (!$$0.isAlive()) {
                return false;
            } else {
                if ($$0 instanceof Player) {
                    Player $$1 = (Player)$$0;
                    if ($$0.isSpectator() || $$1.isCreative()) {
                        return false;
                    }
                }

                if (!this.canUse()) {
                    return false;
                } else {
                    if (NueEntity.this.tickCount > this.catSearchTick) {
                        this.catSearchTick = NueEntity.this.tickCount + 20;
                        List<Cat> $$2 = NueEntity.this.level().getEntitiesOfClass(Cat.class, NueEntity.this.getBoundingBox().inflate((double)16.0F), EntitySelector.ENTITY_STILL_ALIVE);

                        for(Cat $$3 : $$2) {
                            $$3.hiss();
                        }

                        this.isScaredOfCat = !$$2.isEmpty();
                    }

                    return !this.isScaredOfCat;
                }
            }
        }

        public void start() {

        }

        public void stop() {
            NueEntity.this.setTarget((LivingEntity)null);
            NueEntity.this.attackPhase = NueEntity.AttackPhase.CIRCLE;
        }

        public void tick() {
            LivingEntity $$0 = NueEntity.this.getTarget();
            if ($$0 != null) {
                NueEntity.this.moveTargetPoint = new Vec3($$0.getX(), $$0.getY((double)0.5F) + 1.5, $$0.getZ());

                if (NueEntity.this.getBoundingBox().inflate((double)3.0F).intersects($$0.getBoundingBox())) {
                    NueEntity.this.setAttacking(true);
                    NueEntity.this.performCastAttack(NueEntity.this, NueEntity.this.getTarget(), nueCastSpell, nueColor);
                    NueEntity.this.attackPhase = NueEntity.AttackPhase.CIRCLE;
                    if (!NueEntity.this.isSilent()) {
                        NueEntity.this.level().levelEvent(1039, NueEntity.this.blockPosition(), 0);
                    }
                }
                else if (NueEntity.this.horizontalCollision || NueEntity.this.hurtTime > 0) {
                    NueEntity.this.attackPhase = NueEntity.AttackPhase.CIRCLE;
                }

            }
        }
    }

    class NueAttackStrategyGoal extends Goal {
        private int nextSweepTick;

        NueAttackStrategyGoal() {
        }

        public boolean canUse() {
            LivingEntity $$0 = NueEntity.this.getTarget();
            return $$0 != null ? NueEntity.this.canAttack($$0, TargetingConditions.DEFAULT) : false;
        }

        public void start() {
            this.nextSweepTick = this.adjustedTickDelay(10);
            NueEntity.this.attackPhase = NueEntity.AttackPhase.CIRCLE;
            this.setAnchorAboveTarget();
        }

        public void stop() {
            NueEntity.this.anchorPoint = NueEntity.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, NueEntity.this.anchorPoint).above(7 + NueEntity.this.random.nextInt(5));
        }

        public void tick() {
            if (NueEntity.this.attackPhase == NueEntity.AttackPhase.CIRCLE) {
                --this.nextSweepTick;
                if (this.nextSweepTick <= 0) {
                    NueEntity.this.attackPhase = NueEntity.AttackPhase.SWOOP;
                    this.setAnchorAboveTarget();
                    this.nextSweepTick = this.adjustedTickDelay((8 + NueEntity.this.random.nextInt(4)) * 20);
                    NueEntity.this.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + NueEntity.this.random.nextFloat() * 0.1F);
                }
            }

        }

        private void setAnchorAboveTarget() {
            NueEntity.this.anchorPoint = NueEntity.this.getTarget().blockPosition().above(7 + NueEntity.this.random.nextInt(5));

        }
    }

    class NueAttackPlayerTargetGoal extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range((double)64.0F);
        private int nextScanTick = reducedTickDelay(20);

        NueAttackPlayerTargetGoal() {
        }

        public boolean canUse() {
            if(this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            } else {
                this.nextScanTick = reducedTickDelay(60);
                List<Player> $$0 = NueEntity.this.level().getNearbyPlayers(this.attackTargeting, NueEntity.this, NueEntity.this.getBoundingBox().inflate((double)16.0F, (double)64.0F, (double)16.0F));
                if (!$$0.isEmpty()) {
                    $$0.sort(Comparator.comparing(Entity::getBlockY).reversed());

                    for(Player $$1 : $$0) {
                        if (NueEntity.this.canAttack($$1, TargetingConditions.DEFAULT)) {
                            NueEntity.this.setTarget($$1);
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        public boolean canContinueToUse() {
            LivingEntity $$0 = NueEntity.this.getTarget();
            return $$0 != null ? NueEntity.this.canAttack($$0, TargetingConditions.DEFAULT) : false;
        }
    }

    void performCastAttack(LivingEntity entity, LivingEntity target, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
        DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
        projectileSpell.setColor(color);

        projectileSpell.shoot(entity,Mth.clamp(entity.getXRot() + 45, 45, 90), entity.getYRot(), 0.0F, 1.0f, 0.9f);

        entity.level().addFreshEntity(projectileSpell);
    }

    private ParticleColor nueColor = new ParticleColor(200, 200, 255);

    public Spell nueCastSpell = new Spell()
            .add(AugmentDurationDown.INSTANCE,2)

            .add(EffectLightning.INSTANCE)
            .add(AugmentDampen.INSTANCE)
            .add(AugmentExtendTime.INSTANCE, 3)

            .withColor(nueColor);

    // Ten Shadows Ritual Reward Control
    public boolean isRitualFailed(Entity attacker){
        boolean isFailed = false;

        if(attacker instanceof LivingEntity leAttacker) {
            if (leAttacker instanceof ISummon) {
                ISummon summon = (ISummon) attacker;
                    this.attackersList[checkDupes(summon.getOwner())] = summon.getOwner();
            } else {
                    this.attackersList[checkDupes(leAttacker)] = leAttacker;
            }
        }

        if(this.attackersList[1] != null){
            isFailed = true;
        }

        return isFailed;
    }

    public int checkDupes(LivingEntity newEntity) {
        if(this.attackersList[0] == null || this.attackersList[0] == newEntity){
            return 0;
        }
        return 1;
    }
}