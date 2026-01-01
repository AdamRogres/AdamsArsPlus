package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.entities.ai.*;
import com.adamsmods.adamsarsplus.entities.ai.AdamCastingAGoal;
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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Supplier;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.*;
import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.ADAM_ENTITY;
import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;
import static java.lang.Math.*;

public class AdamEntity extends Monster implements RangedAttackMob {

    public static final EntityDataAccessor<Boolean> ATTACKING_AA =
            SynchedEntityData.defineId(AdamEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING_AB =
            SynchedEntityData.defineId(AdamEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING_B =
            SynchedEntityData.defineId(AdamEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> BLOCKING =
            SynchedEntityData.defineId(AdamEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_A =
            SynchedEntityData.defineId(AdamEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_B =
            SynchedEntityData.defineId(AdamEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_DOMAIN =
            SynchedEntityData.defineId(AdamEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> CASTING_BARRIER =
            SynchedEntityData.defineId(AdamEntity.class, EntityDataSerializers.BOOLEAN);

    public int attackAACooldown;
    public int attackABCooldown;
    public int attackBCooldown;
    public int blockCooldown;
    public int castingACooldown;
    public int castingBCooldown;
    public int castingCCooldown;
    public int castingDCooldown;
    public int castingECooldown;
    public int domainCooldown;
    public int domainbCooldown = 1000;

    public int barrierCooldown;
    public int recoverCooldown;
    public int blinkCooldown;

    public int age;

    private final ServerBossEvent bossEvent;

    public AdamEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        this.moveControl = new AdamEntity.BossMoveControl(this);
    }

    public AdamEntity(Level pLevel){
        this(ADAM_ENTITY.get(), pLevel);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAAAnimationState = new AnimationState();
    public int attackAAAnimationTimeout = 0;

    public final AnimationState attackABAnimationState = new AnimationState();
    public int attackABAnimationTimeout = 0;

    public final AnimationState attackBAnimationState = new AnimationState();
    public int attackBAnimationTimeout = 0;

    public final AnimationState blockAnimationState = new AnimationState();
    public int blockAnimationTimeout = 0;

    public final AnimationState castAAnimationState = new AnimationState();
    public int castAAnimationTimeout = 0;

    public final AnimationState castBAnimationState = new AnimationState();
    public int castBAnimationTimeout = 0;

    public final AnimationState castDomainAnimationState = new AnimationState();
    public int castDomainAnimationTimeout = 0;

    public final AnimationState castBarrierAnimationState = new AnimationState();
    public int castBarrierAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();
        age++;

        if(attackABCooldown > 0) {
            attackABCooldown--;
        }
        if(attackBCooldown > 0) {
            attackBCooldown--;
        }
        if(blockCooldown > 0) {
            blockCooldown--;
        }
        if(castingACooldown > 0) {
            castingACooldown--;
        }
        if(castingBCooldown > 0) {
            castingBCooldown--;
        }
        if(castingCCooldown > 0) {
            castingCCooldown--;
        }
        if(castingDCooldown > 0) {
            castingDCooldown--;
        }
        if(castingECooldown > 0) {
            castingECooldown--;
        }
        if(domainCooldown > 0) {
            domainCooldown--;
        }
        else{
            performChargeParticle(this);
        }
        if(domainbCooldown > 0) {
            domainbCooldown--;
        }
        if(barrierCooldown > 0) {
            barrierCooldown--;
        }
        if(recoverCooldown > 0) {
            recoverCooldown--;
        }
        else {
            performSpellSelf(this, adamRecoverSpell, adamColor);
            recoverCooldown = random.nextInt(200) + 200;
        }
        if(blinkCooldown > 0) {
            blinkCooldown--;
        }
        //Projectile reflection
        if(barrierCooldown == 0){
            if(scanForProjectiles(this, 8)){
                this.setUsingBarrier(true);

                this.barrierCooldown = random.nextInt(200) + 40;

                if(!this.level().isClientSide()) {
                    performSpellSelf(this, adamBarrierSpell,adamColor);
                }
            }
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
        if(this.isCastingA() && castAAnimationTimeout <= 0) {
            castAAnimationTimeout = 27;
            castAAnimationState.start(this.tickCount);
        } else {
            --this.castAAnimationTimeout;
        }
        if(!this.isCastingA()) {
            castAAnimationState.stop();
        }

        //Casting B Animation control
        if(this.isCastingB() && castBAnimationTimeout <= 0) {
            castBAnimationTimeout = 27;
            castBAnimationState.start(this.tickCount);
        } else {
            --this.castBAnimationTimeout;
        }
        if(!this.isCastingB()) {
            castBAnimationState.stop();
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

        //Barrier Animation Control
        if(this.isUsingBarrier() && castBarrierAnimationTimeout <= 0) {
            castBarrierAnimationTimeout = 20;
            castBarrierAnimationState.start(this.tickCount);
        } else {
            --this.castBarrierAnimationTimeout;
        }
        if(!this.isUsingBarrier()) {
            castBarrierAnimationState.stop();
        }
        if(castBarrierAnimationTimeout == 1){
            setUsingBarrier(false);
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

    private ParticleColor adamColor = new ParticleColor(150, 0, 255);

    public Spell adamBlinkSpell = new Spell()
            .add(EffectBlink.INSTANCE)

            .withColor(adamColor);

    public Spell adamRecoverSpell = new Spell()
            .add(EffectDispel.INSTANCE)
            .add(EffectHeal.INSTANCE)
            .add(AugmentAmplify.INSTANCE,5)

            .withColor(adamColor);

    public Spell adamBarrierSpell = new Spell()
            .add(EffectLimitless.INSTANCE)
            .add(AugmentAOEThree.INSTANCE,3)
            .add(AugmentAmplify.INSTANCE)

            .withColor(adamColor);

    public void performSpellSelf(LivingEntity entity, Spell spell, ParticleColor color){
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

        resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));
    }

    public void setAttackingAA(boolean attackingAA) { this.entityData.set(ATTACKING_AA, attackingAA); }
    public boolean isAttackingAA(){ return this.entityData.get(ATTACKING_AA); }

    public void setAttackingAB(boolean attackingAB) { this.entityData.set(ATTACKING_AB, attackingAB); }
    public boolean isAttackingAB(){ return this.entityData.get(ATTACKING_AB); }

    public void setAttackingB(boolean attackingB) { this.entityData.set(ATTACKING_B, attackingB); }
    public boolean isAttackingB(){ return this.entityData.get(ATTACKING_B); }

    public void setBlocking(boolean blocking) { this.entityData.set(BLOCKING, blocking); }
    public boolean isBlock(){ return this.entityData.get(BLOCKING); }

    public void setCastingA(boolean castingA) { this.entityData.set(CASTING_A, castingA); }
    public boolean isCastingA(){ return this.entityData.get(CASTING_A); }

    public void setCastingB(boolean castingB) { this.entityData.set(CASTING_B, castingB); }
    public boolean isCastingB(){ return this.entityData.get(CASTING_B); }

    public void setUsingDomain(boolean usingDomain) { this.entityData.set(CASTING_DOMAIN, usingDomain); }
    public boolean isUsingDomain(){ return this.entityData.get(CASTING_DOMAIN); }

    public void setUsingBarrier(boolean usingBarrier) { this.entityData.set(CASTING_BARRIER, usingBarrier); }
    public boolean isUsingBarrier(){ return this.entityData.get(CASTING_BARRIER); }


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
        this.entityData.define(BLOCKING, false);
        this.entityData.define(CASTING_A, false);
        this.entityData.define(CASTING_B, false);
        this.entityData.define(CASTING_DOMAIN, false);
        this.entityData.define(CASTING_BARRIER, false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("attackaa", attackAACooldown);
        tag.putInt("attackab", attackABCooldown);
        tag.putInt("attackb", attackBCooldown);
        tag.putInt("block", blockCooldown);
        tag.putInt("castinga", castingACooldown);
        tag.putInt("castingb", castingBCooldown);
        tag.putInt("castingc", castingCCooldown);
        tag.putInt("castingd", castingDCooldown);
        tag.putInt("castinge", castingECooldown);
        tag.putInt("domain", domainCooldown);
        tag.putInt("domainb", domainbCooldown);

        tag.putInt("barrier", barrierCooldown);
        tag.putInt("heal", recoverCooldown);
        tag.putInt("blink", blinkCooldown);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.LAVA) || source.is(DamageTypes.FALL)  || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.MAGIC) || source.is(DamageTypes.IN_FIRE))
            return false;

        if(this.isBlock()){
            this.attackBCooldown = 0;
            this.blockCooldown = random.nextInt(80) + 30;

            this.playSound(SoundEvents.SHIELD_BLOCK, 1.5F, 1F);

            if(source.getEntity() instanceof LivingEntity enemy && (source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK))){
                knockback(enemy, this, 0.3f);
            }

            return false;
        }

        if(this.blinkCooldown <= 0){
            if(source.is(DamageTypes.IN_WALL)){
                this.teleportRelative(0,8,0);
            } else if(!this.level().isClientSide()) {
                performSpellSelf(this, adamBlinkSpell, adamColor);
            }

            this.blinkCooldown = random.nextInt(160) + 40;

            return super.hurt(source, amount);
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

        this.attackAACooldown = tag.getInt("attackaa");
        this.attackAACooldown = tag.getInt("attackab");
        this.attackBCooldown = tag.getInt("attackb");
        this.blockCooldown = tag.getInt("block");
        this.castingACooldown = tag.getInt("castinga");
        this.castingBCooldown = tag.getInt("castingb");
        this.castingCCooldown = tag.getInt("castingc");
        this.castingDCooldown = tag.getInt("castingd");
        this.castingECooldown = tag.getInt("castinge");
        this.domainCooldown = tag.getInt( "domain");
        this.domainbCooldown = tag.getInt( "domainb");

        this.barrierCooldown = tag.getInt( "barrier");
        this.recoverCooldown = tag.getInt( "heal");
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

        this.goalSelector.addGoal(1, new AdamDomainGoal<>(this, 1.0D, 20f, () -> domainCooldown <= 0, 0, 13));
        this.goalSelector.addGoal(1, new AdamDomainGoalB<>(this, 1.0D, 20f, () -> (domainbCooldown <= 0) && (!this.hasEffect(DOMAIN_BURNOUT_EFFECT.get())), 0, 13));

        this.goalSelector.addGoal(2, new AdamCastingGoalA<>(this, 1.0D, 70f, () -> castingACooldown <= 0, 0, 15));
        this.goalSelector.addGoal(2, new AdamCastingGoalB<>(this, 1.0D, 70f, () -> castingBCooldown <= 0, 0, 15));
        this.goalSelector.addGoal(2, new AdamCastingGoalC<>(this, 1.0D, 70f, () -> castingCCooldown <= 0, 0, 15));
        this.goalSelector.addGoal(2, new AdamCastingGoalD<>(this, 1.0D, 70f, () -> castingDCooldown <= 0, 0, 15));

        this.goalSelector.addGoal(3, new AdamAttackGoalAB(this, 1.3D, true, () -> attackABCooldown != 0));
        this.goalSelector.addGoal(3, new AdamAttackGoalB(this, 1.0D, true, () -> attackBCooldown <= 0));
        this.goalSelector.addGoal(3, new AdamBlockGoal(this, 0.7D, true, () -> blockCooldown <= 0));

        this.goalSelector.addGoal(4, new AdamAttackGoalAA(this, 1.3D, true, () -> true));

        this.goalSelector.addGoal(7, new AdamEntity.AdamEntityRandomMoveGoal());
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, false));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 2000D)
                .add(Attributes.ARMOR, 23D)
                .add(Attributes.ATTACK_DAMAGE, (double)20.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.8F)
                .add(Attributes.FLYING_SPEED, (double)0.8F)
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
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
    }
    @Override
    public void performRangedAttack(LivingEntity entity, float p_82196_2_) {

    }

    void performChargeParticle(LivingEntity entity) {
        Level world = entity.level();

        Vec3 livingEyes = entity.getEyePosition();
        double x = livingEyes.x;
        double y = livingEyes.y;
        double z = livingEyes.z;

        if (world instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 1, 0, 0, 0, 0.5);

        }
    }

    public boolean scanForProjectiles(LivingEntity shooter, int range){

        boolean ret = false;

        int i = 0;
        for (Entity entity : level().getEntities(null, new AABB(shooter.blockPosition()).inflate(range,range,range))) {

            if (entity instanceof Projectile){
                if(entity instanceof EntityProjectileSpell && ((EntityProjectileSpell) entity).getOwner() == shooter){
                    continue;
                } else {
                    ((Projectile) entity).setOwner(shooter);

                    ret = true;
                }
            }

            i++;
            if (i > 5)
                break;
        }

        return ret;
    }

    // Goals and Movement
    class BossMoveControl extends MoveControl {
        public BossMoveControl(AdamEntity pAdam) {
            super(pAdam);
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 $$0 = new Vec3(this.wantedX - AdamEntity.this.getX(), this.wantedY - AdamEntity.this.getY(), this.wantedZ - AdamEntity.this.getZ());
                double $$1 = $$0.length();
                if ($$1 < AdamEntity.this.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                    AdamEntity.this.setDeltaMovement(AdamEntity.this.getDeltaMovement().scale((double)0.5F));
                } else {
                    AdamEntity.this.setDeltaMovement(AdamEntity.this.getDeltaMovement().add($$0.scale(this.speedModifier * 0.05 / $$1)));
                    if (AdamEntity.this.getTarget() == null) {
                        Vec3 $$2 = AdamEntity.this.getDeltaMovement();
                        AdamEntity.this.setYRot(-((float) Mth.atan2($$2.x, $$2.z)) * (180F / (float)Math.PI));
                        AdamEntity.this.yBodyRot = AdamEntity.this.getYRot();
                    } else {
                        double $$3 = AdamEntity.this.getTarget().getX() - AdamEntity.this.getX();
                        double $$4 = AdamEntity.this.getTarget().getZ() - AdamEntity.this.getZ();
                        AdamEntity.this.setYRot(-((float)Mth.atan2($$3, $$4)) * (180F / (float)Math.PI));
                        AdamEntity.this.yBodyRot = AdamEntity.this.getYRot();
                    }
                }
            }
        }
    }

