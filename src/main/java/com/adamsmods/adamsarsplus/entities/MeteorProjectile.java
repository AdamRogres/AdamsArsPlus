package com.adamsmods.adamsarsplus.entities;

import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectMeteorSwarm;
import com.adamsmods.adamsarsplus.util.GlyphEffectUtil;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.block.PortalBlock;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PlayMessages;
import org.apache.logging.log4j.core.config.builder.api.Component;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class MeteorProjectile extends EntityProjectileSpell {

    public int iTime = 0;
    public double accelerates = 0;
    public SpellResolver spellResolver;

    public MeteorProjectile(EntityType<? extends EntityProjectileSpell> type, Level worldIn) {
        super(type, worldIn);
    }

    public MeteorProjectile(Level world, SpellResolver resolver) {
        super(AdamsModEntities.METEOR_SPELL.get(), world, resolver);
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

    List<Predicate<LivingEntity>> ignore;
    LivingEntity target;

    public void setIgnored(List<Predicate<LivingEntity>> ignore) {
        this.ignore = ignore;
    }

    public List<Predicate<LivingEntity>> getIgnored() {
        return this.ignore;
    }

    public static List<Predicate<LivingEntity>> basicIgnores(LivingEntity shooter, Boolean targetPlayers, Spell spell) {
        List<Predicate<LivingEntity>> ignore = new ArrayList<>();

        ignore.add((entity -> !entity.isAlive()));
        ignore.add((entity -> entity == shooter));
        ignore.add(entity -> entity instanceof FamiliarEntity);
        ignore.add(entity -> entity.hasEffect(MobEffects.INVISIBILITY));
        ignore.add(shooter::isAlliedTo);
        if (targetPlayers) {
            ignore.add(entity -> entity instanceof Player);
        }

        return ignore;
    }

    @Override
    public void tickNextPosition() {
        if (!this.isRemoved()) {

            if ((target != null) && (!target.isAlive() || (target.distanceToSqr(this) > 50))) target = null;

            if (target == null && tickCount % 3 == 0) {


                List<LivingEntity> entities;
                if (getOwner() instanceof Player) {
                    entities = level().getEntitiesOfClass(LivingEntity.class,
                            this.getBoundingBox().inflate(4), this::shouldTarget);
                } else {
                    entities = level().getEntitiesOfClass(LivingEntity.class,
                            this.getBoundingBox().inflate(4), this::shouldTarget);
                }
                //update target or keep going
                if (entities.isEmpty() && target == null) {
                    super.tickNextPosition();
                } else if (!entities.isEmpty()) {
                    target = entities.stream().filter(e -> e.distanceToSqr(this) < 75).min(Comparator.comparingDouble(e -> e.distanceToSqr(this))).orElse(target);
                }
            }

            if (target != null) {
                homeTo(target.blockPosition());
            } else {
                super.tickNextPosition();
            }

        }
    }

    private void homeTo(BlockPos dest) {

        double posX = getX();
        double posY = getY();
        double posZ = getZ();
        double motionX = this.getDeltaMovement().x;
        double motionY = this.getDeltaMovement().y;
        double motionZ = this.getDeltaMovement().z;

        if (dest.getX() != 0 || dest.getY() != 0 || dest.getZ() != 0) {
            double targetX = dest.getX() + 0.5;
            double targetY = dest.getY() + 0.75;
            double targetZ = dest.getZ() + 0.5;
            Vec3 targetVector = new Vec3(targetX - posX, targetY - posY, targetZ - posZ);
            double length = targetVector.length();
            targetVector = targetVector.scale(0.3 / length);
            double weight = 0;
            if (length <= 3) {
                weight = (3.0 - length) * 0.3;
            }

            motionX = (0.9 - weight) * motionX + (0.1 + weight) * targetVector.x;
            motionY = (0.9 - weight) * motionY + (0.1 + weight) * targetVector.y;
            motionZ = (0.9 - weight) * motionZ + (0.1 + weight) * targetVector.z;
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        this.setPos(posX, posY, posZ);

        this.setDeltaMovement(motionX, motionY, motionZ);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        boolean b = super.canHitEntity(entity);
        if (entity instanceof LivingEntity) b &= shouldTarget((LivingEntity) entity);
        return b;
    }

    private boolean shouldTarget(LivingEntity e) {
        if (ignore == null) return false;
        for (Predicate<LivingEntity> p : getIgnored()) {
            if (p.test(e)) {
                return false;
            }
        }
        return true;
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
