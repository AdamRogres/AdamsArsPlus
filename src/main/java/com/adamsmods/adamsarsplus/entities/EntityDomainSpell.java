package com.adamsmods.adamsarsplus.entities;

import com.adamsmods.adamsarsplus.entities.AdamsModEntities;

import com.adamsmods.adamsarsplus.lib.AdamsEntityTags;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.item.IWandable;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.common.entity.*;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.lib.EntityTags;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLinger;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectWall;
import com.hollingsworth.arsnouveau.setup.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import com.hollingsworth.arsnouveau.api.entity.ChangeableBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Predicate;


public class EntityDomainSpell extends EntityProjectileSpell {

    public static final EntityDataAccessor<Integer> ACCELERATES = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> AOE = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> LANDED = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> SENSITIVE = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> SHOULD_FALL = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> DOME = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> FILTER_SELF = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.BOOLEAN);

    public double extendedTime;
    public int maxProcs = 100;
    public int totalProcs;

    public EntityDomainSpell(EntityType<? extends EntityProjectileSpell> type, Level worldIn) {
        super(AdamsModEntities.DOMAIN_SPELL.get(), worldIn);
    }

    public EntityDomainSpell(Level worldIn, double x, double y, double z) {
        super(AdamsModEntities.DOMAIN_SPELL.get(), worldIn, x, y, z);
    }

    public EntityDomainSpell(Level worldIn, LivingEntity shooter) {
        super(AdamsModEntities.DOMAIN_SPELL.get(), worldIn, shooter);
    }

    public void setAccelerates(int accelerates) {
        entityData.set(ACCELERATES, accelerates);
    }


    @Override
    public void tick() {
        if (!level().isClientSide) {
            boolean isOnGround = level().getBlockState(blockPosition()).blocksMotion();
            this.setLanded(isOnGround);
        }
        super.tick();
        castSpells();
    }

    @Override
    public void traceAnyHit(@Nullable HitResult raytraceresult, Vec3 thisPosition, Vec3 nextPosition) {
    }

    @Override
    public void tickNextPosition() {
        if(true)
            return;
        if (!getLanded()) {
            this.setDeltaMovement(0, -0.2, 0);
        } else {
            this.setDeltaMovement(0, 0, 0);
        }
        super.tickNextPosition();
    }

    public void castSpells() {
        float aoe = getAoe();
        int flatAoe = Math.round(aoe);
        int radius = 3 + flatAoe;
        Predicate<Double> Sphere = shouldFall() ? (distance) -> distance <= radius + 0.5 && distance >= radius - 0.5 : (distance) -> (distance <= radius + 0.5);

        if (!level().isClientSide && age % (20 - 2 * getAccelerates()) == 0) {
            if (isSensitive()) {
                for (BlockPos p : BlockPos.withinManhattan(blockPosition(), radius, radius, radius)) {
                    if (Sphere.test(BlockUtil.distanceFromCenter(p, blockPosition()))) {
                        if(!getDome() || (blockPosition().getY() - 2 <  p.getY())) {
                            spellResolver.onResolveEffect(level(), new BlockHitResult(new Vec3(p.getX(), p.getY(), p.getZ()), Direction.UP, p, false));
                        }
                    }
                }
            } else {
                int i = 0;
                for (Entity entity : level().getEntities(null, new AABB(this.blockPosition()).inflate(getAoe(),getAoe(),getAoe()))) {
                    if (entity.equals(this) || entity.getType().is(AdamsEntityTags.DOMAIN_BLACKLIST))
                        continue;
                        if(!getDome() || (blockPosition().getY() - 2 < entity.getBlockY())) {
                            if (!getFilter() || !(spellResolver.spellContext.getUnwrappedCaster().equals(entity))) {
                                    spellResolver.onResolveEffect(level(), new EntityHitResult(entity));
                            }
                        }
                    i++;
                    if (i > 5)
                        break;
                }
                if(shouldFall()){
                    for (BlockPos p : BlockPos.withinManhattan(blockPosition(), radius, radius, radius)) {
                        if (Sphere.test(BlockUtil.distanceFromCenter(p, blockPosition()))) {
                            if(!getDome() || (blockPosition().getY() - 2 <  p.getY())) {
                                spellResolver.onResolveEffect(level(), new BlockHitResult(new Vec3(p.getX(), p.getY(), p.getZ()), Direction.UP, p, false));
                            }
                        }
                    }
                }
                totalProcs += i;
                if (totalProcs >= maxProcs)
                    this.remove(RemovalReason.DISCARDED);
            }
        }
    }


    @Override
    public int getExpirationTime() {
        return (int) (100 + extendedTime * 20);
    }

    @Override
    public int getParticleDelay() {
        return 0;
    }

   // @Override
   // public void playParticles() {
   //     ParticleUtil.spawnRitualAreaEffect(getOnPos(), level(), random, getParticleColor(), Math.round(getAoe()), 5, 20);
   //     ParticleUtil.spawnLight(level(), getParticleColor(), position().add(0, 0.5, 0), 10);
    //}

    public EntityDomainSpell(PlayMessages.SpawnEntity packet, Level world) {
        super(AdamsModEntities.DOMAIN_SPELL.get(), world);
    }

    @Override
    public EntityType<?> getType() {
        return AdamsModEntities.DOMAIN_SPELL.get();
    }

    @Override
    protected void onHit(HitResult result) {
        if (!level().isClientSide && result instanceof BlockHitResult && !this.isRemoved()) {
            BlockState state = level().getBlockState(((BlockHitResult) result).getBlockPos());
            if (state.is(BlockTags.PORTALS)) {
                state.getBlock().entityInside(state, level(), ((BlockHitResult) result).getBlockPos(), this);
                return;
            }
            this.setLanded(true);
        }
    }

    public int getAccelerates() {
        return entityData.get(ACCELERATES);
    }

    public void setAoe(float aoe) {
        entityData.set(AOE, aoe);
    }

    public float getAoe() {
        return (this.isSensitive() ? 1 : 3) + entityData.get(AOE);
    }

    public void setLanded(boolean landed) {
        entityData.set(LANDED, landed);
    }

    public boolean getLanded() {
        return entityData.get(LANDED);
    }

    public void setSensitive(boolean sensitive) {
        entityData.set(SENSITIVE, sensitive);
    }

    public boolean isSensitive() {
        return entityData.get(SENSITIVE);
    }

    public void setShouldFall(boolean shouldFall) {
        entityData.set(SHOULD_FALL, shouldFall);
    }

    public boolean shouldFall() {
        return entityData.get(SHOULD_FALL);
    }

    public boolean getDome() {
        return entityData.get(DOME);
    }

    public boolean getFilter() {
        return entityData.get(FILTER_SELF);
    }

    public void setDome(boolean dome) {
        entityData.set(DOME, dome);
    }

    public void setFilter(boolean filtered) {
        entityData.set(FILTER_SELF, filtered);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ACCELERATES, 0);
        entityData.define(AOE, 0f);
        entityData.define(LANDED, false);
        entityData.define(SENSITIVE, false);
        entityData.define(SHOULD_FALL, true);
        entityData.define(DOME, false);
        entityData.define(FILTER_SELF, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("sensitive", isSensitive());
        tag.putBoolean("shouldFall", shouldFall());
        tag.putBoolean("dome", getDome());
        tag.putBoolean("selfFiltered", getFilter());
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        setSensitive(compound.getBoolean("sensitive"));
        setShouldFall(compound.getBoolean("shouldFall"));
        setDome(compound.getBoolean("dome"));
        setFilter(compound.getBoolean("selfFiltered"));
    }

}