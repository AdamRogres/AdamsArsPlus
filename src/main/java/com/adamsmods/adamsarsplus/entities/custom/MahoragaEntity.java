package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.entities.DetonateProjectile;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateTwo;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAmplifyThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentLesserAOE;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectAnnihilate;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectFracture;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectLimitless;
import com.adamsmods.adamsarsplus.registry.AdamCapabilityRegistry;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.IFollowingSummon;
import com.hollingsworth.arsnouveau.common.entity.goal.FollowSummonerGoal;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
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
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.*;
import static java.lang.Math.*;
import static net.minecraft.world.effect.MobEffects.REGENERATION;
import static net.minecraft.world.item.Items.NETHERITE_SWORD;

public class MahoragaEntity extends Monster implements IFollowingSummon, ISummon {
    // Ten Shadows Reward
    public boolean ritualStatus;
    public LivingEntity[] attackersList = {null, null};
    public boolean isSummon;
    private LivingEntity owner;
    public int ticksLeft;
    public boolean sealed;
    public String sealTexture;
    public int sealCount;
    public boolean leftRight;

    public int attackBCooldown;
    public int attackB2Cooldown;
    public int attackCCooldown;
    public int rangedAttackCooldown;

    public int adaptCooldown;
    public int adaptPoints;
    public int adaptPointsTotal;

