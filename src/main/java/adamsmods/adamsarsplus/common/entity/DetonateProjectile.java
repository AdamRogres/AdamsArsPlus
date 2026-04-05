package adamsmods.adamsarsplus.common.entity;

import adamsmods.adamsarsplus.common.glyphs.method_glyph.MethodDetonate;
import adamsmods.adamsarsplus.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.block.PortalBlock;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;


import javax.annotation.Nullable;

public class DetonateProjectile extends EntityProjectileSpell {

    public static final EntityDataAccessor<Integer> DELAY = SynchedEntityData.defineId(DetonateProjectile.class, EntityDataSerializers.INT);

    public double extendedTime;
    public int    iTime = 0;

    public DetonateProjectile(EntityType<? extends EntityProjectileSpell> type, Level worldIn) {
        super(type, worldIn);
    }

    public DetonateProjectile(Level world, SpellResolver resolver) {
        super(ModEntities.DETONATE_SPELL.get(), world, resolver);
    }

    @Override
    public int getExpirationTime() {
        return MethodDetonate.INSTANCE.getProjectileLifespan() * 20;
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.DETONATE_SPELL.get();
    }

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

        if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
            this.hasImpulse = true;
        }
        if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.MISS && raytraceresult instanceof BlockHitResult blockHitResult
                && canTraversePortals()) {
            BlockRegistry.PORTAL_BLOCK.get().onProjectileHit(level, level.getBlockState(BlockPos.containing(raytraceresult.getLocation())),
                    blockHitResult, this);

        }
    }

    public void castSpells() {
        BlockPos p = this.blockPosition();

        if (!level.isClientSide) {
            resolver().getNewResolver(resolver().spellContext.clone().makeChildContext()).onResolveEffect(level, new
                    BlockHitResult(new Vec3(p.getX(), p.getY(), p.getZ()), Direction.UP, p, false));
        } else {
            resolveEmitter.setPositionOffset(p.subtract(getOnPos()).getCenter());
            resolveEmitter.tick(level);
        }
        if (!level.isClientSide) {
            resolveSound.playSound(level, getX(), getY(), getZ());
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
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DELAY, 0);
    }

}