    class AdamEntityRandomMoveGoal extends Goal {
        public AdamEntityRandomMoveGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !AdamEntity.this.getMoveControl().hasWanted() && AdamEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos $$0 = AdamEntity.this.blockPosition();

            for(int $$1 = 0; $$1 < 3; ++$$1) {
                BlockPos $$2 = $$0.offset(AdamEntity.this.random.nextInt(15) - 7, AdamEntity.this.random.nextInt(11) - 5, AdamEntity.this.random.nextInt(15) - 7);
                if (AdamEntity.this.level().isEmptyBlock($$2)) {
                    AdamEntity.this.moveControl.setWantedPosition((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, (double)0.25F);
                    if (AdamEntity.this.getTarget() == null) {
                        AdamEntity.this.getLookControl().setLookAt((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }

    class AdamAttackGoalAA extends MeleeAttackGoal {
        private final AdamEntity entity;

        private int totalAnimation = 15;
        private int attackDelay = 8;
        private int ticksUntilNextAttack = 8;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public AdamAttackGoalAA(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((AdamEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 8;
            ticksUntilNextAttack = 8;

            LivingEntity $$0 = AdamEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                AdamEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, speedModifier);
            }
        }

        private ParticleColor AdamColor = new ParticleColor(0, 0, 0);

        public Spell AdamAttackSpell = new Spell()
                .add(EffectAnnihilate.INSTANCE)
                .add(AugmentAmplify.INSTANCE,3)

                .withColor(AdamColor);

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
                    if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
                        performAttack(pEnemy);
                        if(!pEnemy.isBlocking()){
                            performSpellAttack(this.mob, AdamAttackSpell, AdamColor, pEnemy);
                        }
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
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 4.0F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;

            pEnemy.addEffect(new MobEffectInstance(DISRUPTION_EFFECT.get(), 100, 0, true, true));
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
            LivingEntity $$0 = AdamEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = AdamEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    AdamEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, speedModifier);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    class AdamAttackGoalAB extends MeleeAttackGoal {
        private final AdamEntity entity;

        private int totalAnimation = 15;
        private int attackDelay = 10;
        private int ticksUntilNextAttack = 10;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public AdamAttackGoalAB(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((AdamEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 10;
            ticksUntilNextAttack = 10;

            LivingEntity $$0 = AdamEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                AdamEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, speedModifier);
            }
        }

        private ParticleColor AdamColor = new ParticleColor(0, 0, 0);

        public Spell AdamAttackSpell = new Spell()
                .add(EffectAnnihilate.INSTANCE)
                .add(AugmentAmplify.INSTANCE,3)

                .add(EffectDispel.INSTANCE)
                .add(EffectHex.INSTANCE)
                .add(EffectSnare.INSTANCE)
                .add(AugmentExtendTime.INSTANCE,4)
                .add(EffectGravity.INSTANCE)
                .add(AugmentExtendTime.INSTANCE)

                .withColor(AdamColor);

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
                    if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
                        performAttack(pEnemy);
                        if(!pEnemy.isBlocking()){
                            performSpellAttack(this.mob, AdamAttackSpell, AdamColor, pEnemy);
                        }

                        this.entity.attackABCooldown = 0;
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

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;

            pEnemy.addEffect(new MobEffectInstance(DISRUPTION_EFFECT.get(), 100, 0, true, true));
        }

        void performSpellAttack(LivingEntity entity, Spell spell, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));
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
            LivingEntity $$0 = AdamEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = AdamEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    AdamEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, speedModifier);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    class AdamAttackGoalB extends MeleeAttackGoal {
        private final AdamEntity entity;

        private int totalAnimation = 20;
        private int attackDelay = 12;
        private int ticksUntilNextAttack = 12;
        private boolean shouldCountTillNextAttack = false;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public AdamAttackGoalB(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((AdamEntity) pMob);
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
        }

        public void start() {
            this.mob.setAggressive(true);
            attackDelay = 12;
            ticksUntilNextAttack = 12;

            LivingEntity $$0 = AdamEntity.this.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                AdamEntity.this.moveControl.setWantedPosition($$1.x, $$1.y - 1, $$1.z, speedModifier);
            }
        }

        private ParticleColor AdamColor = new ParticleColor(0, 0, 0);

        public Spell AdamAttackSpell = new Spell()
                .add(EffectAnnihilate.INSTANCE)
                .add(AugmentAmplify.INSTANCE,3)

                .add(EffectDispel.INSTANCE)
                .add(EffectHex.INSTANCE)
                .add(EffectSnare.INSTANCE)
                .add(AugmentExtendTime.INSTANCE,4)
                .add(EffectGravity.INSTANCE)
                .add(AugmentExtendTime.INSTANCE)

                .withColor(AdamColor);

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
                            performSpellAttack(this.mob, AdamAttackSpell, AdamColor, pEnemy);
                        }

                        this.entity.attackBCooldown = random.nextInt(200) + 100;
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
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 6.5F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;

            pEnemy.addEffect(new MobEffectInstance(DISRUPTION_EFFECT.get(), 100, 0, true, true));
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
            LivingEntity $$0 = AdamEntity.this.getTarget();
            if ($$0 != null) {
                double $$1 = AdamEntity.this.distanceToSqr($$0);
                if ($$1 < (double)9.0F) {
                    Vec3 $$2 = $$0.getEyePosition();
                    AdamEntity.this.moveControl.setWantedPosition($$2.x, $$2.y - 1, $$2.z, speedModifier);
                }
                double d0 = this.mob.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                this.checkAndPerformAttack($$0, d0);
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    class AdamBlockGoal extends Goal {
        private int ticksUntilNextAttack = 100;
        private double speedModifier;

        Supplier<Boolean> canUse;
        boolean done;

        public AdamBlockGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.speedModifier = pSpeedModifier;
            this.canUse = canUse;
        }

        public boolean canUse() {
            return this.canUse.get();
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !AdamEntity.this.getNavigation().isDone()) && !this.done;
        }

        public void tick() {
            BlockPos $$0 = AdamEntity.this.blockPosition();

            for(int $$1 = 0; $$1 < 3; ++$$1) {
                BlockPos $$2 = $$0.offset(AdamEntity.this.random.nextInt(15) - 7, AdamEntity.this.random.nextInt(11) - 5, AdamEntity.this.random.nextInt(15) - 7);
                if (AdamEntity.this.level().isEmptyBlock($$2)) {
                    AdamEntity.this.moveControl.setWantedPosition((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, (double)0.25F);
                    if (AdamEntity.this.getTarget() == null) {
                        AdamEntity.this.getLookControl().setLookAt((double)$$2.getX() + (double)0.5F, (double)$$2.getY() + (double)0.5F, (double)$$2.getZ() + (double)0.5F, 180.0F, 20.0F);
                    }
                    break;
                }
            }

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);

            if(this.ticksUntilNextAttack > 0){
                AdamEntity.this.setBlocking(true);
            } else {
                AdamEntity.this.setBlocking(false);
                this.done = true;
                AdamEntity.this.blockCooldown = random.nextInt(100) + 30;
            }
        }

        public void start() {
            super.start();
            AdamEntity.this.setAggressive(true);
        }

        public void stop() {
            super.stop();
            AdamEntity.this.setBlocking(false);
            AdamEntity.this.setAggressive(false);
            this.done = false;
        }
    }

    public class AdamCastingGoalA<T extends Mob & RangedAttackMob> extends Goal {
        AdamEntity AdamEntity;

        private final double speedModifier;
        boolean hasAnimated;
        int animatedTicks;
        int delayTicks;
        int animId;
        boolean done;

        Supplier<Boolean> canUse;

        private int attackDelay = 13;
        private int ticksUntilNextAttack = 13;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public AdamCastingGoalA(AdamEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.AdamEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor  AdamColor = new ParticleColor(150, 0, 255);

        public Spell AdamCastSpell = new Spell()
                .add(AugmentAccelerateThree.INSTANCE)
                .add(AugmentPierce.INSTANCE,8)
                .add(AugmentDurationDown.INSTANCE,8)

                .add(EffectBurst.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(AugmentAOEThree.INSTANCE)
                .add(EffectAnnihilate.INSTANCE)
                .add(AugmentAmplifyThree.INSTANCE,1)

                .add(EffectBurst.INSTANCE)
                .add(FilterNotSelf.INSTANCE)
                .add(EffectAnnihilate.INSTANCE)
                .add(AugmentAmplifyThree.INSTANCE,1)

                .withColor(AdamColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
            projectileSpell.setColor(color);

            projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 1.5f, 0.8f);

            entity.level().addFreshEntity(projectileSpell);

            this.AdamEntity.castingACooldown = random.nextInt(200) + 80;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.AdamEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.AdamEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.AdamEntity.setAggressive(true);
            attackDelay = 13;
            ticksUntilNextAttack = 13;
            
            LivingEntity target = this.AdamEntity.getTarget();

            if (target != null) {
                BlockPos $$0 = target.blockPosition();

                for(int $$1 = 0; $$1 < 3; ++$$1) {
                    int rotation = AdamEntity.this.random.nextInt(360);
                    BlockPos $$3 = $$0.offset((int)(8 * cos((rotation * PI / 180))), 4, (int)(8 * sin((rotation * PI / 180))));

                    if (AdamEntity.this.level().isEmptyBlock($$3)) {
                        AdamEntity.teleportTo($$3.getX(), $$3.getY(), $$3.getZ());
                        break;
                    }
                }

                Vec3 $$1 = target.getEyePosition();
                this.AdamEntity.moveControl.setWantedPosition($$1.x + AdamEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + AdamEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.AdamEntity.setCastingA(false);
            this.AdamEntity.setAggressive(false);
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
            LivingEntity livingentity = this.AdamEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.AdamEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation() && !isTimeToAttack()){
                        performChargeParticleA(this.AdamEntity);
                    }

                    if(isTimeToStartAttackAnimation()) {
                        AdamEntity.setCastingA(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.AdamEntity, 1.0F, AdamCastSpell, AdamColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    AdamEntity.setCastingA(false);
                    AdamEntity.castAAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }

        void performChargeParticleA(LivingEntity entity) {
            Level world = entity.level();

            Vec3 livingEyes = entity.getEyePosition();
            double x = livingEyes.x;
            double y = livingEyes.y;
            double z = livingEyes.z;

            double xOff = cos(entity.getXRot());
            double zOff = sin(entity.getXRot());

            if (world instanceof ServerLevel level) {
                level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x + xOff, y, z + zOff, 1, 0, 0, 0, 0.2);

            }
        }
    }

    public class AdamCastingGoalB<T extends Mob & RangedAttackMob> extends Goal {
        AdamEntity AdamEntity;

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

        int randomSpell = 0;

        public AdamCastingGoalB(AdamEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.AdamEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor ryanColor = new ParticleColor(255, 0, 0);
        private ParticleColor cadeColor = new ParticleColor(150, 150, 255);
        private ParticleColor nickColor = new ParticleColor(0, 150, 0);
        private ParticleColor camColor = new ParticleColor(255, 255, 255);
        private ParticleColor mattColor = new ParticleColor(255, 255, 0);

        public Spell ryanCastSpell = new Spell()
                .add(AugmentAccelerateTwo.INSTANCE)
                .add(EffectIgnite.INSTANCE)
                .add(EffectFlare.INSTANCE)
                .add(AugmentAmplify.INSTANCE)

                .add(EffectExplosion.INSTANCE)
                .add(AugmentAmplify.INSTANCE,4)

                .add(EffectBurst.INSTANCE)
                .add(AugmentAOEThree.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(EffectIgnite.INSTANCE)
                .add(EffectEvaporate.INSTANCE)

                .withColor(ryanColor);

        public Spell cadeCastSpell = new Spell()
                .add(AugmentAccelerateThree.INSTANCE)

                .add(EffectFreeze.INSTANCE)
                .add(AugmentExtendTimeTwo.INSTANCE)
                .add(EffectColdSnap.INSTANCE)
                .add(AugmentAmplify.INSTANCE,8)

                .add(EffectBurst.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(EffectConjureWater.INSTANCE)
                .add(AugmentPierce.INSTANCE,2)
                .add(EffectFreeze.INSTANCE)
                .add(AugmentPierce.INSTANCE,2)

                .add(EffectDelay.INSTANCE)
                .add(EffectIceburst.INSTANCE)
                .add(AugmentAmplify.INSTANCE,3)
                .add(AugmentAOEThree.INSTANCE,2)

                .withColor(cadeColor);

        public Spell nickCastSpell = new Spell()
                .add(AugmentAccelerateThree.INSTANCE)

                .add(EffectBurst.INSTANCE)
                .add(AugmentSensitive.INSTANCE)
                .add(AugmentAOETwo.INSTANCE)
                .add(EffectRaiseEarth.INSTANCE)
                .add(AugmentSensitive.INSTANCE)

                .add(EffectBurst.INSTANCE)
                .add(EffectFracture.INSTANCE)

                .withColor(nickColor);

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

                .withColor(camColor);

        public Spell mattCastSpell = new Spell()
                .add(AugmentAccelerateTwo.INSTANCE)
                .add(AugmentDurationDown.INSTANCE)

                .add(EffectSummonUndead_boss.INSTANCE)
                .add(AugmentSplit.INSTANCE, 2)
                .add(AugmentAmplify.INSTANCE,4)

                .add(EffectConjureBlade.INSTANCE)
                .add(AugmentAOEThree.INSTANCE)
                .add(AugmentAmplifyThree.INSTANCE, 16)

                .withColor(mattColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            EntityProjectileSpell projectileSpell = new EntityProjectileSpell(entity.level(), resolver);
            projectileSpell.setColor(color);

            projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 1.5f, 0.8f);

            entity.level().addFreshEntity(projectileSpell);

            this.AdamEntity.castingBCooldown = random.nextInt(100) + 200;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.AdamEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.AdamEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.AdamEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 15;

            LivingEntity $$0 = this.AdamEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.AdamEntity.moveControl.setWantedPosition($$1.x + AdamEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + AdamEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.AdamEntity.setCastingB(false);
            this.AdamEntity.setAggressive(false);
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
            LivingEntity livingentity = this.AdamEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.AdamEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        AdamEntity.setCastingB(true);
                    }

                    if(isTimeToAttack()) {
                        randomSpell = random.nextInt(100);
                        if(randomSpell > 80){
                            performCastAttack(this.AdamEntity, 1.0F, ryanCastSpell, ryanColor);
                        } else if (randomSpell > 60) {
                            performCastAttack(this.AdamEntity, 1.0F, cadeCastSpell, cadeColor);
                        } else if (randomSpell > 40){
                            performCastAttack(this.AdamEntity, 1.0F, nickCastSpell, nickColor);
                        } else if (randomSpell > 20){
                            performCastAttack(this.AdamEntity, 1.0F, camCastSpell, camColor);
                        } else {
                            performCastAttack(this.AdamEntity, 1.0F, mattCastSpell, mattColor);
                        }

                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    AdamEntity.setCastingB(false);
                    AdamEntity.castBAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    public class AdamCastingGoalC<T extends Mob & RangedAttackMob> extends Goal {
        AdamEntity AdamEntity;

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

        public AdamCastingGoalC(AdamEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.AdamEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.attackRadiusSqr = attackRange * attackRange;

        }
        private ParticleColor  AdamColor = new ParticleColor(150, 0, 255);

        public Spell AdamCastSpell = new Spell()
                .add(AugmentDurationDown.INSTANCE, 2)

                .add(EffectLimitless.INSTANCE)
                .add(AugmentPierce.INSTANCE)
                .add(AugmentExtendTime.INSTANCE)

                .add(AugmentAmplifyThree.INSTANCE, 1)
                .add(AugmentAOEThree.INSTANCE, 1)
                .add(AugmentExtendTimeThree.INSTANCE, 1)

                .withColor(AdamColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            this.AdamEntity.castingCCooldown = random.nextInt(400) + 300;
            int time = 100;

            Vec3 pos = this.AdamEntity.position();

            AdamsArsPlus.setInterval(() -> {

                DetonateProjectile projectileSpell = new DetonateProjectile(entity.level(), resolver);
                projectileSpell.setColor(color);
                projectileSpell.shoot(entity, 90, 0, 0.0F, 0.5f, 0.8f);
                projectileSpell.setPos(pos.add(AdamEntity.this.random.nextInt(19) - 9, AdamEntity.this.random.nextInt(19), AdamEntity.this.random.nextInt(19) - 9));
                entity.level().addFreshEntity(projectileSpell);

            }, 10, time, () -> !this.AdamEntity.isAlive());
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.AdamEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.AdamEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.AdamEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 15;

            LivingEntity $$0 = this.AdamEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.AdamEntity.moveControl.setWantedPosition($$1.x + AdamEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + AdamEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.AdamEntity.setCastingB(false);
            this.AdamEntity.setAggressive(false);
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
            LivingEntity livingentity = this.AdamEntity.getTarget();
            if (livingentity != null) {
                double d0 = this.AdamEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean canSeeEnemy = this.AdamEntity.getSensing().hasLineOfSight(livingentity);
                if (canSeeEnemy != this.seeTime > 0) {
                    this.seeTime = 0;
                }

                if (canSeeEnemy) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.AdamEntity.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.AdamEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 10) {
                    if ((double)this.AdamEntity.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.AdamEntity.getRandom().nextFloat() < 0.3) {
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

                    this.AdamEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.AdamEntity.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.AdamEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if (this.seeTime >= 20 && !this.hasAnimated) {
                    this.hasAnimated = true;
                    Networking.sendToNearby(this.AdamEntity.level(), this.AdamEntity, new PacketAnimEntity(this.AdamEntity.getId(), this.animId));
                }

                if (this.hasAnimated) {
                    shouldCountTillNextAttack = true;
                    this.AdamEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        AdamEntity.setCastingB(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.AdamEntity, 1.0F, AdamCastSpell, AdamColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    AdamEntity.setCastingB(false);
                    AdamEntity.castBAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    public class AdamCastingGoalD<T extends Mob & RangedAttackMob> extends Goal {
        AdamEntity AdamEntity;

        private final double speedModifier;
        boolean hasAnimated;
        int animatedTicks;
        int delayTicks;
        int animId;
        boolean done;

        Supplier<Boolean> canUse;

        private int attackDelay = 13;
        private int ticksUntilNextAttack = 13;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public AdamCastingGoalD(AdamEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.AdamEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }
        private ParticleColor  AdamColor = new ParticleColor(0, 0, 0);

        public Spell AdamCastSpell = new Spell()
                .add(EffectBurst.INSTANCE)
                .add(AugmentAOEThree.INSTANCE)
                .add(FilterNotSelf.INSTANCE)

                .add(EffectSnare.INSTANCE)
                .add(AugmentExtendTime.INSTANCE)
                .add(EffectGravity.INSTANCE)
                .add(AugmentExtendTime.INSTANCE)
                .add(EffectSwapTarget.INSTANCE)
                .add(EffectCraft.INSTANCE)

                .withColor(AdamColor);

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));
            EntityProjectileSpell projectileSpell = new EntityProjectileSpell(entity.level(), resolver);
            projectileSpell.setColor(color);

            projectileSpell.shoot(entity, entity.getXRot(), entity.getYHeadRot(), 0.0F, 3f, 0.8f);

            entity.level().addFreshEntity(projectileSpell);

            this.AdamEntity.castingDCooldown = random.nextInt(200) + 200;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.AdamEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.AdamEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.AdamEntity.setAggressive(true);
            attackDelay = 13;
            ticksUntilNextAttack = 13;

            LivingEntity target = this.AdamEntity.getTarget();

            if (target != null) {
                BlockPos $$0 = target.blockPosition();

                for(int $$1 = 0; $$1 < 3; ++$$1) {
                    int rotation = AdamEntity.this.random.nextInt(360);
                    BlockPos $$3 = $$0.offset((int)(8 * cos((rotation * PI / 180))), 4, (int)(8 * sin((rotation * PI / 180))));

                    if (AdamEntity.this.level().isEmptyBlock($$3)) {
                        AdamEntity.teleportTo($$3.getX(), $$3.getY(), $$3.getZ());
                        break;
                    }
                }

                Vec3 $$1 = target.getEyePosition();
                this.AdamEntity.moveControl.setWantedPosition($$1.x + AdamEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + AdamEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.AdamEntity.setCastingA(false);
            this.AdamEntity.setAggressive(false);
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
            LivingEntity livingentity = this.AdamEntity.getTarget();
            if (livingentity != null) {
                if(true){
                    shouldCountTillNextAttack = true;
                    this.AdamEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        AdamEntity.setCastingA(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.AdamEntity, 1.0F, AdamCastSpell, AdamColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    AdamEntity.setCastingA(false);
                    AdamEntity.castAAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

    public class AdamDomainGoalB<T extends Mob & RangedAttackMob> extends Goal {
        AdamEntity AdamEntity;

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

        public AdamDomainGoalB(AdamEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
            this.AdamEntity = entity;
            this.speedModifier = speed;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.attackRadiusSqr = attackRange * attackRange;

        }
        private ParticleColor  AdamColor = new ParticleColor(0, 0, 0);

        public Spell AdamCastSpell = new Spell()
                .add(EffectDomain.INSTANCE)
                .add(AugmentExtendTimeThree.INSTANCE)
                .add(AugmentAOEThree.INSTANCE, 4)
                .add(AugmentAccelerateThree.INSTANCE, 2)
                .add(AugmentExtract.INSTANCE)

                .add(FilterNotSelf.INSTANCE)
                .add(EffectSnare.INSTANCE)
                .add(AugmentExtendTime.INSTANCE)
                .add(EffectSwapTarget.INSTANCE)
                .add(EffectCraft.INSTANCE)

                .withColor(AdamColor);

        void performDomainAttack(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));

            this.AdamEntity.domainbCooldown = random.nextInt(2000) + 1000;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.AdamEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.AdamEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.AdamEntity.setAggressive(true);
            attackDelay = 10;
            ticksUntilNextAttack = 10;

            LivingEntity $$0 = this.AdamEntity.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                this.AdamEntity.moveControl.setWantedPosition($$1.x + AdamEntity.this.random.nextInt(15) - 7, $$1.y + 3, $$1.z + AdamEntity.this.random.nextInt(15) - 7, (double)this.speedModifier);
            }
        }

        public void stop() {
            super.stop();
            this.AdamEntity.setUsingDomain(false);
            this.AdamEntity.setAggressive(false);
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
            LivingEntity livingentity = this.AdamEntity.getTarget();
            if (livingentity != null) {
                double d0 = this.AdamEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean canSeeEnemy = this.AdamEntity.getSensing().hasLineOfSight(livingentity);
                if (canSeeEnemy != this.seeTime > 0) {
                    this.seeTime = 0;
                }

                if (canSeeEnemy) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.AdamEntity.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.AdamEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 10) {
                    if ((double)this.AdamEntity.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.AdamEntity.getRandom().nextFloat() < 0.3) {
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

                    this.AdamEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.AdamEntity.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.AdamEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if (this.seeTime >= 20 && !this.hasAnimated) {
                    this.hasAnimated = true;
                    Networking.sendToNearby(this.AdamEntity.level(), this.AdamEntity, new PacketAnimEntity(this.AdamEntity.getId(), this.animId));
                }

                if (this.hasAnimated) {
                    shouldCountTillNextAttack = true;
                    this.AdamEntity.getLookControl().setLookAt(livingentity);

                    if(isTimeToStartAttackAnimation()) {
                        AdamEntity.setUsingDomain(true);
                    }

                    if(isTimeToAttack()) {
                        performDomainAttack(this.AdamEntity, 1.0F, AdamCastSpell, AdamColor);
                        this.done = true;
                        resetAttackLoopCooldown();
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    AdamEntity.setUsingDomain(false);
                    AdamEntity.castDomainAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }
    }

}