    public int regenCount = 0;
    public DamageType[] adaptedDamageTypes = {null, null, null, null, null, null, null, null};
    public int[] adaptedDamageStage = {0,0,0,0,0,0,0,0};
    public MobEffect[] adaptedEffects = {null, null, null, null, null, null, null, null};
    public boolean canRangedAttack = false;
    public boolean canSlashAttack = false;
    public boolean canDisrupt = false;

    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    public static final EntityDataAccessor<Boolean> ATTACKINGA =
            SynchedEntityData.defineId(MahoragaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKINGBAA =
            SynchedEntityData.defineId(MahoragaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKINGBAB =
            SynchedEntityData.defineId(MahoragaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKINGBBA =
            SynchedEntityData.defineId(MahoragaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKINGC =
            SynchedEntityData.defineId(MahoragaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ROAR =
            SynchedEntityData.defineId(MahoragaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> WHEEL =
            SynchedEntityData.defineId(MahoragaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> SEALED =
            SynchedEntityData.defineId(MahoragaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> UNSEALED =
            SynchedEntityData.defineId(MahoragaEntity.class, EntityDataSerializers.BOOLEAN);

    public MahoragaEntity(Level level, LivingEntity owner, boolean summon) {
        super((EntityType) AdamsModEntities.MAHORAGA.get(), level);

        ItemStack weapon = NETHERITE_SWORD.asItem().getDefaultInstance();
        weapon.enchant(Enchantments.SMITE, 10);

        this.owner = owner;
        this.setOwnerID(owner.getUUID());
        this.isSummon = summon;
        this.ritualStatus = false;
        this.setWeapon(weapon);
        this.leftRight = false;

        this.attackBCooldown = 0;
        this.attackB2Cooldown = 0;
        this.rangedAttackCooldown = 0;
        this.attackCCooldown = 0;

        this.adaptCooldown = 400;
        this.adaptPoints = 0;
        this.adaptPointsTotal = 0;
    }

    public MahoragaEntity(Level level, boolean summon) {
        super((EntityType) AdamsModEntities.MAHORAGA.get(), level);

        ItemStack weapon = NETHERITE_SWORD.asItem().getDefaultInstance();
        weapon.enchant(Enchantments.SMITE, 10);

        this.isSummon = summon;
        this.ritualStatus = false;
        this.sealed = true;
        this.sealCount = 0;
        this.setWeapon(weapon);
        this.leftRight = false;

        this.attackBCooldown = 0;
        this.attackB2Cooldown = 0;
        this.rangedAttackCooldown = 0;
        this.attackCCooldown = 0;

        this.adaptCooldown = 400;
        this.adaptPoints = 0;
        this.adaptPointsTotal = 0;
    }

    public MahoragaEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityType<?> getType() {
        return (EntityType) AdamsModEntities.MAHORAGA.get();
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public final AnimationState walkAnimationState = new AnimationState();
    private int walkAnimationTimeout = 0;
    public final AnimationState attackAAnimationState = new AnimationState();
    private int attackAAnimationTimeout = 0;
    public final AnimationState attackBAAAnimationState = new AnimationState();
    private int attackBAAAnimationTimeout = 0;
    public final AnimationState attackBABAnimationState = new AnimationState();
    private int attackBABAnimationTimeout = 0;
    public final AnimationState attackBBAAnimationState = new AnimationState();
    private int attackBBAAnimationTimeout = 0;
    public final AnimationState attackCAnimationState = new AnimationState();
    private int attackCAnimationTimeout = 0;
    public final AnimationState attackRoarAnimationState = new AnimationState();
    private int attackRoarAnimationTimeout = 0;
    public final AnimationState wheelAnimationState = new AnimationState();
    private int wheelAnimationTimeout = 0;
    public final AnimationState sealedAnimationState = new AnimationState();
    private int sealedAnimationTimeout = 0;
    public final AnimationState unsealedAnimationState = new AnimationState();
    private int unsealedAnimationTimeout = 0;

    public void setAttackingA(boolean attacking) {
        this.entityData.set(ATTACKINGA, attacking);
    }
    public boolean isAttackingA() {
        return this.entityData.get(ATTACKINGA);
    }
    public void setAttackingBAA(boolean attacking) {
        this.entityData.set(ATTACKINGBAA, attacking);
    }
    public boolean isAttackingBAA() {
        return this.entityData.get(ATTACKINGBAA);
    }
    public void setAttackingBAB(boolean attacking) {
        this.entityData.set(ATTACKINGBAB, attacking);
    }
    public boolean isAttackingBAB() {
        return this.entityData.get(ATTACKINGBAB);
    }
    public void setAttackingBBA(boolean attacking) {
        this.entityData.set(ATTACKINGBBA, attacking);
    }
    public boolean isAttackingBBA() {
        return this.entityData.get(ATTACKINGBBA);
    }
    public void setAttackingC(boolean attacking) {
        this.entityData.set(ATTACKINGC, attacking);
    }
    public boolean isAttackingC() {
        return this.entityData.get(ATTACKINGC);
    }
    public void setRoar(boolean attacking) {
        this.entityData.set(ROAR, attacking);
    }
    public boolean isRoar() {
        return this.entityData.get(ROAR);
    }
    public void setWheel(boolean attacking) { this.entityData.set(WHEEL, attacking); }
    public boolean isWheel() { return this.entityData.get(WHEEL); }
    public void setSealed(boolean attacking) {
        this.entityData.set(SEALED, attacking);
    }
    public boolean isSealed() {
        return this.entityData.get(SEALED);
    }
    public void setUnsealed(boolean attacking) {
        this.entityData.set(UNSEALED, attacking);
    }
    public boolean isUnsealed() {
        return this.entityData.get(UNSEALED);
    }

    public void startSeenByPlayer(ServerPlayer pPlayer) {
        super.startSeenByPlayer(pPlayer);
        this.bossEvent.addPlayer(pPlayer);
    }

    public void stopSeenByPlayer(ServerPlayer pPlayer) {
        super.stopSeenByPlayer(pPlayer);
        this.bossEvent.removePlayer(pPlayer);
    }

    @Override
    public void tick() {
        super.tick();

        if(attackBCooldown > 0)     { attackBCooldown--; }
        if(attackB2Cooldown > 0)    { attackB2Cooldown--; }
        if(this.canSlashAttack)     { attackCCooldown++; }

        // Adaptive Regeneration
        if(this.getHealth() < this.getMaxHealth() * 0.2){
            if(canAdaptCheck(this)){
                this.regenCount++;
            }
        }
        if(this.regenCount > 0 && !this.hasEffect(REGENERATION)){
            this.addEffect(new MobEffectInstance((MobEffect) REGENERATION, 40, regenCount - 1, false, false));
        }

        // Adaptive Effect Removal
        if(effectAdaptCheck(this)){}

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
            // Unsealing Ritual "Cutscene"
            if(this.sealed){
                if(this.sealCount <= 30 && this.level().getGameTime() % 5 == 0){
                    spawnChains((ServerLevel) this.level(), this.blockPosition(), 45);
                    spawnChains((ServerLevel) this.level(), this.blockPosition(), 135);
                    spawnChains((ServerLevel) this.level(), this.blockPosition(), 225);
                    spawnChains((ServerLevel) this.level(), this.blockPosition(), 315);
                }

                if(this.sealCount == 0){
                    this.setSealed(true);
                }
                if(this.sealCount == 40){
                    this.setUnsealed(true);
                    this.setSealed(false);
                }
                if(this.sealCount >= 75){
                    this.setUnsealed(false);
                    this.sealed = false;
                }
                this.sealCount++;
            } else {
                this.setSealed(false);
                this.setUnsealed(false);
            }

            //Adaptation Control
            if(this.adaptCooldown > 0){
                this.adaptCooldown--;

                if(this.adaptCooldown < 390){
                    this.setWheel(false);
                }
            } else {
                this.adaptPoints++;
                this.adaptPointsTotal++;
                this.adaptCooldown = 400;

                this.setWheel(true);
                this.playSound(SoundEvents.IRON_DOOR_OPEN, 1.5F, 1F);
            }
        }

        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    private void setupAnimationStates() {

        if (this.idleAnimationTimeout <= 0 && !(this.isSealed() || this.isUnsealed())) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        // Unsealing Animation Control
        if(this.isSealed()){
            this.sealTexture = "_sealed";
        } else {
            this.sealTexture = "";
        }

        if (this.sealedAnimationTimeout <= 0 && this.isSealed()) {
            this.sealedAnimationTimeout = 40;
            this.sealedAnimationState.start(this.tickCount);
        } else {
            --this.sealedAnimationTimeout;
        }
        if(!isSealed()){
            this.sealedAnimationState.stop();
        }
        if (this.unsealedAnimationTimeout <= 0 && this.isUnsealed()) {
            this.unsealedAnimationTimeout = 35;
            this.unsealedAnimationState.start(this.tickCount);
        } else {
            --this.unsealedAnimationTimeout;
        }
        if(!isUnsealed()){
            this.unsealedAnimationState.stop();
        }

        //AttackA Animation control
        if(this.isAttackingA() && attackAAnimationTimeout <= 0) {
            attackAAnimationTimeout = 20;
            attackAAnimationState.start(this.tickCount);
        } else {
            --this.attackAAnimationTimeout;
        }
        if(!this.isAttackingA()) {
            attackAAnimationState.stop();
        }
        //AttackBAA Animation control
        if(this.isAttackingBAA() && attackBAAAnimationTimeout <= 0) {
            attackBAAAnimationTimeout = 10;
            attackBAAAnimationState.start(this.tickCount);
        } else {
            --this.attackBAAAnimationTimeout;
        }
        if(!this.isAttackingBAA()) {
            attackBAAAnimationState.stop();
        }
        //AttackBAB Animation control
        if(this.isAttackingBAB() && attackBABAnimationTimeout <= 0) {
            attackBABAnimationTimeout = 10;
            attackBABAnimationState.start(this.tickCount);
        } else {
            --this.attackBABAnimationTimeout;
        }
        if(!this.isAttackingBAB()) {
            attackBABAnimationState.stop();
        }
        //AttackBBA Animation control
        if(this.isAttackingBBA() && attackBBAAnimationTimeout <= 0) {
            attackBBAAnimationTimeout = 30;
            attackBBAAnimationState.start(this.tickCount);
        } else {
            --this.attackBBAAnimationTimeout;
        }
        if(!this.isAttackingBBA()) {
            attackBBAAnimationState.stop();
        }
        //AttackC Animation control
        if(this.isAttackingC() && attackCAnimationTimeout <= 0) {
            attackCAnimationTimeout = 15;
            attackCAnimationState.start(this.tickCount);
        } else {
            --this.attackCAnimationTimeout;
        }
        if(!this.isAttackingC()) {
            attackCAnimationState.stop();
        }
        //AttackRoar Animation control
        if(this.isRoar() && attackRoarAnimationTimeout <= 0) {
            attackRoarAnimationTimeout = 20;
            attackRoarAnimationState.start(this.tickCount);
        } else {
            --this.attackRoarAnimationTimeout;
        }
        if(!this.isRoar()) {
            attackRoarAnimationState.stop();
        }
        //Wheel Animation control
        if(this.isWheel() && wheelAnimationTimeout <= 0) {
            wheelAnimationTimeout = 10;
            wheelAnimationState.start(this.tickCount);
        } else {
            --this.wheelAnimationTimeout;
        }
        if(!this.isWheel()) {
            wheelAnimationState.stop();
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
        if(this.isAttackingA() || this.isAttackingBAA() || this.isAttackingBAB() || this.isAttackingBBA() || this.isAttackingC() || this.isRoar() || this.isSealed() || this.isUnsealed()){
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(MahoragaEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UUID, Optional.of(Util.NIL_UUID));
        this.entityData.define(ATTACKINGA, false);
        this.entityData.define(ATTACKINGBAA, false);
        this.entityData.define(ATTACKINGBAB, false);
        this.entityData.define(ATTACKINGBBA, false);
        this.entityData.define(ATTACKINGC, false);
        this.entityData.define(ROAR, false);
        this.entityData.define(WHEEL, false);
        this.entityData.define(SEALED, false);
        this.entityData.define(UNSEALED, false);
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        onSummonDeath(level(), cause, false);

        if (!this.ritualStatus && !this.isSummon) {
            if (this.attackersList[0] instanceof Player player) {
                AdamCapabilityRegistry.getTsTier(player).ifPresent((pRank) -> {
                    if (pRank.getTsTier() >= 3) {
                        pRank.setTsTier(Math.max(4, pRank.getTsTier()));
                        PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.tenshadows.maho_tamed"));
                    } else {
                        PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.tenshadows.tame_failed"));
                    }
                });
            }
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        Entity var4 = pSource.getEntity();

        if(this.sealed){
            return false;
        }

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

        // Damage Adaption
        pAmount = damageAdaptResults(this, pSource, pAmount);
        if(pAmount <= 0){
            return false;
        }
        if(damageAdaptCheck(this,pSource)){
            this.adaptCooldown = Math.max(0, this.adaptCooldown - 80);
        }

        // Tenshadows Ritual
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

        compound.putInt("attackB_CD", attackBCooldown);
        compound.putInt("attackB2_CD", attackB2Cooldown);
        compound.putInt("adapt_CD", adaptCooldown);
        compound.putInt("adaptPoints", adaptPointsTotal);
        compound.putInt("attackC_CD", attackCCooldown);

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

        this.attackBCooldown = compound.getInt("attackB_CD");
        this.attackB2Cooldown = compound.getInt("attackB2_CD");
        this.attackCCooldown = compound.getInt("attackC_CD");

        this.adaptCooldown = compound.getInt("adapt_CD");
        this.adaptPointsTotal = compound.getInt("adaptPoints");
        this.adaptPoints = this.adaptPointsTotal;

        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    public void setCustomName(@javax.annotation.Nullable Component pName) {
        super.setCustomName(pName);
        this.bossEvent.setName(this.getDisplayName());
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

        this.goalSelector.addGoal(1, new MahoragaRitualGoal(this, () -> this.sealed));
        this.goalSelector.addGoal(2, new FollowSummonerGoal(this, this.owner, (double) 1.0F, 30.0F, 3.0F));
        this.goalSelector.addGoal(3, new MahoragaAttackGoalC(this, 1.0D, true, () -> (this.attackCCooldown > 300 && this.canSlashAttack), 100));
        this.goalSelector.addGoal(4, new MahoragaRangeGoal<>(this, 1.0D, 40f, () -> this.rangedAttackCooldown > 100 && this.canRangedAttack));
        this.goalSelector.addGoal(9, new MahoragaAttackGoalBBA(this, 1, true, () -> this.attackB2Cooldown > 0));
        this.goalSelector.addGoal(10, new MahoragaAttackGoalBAA(this, 1, true, () -> this.attackBCooldown > 0, () -> this.leftRight));
        this.goalSelector.addGoal(11, new MahoragaAttackGoalA(this, 1, true, () -> true));
        this.goalSelector.addGoal(12, new FollowSummonerGoal(this, this.owner, (double) 1.0F, 9.0F, 3.0F));

        this.goalSelector.addGoal(13, new WaterAvoidingRandomStrollGoal(this, (double) 1.0F));
        this.goalSelector.addGoal(14, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(15, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[]{MahoragaEntity.class}) {
            protected boolean canAttack(@Nullable LivingEntity pPotentialTarget, TargetingConditions pTargetPredicate) {
                return pPotentialTarget != null && super.canAttack(pPotentialTarget, pTargetPredicate) && !pPotentialTarget.getUUID().equals(MahoragaEntity.this.getOwnerUUID()) && !pPotentialTarget.getUUID().equals(MahoragaEntity.this.getUUID());
            }
        });
        this.targetSelector.addGoal(1, new CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Villager.class, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, Mob.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 500D)
                .add(Attributes.ATTACK_DAMAGE, (double) 10.0F)
                .add(Attributes.MOVEMENT_SPEED, (double) 0.35F)
                .add(Attributes.FOLLOW_RANGE, (double) 90.0F)
                .add(Attributes.ATTACK_KNOCKBACK, (double) 1.0F)
                .add(Attributes.ARMOR, 20)
                .add(Attributes.ARMOR_TOUGHNESS, 8)
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

    // Mahoraga Stuff
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WARDEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WARDEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }

    public boolean canAdaptCheck(MahoragaEntity entity){
        boolean ret = false;

        if(entity.adaptPoints > 0){
            ret = true;
            entity.adaptPoints--;

            entity.playSound(SoundEvents.AMETHYST_BLOCK_RESONATE);
        }

        return ret;
    }

    public boolean effectAdaptCheck(MahoragaEntity entity){

        for(int n = 0; n < 8; n++){
            for(int i = 0; i < entity.getActiveEffects().size(); i++){
                if(entity.getActiveEffects().stream().toList().get(i).getEffect() == entity.adaptedEffects[n]){
                    entity.removeEffect(entity.adaptedEffects[n]);
                    return true;
                } else if(entity.adaptedEffects[n] == null && !entity.getActiveEffects().stream().toList().get(i).getEffect().isBeneficial()){
                    if(canAdaptCheck(entity)){
                        entity.adaptedEffects[n] = entity.getActiveEffects().stream().toList().get(i).getEffect();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public float damageAdaptResults(MahoragaEntity entity, DamageSource damageSource, float Amount){
        for(int i = 0; i < 8; i++){
            if(entity.adaptedDamageTypes[i] == damageSource.type()){
                if(entity.adaptedDamageStage[i] == 3){
                    Amount = 0;
                } else {
                    Amount = Amount / (2 * entity.adaptedDamageStage[i]);
                }
            }
        }
        return Amount;
    }

    public boolean damageAdaptCheck(MahoragaEntity entity, DamageSource damageSource){
        for(int i = 0; i < 8; i++){
            if(entity.adaptedDamageTypes[i] == damageSource.type() && entity.adaptedDamageStage[i] < 3){
                if(canAdaptCheck(entity)){
                    entity.adaptedDamageStage[i]++;
                }
                return true;
            } else if(entity.adaptedDamageTypes[i] == damageSource.type() && entity.adaptedDamageStage[i] == 3){
                return true;
            }
        }
        for(int i = 0; i < 8; i++){
            if(entity.adaptedDamageTypes[i] == null){
                if(canAdaptCheck(entity)){
                    entity.adaptedDamageTypes[i] = damageSource.type();
                    entity.adaptedDamageStage[i] = 1;
                }
                return true;
            }
        }
        return true;
    }

    public void applyDisruption(MahoragaEntity entity, LivingEntity target){
        if(entity.canDisrupt){
            target.addEffect(new MobEffectInstance(DISRUPTION_EFFECT.get(), 200));
        } else {
            for(int i = 0; i < target.getActiveEffects().size(); i++){
                if(target.getActiveEffects().stream().toList().get(i).getEffect().isBeneficial()){
                   if(entity.canAdaptCheck(entity)){
                       entity.canDisrupt = true;
                   }
                   break;
                }
            }
        }
    }

    public static void spawnChains(ServerLevel world, BlockPos pos, int angle) {
        float x = (float)Math.cos(angle * PI/360);
        float z = (float)Math.sin(angle * PI/360);

        for (int i = 0; i < 10; ++i) {

            double d0 = (double) pos.getX() + (i * 0.3) * x;
            double d1 = (double) pos.getY() - (i * 0.3) + 3;
            double d2 = (double) pos.getZ() + (i * 0.3) * z;
            world.sendParticles(ParticleTypes.END_ROD, d0, d1, d2, 1, (double) (world.random.nextFloat() * 1.0F)/10, (double) (world.random.nextFloat() * 1.0F)/10, (double) (world.random.nextFloat() * 1.0F)/10, 0);
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(pLevel.getRandom(), pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
    }

    protected void dropAllDeathLoot(DamageSource pDamageSource) {
    }

    protected boolean shouldDropLoot() {
        return false;
    }

    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
    }

    protected void dropEquipment() {
    }

    public void setWeapon(ItemStack item) {
        this.setItemSlot(EquipmentSlot.MAINHAND, item);
    }

    public class MahoragaRitualGoal extends Goal{
        private final MahoragaEntity entity;
        Supplier<Boolean> canUse;

        public MahoragaRitualGoal(Mob pMob, Supplier<Boolean> canUse) {
            entity = ((MahoragaEntity) pMob);
            this.canUse = canUse;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get();
        }

        public boolean canContinueToUse() {
            return (Boolean)this.canUse.get();
        }

        public void tick() {
            BlockPos $$0 = this.entity.blockPosition();

            this.entity.setTarget(null);

            this.entity.moveControl.setWantedPosition((double)$$0.getX(), (double)$$0.getY(), (double)$$0.getZ(), (double)0F);
            this.entity.getLookControl().setLookAt((double)$$0.getX(), (double)$$0.getY() + (double)5F, (double)$$0.getZ(), 180.0F, 20.0F);
        }

    }

    public class MahoragaAttackGoalA extends MeleeAttackGoal {
        private final MahoragaEntity entity;

        private int attackDelay = 16;
        private int ticksUntilNextAttack = 16;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        Supplier<Boolean> canUse;
        boolean done;

        public MahoragaAttackGoalA(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((MahoragaEntity) pMob);
            this.canUse = canUse;
        }

        @Override
        public void start() {
            super.start();
            attackDelay = 16;
            ticksUntilNextAttack = 16;

            this.entity.rangedAttackCooldown = 0;
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
                    entity.setAttackingA(true);
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
            return (double)(this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() * 2.0F + 4.5F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;

            this.entity.applyDisruption(this.entity, pEnemy);

            if(this.entity.level().random.nextInt(0,100) < 40){
                this.entity.attackBCooldown = 40;
            }

            this.entity.rangedAttackCooldown = 0;

            if(!this.entity.canSlashAttack && this.entity.level().random.nextInt(0,100) < 15){
                if(canAdaptCheck(this.entity)){
                    this.entity.canSlashAttack = true;
                }
            }
        }

        void performSpellAttack(LivingEntity entity, Spell spell, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));
        }

        @Override
        public void tick() {
            super.tick();
            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

            this.entity.rangedAttackCooldown++;
            if(this.entity.rangedAttackCooldown > 200 && !this.entity.canRangedAttack){
                if(this.entity.canAdaptCheck(this.entity)){
                    this.entity.canRangedAttack = true;
                }
            }
        }

        @Override
        public void stop() {
            entity.setAttackingA(false);
            this.done = false;
            super.stop();
        }
    }

    public class MahoragaAttackGoalBAA extends MeleeAttackGoal {
        private final MahoragaEntity entity;

        private int attackDelay = 7;
        private int ticksUntilNextAttack = 7;
        private int totalAnimation = 10;
        private boolean shouldCountTillNextAttack = false;

        Supplier<Boolean> canUse;
        Supplier<Boolean> leftRight;
        boolean done;

        public MahoragaAttackGoalBAA(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse, Supplier<Boolean> leftRight) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((MahoragaEntity) pMob);
            this.canUse = canUse;
            this.leftRight = leftRight;
        }

        @Override
        public void start() {
            super.start();
            attackDelay = 7;
            ticksUntilNextAttack = 7;
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
                    if(leftRight.get()){
                        entity.setAttackingBAB(true);
                    } else {
                        entity.setAttackingBAA(true);
                    }
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());

                    performAttack(pEnemy);
                    performSpellAttack(this.mob, punchAttackSpell, punchColor, pEnemy);

                    if(leftRight.get()){
                        entity.leftRight = false;
                    } else {
                        entity.leftRight = true;
                    }

                    if(pEnemy.isBlocking()){
                        if(pEnemy instanceof Player playerEnemy){
                            this.entity.attackBCooldown += 40;
                        }
                    }
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttackingBAA(false);
                entity.setAttackingBAB(false);
                entity.attackBAAAnimationTimeout = 0;
                entity.attackBABAnimationTimeout = 0;
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
            return (double)(this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() * 2.0F + 2.0F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.doHurtTargetNW(pEnemy);
            this.done = true;

            this.entity.applyDisruption(this.entity, pEnemy);

            if(this.entity.level().getRandom().nextInt(0,100) < 85){
                this.entity.attackBCooldown = 40;
            } else {
                this.entity.attackB2Cooldown = 80;
            }
        }

        void performSpellAttack(LivingEntity entity, Spell spell, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));
        }

        private ParticleColor punchColor = new ParticleColor(255, 255, 255);

        public Spell punchAttackSpell = new Spell()
                .add(EffectLimitless.INSTANCE)
                .add(AugmentAOE.INSTANCE, 4)
                .withColor(punchColor);

        @Override
        public void tick() {
            super.tick();
            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }

        @Override
        public void stop() {
            entity.setAttackingBAA(false);
            entity.setAttackingBAB(false);
            this.done = false;
            super.stop();
        }

        public boolean doHurtTargetNW(Entity pEntity) {
            float f = (float)this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float f1 = (float)this.mob.getAttributeValue(Attributes.ATTACK_KNOCKBACK);

            boolean flag = pEntity.hurt(this.mob.damageSources().mobAttack(this.mob), f);
            if (flag) {
                if (f1 > 0.0F && pEntity instanceof LivingEntity) {
                    ((LivingEntity)pEntity).knockback((double)(f1 * 0.5F), (double) Mth.sin(this.mob.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.mob.getYRot() * ((float)Math.PI / 180F))));
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().multiply(0.6, (double)1.0F, 0.6));
                }
                this.mob.setLastHurtMob(pEntity);
            }
            return flag;
        }
    }

    public class MahoragaAttackGoalBBA extends MeleeAttackGoal {
        private final MahoragaEntity entity;

        private int attackDelay = 20;
        private int ticksUntilNextAttack = 20;
        private int totalAnimation = 30;
        private boolean shouldCountTillNextAttack = false;

        Supplier<Boolean> canUse;
        boolean done;

        public MahoragaAttackGoalBBA(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((MahoragaEntity) pMob);
            this.canUse = canUse;
        }

        @Override
        public void start() {
            super.start();
            attackDelay = 20;
            ticksUntilNextAttack = 20;
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
                    entity.setAttackingBBA(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());

                    performAttack(pEnemy);
                    pEnemy.invulnerableTime = 0;
                    performSpellAttack(this.mob, punchAttackSpell, punchColor, this.mob);
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttackingBBA(false);
                entity.attackBBAAnimationTimeout = 0;
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
            return (double)(this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() * 2.0F + 4.5F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.doHurtTargetNW(pEnemy);
            this.done = true;

            this.entity.applyDisruption(this.entity, pEnemy);

            this.entity.attackB2Cooldown = 0;
        }

        void performSpellAttack(LivingEntity entity, Spell spell, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));
        }

        private ParticleColor punchColor = new ParticleColor(255, 255, 255);

        public Spell punchAttackSpell = new Spell()
                .add(EffectExplosion.INSTANCE)
                .add(AugmentAmplify.INSTANCE, 3)

                .add(EffectBurst.INSTANCE)
                .add(AugmentAOE.INSTANCE, 3)
                .add(EffectFracture.INSTANCE)

                .withColor(punchColor);

        @Override
        public void tick() {
            super.tick();
            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }

        @Override
        public void stop() {
            entity.setAttackingBBA(false);
            this.done = false;
            super.stop();
        }

        public boolean doHurtTargetNW(Entity pEntity) {
            float f = (float)this.mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float f1 = (float)this.mob.getAttributeValue(Attributes.ATTACK_KNOCKBACK);

            boolean flag = pEntity.hurt(this.mob.damageSources().mobAttack(this.mob), f);
            if (flag) {
                if (f1 > 0.0F && pEntity instanceof LivingEntity) {
                    ((LivingEntity)pEntity).knockback((double)(f1 * 0.5F), (double) Mth.sin(this.mob.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.mob.getYRot() * ((float)Math.PI / 180F))));
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().multiply(0.6, (double)1.0F, 0.6));
                }
                this.mob.setLastHurtMob(pEntity);
            }
            return flag;
        }
    }

    public class MahoragaRangeGoal<T extends Mob> extends Goal {
        MahoragaEntity Entity;

        private final double speedModifier;
        private final float attackRadiusSqr;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;
        boolean hasAnimated;
        int animatedTicks;
        boolean done;

        Supplier<Boolean> canUse;

        private int attackDelay = 15;
        private int ticksUntilNextAttack = 15;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        public MahoragaRangeGoal(MahoragaEntity entity, double speed, float attackRange, Supplier<Boolean> canUse) {
            this.Entity = entity;
            this.speedModifier = speed;
            this.attackRadiusSqr = attackRange * attackRange;
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        }

        private ParticleColor knockbackColor = new ParticleColor(255, 255, 255);

        public Spell knockbackSpell = new Spell()
                .add(EffectFracture.INSTANCE)
                .add(EffectKnockback.INSTANCE)
                .add(AugmentAmplify.INSTANCE, 2)

                .withColor(knockbackColor);

        void performCastAttack(LivingEntity entity, LivingEntity target){
            if(target instanceof Player player && player.isBlocking()){
                player.disableShield(false);

                EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), knockbackSpell, entity, new LivingCaster(entity)).withColors(knockbackColor));
                resolver.onResolveEffect(entity.level(), new EntityHitResult(target));
            } else if(target.hasEffect(LIMITLESS_EFFECT.get())){
                if(!this.Entity.canSlashAttack){
                    if(canAdaptCheck(this.Entity)){
                        this.Entity.canSlashAttack = true;
                    }
                }
            } else {
                target.hurt(target.damageSources().sonicBoom(entity), 8);
                this.Entity.applyDisruption(this.Entity, target);
            }

            entity.playSound(SoundEvents.WARDEN_SONIC_BOOM);

            double x = entity.position().x;
            double y = entity.position().y + 2.5;
            double z = entity.position().z;

            double xDiff = target.position().x - x;
            double yDiff = target.position().y - y;
            double zDiff = target.position().z - z;

            int particleDist = Math.min((int)Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff), 4);

            if(this.Entity.level() instanceof ServerLevel level){
                for(int i = 0; i < particleDist; i++){
                    level.sendParticles(ParticleTypes.SONIC_BOOM, x + (xDiff * i / particleDist), y + (yDiff * i / particleDist), z + (zDiff * i / particleDist), 1, 0, 0, 0, 0);
                }
            }

            this.Entity.rangedAttackCooldown = 0;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.Entity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.Entity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.Entity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 15;

            if(!this.Entity.canSlashAttack){
                this.Entity.attackCCooldown = 0;
            }
        }

        public void stop() {
            super.stop();
            this.Entity.setRoar(false);
            this.Entity.setAggressive(false);
            this.seeTime = 0;
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
            LivingEntity livingentity = this.Entity.getTarget();
            if (livingentity != null) {
                double d0 = this.Entity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean canSeeEnemy = this.Entity.getSensing().hasLineOfSight(livingentity);
                if (canSeeEnemy != this.seeTime > 0) {
                    this.seeTime = 0;
                }

                if (canSeeEnemy) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
                    this.Entity.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.Entity.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 10) {
                    if ((double)this.Entity.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }
                    if ((double)this.Entity.getRandom().nextFloat() < 0.3) {
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

                    this.Entity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.Entity.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.Entity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if (this.seeTime >= 10 && !this.hasAnimated) {
                    this.hasAnimated = true;
                }

                if (this.hasAnimated) {
                    shouldCountTillNextAttack = true;

                    if(isTimeToStartAttackAnimation()) {
                        Entity.setRoar(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.Entity, this.Entity.getTarget());
                        resetAttackLoopCooldown();
                        Entity.setRoar(false);
                        this.done = true;
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    Entity.setRoar(false);
                    Entity.attackRoarAnimationTimeout = 0;
                }
            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

            if(this.Entity.attackCCooldown > 300 && !this.Entity.canSlashAttack){
                if(canAdaptCheck(this.Entity)){
                    this.Entity.canSlashAttack = true;
                }
            }
            this.Entity.attackCCooldown++;

        }

    }

    public class MahoragaAttackGoalC extends MeleeAttackGoal {
        private final MahoragaEntity entity;

        private int attackDelay = 10;
        private int ticksUntilNextAttack = 10;
        private int totalAnimation = 15;
        private boolean shouldCountTillNextAttack = false;

        private int rangeAttackTime = 0;
        private float range = 100;

        Supplier<Boolean> canUse;
        boolean done;

        public MahoragaAttackGoalC(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse, float range) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((MahoragaEntity) pMob);
            this.canUse = canUse;
            this.range = range;
        }

        @Override
        public void start() {
            super.start();
            attackDelay = 10;
            ticksUntilNextAttack = 10;
            rangeAttackTime = 0;
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

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    performAttack(pEnemy);
                    performSpellAttack(this.entity, slashSpell, pEnemy);
                }
            } else if(isEnemyWithinRangeDistance(pEnemy, pDistToEnemySqr) && rangeAttackTime > 100){
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    entity.setAttackingC(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    performSpellAttack(this.entity, slashSpell, pEnemy);
                    this.resetAttackLoopCooldown();
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

        private boolean isEnemyWithinRangeDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= 100;
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
            return (double)(this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() * 2.0F + 4.5F);
        }

        protected double getRangeAttackSqr() {
            return (double)(this.range);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);

            for (Entity entity : this.entity.level().getEntities(null, new AABB(this.entity.blockPosition()).inflate(this.getAttackReachSqr(this.entity),this.getAttackReachSqr(this.entity),this.getAttackReachSqr(this.entity)))) {
                if (entity.equals(this.entity) || entity.equals(this.entity.owner))
                    continue;
                if (entity instanceof LivingEntity){
                    this.mob.doHurtTarget(entity);
                }
            }

            this.entity.applyDisruption(this.entity, pEnemy);

            this.done = true;
            this.entity.attackCCooldown = 0;
        }

        public void summonProjectiles(Level world, LivingEntity shooter, Spell stats, SpellResolver resolver) {
            int numSplits = 1 + stats.getInstanceCount(AugmentSplit.INSTANCE);

            List<DetonateProjectile> projectiles = new ArrayList();

            for(int i = 0; i < numSplits; ++i) {
                DetonateProjectile spell = new DetonateProjectile(world, resolver);

                projectiles.add(spell);
            }

            float velocity = Math.max(0.1F, 0.75F + (stats.getInstanceCount(AugmentAccelerate.INSTANCE) + 2 * stats.getInstanceCount(AugmentAccelerateTwo.INSTANCE) + 4 * stats.getInstanceCount(AugmentAccelerateThree.INSTANCE)) / 2.0F);
            int opposite = -1;
            int counter = 0;

            for(DetonateProjectile proj : projectiles) {
                proj.shoot(shooter, shooter.getXRot(), shooter.getYRot() + (float)(Math.round((double)counter / (double)2.0F) * 10L * (long)opposite), 0.0F, velocity, 0.8F);
                opposite *= -1;
                ++counter;
                world.addFreshEntity(proj);
            }

        }

        void performSpellAttack(LivingEntity entity, Spell spell, LivingEntity target){
            if(target.hasEffect(LIMITLESS_EFFECT.get())){
                target.removeEffect(LIMITLESS_EFFECT.get());
            }

            if(spell != null) {
                EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(spell.color));
                summonProjectiles(entity.level(), entity, spell, resolver);
            }

            this.entity.attackCCooldown = 0;
        }

        private ParticleColor slashSpellcolor = new ParticleColor(0, 0, 0);

        public Spell slashSpell = new Spell()
                .add(AugmentSplit.INSTANCE,6)
                .add(AugmentPierce.INSTANCE, 3)
                .add(AugmentAccelerate.INSTANCE, 2)
                .add(AugmentDurationDown.INSTANCE, 1)

                .add(EffectBurst.INSTANCE)
                .add(AugmentAOE.INSTANCE, 2)
                .add(AugmentSensitive.INSTANCE)
                .add(EffectAnnihilate.INSTANCE)
                .add(AugmentAmplifyThree.INSTANCE)
                .add(EffectBurst.INSTANCE)
                .add(AugmentLesserAOE.INSTANCE)
                .add(EffectAnnihilate.INSTANCE)
                .add(AugmentAmplify.INSTANCE, 2)

                .withColor(slashSpellcolor);

        @Override
        public void tick() {
            super.tick();
            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

            this.rangeAttackTime++;
        }

        @Override
        public void stop() {
            entity.setAttackingC(false);
            this.done = false;
            super.stop();
        }
    }


}
