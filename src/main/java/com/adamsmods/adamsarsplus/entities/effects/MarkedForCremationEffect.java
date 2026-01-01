package com.adamsmods.adamsarsplus.entities.effects;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class MarkedForCremationEffect extends MobEffect {

    public MarkedForCremationEffect() {
        super(MobEffectCategory.HARMFUL, 16724530);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level world;

        world = pLivingEntity.level();

        if (world instanceof ServerLevel level && pLivingEntity.tickCount % 20 == 0) {
            playRingParticles(pLivingEntity, level);
        }
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    public void playRingParticles(LivingEntity living, ServerLevel level) {
        Vec3 livingEyes = living.getEyePosition();
        double x = livingEyes.x;
        double y = livingEyes.y;
        double z = livingEyes.z;

        double offset = living.getBbWidth() / 2;

        level.sendParticles(ParticleTypes.FLAME,x + offset, y + 1.5, z, 1,  0, 0, 0, 0);
        level.sendParticles(ParticleTypes.FLAME,x - offset, y + 1.5, z, 1, 0, 0, 0, 0);
        level.sendParticles(ParticleTypes.FLAME, x, y + offset + 1.5, z, 1, 0,  0, 0, 0);
        level.sendParticles(ParticleTypes.FLAME, x, y - offset + 1.5, z, 1, 0, 0, 0, 0);
        level.sendParticles(ParticleTypes.FLAME, x, y, z + offset + 1.5, 1, 0, 0,  0, 0);
        level.sendParticles(ParticleTypes.FLAME, x, y, z - offset + 1.5, 1, 0, 0, 0, 0);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}