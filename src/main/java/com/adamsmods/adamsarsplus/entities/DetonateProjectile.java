package com.adamsmods.adamsarsplus.entities;

import com.adamsmods.adamsarsplus.glyphs.method_glyph.MethodDetonate;
import com.hollingsworth.arsnouveau.api.block.IPrismaticBlock;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.block.PortalBlock;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.lib.EntityTags;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketANEffect;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;

public class DetonateProjectile extends EntityProjectileSpell {

    public static final EntityDataAccessor<Integer> DELAY = SynchedEntityData.defineId(DetonateProjectile.class, EntityDataSerializers.INT);

    public double extendedTime;
    public int    iTime = 0;

    public DetonateProjectile(EntityType<? extends EntityProjectileSpell> type, Level worldIn) {
        super(type, worldIn);
    }

    public DetonateProjectile(Level world, SpellResolver resolver) {
        super(AdamsModEntities.DETONATE_SPELL.get(), world, resolver);
    }

    public DetonateProjectile(PlayMessages.SpawnEntity packet, Level world) {
        super(AdamsModEntities.DETONATE_SPELL.get(), world);
    }

  //  public DetonateProjectile(PlayMessages.SpawnEntity spawnEntity, Level level) {
    //    super(spawnEntity, level);
 //   }

    @Override
    public int getExpirationTime() {
        return MethodDetonate.INSTANCE.getProjectileLifespan() * 20;
    }

    @Override
    public EntityType<?> getType() {
        return AdamsModEntities.DETONATE_SPELL.get();
    }

    public int maxProcs = 20;
    public int totalProcs;

    @Override
    public void tick() {
        super.tick();
        if (age >= 30 + extendedTime * 10 + iTime) {
            castSpells();
            this.attemptRemoval();
            iTime = age;
        }
    }

    public void traceAnyHit(@Nullable HitResult raytraceresult, Vec3 thisPosition, Vec3 nextPosition) {
        if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS) {
            nextPosition = raytraceresult.getLocation();
        }

        EntityHitResult entityraytraceresult = this.findHitEntity(thisPosition, nextPosition);
        if (entityraytraceresult != null) {
            raytraceresult = entityraytraceresult;
        }

        if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
            this.hasImpulse = true;
        }

        if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.MISS && raytraceresult instanceof BlockHitResult blockHitResult) {
            if (this.canTraversePortals()) {
                ((PortalBlock)BlockRegistry.PORTAL_BLOCK.get()).onProjectileHit(this.level(), this.level().getBlockState(BlockPos.containing(raytraceresult.getLocation())), blockHitResult, this);
            }
        }

    }

    public void castSpells() {
        BlockPos p = this.blockPosition();

        if (!this.level().isClientSide() && this.spellResolver != null) {
            spellResolver.onResolveEffect(level(), new BlockHitResult(new Vec3(p.getX(), p.getY(), p.getZ()), Direction.UP, p, false));
        }
    }

    public int getDelay() {
        return entityData.get(DELAY);
    }

    public int getDuration(){
        return ((int) extendedTime);
    }

    public void setDelay(int time) {
        entityData.set(DELAY, time);
    }


    public boolean isSensitive() {
        return numSensitive > 0;
    }

    @Override
    public int getParticleDelay() {
        return 2;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DELAY, 0);
    }

}