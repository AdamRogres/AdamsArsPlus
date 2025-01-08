package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.EntityDivineSmite;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.DamageUtil;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.datagen.BlockTagProvider;
import com.hollingsworth.arsnouveau.common.entity.LightningEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.setup.registry.ModEntities;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.adamsmods.adamsarsplus.entities.AdamsModEntities.DIVINE_SMITE;
import static com.hollingsworth.arsnouveau.api.util.BlockUtil.removeBlock;

public class EffectDivineSmite extends AbstractEffect implements IDamageEffect {
    public EffectDivineSmite(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public static final EffectDivineSmite INSTANCE = new EffectDivineSmite(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectdivinesmite"), "Divine Smite");

    @Override
    public void onResolve(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Vec3 pos = this.safelyGetHitPos(rayTraceResult);
        EntityDivineSmite lightningBoltEntity = new EntityDivineSmite(DIVINE_SMITE.get(), world);
        lightningBoltEntity.setPos(pos.x(), pos.y(), pos.z());
        lightningBoltEntity.setCause(shooter instanceof ServerPlayer ? (ServerPlayer)shooter : null);
        lightningBoltEntity.setAoe((float)spellStats.getAoeMultiplier());
        lightningBoltEntity.setSensitive(spellStats.hasBuff(AugmentSensitive.INSTANCE));
        lightningBoltEntity.setDamage(this.DAMAGE.get().floatValue() + (float)((this.AMP_VALUE.get()) * spellStats.getAmpMultiplier()));
        lightningBoltEntity.amps = (float)spellStats.getAmpMultiplier();
        lightningBoltEntity.extendTimes = (int)spellStats.getDurationMultiplier();
        lightningBoltEntity.ampScalar = ((Double)this.AMP_VALUE.get()).floatValue();

        world.addFreshEntity(lightningBoltEntity);
    }


    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        this.addDamageConfig(builder, 9.0);
        this.addAmpConfig(builder, 4.0);
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentSensitive.INSTANCE,
                AugmentAOE.INSTANCE,
                AugmentAmplify.INSTANCE
        );
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public int getDefaultManaCost() {
        return 700;
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.ELEMENTAL_AIR);
    }
    @Override
    public void addDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(),4);
        defaults.put(AugmentAOE.INSTANCE.getRegistryName(),4);

    }
}
