package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.ArsNouveauRegistry;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.DamageUtil;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.client.particle.*;
import com.hollingsworth.arsnouveau.common.entity.EnchantedFallingBlock;
import com.hollingsworth.arsnouveau.common.items.curios.ShapersFocus;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.*;
import static java.lang.Math.abs;
import static java.lang.Math.floor;

public class EffectLimitless extends AbstractEffect implements IDamageEffect {
    public static EffectLimitless INSTANCE = new EffectLimitless(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectlimitless"), "Limitless");
    private static final String LIMITLESS_KEY = "adams_limitless_frozen";
    private static final String LIMITLESS_VX = "adams_limitless_prev_vx";
    private static final String LIMITLESS_VY = "adams_limitless_prev_vy";
    private static final String LIMITLESS_VZ = "adams_limitless_prev_vz";
    private static final ConcurrentHashMap<UUID, Vec3> savedVelocities = new ConcurrentHashMap();

    public EffectLimitless(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world,@NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (rayTraceResult.getEntity() instanceof LivingEntity living) {
            if(spellStats.getDurationMultiplier() > 0 && spellStats.hasBuff(AugmentPierce.INSTANCE)){
                double amp = spellStats.hasBuff(AugmentDampen.INSTANCE) ? (spellStats.getAmpMultiplier() / 3) : (spellStats.getAmpMultiplier() / -3);
                int time    = (int)(20 + 20 * spellStats.getDurationMultiplier());
                int radius = (int)((double)1.0F + spellStats.getAoeMultiplier());

                boolean push = amp < 0;

                BlockPos blockPos = rayTraceResult.getEntity().blockPosition();

                AtomicInteger ticks = new AtomicInteger(0);
                AdamsArsPlus.setInterval(() -> {
                    int t = ticks.incrementAndGet();
                    this.makeLSphere(blockPos, world, shooter, spellStats, spellContext, resolver);
                    if (t >= time) {
                        this.restoreLSphere(shooter.blockPosition(), world, shooter, radius);
                    }
                    if(t % 5 == 0 && !world.isClientSide){
                        this.spawnParticles(world, blockPos, radius, push);
                    }
                }, 1, time, () -> false);

            } else if(spellStats.getDurationMultiplier() > 0){
                int amp     = (int)(spellStats.getAmpMultiplier());
                int time    = (int)(20 + 20 * spellStats.getDurationMultiplier());

                shooter.addEffect(new MobEffectInstance((MobEffect)ArsNouveauRegistry.LIMITLESS_EFFECT.get(), time, amp));
                int radius = (int)((double)1.0F + spellStats.getAoeMultiplier());

                AtomicInteger ticks = new AtomicInteger(0);
                AdamsArsPlus.setInterval(() -> {
                    int t = ticks.incrementAndGet();
                    this.makeLSphere(shooter.blockPosition(), world, shooter, spellStats, spellContext, resolver);
                    this.restoreOutOfSphere(shooter.blockPosition(), world, shooter, radius);
                    if (t >= time) {
                        this.restoreLSphere(shooter.blockPosition(), world, shooter, radius);
                    }

                }, 1, time, () -> !(shooter.hasEffect(LIMITLESS_EFFECT.get())));
            } else if(spellStats.hasBuff(AugmentPierce.INSTANCE)){
                double amp = spellStats.hasBuff(AugmentDampen.INSTANCE) ? (spellStats.getAmpMultiplier() / 3) : (spellStats.getAmpMultiplier() / -3);
                int radius = (int) (1 + spellStats.getAoeMultiplier());

                this.knockback(living, shooter, (float) amp, radius);

                float damage = (float) (4 + (3 * (spellStats.getAmpMultiplier())));
                DamageSource damageS = DamageUtil.source(world, DamageTypes.FLY_INTO_WALL, shooter);
                if(spellStats.hasBuff(AugmentExtract.INSTANCE)){
                    attemptDamage(world, shooter, spellStats, spellContext, resolver, living, damageS, 0);
                } else {
                    attemptDamage(world, shooter, spellStats, spellContext, resolver, living, damageS, damage);
                }
                this.spawnParticles(world, living.blockPosition(), radius, amp < 0);
            } else {
                this.makeLSphere(rayTraceResult.getEntity().blockPosition(), world, shooter, spellStats, spellContext, resolver);
            }
        }
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world,@NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if(spellStats.getDurationMultiplier() > 0 && spellStats.hasBuff(AugmentPierce.INSTANCE)){
            double amp = spellStats.hasBuff(AugmentDampen.INSTANCE) ? (spellStats.getAmpMultiplier() / 3) : (spellStats.getAmpMultiplier() / -3);
            int time    = (int)(20 + 20 * spellStats.getDurationMultiplier());
            int radius = (int)((double)1.0F + spellStats.getAoeMultiplier());

            boolean push = amp < 0;

            BlockPos blockPos = rayTraceResult.getBlockPos();

            AtomicInteger ticks = new AtomicInteger(0);
            AdamsArsPlus.setInterval(() -> {
                int t = ticks.incrementAndGet();
                this.makeLSphere(blockPos, world, shooter, spellStats, spellContext, resolver);
                if (t >= time) {
                    this.restoreLSphere(shooter.blockPosition(), world, shooter, radius);
                }
                if(t % 5 == 0 && !world.isClientSide){
                    this.spawnParticles(world, blockPos, radius, push);
                }
            }, 1, time, () -> false);

        } else if(spellStats.getDurationMultiplier() > 0){
            int amp     = (int)(spellStats.getAmpMultiplier());
            int time    = (int)(20 + 20 * spellStats.getDurationMultiplier());

            shooter.addEffect(new MobEffectInstance((MobEffect)ArsNouveauRegistry.LIMITLESS_EFFECT.get(), time, amp));
            int radius = (int)((double)1.0F + spellStats.getAoeMultiplier());

            AtomicInteger ticks = new AtomicInteger(0);
            AdamsArsPlus.setInterval(() -> {
                int t = ticks.incrementAndGet();
                this.makeLSphere(shooter.blockPosition(), world, shooter, spellStats, spellContext, resolver);
                this.restoreOutOfSphere(shooter.blockPosition(), world, shooter, radius);
                if (t >= time) {
                    this.restoreLSphere(shooter.blockPosition(), world, shooter, radius);
                }

            }, 1, time, () -> !(shooter.hasEffect(LIMITLESS_EFFECT.get())));
        } else {
            this.makeLSphere(rayTraceResult.getBlockPos(), world, shooter, spellStats, spellContext, resolver);
        }

    }

    public double absDifference(int radius, double ratio) {
        double value = (double)0.0F;
        if (ratio >= (double)0.0F) {
            value = (double)radius - ratio;
        } else {
            value = ((double)radius + ratio) * (double)-1.0F;
        }

        return value;
    }

    public void knockback(Entity target, LivingEntity shooter, float amp, float radius) {
        this.knockback(target, (double)amp * radius, (double)Mth.sin(shooter.getYHeadRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(shooter.getYHeadRot() * ((float)Math.PI / 180F))));
    }

    public void knockback(Entity entity, double strength, double xRatio, double zRatio) {
        if (entity instanceof LivingEntity living) {
            strength *= 5.0;
        }

        if (strength >= (double)0.0F) {
            entity.hasImpulse = true;
            Vec3 vec3 = entity.getDeltaMovement();
            Vec3 vec31 = (new Vec3(xRatio, (double)0.0F, zRatio)).normalize().scale(strength);
            entity.setDeltaMovement(vec3.x / (double)2.0F - vec31.x, entity.onGround() ? Math.min(0.4, vec3.y / (double)2.0F + strength) : vec3.y, vec3.z / (double)2.0F - vec31.z);
        } else {
            entity.hasImpulse = true;
            Vec3 vec3 = entity.getDeltaMovement();
            Vec3 vec31 = (new Vec3(xRatio, (double)0.0F, zRatio)).normalize().scale(strength);
            entity.setDeltaMovement(vec3.x / (double)2.0F - vec31.x, entity.onGround() ? Math.min(0.4, vec3.y / (double)2.0F + strength) : vec3.y, vec3.z / (double)2.0F - vec31.z);
        }

    }

    public void knockback(Entity target, double xPosition, double yPosition, double zPosition, float strength, int radius) {
        double x = xPosition - target.getBlockX();
        double y = yPosition - target.getBlockY();
        double z = zPosition - target.getBlockZ();

        knockback(target, strength, x, y, z, radius);
    }

    public void knockback(Entity entity, double strength, double xRatio, double yRatio, double zRatio, int radius) {
            strength = Math.max(Math.min(strength, 1), -1);

            entity.setDeltaMovement(Vec3.ZERO);
            if(strength <=  0) {
                entity.setDeltaMovement(strength * -0.5 * xRatio, strength * -0.5 * yRatio, strength * -0.5 * zRatio);
            }
            else{
                entity.setDeltaMovement(strength * -0.5 * absDifference(radius, xRatio), strength * -0.5 * absDifference(radius, yRatio), strength * -0.5 * absDifference(radius, zRatio));
            }

            entity.hasImpulse = true;
            entity.hurtMarked = true;
    }

    public void makeLSphere(BlockPos center, Level world, @NotNull Entity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver){
        int radius = (int) (1 + spellStats.getAoeMultiplier());
        double amp = spellStats.hasBuff(AugmentDampen.INSTANCE) ? (spellStats.getAmpMultiplier() / 3) : (spellStats.getAmpMultiplier() / -3);

        Predicate<Double> Sphere = (distance) -> (distance <= radius + 0.5);

        //Target non-living entities like arrows and spell projectiles
        for(Entity entity : world.getEntities(shooter, (new AABB(center)).inflate((double)radius, (double)radius, (double)radius))) {
            if (Sphere.test(BlockUtil.distanceFromCenter(entity.blockPosition(), center)) && (spellStats.hasBuff(AugmentSensitive.INSTANCE) || !spellContext.getUnwrappedCaster().equals(entity))) {
                if (spellStats.hasBuff(AugmentPierce.INSTANCE)) {
                    this.knockback(entity, (double)center.getX(), (double)center.getY(), (double)center.getZ(), (float)amp, radius);
                } else {
                    this.applyFreezeOrScale(entity, amp);
                }
            }
        }
        //Target living entities
        for(LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, (new AABB(center)).inflate((double)radius, (double)radius, (double)radius))) {
            if (Sphere.test(BlockUtil.distanceFromCenter(entity.blockPosition(), center)) && (spellStats.hasBuff(AugmentSensitive.INSTANCE) || !spellContext.getUnwrappedCaster().equals(entity))) {
                if (spellStats.hasBuff(AugmentPierce.INSTANCE)) {

                    this.knockback(entity, (double)center.getX(), (double)center.getY(), (double)center.getZ(), (float)amp, radius);
                    if (shooter instanceof LivingEntity) {
                        LivingEntity shooterL = (LivingEntity)shooter;
                        float damage = (float)((double)4.0F + (double)3.0F * spellStats.getAmpMultiplier());
                        DamageSource damageS = DamageUtil.source(world, DamageTypes.FLY_INTO_WALL, shooter);
                        if(spellStats.getDurationMultiplier() <= 0){
                            if (spellStats.hasBuff(AugmentExtract.INSTANCE)) {
                                this.attemptDamage(world, shooterL, spellStats, spellContext, resolver, entity, damageS, 0.0F);
                            } else {
                                this.attemptDamage(world, shooterL, spellStats, spellContext, resolver, entity, damageS, damage);
                            }
                        }
                    }
                } else {
                    this.applyFreezeOrScale(entity, amp);
                }
            }
        }
    }

