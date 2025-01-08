package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.hollingsworth.arsnouveau.api.util.BlockUtil.removeBlock;
import static org.openjdk.nashorn.internal.objects.NativeError.getStack;

public class EffectAnnihilate extends AbstractEffect implements IDamageEffect {
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

            level.sendParticles(ParticleTypes.ENCHANT, x, y, z, 1, 0, 0, 0.5, 0);

            //float damage = (float) ((this.DAMAGE.get() + ((this.AMP_VALUE.get())/1.5 * (spellStats.getAmpMultiplier()))));
            float damage = (float) (10 + (5 * (spellStats.getAmpMultiplier())));

            DamageSource magic = DamageUtil.source(level, DamageTypes.MAGIC, shooter);

            attemptDamage(world, shooter, spellStats, spellContext, resolver, entity, magic, damage);

            living.invulnerableTime = 0;
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
        BlockPos pos = rayTraceResult.getBlockPos();
        BlockState state;

        if (world instanceof ServerLevel level) {
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();

            level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, 0, 0, 0, 0.1);
            level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, 0, 0, 0, 0.1);
            level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, -0.5, -0.5, 0, 0.1);
            level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, 0.5, -0.5, 0, 0.1);
            level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, 0.5, 0.5, 0, 0.1);
            level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 1, -0.5, 0.5, 0, 0.1);

            level.sendParticles(ParticleTypes.ENCHANT, x, y, z, 1, 0, 0, 0.5, 0);
        }


        MobEffectInstance miningFatigue = shooter.getEffect(MobEffects.DIG_SLOWDOWN);
        if (miningFatigue != null)
            spellStats.setAmpMultiplier(spellStats.getAmpMultiplier() - miningFatigue.getAmplifier());

        double aoeBuff = spellStats.getAoeMultiplier();
        int pierceBuff = spellStats.getBuffCount(AugmentPierce.INSTANCE);

        List<BlockPos> posList = SpellUtil.calcAOEBlocks(shooter, pos, rayTraceResult, aoeBuff, pierceBuff);
        ItemStack stack = spellStats.isSensitive() ? new ItemStack(Items.SHEARS) : getStack(shooter, rayTraceResult);

        for (BlockPos pos1 : posList) {
            this.evaporate(world, pos1, rayTraceResult, shooter, spellContext, resolver);

            if (world.isOutsideBuildHeight(pos1) || world.random.nextFloat() < spellStats.getBuffCount(AugmentRandomize.INSTANCE) * 0.25F) {
                continue;
            }
            state = world.getBlockState(pos1);

            if (!canBlockBeHarvested(spellStats, world, pos1) || !BlockUtil.destroyRespectsClaim(getPlayer(shooter, (ServerLevel) world), world, pos1) ) {
                continue; //|| state.is(BlockTagProvider.BREAK_BLACKLIST)
            }

            if (!breakVoidBlock((ServerLevel) world, pos1, stack, shooter.getUUID(), true)) {
                continue;
            }
        }
    }

    public static boolean breakVoidBlock(ServerLevel world, BlockPos pos, ItemStack mainhand, @Nullable UUID source, boolean bypassToolCheck) {
        BlockState blockstate = world.getBlockState(pos);
        FakePlayer player;
        if (source != null) {
            player = FakePlayerFactory.get(world, new GameProfile(source, UsernameCache.getLastKnownUsername(source)));
            Player realPlayer = world.getPlayerByUUID(source);
            if (realPlayer != null) {
                player.setPos(realPlayer.position());
            }
        } else {
            player = FakePlayerFactory.getMinecraft(world);
        }

        player.getInventory().items.set(player.getInventory().selected, mainhand);
        if (!(blockstate.getDestroySpeed(world, pos) < 0.0F) && (blockstate.canHarvestBlock(world, pos, player) || bypassToolCheck)) {
            GameType type = player.getAbilities().instabuild ? GameType.CREATIVE : GameType.SURVIVAL;
            int exp = ForgeHooks.onBlockBreakEvent(world, type, player, pos);
            if (exp == -1) {
                return false;
            } else {
                BlockEntity tileentity = world.getBlockEntity(pos);
                Block block = blockstate.getBlock();
                if ((block instanceof CommandBlock || block instanceof StructureBlock || block instanceof JigsawBlock) && !player.canUseGameMasterBlocks()) {
                    world.sendBlockUpdated(pos, blockstate, blockstate, 3);
                    return false;
                } else if (player.getMainHandItem().onBlockStartBreak(pos, player)) {
                    return false;
                } else if (player.blockActionRestricted(world, pos, type)) {
                    return false;
                } else if (player.getAbilities().instabuild) {
                    removeBlock(world, player, pos, false);
                    return true;
                } else {
                    ItemStack copyMain = mainhand.copy();
                    boolean canHarvest = blockstate.canHarvestBlock(world, pos, player) || bypassToolCheck;
                    mainhand.mineBlock(world, blockstate, pos, player);
                    if (mainhand.isEmpty() && !copyMain.isEmpty()) {
                        ForgeEventFactory.onPlayerDestroyItem(player, copyMain, InteractionHand.MAIN_HAND);
                    }

                    boolean removed = removeBlock(world, player, pos, canHarvest);
                    if (removed && canHarvest) {
                       // block.playerDestroy(world, player, pos, blockstate, tileentity, copyMain);
                    }

                    if (removed && exp > 0) {
                      //  blockstate.getBlock().popExperience(world, pos, exp);
                    }

                    return true;
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
