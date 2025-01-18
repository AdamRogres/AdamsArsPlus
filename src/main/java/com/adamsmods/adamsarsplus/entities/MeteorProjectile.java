package com.adamsmods.adamsarsplus.entities;

import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectMeteorSwarm;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.block.PortalBlock;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;

public class MeteorProjectile extends EntityProjectileSpell {

    public int iTime = 0;
    public double accelerates = 0;
    //public SpellResolver spellResolver;

    public MeteorProjectile(EntityType<? extends EntityProjectileSpell> type, Level worldIn) {
        super(type, worldIn);
    }

    public MeteorProjectile(Level world, SpellResolver resolver) {
        super(AdamsModEntities.METEOR_SPELL.get(), world, resolver);
        this.spellResolver = resolver;
    }

    public MeteorProjectile(PlayMessages.SpawnEntity packet, Level world) {
        super(AdamsModEntities.METEOR_SPELL.get(), world);
    }

    @Override
    public int getExpirationTime() {
        return EffectMeteorSwarm.INSTANCE.getProjectileLifespan() * 20;
    }

    @Override
    public EntityType<?> getType() {
        return AdamsModEntities.METEOR_SPELL.get();
    }

    @Override
    public void tick() {
        super.tick();

        iTime++;
        if(!level().isClientSide && iTime % (20 - 2 * accelerates) == 0){
            this.spellResolver.spell.add(AugmentAmplify.INSTANCE);
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
                ((PortalBlock) BlockRegistry.PORTAL_BLOCK.get()).onProjectileHit(this.level(), this.level().getBlockState(BlockPos.containing(raytraceresult.getLocation())), blockHitResult, this);
            }
        }

    }

    @Override
    public int getParticleDelay() {
        return 2;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

    }

}
