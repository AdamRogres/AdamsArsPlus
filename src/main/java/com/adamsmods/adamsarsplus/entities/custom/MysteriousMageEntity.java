package com.adamsmods.adamsarsplus.entities.custom;

import com.adamsmods.adamsarsplus.datagen.CommunityMages;
import com.adamsmods.adamsarsplus.entities.ai.CadeCastingAGoal;
import com.adamsmods.adamsarsplus.entities.ai.CadeCastingBGoal;
import com.adamsmods.adamsarsplus.entities.ai.CadeDomainGoal;
import com.adamsmods.adamsarsplus.entities.ai.MageCastingGoal;
import com.adamsmods.adamsarsplus.entities.ai.pathfinding.AdvancedPathNavigate;
import com.adamsmods.adamsarsplus.entities.animations.ModAnimationsDefinition;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAccelerateThree;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.pathfinding.PathingStuckHandler;
import com.hollingsworth.arsnouveau.setup.registry.DamageTypesRegistry;
import com.hollingsworth.arsnouveau.setup.reward.Rewards;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MysteriousMageEntity extends Monster implements RangedAttackMob {

    public String color = "white";
    public Spell mageSpell;
    public int spellCooldown;

    public static final EntityDataAccessor<Boolean> CASTING =
            SynchedEntityData.defineId(MysteriousMageEntity.class, EntityDataSerializers.BOOLEAN);

    public int castCooldown;

    public MysteriousMageEntity(EntityType<? extends Monster> pEntityType, Level pLevel){
        super(pEntityType, pLevel);

        this.mageSpawn(pLevel);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState castAnimationState = new AnimationState();
    public int castAnimationTimeout = 0;


    @Override
    public void tick() {
        super.tick();

        if(castCooldown > 0) {
            castCooldown--;
        }

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

        //Cast A Animation control
        if(this.isCasting() && castAnimationTimeout <= 0) {
            castAnimationTimeout = 20;
            castAnimationState.start(this.tickCount);
        } else {
            --this.castAnimationTimeout;
        }
        if(!this.isCasting()) {
            castAnimationState.stop();
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

    public void setColor(String newColor){
        this.color = newColor;
    }

    public ParticleColor stringColor(String color){
        int r = 0;
        int g = 0;
        int b = 0;

        switch(color){
            case "white"        -> r = 255;
            case "light_gray"   -> r = 180;
            case "gray"         -> r = 100;
            case "black"        -> r = 0;
            case "red"          -> r = 255;
            case "orange"       -> r = 255;
            case "yellow"       -> r = 182;
            case "lime"         -> r = 255;
            case "green"        -> r = 0;
            case "light_blue"   -> r = 200;
            case "cyan"         -> r = 0;
            case "blue"         -> r = 0;
            case "purple"       -> r = 255;
            case "magenta"      -> r = 120;
            case "pink"         -> r = 255;
            case "brown"        -> r = 132;

            default             -> r = 255;
        }

        switch(color){
            case "white"        -> g = 255;
            case "light_gray"   -> g = 180;
            case "gray"         -> g = 100;
            case "black"        -> g = 0;
            case "red"          -> g = 0;
            case "orange"       -> g = 106;
            case "yellow"       -> g = 255;
            case "lime"         -> g = 255;
            case "green"        -> g = 255;
            case "light_blue"   -> g = 200;
            case "cyan"         -> g = 82;
            case "blue"         -> g = 0;
            case "purple"       -> g = 0;
            case "magenta"      -> g = 55;
            case "pink"         -> g = 0;
            case "brown"        -> g = 95;

            default             -> g = 255;
        }

        switch(color){
            case "white"        -> b = 255;
            case "light_gray"   -> b = 180;
            case "gray"         -> b = 100;
            case "black"        -> b = 0;
            case "red"          -> b = 0;
            case "orange"       -> b = 0;
            case "yellow"       -> b = 0;
            case "lime"         -> b = 0;
            case "green"        -> b = 0;
            case "light_blue"   -> b = 255;
            case "cyan"         -> b = 105;
            case "blue"         -> b = 255;
            case "purple"       -> b = 255;
            case "magenta"      -> b = 105;
            case "pink"         -> b = 110;
            case "brown"        -> b = 59;

            default             -> b = 255;
        }

        ParticleColor returnColor = new ParticleColor(r,g,b);

        return returnColor;
    }

    public Spell parseSpellData(String spellString, String color){

        Spell returnSpell = new Spell()
                .withColor(stringColor(color));

        String[] tokens = spellString.split("-");

        for(String t : tokens){
            ResourceLocation registryName = new ResourceLocation(t);
            AbstractSpellPart part = (AbstractSpellPart)GlyphRegistry.getSpellpartMap().get(registryName);
            if (part != null) {
                returnSpell.recipe.add(part);
            }
        }

        return returnSpell;
    }

    public void setCooldown(int newCooldown){
        this.spellCooldown = newCooldown;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CASTING, false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("cast", castCooldown);

    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.castCooldown = tag.getInt("cast");

    }

    public void setCustomName(@javax.annotation.Nullable Component pName) {
        super.setCustomName(pName);
    }

    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(2, new MageCastingGoal<>(this, 1.0D, 40f, () -> (castCooldown <= 0 && this.mageSpell != null), ModAnimationsDefinition.MAGE_CAST.hashCode(), 15, this.spellCooldown, this.mageSpell));

        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, (double)1.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.2F)
                .add(Attributes.FOLLOW_RANGE, (double)40.0F);
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

    public @org.jetbrains.annotations.Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @org.jetbrains.annotations.Nullable SpawnGroupData pSpawnData, @org.jetbrains.annotations.Nullable CompoundTag pDataTag) {
        RandomSource randomSource = pLevel.getRandom();
        if (randomSource.nextFloat() <= 0.1F && !CommunityMages.mages.isEmpty()) {
            try {
                CommunityMages.ComMages communityMage = (CommunityMages.ComMages)CommunityMages.mages.get(randomSource.nextInt(CommunityMages.mages.size()));
                this.setColor(communityMage.color);
                this.setCustomName(Component.literal(communityMage.name));
                this.setCooldown(Integer.parseInt(communityMage.coold));
                this.mageSpell = parseSpellData(communityMage.spell, communityMage.color);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.setColor("white");
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public void mageSpawn(Level pLevel) {
        RandomSource randomSource = pLevel.getRandom();
        if (randomSource.nextFloat() <= 0.1F && !CommunityMages.mages.isEmpty()) {
            try {
                CommunityMages.ComMages communityMage = (CommunityMages.ComMages)CommunityMages.mages.get(randomSource.nextInt(CommunityMages.mages.size()));
                this.setColor(communityMage.color);
                this.setCustomName(Component.literal(communityMage.name));
                this.setCooldown(Integer.parseInt(communityMage.coold));
                this.mageSpell = parseSpellData(communityMage.spell, communityMage.color);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.setColor("white");
        }
    }

}
