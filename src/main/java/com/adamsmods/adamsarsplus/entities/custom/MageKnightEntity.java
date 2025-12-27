package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.datagen.CommunityMages;
import com.adamsmods.adamsarsplus.glyphs.method_glyph.MethodDetonate;
import com.adamsmods.adamsarsplus.util.SpellString;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.api.util.CasterUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Supplier;

import static com.adamsmods.adamsarsplus.Config.Common.COM_MAGES;
import static com.adamsmods.adamsarsplus.Config.Common.MAGES_GRIEF;
import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.MAGE_KNIGHT;
import static com.adamsmods.adamsarsplus.registry.ModRegistry.MAGE_TOME;

public class MageKnightEntity extends Monster {

    public String color = "white";
    public String coold = "";
    public String spell = "";
    public String name  = "";
    public String type = "";
    public String tier = "";

    public Spell mageSpell;
    public int spellCooldown;
    public boolean init = true;
    public int castCooldown = 0;
    public int blockCooldown = 0;
    public boolean blocking = false;

    public int counterTimer = 0;
    public int reflectTimer = 0;

    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(MageKnightEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> BLOCKING =
            SynchedEntityData.defineId(MageKnightEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IDLE_TO_BLOCK =
            SynchedEntityData.defineId(MageKnightEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> REFLECT =
            SynchedEntityData.defineId(MageKnightEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> COUNTER =
            SynchedEntityData.defineId(MageKnightEntity.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Integer> INDEX =
            SynchedEntityData.defineId(MageKnightEntity.class, EntityDataSerializers.INT);


    public MageKnightEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);
    }

    public MageKnightEntity(Level pLevel){
        this(MAGE_KNIGHT.get(), pLevel);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;
    public final AnimationState blockAnimationState = new AnimationState();
    public int blockAnimationTimeout = 0;
    public final AnimationState toBlockAnimationState = new AnimationState();
    public int toBlockAnimationTimeout = 0;
    public final AnimationState reflectAnimationState = new AnimationState();
    public int reflectAnimationTimeout = 0;
    public final AnimationState counterAnimationState = new AnimationState();
    public int counterAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if(!this.level().isClientSide()){
            if(init){
                mageSpawn();
                init = false;
            }

            /*
            if(blockCooldown <= 10 && blockCooldown != 0 && animCheckToBlock()){
                if(!this.isToBlock()){
                    this.setToBlock(true);
                }
            } else {
                this.setToBlock(false);
            }
            */

            if(blocking && animCheckBlock()){
                this.setBlockingS(true);
            } else {
                this.setBlockingS(false);
            }

            if(reflectTimer > 0){
                this.setReflect(true);
            } else {
                this.setReflect(false);
            }

        } else {
            setColor(CommunityMages.mages.get(this.getIndex()).color);
        }

        if(castCooldown > 0){ castCooldown--; }
        if(counterTimer > 0){ counterTimer--; }
        if(reflectTimer > 0){ reflectTimer--; }

        if(this.level().isClientSide()) {
            setupAnimationStates();
        }
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
            attackAnimationTimeout = 15;
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }
        if(!this.isAttacking()) {
            attackAnimationState.stop();
        }

        //Block Animation control
        if(this.isBlockingS() && blockAnimationTimeout <= 0) {
            blockAnimationTimeout = 25;
            blockAnimationState.start(this.tickCount);
        } else {
            --this.blockAnimationTimeout;
        }
        if(!this.isBlockingS()) {
            blockAnimationState.stop();
        }

        //Block to Idle Animation control
        if(this.isToBlock() && toBlockAnimationTimeout <= 0) {
            toBlockAnimationTimeout = 10;
            toBlockAnimationState.start(this.tickCount);
        } else {
            --this.toBlockAnimationTimeout;
        }
        if(!this.isToBlock()) {
            toBlockAnimationState.stop();
        }

        //Reflect Animation control
        if(this.isReflect() && reflectAnimationTimeout <= 0) {
            reflectAnimationTimeout = 10;
            reflectAnimationState.start(this.tickCount);
        } else {
            --this.reflectAnimationTimeout;
        }
        if(!this.isReflect()) {
            reflectAnimationState.stop();
        }

        //Counter Animation control
        if(this.isCounter() && counterAnimationTimeout <= 0) {
            counterAnimationTimeout = 15;
            counterAnimationState.start(this.tickCount);
        } else {
            --this.counterAnimationTimeout;
        }
        if(!this.isCounter()) {
            counterAnimationState.stop();
        }

    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(this.blocking && this.counterTimer == 0){
            this.blocking = false;

            if(this.blockCooldown == 0){
                this.counterTimer = 30;
                this.blockCooldown = 30 + random.nextInt(0, 30);
            }

            this.playSound(SoundEvents.SHIELD_BLOCK, 1.5F, 1F);

            if(pSource.getEntity() instanceof LivingEntity enemy && (pSource.is(DamageTypes.PLAYER_ATTACK) || pSource.is(DamageTypes.MOB_ATTACK))){
                knockback(enemy, this, 0.3f);
            }

            return(false);
        }

        return super.hurt(pSource, pAmount);
    }

    @Override
    public void die(DamageSource cause) {
        if(this.random.nextInt(0, 5) == 4){
            Item tomeType = MAGE_TOME.get().asItem();
            Spell tomeSpell = this.mageSpell;
            switch (this.type){
                case "projectile" -> {
                    tomeSpell.add(MethodProjectile.INSTANCE, 1, 0);
                }
                case "melee" -> {
                    tomeSpell.add(MethodTouch.INSTANCE, 1, 0);
                }
                case "self" -> {
                    tomeSpell.add(MethodSelf.INSTANCE, 1, 0);
                }
                case "detonate" -> {
                    tomeSpell.add(MethodDetonate.INSTANCE, 1, 0);
                }
            }
            ItemStack Tome = makeTome(tomeType, this.name, tomeSpell);

            this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), Tome));
        }