    private void applyFreezeOrScale(Entity entity, double amp) {
        Vec3 current = entity.getDeltaMovement();
        CompoundTag pdata = entity.getPersistentData();
        if (Math.abs(amp) < 1.0E-6) {
            UUID id = entity.getUUID();
            if (!savedVelocities.containsKey(id)) {
                savedVelocities.put(id, current);
                pdata.putDouble("adams_limitless_prev_vx", current.x);
                pdata.putDouble("adams_limitless_prev_vy", current.y);
                pdata.putDouble("adams_limitless_prev_vz", current.z);
                pdata.putBoolean("adams_limitless_frozen", true);
            }

            entity.setDeltaMovement(Vec3.ZERO);
            if (entity instanceof Projectile) {
                try {
                    entity.setNoGravity(true);
                } catch (Throwable var8) {
                }
            }
        } else if (!pdata.getBoolean("adams_limitless_frozen") && !savedVelocities.containsKey(entity.getUUID())) {
            entity.setDeltaMovement(current.scale(amp));
        }

        entity.hurtMarked = true;
    }

    private void restoreOutOfSphere(BlockPos center, Level world, Entity shooter, int radius){
        AABB box = (new AABB(center)).inflate((double)radius + 0.5, (double)radius + 0.5, (double)radius + 0.5);

        if (!savedVelocities.isEmpty()) {
            for(Entity e : world.getEntities((Entity)null, (new AABB(center)).inflate((double)Math.max(32, radius * 8), (double)Math.max(32, radius * 8), (double)Math.max(32, radius * 8)))) {
                if(world.getEntities((Entity)null, box).contains(e)){
                    continue;
                }

                UUID id = e.getUUID();
                if (savedVelocities.containsKey(id)) {
                    Vec3 prev = (Vec3)savedVelocities.remove(id);
                    if (prev != null) {
                        e.setDeltaMovement(prev);
                        e.hurtMarked = true;
                        e.getPersistentData().remove("adams_limitless_prev_vx");
                        e.getPersistentData().remove("adams_limitless_prev_vy");
                        e.getPersistentData().remove("adams_limitless_prev_vz");
                        e.getPersistentData().remove("adams_limitless_frozen");
                        if (e instanceof Projectile) {
                            try {
                                e.setNoGravity(false);
                            } catch (Throwable var18) {
                            }
                        }
                    }
                }
            }
        }

    }

