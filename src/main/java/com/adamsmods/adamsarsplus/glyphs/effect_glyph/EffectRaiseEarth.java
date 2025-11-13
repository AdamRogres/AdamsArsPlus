package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.DamageUtil;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.entity.EnchantedFallingBlock;
import com.hollingsworth.arsnouveau.common.items.curios.ShapersFocus;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static net.minecraft.world.level.block.Blocks.AIR;

public class EffectRaiseEarth extends AbstractEffect implements IDamageEffect {
    public EffectRaiseEarth(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public static final EffectRaiseEarth INSTANCE = new EffectRaiseEarth(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectraiseearth"), "Raise Earth");

    final static int maxCheckUp = 4;

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        //raise actual earth
        BlockPos pos1 = rayTraceResult.getEntity().getOnPos();

        for(int i = 0; i < spellStats.getBuffCount(AugmentPierce.INSTANCE); i++){
            if(world.getBlockState(pos1.below(i + 1)).getBlock() != AIR){
                pos1 = new BlockPos(pos1.getX(), pos1.getY() - i, pos1.getZ());

                break;
            }
        }

        double aoeBuff = spellStats.getAoeMultiplier();

        BlockHitResult newHit = new BlockHitResult(rayTraceResult.getLocation(), Direction.DOWN, pos1, true);

        List<BlockPos> posList = SpellUtil.calcAOEBlocks(shooter, pos1, newHit, aoeBuff, 0);
        int highestReached = 0;
        Iterator var12 = posList.iterator();
        while (var12.hasNext()) {
            BlockPos pos = (BlockPos) var12.next();
            if (world.getBlockState(pos).isAir()) {
                //allow going downards 1 time for hills and stuff
                pos = pos.below();
                if (world.getBlockState(pos).isAir()) {
                    continue;
                }
            }
            else {
                for (int i = 0; i < maxCheckUp; i++) {
                    if (!world.getBlockState(pos.above()).isAir()) {
                        pos = pos.above();
                    }
                }
            }

            int maxHeight = 5 + 3 * spellStats.getBuffCount(AugmentPierce.INSTANCE);

            int actualHeight = 0;
            for (int i = 0; i < maxHeight; i++) {
                if (world.getBlockState(pos.above(actualHeight + 1)).isAir()) {
                    actualHeight += 1;
                } else {
                    break;
                }
            }

            //used for damage hitbox size
            if(actualHeight > highestReached){
                highestReached = actualHeight;
            }

            for (int i = 0; i < actualHeight; i++) {
                BlockPos targetPos = pos.below(i);
                BlockState targetState = world.getBlockState(targetPos);
                if (targetState.isAir()) {
                    // targetState = Blocks.STONE.defaultBlockState();
                    targetState = AIR.defaultBlockState();

                }
                //don't destroy bedrock
                if(targetState.getBlock().defaultDestroyTime() < 0 || targetState.getBlock().getPistonPushReaction(targetState) == PushReaction.BLOCK || world.getBlockEntity(targetPos) != null || targetState.getBlock().getExplosionResistance() > 40){
                    break;
                }
                world.setBlockAndUpdate(pos.above(actualHeight - i), targetState);
                world.setBlockAndUpdate(targetPos, AIR.defaultBlockState());

                if(spellStats.hasBuff(AugmentSensitive.INSTANCE)){
                    EnchantedFallingBlock fallingBlock = EnchantedFallingBlock.fall(world, pos.above(actualHeight - i), shooter, spellContext, resolver, spellStats);
                }
            }
        }

        //deal damage
        int x = pos1.getX();
        int y = pos1.getY();
        int z = pos1.getZ();
        Predicate<? super Entity> test = (entity) -> {
            return (entity instanceof LivingEntity) && entity.isAlive();
        };
        List<Entity> list = world.getEntities(shooter, new AABB(x - 3.0D, y - 3.0D, z - 3.0D, x + 3.0D, y + highestReached + 3.0D, z + 3.0D).inflate(aoeBuff), test);
        for(Entity entity : list){
            entity.setPos(entity.position().x,pos1.getY() + highestReached + 2, entity.position().z);
            if(shooter!=null) {
                double speed = 2 + (0.3f * spellStats.getAmpMultiplier()) + (0.5f * spellStats.getBuffCount(AugmentPierce.INSTANCE) + spellStats.getBuffCount(AugmentAccelerate.INSTANCE));
                entity.hurtMarked = true;
                entity.hasImpulse = true;

                knockback(entity, shooter, (float)speed);
            }

            float damage = 4 + 2*(float)spellStats.getAmpMultiplier();
            DamageSource damageT = DamageUtil.source(world, DamageTypes.FALL, shooter);

            this.attemptDamage(world,shooter,spellStats,spellContext,resolver, entity,  damageT, damage);
        }
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        //raise actual earth
        BlockPos pos1 = rayTraceResult.getBlockPos();
        double aoeBuff = spellStats.getAoeMultiplier();

        BlockHitResult newHit = new BlockHitResult(rayTraceResult.getLocation(), Direction.DOWN, rayTraceResult.getBlockPos(), rayTraceResult.isInside());

        List<BlockPos> posList = SpellUtil.calcAOEBlocks(shooter, pos1, newHit, aoeBuff, 0);
        int highestReached = 0;
        Iterator var12 = posList.iterator();
        while (var12.hasNext()) {
            BlockPos pos = (BlockPos) var12.next();
            if (world.getBlockState(pos).isAir()) {
                //allow going downards 1 time for hills and stuff
                pos = pos.below();
                if (world.getBlockState(pos).isAir()) {
                    continue;
                }
            }
            else {
                for (int i = 0; i < maxCheckUp; i++) {
                    if (!world.getBlockState(pos.above()).isAir()) {
                        pos = pos.above();
                    }
                }
            }

            int maxHeight = 5 + 3 * spellStats.getBuffCount(AugmentPierce.INSTANCE);

            int actualHeight = 0;
            for (int i = 0; i < maxHeight; i++) {
                if (world.getBlockState(pos.above(actualHeight + 1)).isAir()) {
                    actualHeight += 1;
                } else {
                    break;
                }
            }

            //used for damage hitbox size
            if(actualHeight > highestReached){
                highestReached = actualHeight;
            }

            for (int i = 0; i < actualHeight; i++) {
                BlockPos targetPos = pos.below(i);
                BlockState targetState = world.getBlockState(targetPos);
                if (targetState.isAir()) {
                   // targetState = Blocks.STONE.defaultBlockState();
                    targetState = AIR.defaultBlockState();

                }
                //don't destroy bedrock
                if(targetState.getBlock().defaultDestroyTime() < 0 || targetState.getBlock().getPistonPushReaction(targetState) == PushReaction.BLOCK || world.getBlockEntity(targetPos) != null || targetState.getBlock().getExplosionResistance() > 40){
                    break;
                }
                world.setBlockAndUpdate(pos.above(actualHeight - i), targetState);
                world.setBlockAndUpdate(targetPos, AIR.defaultBlockState());

                if(spellStats.hasBuff(AugmentSensitive.INSTANCE)){
                    EnchantedFallingBlock fallingBlock = EnchantedFallingBlock.fall(world, pos.above(actualHeight - i), shooter, spellContext, resolver, spellStats);
                }
            }
        }

        //deal damage
        int x = pos1.getX();
        int y = pos1.getY();
        int z = pos1.getZ();
        Predicate<? super Entity> test = (entity) -> {
            return (entity instanceof LivingEntity) && entity.isAlive();
        };
        List<Entity> list = world.getEntities(shooter, new AABB(x - 3.0D, y - 3.0D, z - 3.0D, x + 3.0D, y + highestReached + 3.0D, z + 3.0D).inflate(aoeBuff), test);
        for(Entity entity : list){
            entity.setPos(entity.position().x,pos1.getY() + highestReached + 2, entity.position().z);
            if(shooter!=null) {
                double speed = 2 + (0.3f * spellStats.getAmpMultiplier()) + (0.5f * spellStats.getBuffCount(AugmentPierce.INSTANCE) + spellStats.getBuffCount(AugmentAccelerate.INSTANCE));
                entity.hurtMarked = true;
                entity.hasImpulse = true;

                knockback(entity, shooter, (float)speed);
            }

            float damage = 4 + 2*(float)spellStats.getAmpMultiplier();
            DamageSource damageT = DamageUtil.source(world, DamageTypes.FALL, shooter);

            this.attemptDamage(world,shooter,spellStats,spellContext,resolver, entity,  damageT, damage);
        }

    }

    public void knockback(Entity target, LivingEntity shooter, float strength) {
        this.knockback(target, (double)strength, (double) Mth.sin(shooter.getYHeadRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(shooter.getYHeadRot() * ((float)Math.PI / 180F))));
    }

    public void knockback(Entity entity, double strength, double xRatio, double zRatio) {
        if (entity instanceof LivingEntity living) {
            strength *= 1.0;
        }

        if (strength > (double)0.0F) {
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

        strength *= 5.0;

        entity.hasImpulse = true;
        entity.setDeltaMovement(entity.getDeltaMovement().scale(0));
        if(strength <=  0) {
            entity.setDeltaMovement(strength * -0.5 * xRatio, strength * -0.5 * yRatio, strength * -0.5 * zRatio);
        }
        else{
            entity.setDeltaMovement(strength * -0.5 * absDifference(radius, xRatio), strength * -0.5 * absDifference(radius, yRatio), strength * -0.5 * absDifference(radius, zRatio));
        }
    }

    public double absDifference(int radius, double ratio){
        double value = 0;

        if(ratio >= 0){
            value = radius - ratio;
        }
        else{
            value = (radius + ratio) * -1;
        }
        return value;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDamageConfig(builder, 14);
        addAmpConfig(builder, 8);
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentAOE.INSTANCE,
                AugmentAmplify.INSTANCE,
                AugmentPierce.INSTANCE,
                AugmentAccelerate.INSTANCE,
                AugmentSensitive.INSTANCE
        );
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    public int getDefaultManaCost() {
        return 200;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.ELEMENTAL_EARTH);
    }
    @Override
    public void addDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(),4);
        defaults.put(AugmentAOE.INSTANCE.getRegistryName(),4);

    }
}
