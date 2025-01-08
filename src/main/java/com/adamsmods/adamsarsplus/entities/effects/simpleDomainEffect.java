package com.adamsmods.adamsarsplus.entities.effects;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
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

import static java.lang.Math.*;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class simpleDomainEffect extends MobEffect {

    public simpleDomainEffect() {
        super(MobEffectCategory.BENEFICIAL, 13565951);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level world;

        world = pLivingEntity.level();

        if (world instanceof ServerLevel level) {
            playRingParticles(pLivingEntity, level, 2);
        }
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    public void playRingParticles(LivingEntity living, ServerLevel level, double radius) {
        Vec3 livingEyes = living.getEyePosition();
        double x = livingEyes.x;
        double y = livingEyes.y;
        double z = livingEyes.z;

        double xOff = 0;
        double yOff = -1;
        double zOff = 0;

        double iOff = Math.random() * 180;

        for (int i = 0; i < 360; i = i + 180) {
            xOff = radius * cos(toRadians(i + iOff));
            zOff = radius * sin(toRadians(i + iOff));

            level.sendParticles(ParticleTypes.END_ROD, x + xOff, y + yOff, z + zOff, 1, 0, 0, 0, 0);

        }
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
