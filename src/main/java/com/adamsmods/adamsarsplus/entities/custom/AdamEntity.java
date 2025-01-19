package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.entities.ai.*;
import com.adamsmods.adamsarsplus.entities.ai.AdamCastingAGoal;
import com.adamsmods.adamsarsplus.entities.ai.pathfinding.AdvancedPathNavigate;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAOEThree;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectLimitless;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.entity.pathfinding.PathingStuckHandler;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBlink;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectDispel;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectHeal;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
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

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.SIMPLE_DOMAIN_EFFECT;
import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.ADAM_ENTITY;
import static com.hollingsworth.arsnouveau.client.particle.ParticleColor.random;

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
    public int domainCooldown;

    public int barrierCooldown;
    public int recoverCooldown;
    public int blinkCooldown;

    public int navigatorType;
    public int age;

    private final ServerBossEvent bossEvent;

    public AdamEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.navigation = createNavigator(level(), AdvancedPathNavigate.MovementType.FLYING);
        this.navigatorType = 1;

    }

    public AdamEntity(Level pLevel){
        this(ADAM_ENTITY.get(), pLevel);
    }

    protected PathingStuckHandler createStuckHandler() {
        return PathingStuckHandler.createStuckHandler();
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level worldIn) {
        return createNavigator(worldIn, AdvancedPathNavigate.MovementType.WALKING);
    }


    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type) {
        return createNavigator(worldIn, type, createStuckHandler());
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type, PathingStuckHandler stuckHandler) {
        return createNavigator(worldIn, type, stuckHandler, 4f, 4f);
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type, PathingStuckHandler stuckHandler, float width, float height) {
        AdvancedPathNavigate newNavigator = new AdvancedPathNavigate(this, level(), type, width, height);
        this.navigation = newNavigator;
        newNavigator.setCanFloat(true);
        newNavigator.getNodeEvaluator().setCanOpenDoors(true);
        return newNavigator;
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
        if(domainCooldown > 0) {
            domainCooldown--;
        }
        else{
            performChargeParticle(this);
        }
        if(barrierCooldown > 0) {
            barrierCooldown--;
        }
        if(recoverCooldown > 0) {
            recoverCooldown--;
        }
        else{
            performSpellSelf(this,1.0F, adamRecoverSpell, adamColor);
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
                    performSpellSelf(this,1.0F,adamBarrierSpell,adamColor);
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

    public void performSpellSelf(LivingEntity entity, float p_82196_2_, Spell spell, ParticleColor color){
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
        tag.putInt("domain", domainCooldown);

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

            return false;
        }

        if(this.blinkCooldown <= 0){
            if(source.is(DamageTypes.IN_WALL)){
                this.teleportRelative(0,8,0);
            } else if(!this.level().isClientSide()) {
                performSpellSelf(this, 1.0F, adamBlinkSpell, adamColor);
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
        this.attackAACooldown = tag.getInt("attackab");
        this.attackBCooldown = tag.getInt("attackb");
        this.blockCooldown = tag.getInt("block");
        this.castingACooldown = tag.getInt("castinga");
        this.castingBCooldown = tag.getInt("castingb");
        this.domainCooldown = tag.getInt( "domain");

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

        this.goalSelector.addGoal(1, new AdamDomainGoal(this, 1.0D, 20f, () -> domainCooldown <= 0, 0, 13));

        this.goalSelector.addGoal(2, new AdamCastingAGoal<>(this, 1.0D, 70f, () -> castingACooldown <= 0, 0, 15));
        this.goalSelector.addGoal(2, new AdamCastingBGoal<>(this, 1.0D, 70f, () -> castingBCooldown <= 0, 0, 15));

        this.goalSelector.addGoal(3, new AdamAttackABGoal(this, 1.0D, true, () -> attackABCooldown != 0));
        this.goalSelector.addGoal(3, new AdamAttackBGoal(this, 1.0D, true, () -> attackBCooldown <= 0));
        this.goalSelector.addGoal(3, new AdamBlockingGoal<>(this, 1.0D, 70f, () -> blockCooldown <= 0, 0, 12));

        this.goalSelector.addGoal(4, new AdamAttackAAGoal(this, 1.0D, true, () -> true));

        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, (double)1.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 2000D)
                .add(Attributes.ARMOR, 23D)
                .add(Attributes.ATTACK_DAMAGE, (double)20.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.8F)
                .add(Attributes.FLYING_SPEED, (double)0.8F)
                .add(Attributes.FOLLOW_RANGE, (double)70.0F)
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

}
