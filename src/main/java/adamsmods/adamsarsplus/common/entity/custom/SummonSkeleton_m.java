package adamsmods.adamsarsplus.common.entity.custom;

import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.IFollowingSummon;
import com.hollingsworth.arsnouveau.common.entity.goal.FollowSummonerGoal;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import adamsmods.adamsarsplus.registry.ModEntities;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;



public class SummonSkeleton_m extends Skeleton implements IFollowingSummon, ISummon {
    public static final net.minecraft.network.syncher.EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID =
            SynchedEntityData.defineId(SummonSkeleton_m.class, net.minecraft.network.syncher.EntityDataSerializers.OPTIONAL_UUID);
    private final RangedBowAttackGoal<SummonSkeleton_m> bowGoal = new RangedBowAttackGoal(this, (double)1.0F, 20, 15.0F);
    private final MeleeAttackGoal meleeGoal;
    private LivingEntity owner;
    private @Nullable BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;
    private Spell spell = new Spell();

    class NamelessClass_1 extends MeleeAttackGoal {
        // Reach in blocks (entity-position distance). Edit independently for this goal.
        public double meleeReach = 2.0;

        @Override
        protected boolean canPerformAttack(LivingEntity target) {
            return this.isTimeToAttack() && target.isAlive()
                    && this.mob.distanceToSqr(target) <= this.getAttackReachSqr(target)
                    && this.mob.getSensing().hasLineOfSight(target);
        }

        NamelessClass_1(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(target);

                performSpellAttack(this.mob, spell, target);
            }

        }

        public void stop() {
            super.stop();
            SummonSkeleton_m.this.setAggressive(false);
        }

