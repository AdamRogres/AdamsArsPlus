package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.entities.ai.DDogAttackGoal;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectAnnihilate;
import com.adamsmods.adamsarsplus.lib.AdamsEntityTags;
import com.adamsmods.adamsarsplus.registry.AdamCapabilityRegistry;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.IFollowingSummon;
import com.hollingsworth.arsnouveau.common.entity.goal.FollowSummonerGoal;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.*;
import static com.adamsmods.adamsarsplus.Config.MAX_DOMAIN_ENTITIES;
import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class RDeerEntity extends Monster implements IFollowingSummon, ISummon {
    // Ten Shadows Reward
    public boolean ritualStatus;
    public LivingEntity[] attackersList = {null, null};
    public boolean isSummon;
    private LivingEntity owner;
    public int ticksLeft;

    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(RDeerEntity.class, EntityDataSerializers.BOOLEAN);

    public RDeerEntity(Level level, LivingEntity owner, boolean summon) {
        super((EntityType) AdamsModEntities.ROUND_DEER.get(), level);

        this.owner = owner;
        this.setOwnerID(owner.getUUID());
        this.isSummon = summon;
        this.ritualStatus = false;
    }

    public RDeerEntity(Level level, boolean summon) {
        super((EntityType) AdamsModEntities.ROUND_DEER.get(), level);

        this.isSummon = summon;
        this.ritualStatus = false;
    }

    public RDeerEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityType<?> getType() {
        return (EntityType) AdamsModEntities.ROUND_DEER.get();
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState walkAnimationState = new AnimationState();
    private int walkAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    private int attackAnimationTimeout = 0;

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }


    @Override
    public void tick() {
        super.tick();

        if (this.getSummoner() != null) {
            if (!this.level().isClientSide && this.isSummon && !this.getSummoner().hasEffect(TENSHADOWS_EFFECT.get())) {
                spawnShadowPoof((ServerLevel) this.level(), this.blockPosition());
                this.remove(RemovalReason.DISCARDED);
                this.onSummonDeath(this.level(), (DamageSource) null, true);
            }
        }

        if (this.level().isClientSide()) {
            setupAnimationStates();
        } else {
            if(this.level().getRandom().nextInt(0,10) > 5){
                spawnAuraParticles(this, (ServerLevel) this.level(), 20.0D, 6);
            }

            for (Entity entity : level().getEntities(null, new AABB(this.blockPosition()).inflate(20,6,20))) {
                if (entity instanceof LivingEntity){
                    if(entity == this || entity == this.getSummoner()){
                        if(((LivingEntity) entity).hasEffect(MobEffects.REGENERATION)){
                            continue;
                        }
                        if(entity == this){
                            if(this.getHealth() < this.getMaxHealth() * 0.25){
                                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 4, false, true));
                                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, false, true));
                            } else {
                                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 3, false, true));
                            }
                        } else {
                            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 2, false, true));
                        }
                    } else {
                        if(((LivingEntity) entity).hasEffect(SIMPLE_DOMAIN_EFFECT.get())){
                            continue;
                        }
                        ((LivingEntity) entity).addEffect(new MobEffectInstance((MobEffect) MANA_EXHAUST_EFFECT.get(), 60, 0, false, true));
                    }
                }
            }
        }

    }

    private void setupAnimationStates() {

        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        //Attack Animation control
        if(this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 25;
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }
        if(!this.isAttacking()) {
            attackAnimationState.stop();
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1F);
        } else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(RDeerEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UUID, Optional.of(Util.NIL_UUID));
        this.entityData.define(ATTACKING, false);
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        onSummonDeath(level(), cause, false);

        if (!this.ritualStatus && !this.isSummon) {
            if (this.attackersList[0] instanceof Player player) {
                AdamCapabilityRegistry.getTsTier(player).ifPresent((pRank) -> {
                    if (pRank.getTsTier() >= 2) {
                        pRank.setTsTier(Math.max(3, pRank.getTsTier()));
                        PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.tenshadows.deer_tamed"));
                    } else {
                        PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.tenshadows.tame_failed"));
                    }
                });
            }
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        Entity var4 = pSource.getEntity();

        if (pSource.is(DamageTypes.MOB_ATTACK)) {
            if (var4 instanceof ISummon) {
                ISummon summon = (ISummon) var4;
                if (summon.getOwnerUUID() != null && summon.getOwnerUUID().equals(this.getOwnerUUID())) {
                    return false;
                }
            }
        }

        if(var4 instanceof Player && this.isSummon){
            Player summoner = (Player) var4;
            if(summoner.getUUID() == this.getOwnerUUID()){
                return false;
            }
        }

        if (!this.ritualStatus) {
            this.ritualStatus = isRitualFailed(pSource.getEntity());
        }

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
                ISummon summon = (ISummon) pEntity;
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
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));

        this.goalSelector.addGoal(3, new FollowSummonerGoal(this, this.owner, (double) 1.0F, 30.0F, 3.0F));
        this.goalSelector.addGoal(4, new RDeerHealSummoner(this, this.owner, (double) 1.0F, 9.0F, 3.0F, () -> (this.getSummoner().getHealth() < (this.getSummoner().getMaxHealth() * 0.5)) ));
        this.goalSelector.addGoal(5, new RDeerAvoidEntityGoal(this, LivingEntity.class, 8, () -> (this.getHealth() < (this.getMaxHealth() * 0.5)), 1.2, 1.2));
        this.goalSelector.addGoal(6, new RDeerAttackGoal(this, 1, false, () -> true));

        this.goalSelector.addGoal(8, new FollowSummonerGoal(this, this.owner, (double) 1.0F, 9.0F, 3.0F));

        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double) 1.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(3, new HurtByTargetGoal(this, new Class[]{RDeerEntity.class}) {
            protected boolean canAttack(@Nullable LivingEntity pPotentialTarget, TargetingConditions pTargetPredicate) {
                return pPotentialTarget != null && super.canAttack(pPotentialTarget, pTargetPredicate) && !pPotentialTarget.getUUID().equals(RDeerEntity.this.getOwnerUUID()) && !pPotentialTarget.getUUID().equals(RDeerEntity.this.getUUID());
            }
        });
        this.targetSelector.addGoal(1, new CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Villager.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 85D)
                .add(Attributes.ATTACK_DAMAGE, (double) 13.0F)
                .add(Attributes.MOVEMENT_SPEED, (double) 0.35F)
                .add(Attributes.FOLLOW_RANGE, (double) 70.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double) 1.0F)
                .add(Attributes.ARMOR, 15)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7);
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

    protected float getSoundVolume() {
        return 0.4F;
    }

    public static void spawnShadowPoof(ServerLevel world, BlockPos pos) {
        for (int i = 0; i < 10; ++i) {
            double d0 = (double) pos.getX() + (double) 0.5F;
            double d1 = (double) pos.getY() + 1.2;
            double d2 = (double) pos.getZ() + (double) 0.5F;
            world.sendParticles(ParticleTypes.SQUID_INK, d0, d1, d2, 2, ((double) (world.random.nextFloat() * 1.0F) - (double) 0.5F) / (double) 3.0F, ((double) (world.random.nextFloat() * 1.0F) - (double) 0.5F) / (double) 3.0F, ((double) (world.random.nextFloat() * 1.0F) - (double) 0.5F) / (double) 3.0F, (double) 0.1F);
        }
    }

    // Ten Shadows Ritual Reward Control
    public boolean isRitualFailed(Entity attacker) {
        boolean isFailed = false;

        if (attacker instanceof LivingEntity leAttacker) {
            if (leAttacker instanceof ISummon) {
                ISummon summon = (ISummon) attacker;
                this.attackersList[checkDupes(summon.getOwner())] = summon.getOwner();
            } else {
                this.attackersList[checkDupes(leAttacker)] = leAttacker;
            }
        }

        if (this.attackersList[1] != null) {
            isFailed = true;
        }

        return isFailed;
    }

    public int checkDupes(LivingEntity newEntity) {
        if (this.attackersList[0] == null || this.attackersList[0] == newEntity) {
            return 0;
        }
        return 1;
    }

    // Round Deer Stuff

    public void spawnAuraParticles(LivingEntity living, ServerLevel level, double radius, int height) {
        Vec3 livingEyes = living.getEyePosition();
        double x = livingEyes.x;
        double y = livingEyes.y;
        double z = livingEyes.z;

        double xOff = 0;
        double yOff = -1;
        double zOff = 0;

        double radiusFactor = 360 / radius;
        double iOff = Math.random() * radiusFactor;
        float radiusRandom = 0;
        float heightRandom = 0;

        for (int i = 0; i < 360; i = i + (int)radiusFactor) {
            radiusRandom = (float)getRandom().nextInt(0,32) / 32;
            heightRandom = (float)getRandom().nextInt(0, 32)/ 32;

            xOff = radius * radiusRandom * cos(toRadians(i + iOff));
            zOff = radius * radiusRandom * sin(toRadians(i + iOff));

            level.sendParticles(ParticleTypes.END_ROD, x + xOff, y + yOff + height * heightRandom, z + zOff, 1, 0, 0, 0, 0);

        }
    }

    public class RDeerAttackGoal extends MeleeAttackGoal {
        private final RDeerEntity entity;

        private int attackDelay = 15;
        private int ticksUntilNextAttack = 10;
        private boolean shouldCountTillNextAttack = false;

        Supplier<Boolean> canUse;
        boolean done;

        public RDeerAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((RDeerEntity) pMob);
            this.canUse = canUse;
        }

        @Override
        public void start() {
            super.start();
            attackDelay = 15;
            ticksUntilNextAttack = 10;
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
                    entity.setAttacking(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());

                    performAttack(pEnemy);
                    if(pEnemy.isBlocking()){
                        if(pEnemy instanceof Player playerEnemy){
                            playerEnemy.getCooldowns().addCooldown(Items.SHIELD, 60);
                            playerEnemy.disableShield(false);
                        }
                    }
                    else {
                        performSpellAttack(this.mob, deerAttackSpell, deerColor, pEnemy);
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
            this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay + 10);
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
            return (double)(this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() * 2.0F + 4.5F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;

        }

        void performSpellAttack(LivingEntity entity, Spell spell, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));
        }

        private ParticleColor deerColor = new ParticleColor(255, 255, 255);

        public Spell deerAttackSpell = new Spell()
                .add(EffectBurst.INSTANCE)
                .add(AugmentAOE.INSTANCE)
                .add(EffectLaunch.INSTANCE)
                .add(AugmentAmplify.INSTANCE)
                .add(EffectKnockback.INSTANCE)

                .add(EffectHarm.INSTANCE)
                .add(AugmentAmplify.INSTANCE,4)

                .withColor(deerColor);


        @Override
        public void tick() {
            super.tick();
            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }

        @Override
        public void stop() {
            entity.setAttacking(false);
            this.done = false;
            super.stop();
        }
    }

    static class RDeerAvoidEntityGoal<T extends RDeerEntity> extends AvoidEntityGoal<T> {
        private final RDeerEntity deer;
        Supplier<Boolean> canUse;

        public RDeerAvoidEntityGoal(RDeerEntity Entity, Class<T> pEntityClassToAvoid, float pMaxDist, Supplier<Boolean> canUse, double pWalkSpeedModifier, double pSprintSpeedModifier) {
            super(Entity, pEntityClassToAvoid, pMaxDist, pWalkSpeedModifier, pSprintSpeedModifier);
            this.deer = Entity;
            this.canUse = canUse;
        }

        public boolean canUse() {
            return super.canUse() && this.canUse.get();
        }

        @Override
        public void start() {
            super.start();

            deer.setAttacking(false);
        }
    }

    static class RDeerHealSummoner extends FollowSummonerGoal {
        Supplier<Boolean> canUse;

        RDeerHealSummoner(IFollowingSummon mobEntity, LivingEntity owner, double followSpeedIn, float minDistIn, float maxDistIn,  Supplier<Boolean> canUse) {
            super(mobEntity, owner, followSpeedIn, minDistIn, maxDistIn);
            this.canUse = canUse;
        }

        public boolean canUse() {
            return super.canUse() && this.canUse.get();
        }
    }
}
