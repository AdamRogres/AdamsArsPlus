package com.adamsmods.adamsarsplus.glyphs.effect_glyph;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.DivineDogEntity;
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

public class EffectTenShadows extends AbstractEffect{
    public static EffectTenShadows INSTANCE = new EffectTenShadows(new ResourceLocation(AdamsArsPlus.MOD_ID, "glyph_effecttenshadows"), "Ten Shadows");

    public EffectTenShadows(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public void onResolve(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {

        if (canSummonM(shooter)) {
            int ticks = (int)((double)20.0F * ((double)(Integer)this.GENERIC_INT.get() + (double)(Integer)this.EXTEND_TIME.get() * spellStats.getDurationMultiplier()));

            Vec3 vector3d = this.safelyGetHitPos(rayTraceResult);
            BlockPos pos = BlockPos.containing(vector3d);
            BlockPos blockpos = pos.offset(-2 + shooter.getRandom().nextInt(5), 2, -2 + shooter.getRandom().nextInt(5));

            switch(tenShadowsRank(shooter)){
                case 4 -> {

                }
                case 3 -> {

                }
                case 2 -> {

                }
                case 1 -> {

                }
                default -> {
                    String[] ddColor = {"white","black"};

                    for(int i = 0; i < 2; ++i) {
                        DivineDogEntity tsentity = new DivineDogEntity(world, shooter, ddColor[i]);
                        tsentity.moveTo(blockpos, 0.0F, 0.0F);
                        tsentity.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
                        tsentity.setOwner(shooter);
                        tsentity.setLimitedLife(ticks);
                        this.summonLivingEntity(rayTraceResult, world, shooter, spellStats, spellContext, resolver, tsentity);
                    }
                }
            }

            shooter.addEffect(new MobEffectInstance((MobEffect) ModPotions.SUMMONING_SICKNESS_EFFECT.get(), ticks));
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

    public int tenShadowsRank(LivingEntity entity){
        int Rank = 0;

        if(entity instanceof Player){
            Player player = (Player)entity;

        } else {
            Rank = 0;
        }

        return Rank;
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
                AugmentExtendTime.INSTANCE,
                AugmentDurationDown.INSTANCE,
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
