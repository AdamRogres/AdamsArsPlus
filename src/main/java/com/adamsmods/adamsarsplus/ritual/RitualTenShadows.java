package com.adamsmods.adamsarsplus.ritual;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.entities.custom.*;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.EffectTenShadows;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleLineData;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.entity.WildenChimera;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.MANA_EXHAUST_EFFECT;
import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.SIMPLE_DOMAIN_EFFECT;
import static com.adamsmods.adamsarsplus.datagen.AdamsItemTagsProvider.TS_RITUAL;
import static com.adamsmods.adamsarsplus.registry.ModRegistry.*;

public class RitualTenShadows extends AbstractRitual {
    public static final String ID = "ritual_ten_shadows";

    public RitualTenShadows() {
    }

    protected void tick() {
        spawnRitualParticles(this.getPos().above(), this.getWorld(), 1);
        if (this.getWorld().getGameTime() % 20L == 0L) {
            this.incrementProgress();
        }

        int Rank = 0;

        if (this.getWorld().getGameTime() % 20L == 0L && !this.getWorld().isClientSide) {
            for (ItemStack i : getConsumedItems()) {
                if (i.is(MANA_DIAMOND.get())) {
                    Rank += i.getCount();
                }
            }
            if(this.getProgress() >= 4){
                switch(Math.min(Rank, 4)){
                    // Give Glyph (Divine Dogs are free)
                    default -> {
                        this.getWorld().addFreshEntity(new ItemEntity(this.getWorld(), this.getPos().getX(), this.getPos().getY() + 0.5, this.getPos().getZ(), new ItemStack(EffectTenShadows.INSTANCE.getGlyph().asItem()) ));

                        this.setFinished();
                    }
                    // Summon Nue
                    case 1 -> {
                        NueEntity boss = new NueEntity(this.getWorld(), false);
                        this.summon(boss, this.getPos().above(7));

                        for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                            if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                            }
                        }
                        this.setFinished();
                    }
                    // Summon Rabbit Escape
                    case 2 -> {
                        RabbitEEntity boss = new RabbitEEntity(this.getWorld(), false, "main");
                        this.summon(boss, this.getPos().above());

                        for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                            if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                            }
                        }
                        this.setFinished();
                    }
                    // Summon Round Deer
                    case 3 -> {
                        RDeerEntity boss = new RDeerEntity(this.getWorld(), false);
                        this.summon(boss, this.getPos().above());

                        for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(5).north(5).above(), this.getPos().west(5).south(5).above(5))) {
                            if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                            }
                        }
                        this.setFinished();
                    }
                    // Summon Mahoraga
                    case 4 -> {
                        MahoragaEntity boss = new MahoragaEntity(this.getWorld(), false);

                        if (this.getProgress() <= 10) {
                            for (Entity entity : this.getWorld().getEntities(null, new AABB(this.getPos()).inflate(20,20,20))) {
                                if (entity instanceof LivingEntity){
                                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0, false, false));

                                }
                            }
                        }

                        if (this.getProgress() == 6) {
                            this.getWorld().playSound(null, this.getPos(), SoundEvents.WOLF_HOWL, SoundSource.HOSTILE);
                        } else if (this.getProgress() == 7) {
                            this.getWorld().playSound(null, this.getPos(), SoundEvents.WOLF_HOWL, SoundSource.HOSTILE);
                        } else if (this.getProgress() == 8) {
                            this.getWorld().playSound(null, this.getPos(), SoundEvents.WOLF_HOWL, SoundSource.HOSTILE);
                        } else if (this.getProgress() == 9) {
                            this.getWorld().playSound(null, this.getPos(), SoundEvents.WOLF_HOWL, SoundSource.HOSTILE);
                        } else if (this.getProgress() >= 13) {
                            this.summon(boss, this.getPos().north(3));
                            boss.playSound(SoundEvents.IRON_DOOR_OPEN, 1.5F, 1F);

                            for (BlockPos b : BlockPos.betweenClosed(this.getPos().east(8).north(11).above(), this.getPos().west(8).south(5).above(8))) {
                                if (ForgeEventFactory.getMobGriefingEvent(this.getWorld(), boss) && SpellUtil.isCorrectHarvestLevel(4, this.getWorld().getBlockState(b))) {
                                    BlockUtil.destroyBlockSafelyWithoutSound(this.getWorld(), b, true);
                                }
                            }
                            this.setFinished();
                        }
                    }
                }
            }
        }
    }

    public void summon(Mob mob, BlockPos pos) {
        mob.setPos((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        mob.level().addFreshEntity(mob);
    }

    public String getLangName() {
        return "Challenge Shikigami";
    }

    public String getLangDescription() {
        return "Grants the Ten Shadows Glyph. When augmented with mana diamonds more powerful shikigami can be summoned. If the challenger defeats the shikigami alone they gain the right to summon them through the Ten Shadows Glyph.";
    }

    public boolean canConsumeItem(ItemStack stack) {
        return stack.is(TS_RITUAL);
    }

    public ResourceLocation getRegistryName() {
        return AdamsArsPlus.prefix(ID);
    }

    public static void spawnRitualParticles(BlockPos pos, Level level, int multiplier) {
        if (level.isClientSide) {
            int baseAge = 40;
            float scaleAge = (float) ParticleUtil.inRange(0.1, 0.2);

            for(int i = 0; i < 10 * Math.min(1, multiplier); ++i) {
                Vec3 particlePos = (new Vec3((double)pos.getX(), (double)(pos.getY() + 1), (double)pos.getZ())).add((double)0.5F, (double)0.5F, (double)0.5F);
                particlePos = particlePos.add(ParticleUtil.pointInSphere().multiply((double)3.0F, (double)3.0F, (double)3.0F));
                level.addParticle(ParticleTypes.SQUID_INK, particlePos.x(), particlePos.y(), particlePos.z(), (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F);
            }

        }
    }
}

