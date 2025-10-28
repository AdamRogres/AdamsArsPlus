package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.lib.AdamsEntityTags;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.DamageUtil;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.datagen.BlockTagProvider;
import com.hollingsworth.arsnouveau.common.items.curios.ShapersFocus;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.setup.registry.DamageTypesRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.ForgeEventFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static com.hollingsworth.arsnouveau.api.util.BlockUtil.removeBlock;
import static org.openjdk.nashorn.internal.objects.NativeError.getStack;

public class EffectAnnihilate extends AbstractEffect implements IDamageEffect {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<SpellContext, Set<UUID>> DAMAGED_IN_CONTEXT = Collections.synchronizedMap(new WeakHashMap());

    public EffectAnnihilate(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public static final EffectAnnihilate INSTANCE = new EffectAnnihilate(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectannihilate"), "Annihilate");

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nonnull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Entity entity = rayTraceResult.getEntity();
        if (entity instanceof LivingEntity living && world instanceof ServerLevel level) {

            Vec3 livingEyes = living.getEyePosition();
            double x = livingEyes.x;
            double y = livingEyes.y;
            double z = livingEyes.z;

            level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, 0, 0, 0, 1);
            level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, 0, 0, 0, 1);
            level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, -0.5, -0.5, 0, 1);
            level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, 0.5, -0.5, 0, 1);
            level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, 0.5, 0.5, 0, 1);
            level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, -0.5, 0.5, 0, 1);

            float damage = (float) (10 + (2 * (spellStats.getAmpMultiplier())));

            DamageSource dVoid = DamageUtil.source(level, DamageTypes.FELL_OUT_OF_WORLD, shooter);

            if(entity instanceof  Player player){
                if(!player.isCreative() && !player.isSpectator()){
                    attemptDamage(world, shooter, spellStats, spellContext, resolver, entity, dVoid, damage);
                }
            } else {
                attemptDamage(world, shooter, spellStats, spellContext, resolver, entity, dVoid, damage);
            }

        }
    }

    public ItemStack getStack(LivingEntity shooter, BlockHitResult blockHitResult) {
        ItemStack stack = shooter.getMainHandItem().copy();
        boolean usePick = shooter.level().getBlockState(blockHitResult.getBlockPos()).is(BlockTagProvider.BREAK_WITH_PICKAXE);
        if (usePick) {
            return new ItemStack(Items.DIAMOND_PICKAXE);
        }
        return stack.isEmpty() ? new ItemStack(Items.DIAMOND_PICKAXE) : stack;
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (world instanceof ServerLevel level) {
            BlockPos pos = rayTraceResult.getBlockPos();
            MobEffectInstance miningFatigue = shooter.getEffect(MobEffects.DIG_SLOWDOWN);
            if (miningFatigue != null) {
                spellStats.setAmpMultiplier(spellStats.getAmpMultiplier() - (double)miningFatigue.getAmplifier());
            }

            double aoeBuff = spellStats.getAoeMultiplier();
            int pierceBuff = spellStats.getBuffCount(AugmentPierce.INSTANCE);
            List<BlockPos> posList = SpellUtil.calcAOEBlocks(shooter, pos, rayTraceResult, aoeBuff, pierceBuff);
            ItemStack stack = spellStats.isSensitive() ? new ItemStack(Items.SHEARS) : this.getStack(shooter, rayTraceResult);
            int particleThresholdRadius = 10;
            boolean reduceParticles = Math.max(1, (int)((double)1.0F + aoeBuff)) > particleThresholdRadius;
            double px = (double)pos.getX();
            double py = (double)pos.getY();
            double pz = (double)pos.getZ();
            int particleCount = reduceParticles ? 1 : 6;

            for(int i = 0; i < particleCount; ++i) {
                level.sendParticles(ParticleTypes.DRAGON_BREATH, px, py, pz, 1, (double)0.0F, (double)0.0F, (double)0.0F, 0.1);
            }

            level.sendParticles(ParticleTypes.ENCHANT, px, py, pz, 1, (double)0.0F, (double)0.0F, (double)0.5F, (double)0.0F);
            List<BlockPos> toProcess = new ArrayList();

            for(BlockPos p : posList) {
                if (!world.isOutsideBuildHeight(p) && !(world.random.nextFloat() < (float)spellStats.getBuffCount(AugmentRandomize.INSTANCE) * 0.25F)) {
                    BlockState state = world.getBlockState(p);
                    if (state != null && !state.isAir() && this.canBlockBeHarvested(spellStats, world, p) && BlockUtil.destroyRespectsClaim(this.getPlayer(shooter, level), world, p)) {
                        toProcess.add(p);
                    }
                }
            }

            if (!toProcess.isEmpty()) {
                int radius = (int)((double)1.0F + spellStats.getAoeMultiplier());
                Predicate<Double> Sphere = (distance) -> distance <= (double)radius + (double)0.5F;
                Set<UUID> damagedSet = (Set)DAMAGED_IN_CONTEXT.computeIfAbsent(spellContext, (sc) -> Collections.synchronizedSet(new HashSet()));
                float damage = (float)((double)10.0F + (double)5.0F * spellStats.getAmpMultiplier());
                DamageSource magic = DamageUtil.source(world, DamageTypes.MAGIC, shooter);

                for(LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, (new AABB(pos)).inflate((double)radius, (double)radius, (double)radius))) {
                    if (target != null && Sphere.test(BlockUtil.distanceFromCenter(target.blockPosition(), pos)) && (spellStats.hasBuff(AugmentSensitive.INSTANCE) || !spellContext.getUnwrappedCaster().equals(target)) && !target.getType().is(AdamsEntityTags.DOMAIN_BLACKLIST) && !damagedSet.contains(target.getUUID())) {
                        try {
                            this.attemptDamage(world, shooter, spellStats, spellContext, resolver, target, magic, damage);
                        } catch (Throwable var37) {
                            Throwable t = var37;

                            try {
                                LOGGER.warn("Error applying annihilate damage to entity: {}", t.getMessage());
                            } catch (Throwable var36) {
                            }
                        }

                        damagedSet.add(target.getUUID());
                    }
                }

                int total = toProcess.size();
                int batchSize;
                if (radius <= 30 && total <= 5000) {
                    if (radius <= 15 && total <= 1000) {
                        if (radius <= 10 && total <= 300) {
                            batchSize = Math.max(1, total);
                        } else {
                            batchSize = 100;
                        }
                    } else {
                        batchSize = 50;
                    }
                } else {
                    batchSize = 25;
                }

                if (total <= batchSize) {
                    FakePlayer fake = FakePlayerFactory.getMinecraft(level);
                    fake.setPos(shooter.position());

                    for(BlockPos p : toProcess) {
                        breakVoidBlockWithPlayer(level, p, stack, fake, true);
                    }
                } else {
                    List<BlockPos> workList = new ArrayList(toProcess);
                    AtomicInteger index = new AtomicInteger(0);
                    FakePlayer fake = FakePlayerFactory.getMinecraft(level);
                    fake.setPos(shooter.position());
                    int maxTicks = (int)Math.ceil((double)workList.size() / (double)batchSize) + 5;
                    AdamsArsPlus.setInterval(() -> {
                        int start = index.getAndAdd(batchSize);
                        if (start < workList.size()) {
                            int end = Math.min(workList.size(), start + batchSize);

                            for(int i = start; i < end; ++i) {
                                BlockPos p = (BlockPos)workList.get(i);
                                if (!level.isOutsideBuildHeight(p)) {
                                    BlockState st = level.getBlockState(p);
                                    if (st != null && !st.isAir()) {
                                        breakVoidBlockWithPlayer(level, p, stack, fake, true);
                                    }
                                }
                            }

                        }
                    }, 1, maxTicks, () -> index.get() >= workList.size());
                }

            }
        }
    }

    private static boolean breakVoidBlockWithPlayer(ServerLevel world, BlockPos pos, ItemStack mainhand, FakePlayer player, boolean bypassToolCheck) {
        BlockState blockstate = world.getBlockState(pos);
        if (blockstate != null && !blockstate.isAir()) {
            player.getInventory().items.set(player.getInventory().selected, mainhand);
            player.setPos((double)pos.getX() + (double)0.5F, (double)pos.getY(), (double)pos.getZ() + (double)0.5F);
            if (blockstate.getDestroySpeed(world, pos) < 0.0F) {
                return false;
            } else {
                boolean canHarvest = blockstate.canHarvestBlock(world, pos, player) || bypassToolCheck;

                try {
                    Block block = blockstate.getBlock();
                    if ((block instanceof CommandBlock || block instanceof StructureBlock || block instanceof JigsawBlock) && !player.canUseGameMasterBlocks()) {
                        world.sendBlockUpdated(pos, blockstate, blockstate, 3);
                        return false;
                    } else if (player.getMainHandItem().onBlockStartBreak(pos, player)) {
                        return false;
                    } else if (player.blockActionRestricted(world, pos, player.gameMode.getGameModeForPlayer())) {
                        return false;
                    } else if (player.getAbilities().instabuild) {
                        BlockUtil.removeBlock(world, player, pos, false);
                        return true;
                    } else {
                        ItemStack copyMain = mainhand.copy();
                        if (canHarvest) {
                            mainhand.mineBlock(world, blockstate, pos, player);
                        }

                        boolean removed = BlockUtil.removeBlock(world, player, pos, false);
                        return removed;
                    }
                } catch (Throwable var11) {
                    Throwable t = var11;

                    try {
                        LOGGER.warn("Error breaking block in EffectAnnihilate: " + t.getMessage(), t);
                    } catch (Throwable var10) {
                    }

                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public void evaporate(Level world, BlockPos p, BlockHitResult rayTraceResult, LivingEntity shooter, SpellContext context, SpellResolver resolver) {
        BlockState state = world.getBlockState(p);
        if (!world.getFluidState(p).isEmpty() && state.getBlock() instanceof LiquidBlock) {
            world.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
        } else if (state.getBlock() instanceof SimpleWaterloggedBlock && state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            world.setBlock(p, (BlockState)state.setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE), 3);
        }

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
        return augmentSetOf(AugmentAmplify.INSTANCE);
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public int getDefaultManaCost() {
        return 500;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.ELEMENTAL);
    }
    @Override
    public void addDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(),4);
        defaults.put(AugmentAOE.INSTANCE.getRegistryName(),4);

    }
}
