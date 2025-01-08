package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.SummonSkeleton_m;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.entity.SummonSkeleton;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectSummonUndead;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Set;

public class EffectSummonUndead_boss extends AbstractEffect {
    public static EffectSummonUndead_boss INSTANCE = new EffectSummonUndead_boss(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectsummonundead_m"), "Summon Undead");

    public EffectSummonUndead_boss(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public void onResolve(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (true) {
            Vec3 vector3d = this.safelyGetHitPos(rayTraceResult);
            int ticks = (int)((double)20.0F * ((double)(Integer)this.GENERIC_INT.get() + (double)(Integer)this.EXTEND_TIME.get() * spellStats.getDurationMultiplier()));
            BlockPos pos = BlockPos.containing(vector3d);
            if (ticks > 0) {
                int count = 3 + spellStats.getBuffCount(AugmentSplit.INSTANCE);

                for(int i = 0; i < count; ++i) {
                    BlockPos blockpos = pos.offset(-2 + shooter.getRandom().nextInt(5), 2, -2 + shooter.getRandom().nextInt(5));
                    ItemStack weapon = Items.IRON_SWORD.getDefaultInstance();
                    if (spellStats.hasBuff(AugmentPierce.INSTANCE)) {
                        weapon = Items.BOW.getDefaultInstance();
                        if (spellStats.getAmpMultiplier() > (double)0.0F) {
                            weapon.enchant(Enchantments.POWER_ARROWS, Math.max(4, (int)spellStats.getAmpMultiplier()) - 1);
                        }
                    } else if (spellStats.getAmpMultiplier() >= (double)3.0F) {
                        weapon = Items.NETHERITE_AXE.getDefaultInstance();
                    } else if (spellStats.getAmpMultiplier() > (double)2.0F) {
                        weapon = Items.NETHERITE_SWORD.getDefaultInstance();
                    } else if (spellStats.getAmpMultiplier() > (double)1.0F) {
                        weapon = Items.DIAMOND_SWORD.getDefaultInstance();
                    }

                    SummonSkeleton_m undeadentity = new SummonSkeleton_m(world, shooter, weapon);
                    undeadentity.moveTo(blockpos, 0.0F, 0.0F);
                    undeadentity.finalizeSpawn((ServerLevelAccessor)world, world.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
                    undeadentity.setOwner(shooter);
                    undeadentity.setLimitedLife(ticks);
                    this.summonLivingEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver, undeadentity);
                }

                //shooter.addEffect(new MobEffectInstance((MobEffect) ModPotions.SUMMONING_SICKNESS_EFFECT.get(), ticks));
            }
        }
    }

    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        this.addGenericInt(builder, 15, "Base duration in seconds", "duration");
        this.addExtendTimeConfig(builder, 10);
    }

    public int getDefaultManaCost() {
        return 150;
    }

    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    public @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return this.setOf(new AbstractAugment[]{AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE, AugmentAmplify.INSTANCE, AugmentSplit.INSTANCE, AugmentPierce.INSTANCE});
    }

    public String getBookDescription() {
        return "Summons a number of Skeleton allies that will attack nearby hostile enemies. These Skeletons will last a short time until they begin to take damage, but time may be extended with the Extend Time augment.  Additionally, their summoned weapons are changed using augments, use Amplify to give it a better sword, or Pierce to give it a bow.  Adding Split after the effect will add to the number of summoned skeletons.";
    }

    public @NotNull Set<SpellSchool> getSchools() {
        return this.setOf(new SpellSchool[]{SpellSchools.CONJURATION});
    }
}
