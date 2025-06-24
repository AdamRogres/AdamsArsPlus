package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.SummonSkeleton_m;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.entity.SummonSkeleton;
import com.hollingsworth.arsnouveau.common.items.EnchantersSword;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLinger;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectSummonUndead;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectWall;
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

import static com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry.ENCHANTERS_SWORD;

public class EffectSummonUndead_boss extends AbstractEffect {
    public static EffectSummonUndead_boss INSTANCE = new EffectSummonUndead_boss(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effectsummonundead_m"), "Summon Undead");

    public EffectSummonUndead_boss(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public void onResolve(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {

        spellContext.setCanceled(true);
        if (spellContext.getCurrentIndex() >= spellContext.getSpell().recipe.size())
            return;
        Spell newSpell = spellContext.getRemainingSpell();
        SpellContext newContext = spellContext.clone().withSpell(newSpell);


        if (canSummonM(shooter)) {
            Vec3 vector3d = this.safelyGetHitPos(rayTraceResult);
            int ticks = (int)((double)20.0F * ((double)(Integer)this.GENERIC_INT.get() + (double)(Integer)this.EXTEND_TIME.get() * spellStats.getDurationMultiplier()));
            BlockPos pos = BlockPos.containing(vector3d);
            if (ticks > 0) {
                int count = 2 + spellStats.getBuffCount(AugmentSplit.INSTANCE);

                for(int i = 0; i < count; ++i) {
                    BlockPos blockpos = pos.offset(-2 + shooter.getRandom().nextInt(5), 2, -2 + shooter.getRandom().nextInt(5));
                    ItemStack weapon = ENCHANTERS_SWORD.asItem().getDefaultInstance();

                    if (spellStats.getAmpMultiplier() > (double)0.0F) {
                        weapon.enchant(Enchantments.SHARPNESS, Math.max(4, (int)spellStats.getAmpMultiplier()) - 1);
                    }

                    SummonSkeleton_m undeadentity = new SummonSkeleton_m(world, shooter, weapon, newContext);
                    undeadentity.moveTo(blockpos, 0.0F, 0.0F);
                    undeadentity.finalizeSpawn((ServerLevelAccessor)world, world.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
                    undeadentity.setOwner(shooter);
                    undeadentity.setLimitedLife(ticks);
                    this.summonLivingEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver, undeadentity);
                }

                shooter.addEffect(new MobEffectInstance((MobEffect) ModPotions.SUMMONING_SICKNESS_EFFECT.get(), ticks));
            }
        }
    }

    public boolean canSummonM(LivingEntity playerEntity) {
        boolean var10000;
        label25: {
            if (this.isRealPlayer(playerEntity)) {
                if (playerEntity.getEffect((MobEffect)ModPotions.SUMMONING_SICKNESS_EFFECT.get()) == null) {
                    break label25;
                }

                if (playerEntity instanceof Player) {
                    Player player = (Player)playerEntity;
                    if (player.isCreative()) {
                        break label25;
                    }
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
        return SpellTier.THREE;
    }

    public @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return this.setOf(new AbstractAugment[]{
                AugmentExtendTime.INSTANCE,
                AugmentDurationDown.INSTANCE,
                AugmentAmplify.INSTANCE,
                AugmentSplit.INSTANCE
        });
    }

    public String getBookDescription() {
        return "Summons a number of Skeleton allies that will attack nearby hostile enemies. These Skeletons will last a short time until they begin to take damage, but time may be extended with the Extend Time augment.  Additionally, their summoned weapons are changed using augments, use Amplify to give it a better sword, or Pierce to give it a bow.  Adding Split after the effect will add to the number of summoned skeletons.";
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