    public void restoreLSphere(BlockPos center, Level world, Entity shooter, int radius) {
        if (!(world instanceof ServerLevel)) {
        }

        AABB box = (new AABB(center)).inflate((double)Math.max(8, radius * 2), (double)Math.max(8, radius * 2), (double)Math.max(8, radius * 2));

        for(Entity entity : world.getEntities(shooter, box)) {
            UUID id = entity.getUUID();
            if (savedVelocities.containsKey(id) || entity.getPersistentData().getBoolean("adams_limitless_frozen")) {
                Vec3 prev = (Vec3)savedVelocities.get(id);
                if (prev == null) {
                    CompoundTag pdata = entity.getPersistentData();
                    if (pdata.getBoolean("adams_limitless_frozen")) {
                        double vx = pdata.getDouble("adams_limitless_prev_vx");
                        double vy = pdata.getDouble("adams_limitless_prev_vy");
                        double vz = pdata.getDouble("adams_limitless_prev_vz");
                        prev = new Vec3(vx, vy, vz);
                    }
                }

                if (prev != null) {
                    entity.setDeltaMovement(prev);
                    entity.hurtMarked = true;
                    if (entity instanceof Projectile) {
                        try {
                            entity.setNoGravity(false);
                        } catch (Throwable var19) {
                        }
                    }
                }

                savedVelocities.remove(id);
                CompoundTag pdata = entity.getPersistentData();
                pdata.remove("adams_limitless_prev_vx");
                pdata.remove("adams_limitless_prev_vy");
                pdata.remove("adams_limitless_prev_vz");
                pdata.remove("adams_limitless_frozen");
            }
        }

        if (!savedVelocities.isEmpty()) {
            for(Entity e : world.getEntities((Entity)null, (new AABB(center)).inflate((double)Math.max(32, radius * 8), (double)Math.max(32, radius * 8), (double)Math.max(32, radius * 8)))) {
                UUID id = e.getUUID();
                if (savedVelocities.containsKey(id)) {
                    Vec3 prev = (Vec3)savedVelocities.remove(id);
                    if (prev != null) {
                        e.setDeltaMovement(prev);
                        e.hurtMarked = true;
                        e.getPersistentData().remove("adams_limitless_prev_vx");
                        e.getPersistentData().remove("adams_limitless_prev_vy");
                        e.getPersistentData().remove("adams_limitless_prev_vz");
                        e.getPersistentData().remove("adams_limitless_frozen");
                        if (e instanceof Projectile) {
                            try {
                                e.setNoGravity(false);
                            } catch (Throwable var18) {
                            }
                        }
                    }
                }
            }
        }

    }