        super.die(cause);
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

    public static ItemStack makeTome(Item tome, String name, Spell spell) {
        ItemStack stack = tome.getDefaultInstance();
        ISpellCaster spellCaster = CasterUtil.getCaster(stack);
        spellCaster.setSpell(spell);
        stack.setHoverName(Component.literal(name).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(true)));
        return stack;
    }

    public boolean animCheckToBlock() {
        return (!this.isCounter() && !this.isReflect() && !this.isBlockingS() && !this.isAttacking());
    }

    public boolean animCheckBlock() {
        return (!this.isCounter() && !this.isReflect() && !this.isAttacking());
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
    public boolean isAttacking(){ return this.entityData.get(ATTACKING); }

    public void setBlockingS(boolean blocking) { this.entityData.set(BLOCKING, blocking); }
    public boolean isBlockingS(){ return this.entityData.get(BLOCKING); }

    public void setToBlock(boolean toBlock) { this.entityData.set(IDLE_TO_BLOCK, toBlock); }
    public boolean isToBlock(){ return this.entityData.get(IDLE_TO_BLOCK); }

    public void setReflect(boolean reflect) { this.entityData.set(REFLECT, reflect); }
    public boolean isReflect(){ return this.entityData.get(REFLECT); }

    public void setCounter(boolean counter) { this.entityData.set(COUNTER, counter); }
    public boolean isCounter(){ return this.entityData.get(COUNTER); }

    public void setIndex(Integer index) { this.entityData.set(INDEX, index, true); }
    public Integer getIndex(){ return this.entityData.get(INDEX); }

    public void setColor(String newColor){
        this.color = newColor.replace("_sword", "");
    }

    public void setSpellData(String spellString, String color){
        this.spell = spellString;

        Spell returnSpell = new Spell();

        String[] tokens = spellString.split("-");

        for(String t : tokens){
           returnSpell.add(SpellString.stringSpellComponent(t));
        }

        returnSpell.color = SpellString.stringColor(color);

        this.mageSpell = returnSpell;
    }

    public void setCooldown(String newCooldown){
        this.coold = newCooldown;

        switch(newCooldown){
            case "short"    -> this.spellCooldown = 40;
            case "medium"   -> this.spellCooldown = 80;
            case "long"     -> this.spellCooldown = 200;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(BLOCKING, false);
        this.entityData.define(IDLE_TO_BLOCK, false);
        this.entityData.define(REFLECT, false);
        this.entityData.define(COUNTER, false);
        this.entityData.define(INDEX, 0);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt(   "cast",   castCooldown);
        tag.putString("color",  color);
        tag.putString("name",   name);
        tag.putString("spell",  spell);
        tag.putString("coold",  coold);
        tag.putString("type",   type);
        tag.putString("tier",   tier);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.castCooldown = tag.getInt( "cast");
        this.setColor(tag.getString(    "color"));
        this.setName(tag.getString(     "name"));
        this.setCooldown(tag.getString( "coold"));
        this.type = tag.getString("type");
        this.setSpellData(tag.getString("spell"), tag.getString("color"));
        this.tier = tag.getString("tier");
    }

    public void setName(String name){
        this.name = name;

        if(name != "Mage Knight"){
            setCustomName(Component.literal(name));
        }
    }

    public void setCustomName(@javax.annotation.Nullable Component pName) {
        super.setCustomName(pName);
    }

    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance pDifficulty) {
    }

    protected void dropEquipment() {
    }

    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new KnightBlockGoal(this, 0.7d, false, () -> (this.blockCooldown == 0 || this.counterTimer > 0), () -> Math.max(this.spellCooldown, 1), () -> this.mageSpell));
        this.goalSelector.addGoal(2, new KnightAttackGoal(this, 1.0d, false, () -> true, () -> Math.max(this.spellCooldown, 1), () -> this.mageSpell));

        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, (double)1.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Villager.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 60D)
                .add(Attributes.ARMOR, 15)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3f)
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.FOLLOW_RANGE, (double)40.0F)
                .add(Attributes.ATTACK_DAMAGE, 8D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0F);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ENDERMAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
    }

    public void mageSpawn() {
        RandomSource randomSource = this.level().getRandom();

        if (!CommunityMages.mages.isEmpty()) {
            try {
                int size = 7;
                int Offset = 0;

                if(COM_MAGES.get()){
                    this.setIndex(randomSource.nextInt(CommunityMages.mages.size()));
                    size = CommunityMages.mages.size();
                } else {
                    this.setIndex(randomSource.nextInt(0, 6));
                }

                for(int i = 0; i < size; i++){
                    if(i + this.getIndex() < size){
                        if(CommunityMages.mages.get(i + this.getIndex()).tier.contains("overworld")){
                            if(MAGES_GRIEF.get() || !CommunityMages.mages.get(i + this.getIndex()).tier.contains("griefing")){
                                Offset = i;
                                this.setIndex(this.getIndex() + Offset);
                                break;
                            }
                        }
                    } else {
                        if(CommunityMages.mages.get(i + this.getIndex() - size).tier.contains("overworld")){
                            if(MAGES_GRIEF.get() || !CommunityMages.mages.get(i + this.getIndex()).tier.contains("griefing")){
                                Offset = i;
                                this.setIndex(this.getIndex() + Offset - size);
                                break;
                            }
                        }
                    }
                }

                CommunityMages.ComMages communityMage = (CommunityMages.ComMages)CommunityMages.mages.get(this.getIndex());

                this.setColor(communityMage.color);
                this.setName(communityMage.name);
                this.setCooldown(communityMage.coold);
                this.type = communityMage.type;
                this.setSpellData(communityMage.spell, communityMage.color);
                this.tier = communityMage.tier;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.setColor("white");
            this.setName("Oops");
        }
    }

    public class KnightAttackGoal extends MeleeAttackGoal {
        MageKnightEntity mageEntity;

        private int attackDelay = 12;
        private int ticksUntilNextAttack = 12;
        private int totalAnimation = 15;
        private boolean shouldCountTillNextAttack = false;

        Supplier<Integer> spellCooldown;
        Supplier<Spell> mageSpell;
        Supplier<Boolean> canUse;

        boolean done;

        public KnightAttackGoal(MageKnightEntity entity, double speed, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse, Supplier<Integer> spellCooldown, Supplier<Spell> mageSpell) {
            super(entity, speed, pFollowingTargetEvenIfNotSeen);

            this.mageEntity = entity;
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

            this.spellCooldown = spellCooldown;
            this.mageSpell = mageSpell;
        }

        @Override
        public void start() {
            super.start();
            attackDelay = 12;
            ticksUntilNextAttack = 12;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
        }

        void performSpellAttack(LivingEntity entity, Spell spell, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));

            this.mageEntity.castCooldown = 10 + random.nextInt(this.spellCooldown.get());
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    this.mageEntity.setAttacking(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    this.performAttack(pEnemy);
                    if(!pEnemy.isBlocking() && this.mageEntity.castCooldown <= 0){
                        pEnemy.invulnerableTime = 0;
                        performSpellAttack(this.mageEntity, mageSpell.get(), mageSpell.get().color, pEnemy);
                    }
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                mageEntity.setAttacking(false);
                mageEntity.attackAnimationTimeout = 0;
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
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 3.0F);
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;

        }

        @Override
        public void tick() {
            super.tick();
            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

            if(this.mageEntity.blockCooldown > 0){
                this.mageEntity.blockCooldown--;
            }

            if(this.ticksUntilNextAttack <= 8 && this.ticksUntilNextAttack >= 4){
                this.mageEntity.blocking = true;
            } else {
                this.mageEntity.blocking = false;
            }

        }

        @Override
        public void stop() {
            mageEntity.setAttacking(false);
            mageEntity.blocking = false;
            this.done = false;
            super.stop();
        }
    }

    public class KnightBlockGoal extends MeleeAttackGoal {
        MageKnightEntity mageEntity;

        private int attackDelay = 12;
        private int ticksUntilNextAttack = 12;
        private int totalAnimation = 15;
        private boolean shouldCountTillNextAttack = false;

        Supplier<Integer> spellCooldown;
        Supplier<Spell> mageSpell;
        Supplier<Boolean> canUse;

        boolean done;

        public KnightBlockGoal(MageKnightEntity entity, double speed, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse, Supplier<Integer> spellCooldown, Supplier<Spell> mageSpell) {
            super(entity, speed, pFollowingTargetEvenIfNotSeen);

            this.mageEntity = entity;
            this.canUse = canUse;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

            this.spellCooldown = spellCooldown;
            this.mageSpell = mageSpell;
        }

        @Override
        public void start() {
            super.start();
            attackDelay = 12;
            ticksUntilNextAttack = 12;
            totalAnimation = 15;
            mageEntity.blocking = true;

            AdamsArsPlus.setInterval(() -> {
                double radius = 1.5;
                BlockPos entityBlockPos = this.mageEntity.blockPosition();
                for (Entity entity : this.mageEntity.level().getEntitiesOfClass(Projectile.class, new AABB(entityBlockPos).inflate(radius, radius, radius))) {
                    if(entity.getDeltaMovement().x() == 0 && entity.getDeltaMovement().y() == 0 && entity.getDeltaMovement().z() == 0){
                        continue;
                    }

                    entity.setDeltaMovement(getDeltaMovement().scale(-1));
                    if(entity instanceof Projectile projectile){
                        projectile.setOwner(this.mageEntity);
                    }
                    if(entity instanceof EntityProjectileSpell spell){
                        spell.spellResolver.spellContext.setCaster(this.mageEntity);
                    }

                    this.mageEntity.reflectTimer = 10;
                    this.mageEntity.blockCooldown = 30 + random.nextInt(0, 30);

                    break;
                }
            }, 1, 60 * 20, () -> this.mageEntity.blockCooldown != 0);
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
        }

        void performSpellAttack(LivingEntity entity, Spell spell, ParticleColor color, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)).withColors(color));

            if(Objects.equals(this.mageEntity.type, "self")){
                resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));
            } else {
                resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));
            }

            this.mageEntity.castCooldown = 10 + random.nextInt(this.spellCooldown.get());
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {

                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    this.mageEntity.setCounter(true);
                }

                if(isTimeToAttack()) {

                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());

                    if(!pEnemy.isBlocking() && this.mageEntity.counterTimer > 0){
                        this.mageEntity.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 1.5F, 1F);

                        for(int i = 0; i < 5; i++){
                            if(this.mageEntity.level() instanceof ServerLevel world){
                                world.sendParticles(ParticleTypes.CRIT, pEnemy.getX(), pEnemy.getY(), pEnemy.getZ(), 1, (double) world.random.nextInt(-5, 5) /10, (double) world.random.nextInt(-5, 5) /10, (double) world.random.nextInt(-5, 5) /10, 0.3);
                            }
                        }

                        this.mageEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1, 0, false, false));
                    }

                    this.performAttack(pEnemy);

                    if(!pEnemy.isBlocking() && this.mageEntity.castCooldown <= 0){
                        pEnemy.invulnerableTime = 0;
                        performSpellAttack(this.mageEntity, mageSpell.get(), mageSpell.get().color, pEnemy);
                    }
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                mageEntity.setCounter(false);
                mageEntity.counterAnimationTimeout = 0;
            }
        }

        private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(this.attackDelay);
        }

        protected void resetAttackLoopCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(this.totalAnimation);
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
            if(this.mageEntity.counterTimer > 0){
                return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 4.0F);
            } else {
                return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 7.0F);
            }
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackLoopCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.done = true;
        }

        @Override
        public void tick() {
            super.tick();
            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }

        @Override
        public void stop() {
            mageEntity.setCounter(false);
            mageEntity.blocking = false;
            this.done = false;
            super.stop();
        }
    }
}
