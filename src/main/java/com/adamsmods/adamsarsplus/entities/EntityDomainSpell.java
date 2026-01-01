package com.adamsmods.adamsarsplus.entities;

import com.adamsmods.adamsarsplus.block.tile.DomainShellTile;

import com.adamsmods.adamsarsplus.lib.AdamsEntityTags;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.common.entity.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.SIMPLE_DOMAIN_EFFECT;
import static com.adamsmods.adamsarsplus.Config.Common.MAX_DOMAIN_ENTITIES;


public class EntityDomainSpell extends EntityProjectileSpell {

    public static final EntityDataAccessor<Integer> ACCELERATES = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> AOE = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> LANDED = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> OPEN = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> DOME = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> FILTER_SELF = SynchedEntityData.defineId(EntityDomainSpell.class, EntityDataSerializers.BOOLEAN);

    public double extendedTime;
    public int maxProcs = 100;
    public int totalProcs;
    public int shellblocks;
    public double refinement;

    public EntityDomainSpell(EntityType<? extends EntityProjectileSpell> type, Level worldIn) {
        super(type, worldIn);
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

        if(calcShell() > this.shellblocks / 2){
            this.remove(RemovalReason.DISCARDED);
        }
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
        int radius = 4 + flatAoe;
        Predicate<Double> Sphere = (distance) -> (distance <= radius + 0.5);

        if (!level().isClientSide && age % Math.max(20 - 2 * getAccelerates(), 2) == 0) {
            if (getOpen()) {
                for (BlockPos p : BlockPos.withinManhattan(blockPosition(), radius, radius, radius)) {
                    if (Sphere.test(BlockUtil.distanceFromCenter(p, blockPosition()))) {
                        if(!getDome() || (blockPosition().getY() - 2 <  p.getY())) {
                            if(level().getBlockState(p).getBlock() == Blocks.AIR){
                                if(level().getRandom().nextIntBetweenInclusive(0, 100) > Math.min(5 * radius, 85)){
                                    spellResolver.onResolveEffect(level(), new BlockHitResult(new Vec3(p.getX(), p.getY(), p.getZ()), Direction.UP, p, false));
                                }
                            } else {
                                if(level().getRandom().nextIntBetweenInclusive(0, 100) > Math.min(3 * radius, 60)){
                                    spellResolver.onResolveEffect(level(), new BlockHitResult(new Vec3(p.getX(), p.getY(), p.getZ()), Direction.UP, p, false));
                                }
                            }

                        }
                    }
                }
            }

            int i = 0;
            for (Entity entity : level().getEntities(null, new AABB(this.blockPosition()).inflate(radius,radius,radius))) {
                if (entity.equals(this) || entity.getType().is(AdamsEntityTags.DOMAIN_BLACKLIST))
                    continue;
                if (entity instanceof LivingEntity){
                    if(((LivingEntity) entity).hasEffect(SIMPLE_DOMAIN_EFFECT.get())){
                        continue;
                    }
                } else {
                    continue;
                }

                if(!getDome() || (blockPosition().getY() - 2 < entity.getBlockY())) {
                    if (!getFilter() || !(spellResolver.spellContext.getUnwrappedCaster().equals(entity))) {
                        spellResolver.onResolveEffect(level(), new EntityHitResult(entity));
                    }
                }

                i++;
                if (i > MAX_DOMAIN_ENTITIES.get())
                    break;
            }

            totalProcs += i;
            if (totalProcs >= maxProcs)
                this.remove(RemovalReason.DISCARDED);
        }
    }

    public int calcShell(){
        int breaks = this.shellblocks;

        float aoe = getAoe();
        int flatAoe = Math.round(aoe);
        int radius = 4 + flatAoe;
        Predicate<Double> Sphere =  (distance) -> (distance <= radius + 0.5);

        for (BlockPos p : BlockPos.withinManhattan(blockPosition(), radius, radius, radius)) {
            if (Sphere.test(BlockUtil.distanceFromCenter(p, blockPosition()))) {
               BlockEntity var12 = level().getBlockEntity(p);

               if (var12 instanceof DomainShellTile) {
                   DomainShellTile tile = (DomainShellTile)var12;

                   if(tile.refinement == this.refinement){
                       breaks--;
                   } else if (tile.refinement < this.refinement) {
                       ((DomainShellTile) var12).age = (double)120.0F + (double)20.0F * ((DomainShellTile) var12).lengthModifier;
                   }
               }
            }
        }

        return breaks;
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
        return 1 + entityData.get(AOE);
    }

    public void setLanded(boolean landed) {
        entityData.set(LANDED, landed);
    }

    public boolean getLanded() {
        return entityData.get(LANDED);
    }

    public void setOpen(boolean open) {
        entityData.set(OPEN, open);
    }

    public boolean getOpen() {
        return entityData.get(OPEN);
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
        entityData.define(OPEN, false);
        entityData.define(DOME, false);
        entityData.define(FILTER_SELF, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("open", getOpen());
        tag.putBoolean("dome", getDome());
        tag.putBoolean("selfFiltered", getFilter());
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        setOpen(compound.getBoolean("open"));
        setDome(compound.getBoolean("dome"));
        setFilter(compound.getBoolean("selfFiltered"));
    }

}