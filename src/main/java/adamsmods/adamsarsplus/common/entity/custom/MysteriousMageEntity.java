package adamsmods.adamsarsplus.common.entity.custom;

import adamsmods.adamsarsplus.common.entity.DetonateProjectile;
import adamsmods.adamsarsplus.common.glyphs.augment_glyph.AugmentAccelerateThree;
import adamsmods.adamsarsplus.common.glyphs.augment_glyph.AugmentAccelerateTwo;
import adamsmods.adamsarsplus.datagen.CommunityMages;
import adamsmods.adamsarsplus.util.SpellString;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

import static adamsmods.adamsarsplus.ConfigHandler.Common.COM_MAGES;
import static adamsmods.adamsarsplus.ConfigHandler.Common.MAGES_GRIEF;
import static adamsmods.adamsarsplus.registry.ModEntities.MAGE_ENTITY;

public class MysteriousMageEntity extends Monster implements RangedAttackMob {

    public String color = "white";
    public String coold = "";
    public String spell = "";
    public String name  = "";
    public String type = "";
    public String tier = "";

    public Spell mageSpell;
    public int spellCooldown;
    public boolean init = true;

    public static final EntityDataAccessor<Boolean> CASTING =
            SynchedEntityData.defineId(MysteriousMageEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> SELF_CASTING =
            SynchedEntityData.defineId(MysteriousMageEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(MysteriousMageEntity.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Integer> INDEX =
            SynchedEntityData.defineId(MysteriousMageEntity.class, EntityDataSerializers.INT);

    public int castCooldown = 0;

    public MysteriousMageEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);
    }

    public MysteriousMageEntity(Level pLevel){
        this(MAGE_ENTITY.get(), pLevel);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState castAnimationState = new AnimationState();
    public int castAnimationTimeout = 0;

    public final AnimationState selfcastAnimationState = new AnimationState();
    public int selfcastAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if(!this.level().isClientSide()){
            if(init){
                mageSpawn();
                init = false;
            }
        } else {
            setColor(CommunityMages.colorOrDefault(this.getIndex(), this.color));
        }

        if(castCooldown > 0){ castCooldown--; }

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

        //Cast Animation control
        if(this.isCasting() && castAnimationTimeout <= 0) {
            castAnimationTimeout = 20;
            castAnimationState.start(this.tickCount);
        } else {
            --this.castAnimationTimeout;
        }
        if(!this.isCasting()) {
            castAnimationState.stop();
        }

        //Self Cast Animation control
        if(this.isSelfCasting() && selfcastAnimationTimeout <= 0) {
            selfcastAnimationTimeout = 20;
            selfcastAnimationState.start(this.tickCount);
        } else {
            --this.selfcastAnimationTimeout;
        }
        if(!this.isSelfCasting()) {
            selfcastAnimationState.stop();
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

    public void setCasting(boolean casting) { this.entityData.set(CASTING, casting); }
    public boolean isCasting(){ return this.entityData.get(CASTING); }

    public void setSelfCasting(boolean scasting) { this.entityData.set(SELF_CASTING, scasting); }
    public boolean isSelfCasting(){ return this.entityData.get(SELF_CASTING); }

    public void setAttacking(boolean attacking) { this.entityData.set(ATTACKING, attacking); }
    public boolean isAttacking(){ return this.entityData.get(ATTACKING); }

    public void setIndex(Integer index) { this.entityData.set(INDEX, index, true); }
    public Integer getIndex(){ return this.entityData.get(INDEX); }

    public void setColor(String newColor){
        this.color = newColor;
    }

    public void setSpellData(String spellString, String color){
        this.spell = spellString;

        Spell returnSpell = new Spell();

        String[] tokens = spellString.split("-");

        for(String t : tokens){
           returnSpell.add(SpellString.stringSpellComponent(t));
        }

        returnSpell.withColor(SpellString.stringColor(color));

        this.mageSpell = returnSpell;
    }

    public void setCooldown(String newCooldown){
        this.coold = newCooldown;

        switch(newCooldown){
            case "short"    -> this.spellCooldown = 20;
            case "medium"   -> this.spellCooldown = 50;
            case "long"     -> this.spellCooldown = 200;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(CASTING, false);
        pBuilder.define(SELF_CASTING, false);
        pBuilder.define(ATTACKING, false);
        pBuilder.define(INDEX, 0);
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
        this.setSpellData(tag.getString("spell"), tag.getString("color"));
        this.type = tag.getString("type");
        this.tier = tag.getString("tier");
    }

    public void setName(String name){
        this.name = name;

        if(name != "Mage"){
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

        this.goalSelector.addGoal(1, new MageCastingGoal<>(this, 1.0D, 40f,
                () -> this.type.equals("projectile") && (castCooldown <= 0),         0, 15,
                () -> Math.max(spellCooldown,1),
                () -> mageSpell));
        this.goalSelector.addGoal(1, new MageCastingGoal_Det<>(this, 1.0D, 40f,
                () -> this.type.equals("detonate") && (castCooldown <= 0),         0, 15,
                () -> Math.max(spellCooldown,1),
                () -> mageSpell));
        this.goalSelector.addGoal(1, new MageCastingGoal_Self<>(this, 1.0D, 8f,
                () -> this.type.equals("self") && (castCooldown <= 0),         0, 15,
                () -> Math.max(spellCooldown,1),
                () -> mageSpell));
        this.goalSelector.addGoal(1, new MageCastingGoal_Melee(this, 1.8D, false,
                () -> this.type.equals("melee") && (castCooldown <= 0),
                () -> Math.max(spellCooldown,1),
                () -> mageSpell));

        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this,Player.class,10,1.3D,1.0D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this,IronGolem.class,10,1.3D,1.0D));

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
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.2F)
                .add(Attributes.FOLLOW_RANGE, (double)40.0F)
                .add(Attributes.ATTACK_DAMAGE, 8D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0F);
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float p_82196_2_) {

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
                this.setSpellData(communityMage.spell, communityMage.color);
                this.type = communityMage.type;
                this.tier = communityMage.tier;

            } catch (Exception e1) {
                try {
                    this.setIndex(0);
                    CommunityMages.ComMages communityMage = (CommunityMages.ComMages)CommunityMages.mages.get(0);

                    this.setColor(communityMage.color);
                    this.setName(communityMage.name);
                    this.setCooldown(communityMage.coold);
                    this.setSpellData(communityMage.spell, communityMage.color);
                    this.type = communityMage.type;
                    this.tier = communityMage.tier;
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        } else {
            this.setColor("white");
            this.setName("Oops");
        }
    }

    public class MageCastingGoal<T extends Mob & RangedAttackMob> extends Goal {
        MysteriousMageEntity mageEntity;

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

        Supplier<Integer> spellCooldown;
        Supplier<Spell> mageSpell;

        Supplier<Boolean> canUse;

        private int attackDelay = 15;
        private int ticksUntilNextAttack = 5;
        private boolean shouldCountTillNextAttack = false;

        public MageCastingGoal(MysteriousMageEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks, Supplier<Integer> spellCooldown, Supplier<Spell> mageSpell) {
            this.mageEntity = entity;
            this.speedModifier = speed;
            this.attackRadiusSqr = attackRange * attackRange;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

            this.spellCooldown = spellCooldown;
            this.mageSpell = mageSpell;
        }

        public void summonProjectiles(Level world, LivingEntity shooter, Spell stats, SpellResolver resolver) {
            int numSplits = 1 + stats.getInstanceCount(AugmentSplit.INSTANCE);

            List<EntityProjectileSpell> projectiles = new ArrayList();

            for(int i = 0; i < numSplits; ++i) {
                EntityProjectileSpell spell = new EntityProjectileSpell(world, resolver);

                projectiles.add(spell);
            }

            float velocity = Math.max(0.1F, 0.75F + (stats.getInstanceCount(AugmentAccelerate.INSTANCE) + 2 * stats.getInstanceCount(AugmentAccelerateTwo.INSTANCE) + 4 * stats.getInstanceCount(AugmentAccelerateThree.INSTANCE)) / 2.0F);
            int opposite = -1;
            int counter = 0;

            for(EntityProjectileSpell proj : projectiles) {
                proj.shoot(shooter, shooter.getXRot(), shooter.getYRot() + (float)(Math.round((double)counter / (double)2.0F) * 10L * (long)opposite), 0.0F, velocity, 0.8F);
                opposite *= -1;
                ++counter;
                world.addFreshEntity(proj);
            }

        }

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell){
            if(spell != null) {
                EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)));

                summonProjectiles(entity.level(), entity, spell, resolver);
            }

