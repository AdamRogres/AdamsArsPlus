package adamsmods.adamsarsplus.common.entity;

import adamsmods.adamsarsplus.common.glyphs.augment_glyph.AugmentAccelerateThree;
import adamsmods.adamsarsplus.common.glyphs.augment_glyph.AugmentAccelerateTwo;
import adamsmods.adamsarsplus.common.glyphs.effect_glyph.EffectMeteorSwarm;
import adamsmods.adamsarsplus.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.block.PortalBlock;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class MeteorProjectile extends EntityProjectileSpell {

    public int iTime = 0;
    public double accelerates = 0;
    //public SpellResolver spellResolver;

    public MeteorProjectile(EntityType<? extends EntityProjectileSpell> type, Level worldIn) {
        super(type, worldIn);
    }

    public MeteorProjectile(Level world, SpellResolver resolver) {
        super(ModEntities.METEOR_SPELL.get(), world, resolver);

        this.accelerates =    resolver().spell.getInstanceCount(AugmentAccelerate.INSTANCE)
                + 2 * resolver().spell.getInstanceCount(AugmentAccelerateTwo.INSTANCE)
                + 4 * resolver().spell.getInstanceCount(AugmentAccelerateThree.INSTANCE);
    }

    @Override
    public int getExpirationTime() {
        return EffectMeteorSwarm.INSTANCE.getProjectileLifespan() * 20;
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.METEOR_SPELL.get();
    }

    @Override
    public void tick() {
        super.tick();

        iTime++;
        if(!level().isClientSide && iTime % Math.max(20 - 2 * accelerates, 2) == 0){
            resolver().spell.add(AugmentAmplify.INSTANCE);
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

    @Override
    public int getParticleDelay() {
        return 2;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
    }

}