    public void spawnParticles(Level world, BlockPos pos, float aoe, boolean push) {
        if(world instanceof ServerLevel serverLevel && !world.isClientSide){
            if(!push){
                for(int i = 0; i < 1 + Math.min(1, aoe); ++i) {
                    double d0 = (double)pos.getX();
                    double d1 = (double)pos.getY();
                    double d2 = (double)pos.getZ();
                    //serverLevel.sendParticles(ParticleTypes.DRIPPING_WATER GlowParticleData.createData(new ParticleColor(255, 0 , 0)), d0, d1, d2, 3, 0, 0, 0, aoe / 10);
                    serverLevel.sendParticles(ParticleTypes.DRIPPING_LAVA, d0, d1, d2, 3, 0, 0, 0, aoe / 10);
                }
            } else {
                for(int i = 0; i < 1 + Math.min(1, aoe); ++i) {
                    double d0 = (double)pos.getX();
                    double d1 = (double)pos.getY();
                    double d2 = (double)pos.getZ();
                    //serverLevel.sendParticles(GlowParticleData.createData(new ParticleColor(0, 0, 255)), d0, d1, d2, 3, 0, 0, 0, aoe / 10);
                    serverLevel.sendParticles(ParticleTypes.DRIPPING_WATER, d0, d1, d2, 3, 0, 0, 0, aoe / 10);
                }
            }
        }
    }

    @Override
    public int getDefaultManaCost() {
        return 90;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addPotionConfig(builder, 2);
        addExtendTimeConfig(builder, 1);
    }

   @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentAOE.INSTANCE,
                AugmentAmplify.INSTANCE,
                AugmentDampen.INSTANCE,
                AugmentSensitive.INSTANCE,
                AugmentPierce.INSTANCE,
                AugmentExtendTime.INSTANCE,
                AugmentExtract.INSTANCE
        );
    }

    @Override
    public String getBookDescription() {
        return "Sets the velocity of entities in an area to 0 (includes spell projectiles and other entities not normally target-able). Applying AOE will increase the radius this effect is applied. Augmenting with Amplify will multiply the velocity by: -X/3. Augmenting with Dampen will multiply the velocity by: -1. Augmenting with Sensitive allows the caster to be affected by limitless.";
    }

   @NotNull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.MANIPULATION);
    }

}
