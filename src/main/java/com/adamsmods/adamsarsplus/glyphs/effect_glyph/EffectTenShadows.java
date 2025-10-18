package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.*;
import com.adamsmods.adamsarsplus.registry.AdamCapabilityRegistry;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLinger;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectWall;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.*;

public class EffectTenShadows extends AbstractEffect{
    public static EffectTenShadows INSTANCE = new EffectTenShadows(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effecttenshadows"), "Ten Shadows");

    public EffectTenShadows(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public void onResolve(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {

        if (canSummonTS(shooter)) {
            Vec3 vector3d = this.safelyGetHitPos(rayTraceResult);
            BlockPos pos = BlockPos.containing(vector3d);
            BlockPos blockpos = pos.offset(-2 + shooter.getRandom().nextInt(5), 2, -2 + shooter.getRandom().nextInt(5));

            switch(tenShadowsRank(shooter, spellStats)){
                case 4 -> {
                    // Mahoraga
                    MahoragaEntity tsentity = new MahoragaEntity(world, shooter, true);
                    tsentity.moveTo(blockpos, 0.0F, 0.0F);
                    tsentity.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                    tsentity.setOwner(shooter);
                    this.summonLivingEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver, tsentity);
                }
                case 3 -> {
                    // Round Deer
                    RDeerEntity tsentity = new RDeerEntity(world, shooter, true);
                    tsentity.moveTo(blockpos, 0.0F, 0.0F);
                    tsentity.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                    tsentity.setOwner(shooter);
                    this.summonLivingEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver, tsentity);
                }
                case 2 -> {
                    // Rabbit Escape
                    RabbitEEntity tsentity = new RabbitEEntity(world, shooter, true, "main", false);
                    tsentity.moveTo(blockpos, 0.0F, 0.0F);
                    tsentity.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                    tsentity.setOwner(shooter);
                    this.summonLivingEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver, tsentity);
                }
                case 1 -> {
                    // Nue
                    NueEntity tsentity = new NueEntity(world, shooter, true);
                    tsentity.moveTo(blockpos, 0.0F, 0.0F);
                    tsentity.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                    tsentity.setOwner(shooter);
                    this.summonLivingEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver, tsentity);
                }
                default -> {
                    // Divine Dogs
                    String[] ddColor = {"white","black"};

                    for(int i = 0; i < 2; ++i) {
                        DivineDogEntity tsentity = new DivineDogEntity(world, shooter, ddColor[i], true);
                        tsentity.moveTo(blockpos, 0.0F, 0.0F);
                        tsentity.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                        tsentity.setOwner(shooter);
                        this.summonLivingEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver, tsentity);
                    }
                }
            }

            shooter.addEffect(new MobEffectInstance((MobEffect) TENSHADOWS_EFFECT.get(), -1));
        } else if(shooter.hasEffect(TENSHADOWS_EFFECT.get())){
            shooter.removeEffect(TENSHADOWS_EFFECT.get());
            shooter.addEffect(new MobEffectInstance((MobEffect)ModPotions.SUMMONING_SICKNESS_EFFECT.get(), 200));
        }
    }

    public boolean canSummonTS(LivingEntity playerEntity) {
        boolean var10000;
        label25: {
            if (this.isRealPlayer(playerEntity)) {
                if (playerEntity.getEffect((MobEffect)ModPotions.SUMMONING_SICKNESS_EFFECT.get()) == null && playerEntity.getEffect((MobEffect)TENSHADOWS_EFFECT.get()) == null) {
                    break label25;
                }
            } else {
                break label25;
            }

            var10000 = false;
            return var10000;
        }

        var10000 = true;
        return var10000;
    }

    public int tenShadowsRank(LivingEntity entity, SpellStats spell){
        AtomicInteger Rank = new AtomicInteger();

        if(entity instanceof Player){
            Player player = (Player)entity;

            AdamCapabilityRegistry.getTsTier(player).ifPresent((pRank) -> {
                Rank.set(pRank.getTsTier());
            });

            if(spell.getAmpMultiplier() > Rank.get()){
                PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.tenshadows.rankinvalid"));
            }
        } else {
            Rank.set(0);
        }

        int RetRank = (int) Math.min(spell.getAmpMultiplier(), Rank.get());

        return RetRank;
    }

    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        this.addGenericInt(builder, 15, "Base duration in seconds", "duration");
        this.addExtendTimeConfig(builder, 10);
        PER_SPELL_LIMIT = builder.comment("The maximum number of times this glyph may appear in a single spell").defineInRange("per_spell_limit", 1, 1, 1);

    }

    public int getDefaultManaCost() {
        return 150;
    }

    public SpellTier defaultTier() {
        return SpellTier.ONE;
    }

    public @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return this.setOf(new AbstractAugment[]{
                AugmentAmplify.INSTANCE
        });
    }

    public String getBookDescription() {
        return "Summons forth various shikigami from the well's unknown abyss. Additional shikigami can be tamed through a ritual.";
    }

    public @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(new SpellSchool[]{SpellSchools.CONJURATION});
    }

    @Override
    protected void addDefaultInvalidCombos(Set<ResourceLocation> defaults) {
        defaults.add(EffectLinger.INSTANCE.getRegistryName());
        defaults.add(EffectWall.INSTANCE.getRegistryName());
        defaults.add(EffectDomain.INSTANCE.getRegistryName());
    }
}