            System.out.println(this.spellCooldown.get());

            this.mageEntity.castCooldown = this.spellCooldown.get() + random.nextInt(this.spellCooldown.get());
        /*
        if(this.spellCooldown > 0){
            this.mageEntity.castCooldown = this.spellCooldown + random.nextInt(this.spellCooldown);
        } else if(this.spellCooldown == 0){
            this.mageEntity.castCooldown = 20;
        } else {
            this.mageEntity.castCooldown = 500;
        }
        */
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.mageEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mageEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.mageEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 5;
        }

        public void stop() {
            super.stop();
            this.mageEntity.setCasting(false);
            this.mageEntity.setAggressive(false);
            this.seeTime = 0;
            this.animatedTicks = 0;
            this.done = false;
            this.hasAnimated = false;
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay + 5);
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
            LivingEntity livingentity = this.mageEntity.getTarget();
            if (livingentity != null) {
                double d0 = this.mageEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean canSeeEnemy = this.mageEntity.getSensing().hasLineOfSight(livingentity);
                if (canSeeEnemy != this.seeTime > 0) {
                    this.seeTime = 0;
                }

                if (canSeeEnemy) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
                    this.mageEntity.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.mageEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 10) {
                    if ((double)this.mageEntity.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.mageEntity.getRandom().nextFloat() < 0.3) {
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

                    this.mageEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.mageEntity.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.mageEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if (this.seeTime >= 10 && !this.hasAnimated) {
                    this.hasAnimated = true;
                }

                if (this.hasAnimated) {
                    shouldCountTillNextAttack = true;

                    if(isTimeToStartAttackAnimation()) {
                        mageEntity.setCasting(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.mageEntity, 1.0F, this.mageSpell.get());
                        mageEntity.setCasting(false);
                        this.done = true;
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    mageEntity.setCasting(false);
                    mageEntity.castAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }
    }

    public class MageCastingGoal_Det<T extends Mob & RangedAttackMob> extends Goal {
        MysteriousMageEntity mageEntity;

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

        Supplier<Integer> spellCooldown;
        Supplier<Spell> mageSpell;

        Supplier<Boolean> canUse;

        private int attackDelay = 15;
        private int ticksUntilNextAttack = 5;
        private boolean shouldCountTillNextAttack = false;

        public MageCastingGoal_Det(MysteriousMageEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks, Supplier<Integer> spellCooldown, Supplier<Spell> mageSpell) {
            this.mageEntity = entity;
            this.speedModifier = speed;
            this.attackRadiusSqr = attackRange * attackRange;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

            this.spellCooldown = spellCooldown;
            this.mageSpell = mageSpell;
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

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell){
            if(spell != null) {
                EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)));

                summonProjectiles(entity.level(), entity, spell, resolver);
            }

            System.out.println(this.spellCooldown.get());

            this.mageEntity.castCooldown = this.spellCooldown.get() + random.nextInt(this.spellCooldown.get());
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.mageEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mageEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.mageEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 5;
        }

        public void stop() {
            super.stop();
            this.mageEntity.setCasting(false);
            this.mageEntity.setAggressive(false);
            this.seeTime = 0;
            this.animatedTicks = 0;
            this.done = false;
            this.hasAnimated = false;
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay + 5);
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
            LivingEntity livingentity = this.mageEntity.getTarget();
            if (livingentity != null) {
                double d0 = this.mageEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean canSeeEnemy = this.mageEntity.getSensing().hasLineOfSight(livingentity);
                if (canSeeEnemy != this.seeTime > 0) {
                    this.seeTime = 0;
                }

                if (canSeeEnemy) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
                    this.mageEntity.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.mageEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 10) {
                    if ((double)this.mageEntity.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.mageEntity.getRandom().nextFloat() < 0.3) {
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

                    this.mageEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.mageEntity.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.mageEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if (this.seeTime >= 10 && !this.hasAnimated) {
                    this.hasAnimated = true;
                }

                if (this.hasAnimated) {
                    shouldCountTillNextAttack = true;

                    if(isTimeToStartAttackAnimation()) {
                        mageEntity.setCasting(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.mageEntity, 1.0F, this.mageSpell.get());
                        mageEntity.setCasting(false);
                        this.done = true;
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    mageEntity.setCasting(false);
                    mageEntity.castAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }

    }

    public class MageCastingGoal_Melee extends MeleeAttackGoal {
        MysteriousMageEntity mageEntity;

        private int attackDelay = 15;
        private int ticksUntilNextAttack = 15;
        private int totalAnimation = 20;
        private boolean shouldCountTillNextAttack = false;

        Supplier<Integer> spellCooldown;
        Supplier<Spell> mageSpell;
        Supplier<Boolean> canUse;

        boolean done;

        public MageCastingGoal_Melee(MysteriousMageEntity entity, double speed, boolean pFollowingTargetEvenIfNotSeen, Supplier<Boolean> canUse, Supplier<Integer> spellCooldown, Supplier<Spell> mageSpell) {
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
            attackDelay = 15;
            ticksUntilNextAttack = 15;
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.mob.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
        }

        void performSpellAttack(LivingEntity entity, Spell spell, LivingEntity enemy){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));

            this.mageEntity.castCooldown = 10 + random.nextInt(this.spellCooldown.get());
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            if(isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
                shouldCountTillNextAttack = true;

                if(isTimeToStartAttackAnimation()) {
                    this.mageEntity.setAttacking(true);
                }

                if(isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getY(), pEnemy.getZ());
                    performAttack(pEnemy);
                    if(!pEnemy.isBlocking()){
                        performSpellAttack(this.mageEntity, mageSpell.get(), pEnemy);
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
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 1.0F);
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
            mageEntity.setAttacking(false);
            this.done = false;
            super.stop();
        }
    }

    public class MageCastingGoal_Self<T extends Mob & RangedAttackMob> extends Goal {
        MysteriousMageEntity mageEntity;

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

        Supplier<Integer> spellCooldown;
        Supplier<Spell> mageSpell;

        Supplier<Boolean> canUse;

        private int attackDelay = 15;
        private int ticksUntilNextAttack = 5;
        private boolean shouldCountTillNextAttack = false;

        public MageCastingGoal_Self(MysteriousMageEntity entity, double speed, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks, Supplier<Integer> spellCooldown, Supplier<Spell> mageSpell) {
            this.mageEntity = entity;
            this.speedModifier = speed;
            this.attackRadiusSqr = attackRange * attackRange;
            this.canUse = canUse;
            this.animId = animId;
            this.delayTicks = delayTicks;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

            this.spellCooldown = spellCooldown;
            this.mageSpell = mageSpell;
        }

        public void performSpellSelf(LivingEntity entity, float p_82196_2_, Spell spell){
            EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)));

            resolver.onResolveEffect(entity.level(), new EntityHitResult(entity));
        }

        void performCastAttack(LivingEntity entity, float p_82196_2_, Spell spell){
            if(spell != null) {
                performSpellSelf(entity,1.0F , spell);
            }

            System.out.println(this.spellCooldown.get());

            this.mageEntity.castCooldown = this.spellCooldown.get() + random.nextInt(this.spellCooldown.get());
        }

        public boolean canUse() {
            return (Boolean)this.canUse.get() && this.mageEntity.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mageEntity.getNavigation().isDone()) && !this.done;
        }

        public void start() {
            super.start();
            this.mageEntity.setAggressive(true);
            attackDelay = 15;
            ticksUntilNextAttack = 5;
        }

        public void stop() {
            super.stop();
            this.mageEntity.setSelfCasting(false);
            this.mageEntity.setAggressive(false);
            this.seeTime = 0;
            this.animatedTicks = 0;
            this.done = false;
            this.hasAnimated = false;
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay + 5);
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
            LivingEntity livingentity = this.mageEntity.getTarget();
            if (livingentity != null) {
                double d0 = this.mageEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean canSeeEnemy = this.mageEntity.getSensing().hasLineOfSight(livingentity);
                if (canSeeEnemy != this.seeTime > 0) {
                    this.seeTime = 0;
                }

                if (canSeeEnemy) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
                    this.mageEntity.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.mageEntity.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 10) {
                    if ((double)this.mageEntity.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.mageEntity.getRandom().nextFloat() < 0.3) {
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

                    this.mageEntity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.mageEntity.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.mageEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if (this.seeTime >= 10 && !this.hasAnimated) {
                    this.hasAnimated = true;
                }

                if (this.hasAnimated) {
                    shouldCountTillNextAttack = true;

                    if(isTimeToStartAttackAnimation()) {
                        mageEntity.setSelfCasting(true);
                    }

                    if(isTimeToAttack()) {
                        performCastAttack(this.mageEntity, 1.0F, this.mageSpell.get());
                        mageEntity.setSelfCasting(false);
                        this.done = true;
                    }

                } else {
                    resetAttackCooldown();
                    shouldCountTillNextAttack = false;
                    mageEntity.setSelfCasting(false);
                    mageEntity.castAnimationTimeout = 0;
                }

            }

            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }

        }

    }

}