        public void start() {
            super.start();
            SummonSkeleton_m.this.setAggressive(true);
        }
        protected double getAttackReachSqr(LivingEntity target) {
            return meleeReach * meleeReach;
        }
    }

    public SummonSkeleton_m(Level level, LivingEntity owner, ItemStack item, SpellContext spell) {
        this(ModEntities.SUMMON_SKELETON_M.get(), level);
        this.spell = spell.getSpell();
        this.setWeapon(item);
        this.owner = owner;
        this.limitedLifespan = true;
        this.setOwnerID(owner.getUUID());

    }

    public SummonSkeleton_m(EntityType<? extends Skeleton> entityType, Level level) {
        super(entityType, level);
        this.meleeGoal = new NamelessClass_1(this, 2.2, true);
        this.reassessWeaponGoal();
    }

    void performSpellAttack(LivingEntity entity, Spell spell, LivingEntity enemy) {
        if (entity.level().isClientSide || spell.isEmpty()) return;
        var style = spell.particleTimeline().get(
                adamsmods.adamsarsplus.common.particle.ModDomainTimelines.UNDEAD_MAGE.get()).settings;
        // A fresh emitter on each melee cast sends the selected effect to nearby clients.
        var emitter = new com.hollingsworth.arsnouveau.api.particle.ParticleEmitter(enemy, style.onResolvingEffect);
        emitter.tick(entity.level());
        style.resolveSound.sound.playSound(entity.level(), enemy.getX(), enemy.getY(), enemy.getZ());
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(entity.level(), spell, entity, new LivingCaster(entity)));
        resolver.onResolveEffect(entity.level(), new EntityHitResult(enemy));
    }

    public EntityType<?> getType() {
        return (EntityType)ModEntities.SUMMON_SKELETON_M.get();
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn) {
        this.populateDefaultEquipmentSlots(this.getRandom(), difficultyIn);
        this.populateDefaultEquipmentEnchantments(worldIn, this.getRandom(), difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
    }

    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance pDifficulty) {
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

    protected void registerGoals() {
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(2, new FollowSummonerGoal(this, this.owner, (double) 1.0F, 9.0F, 3.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, (double) 1.0F));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this, new Class[]{SummonSkeleton_m.class}) {
            protected boolean canAttack(@Nullable LivingEntity pPotentialTarget, TargetingConditions pTargetPredicate) {
                return pPotentialTarget != null && super.canAttack(pPotentialTarget, pTargetPredicate) && !pPotentialTarget.getUUID().equals(SummonSkeleton_m.this.getOwnerUUID());
            }
        });
        this.targetSelector.addGoal(1, new CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, IronGolem.class, true));
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    public void setWeapon(ItemStack item) {
        this.setItemSlot(EquipmentSlot.MAINHAND, item);

        this.setItemSlot(EquipmentSlot.HEAD, ItemsRegistry.ARCANIST_HOOD.asItem().getDefaultInstance());
        this.setItemSlot(EquipmentSlot.CHEST, ItemsRegistry.ARCANIST_ROBES.asItem().getDefaultInstance());
        this.setItemSlot(EquipmentSlot.LEGS, ItemsRegistry.ARCANIST_LEGGINGS.asItem().getDefaultInstance());
        this.setItemSlot(EquipmentSlot.FEET, ItemsRegistry.ARCANIST_BOOTS.asItem().getDefaultInstance());

        var style = this.spell.particleTimeline().get(
                adamsmods.adamsarsplus.common.particle.ModDomainTimelines.UNDEAD_MAGE.get()).settings;
        var dye = adamsmods.adamsarsplus.util.SpellArmorColor.closestDyeColor(
                style.onResolvingEffect.particleOptions().colorProp().color().getColor());
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            getItemBySlot(slot).set(net.minecraft.core.component.DataComponents.BASE_COLOR, dye);
            setDropChance(slot, 0.0F);
        }
        setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.reassessWeaponGoal();
    }

    public void reassessWeaponGoal() {
        if (this.level() instanceof ServerLevel && this.meleeGoal != null && this.bowGoal != null) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, (item) -> item instanceof BowItem));
            if (itemstack.is(Items.BOW)) {
                this.bowGoal.setMinAttackInterval(20);
                this.goalSelector.addGoal(4, this.bowGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypes.MOB_ATTACK)) {
            Entity var4 = pSource.getEntity();
            if (var4 instanceof ISummon) {
                ISummon summon = (ISummon)var4;
                if (summon.getOwnerUUID() != null && summon.getOwnerUUID().equals(this.getOwnerUUID())) {
                    return false;
                }
            }
        }

        return super.hurt(pSource, pAmount);
    }

    public void tick() {
        super.tick();
        if (!level().isClientSide && this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(this.level().damageSources().starve(), 20.0F);
        }

    }

    public PlayerTeam getTeam() {
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

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.spell = new Spell();
        if (compound.contains("MageSpell", 8)) {
            try {
                this.spell = Spell.fromBinaryBase64(compound.getString("MageSpell"));
            } catch (RuntimeException exception) {
                com.mojang.logging.LogUtils.getLogger().warn("Unable to load undead mage spell", exception);
            }
        }
        if (compound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
        }

        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }

        UUID s;
        if (compound.hasUUID("OwnerUUID")) {
            s = compound.getUUID("OwnerUUID");
        } else {
            String s1 = compound.getString("Owner");
            s = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s1);
        }

        if (s != null) {
            try {
                this.setOwnerID(s);
            } catch (Throwable var4) {
            }
        }

    }

    public void setLimitedLife(int lifeTicks) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = lifeTicks;
    }

    public LivingEntity getOwnerFromID() {
        try {
            UUID uuid = this.getOwnerUUID();
            if (uuid == null) return null;
            if (owner != null && owner.isAlive() && uuid.equals(owner.getUUID())) return owner;
            if (level() instanceof ServerLevel server && server.getEntity(uuid) instanceof LivingEntity summoner) {
                this.owner = summoner;
                return summoner;
            }
            return this.level().getPlayerByUUID(uuid);
        } catch (IllegalArgumentException var21) {
            return null;
        }
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(OWNER_UNIQUE_ID, Optional.empty());
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("MageSpell", spell.toBinaryBase64());
        if (this.boundOrigin != null) {
            compound.putInt("BoundX", this.boundOrigin.getX());
            compound.putInt("BoundY", this.boundOrigin.getY());
            compound.putInt("BoundZ", this.boundOrigin.getZ());
        }

        if (this.limitedLifespan) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }

        if (this.getOwnerUUID() == null) {
            compound.putUUID("OwnerUUID", Util.NIL_UUID);
        } else {
            compound.putUUID("OwnerUUID", this.getOwnerUUID());
        }

    }

    protected boolean isSunBurnTick() {
        return false;
    }

    public void die(DamageSource cause) {
        super.die(cause);
        this.onSummonDeath(this.level(), cause, false);
    }

    public int getTicksLeft() {
        return this.limitedLifeTicks;
    }

    public void setTicksLeft(int ticks) {
        setLimitedLife(ticks);
    }

    public @Nullable UUID getOwnerUUID() {
        return (UUID)((Optional)this.entityData.get(OWNER_UNIQUE_ID)).orElse((Object)null);
    }

    public void setOwnerID(UUID uuid) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
    }
}

