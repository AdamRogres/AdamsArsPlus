package com.adamsmods.adamsarsplus.entities.variants;

import com.adamsmods.adamsarsplus.datagen.CommunityMages;
import com.adamsmods.adamsarsplus.entities.ai.MageCastingGoal;
import com.adamsmods.adamsarsplus.entities.ai.MageCastingGoal_Det;
import com.adamsmods.adamsarsplus.entities.ai.MageCastingGoal_Melee;
import com.adamsmods.adamsarsplus.entities.ai.MageCastingGoal_Self;
import com.adamsmods.adamsarsplus.entities.custom.MysteriousMageEntity;
import com.adamsmods.adamsarsplus.util.SpellString;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
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
import org.jetbrains.annotations.Nullable;

import static com.adamsmods.adamsarsplus.Config.Common.COM_MAGES;
import static com.adamsmods.adamsarsplus.Config.Common.MAGES_GRIEF;
import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.*;

public class LightningMageEntity extends MysteriousMageEntity implements RangedAttackMob {

    public static final EntityDataAccessor<Boolean> CASTING =
            SynchedEntityData.defineId(LightningMageEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> SELF_CASTING =
            SynchedEntityData.defineId(LightningMageEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(LightningMageEntity.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Integer> INDEX =
            SynchedEntityData.defineId(LightningMageEntity.class, EntityDataSerializers.INT);

    public LightningMageEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);
    }

    public LightningMageEntity(Level pLevel){
        this(LIGHTNING_MAGE_ENTITY.get(), pLevel);
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
            setColor(CommunityMages.mages.get(this.getIndex()).color);
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

        returnSpell.color = SpellString.stringColor(color);

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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CASTING, false);
        this.entityData.define(SELF_CASTING, false);
        this.entityData.define(ATTACKING, false);
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
                .add(Attributes.MAX_HEALTH, 50D)
                .add(Attributes.ARMOR, 8D)
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
                int size = 1;
                int Offset = 0;

                if(COM_MAGES.get()){
                    this.setIndex(randomSource.nextInt(CommunityMages.mages.size()));
                    size = CommunityMages.mages.size();
                } else {
                    this.setIndex(10);
                }

                for(int i = 0; i < size; i++){
                    if(i + this.getIndex() < size){
                        if(CommunityMages.mages.get(i + this.getIndex()).tier.contains("lightning")){
                            if(MAGES_GRIEF.get() || !CommunityMages.mages.get(i + this.getIndex()).tier.contains("griefing")){
                                Offset = i;
                                this.setIndex(this.getIndex() + Offset);
                                break;
                            }
                        }
                    } else {
                        if(CommunityMages.mages.get(i + this.getIndex() - size).tier.contains("lightning")){
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

            } catch (Exception e) {
                try {
                    this.setIndex(10);
                    CommunityMages.ComMages communityMage = (CommunityMages.ComMages)CommunityMages.mages.get(10);

                    this.setColor(communityMage.color);
                    this.setName(communityMage.name);
                    this.setCooldown(communityMage.coold);
                    this.setSpellData(communityMage.spell, communityMage.color);
                    this.type = communityMage.type;
                    this.tier = communityMage.tier;
                } catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        } else {
            this.setColor("white");
            this.setName("Oops");
        }
    }
}
