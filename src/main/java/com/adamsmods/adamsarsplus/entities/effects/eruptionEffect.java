package com.adamsmods.adamsarsplus.entities.effects;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.ERUPTION_EFFECT;
import static java.lang.Math.*;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class eruptionEffect extends MobEffect {

    public eruptionEffect() {
        super(MobEffectCategory.HARMFUL, 15931670);
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

        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0, 0, 0, 0.3);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0, 0, 0, 0.3);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, -0.5, -0.5, 0, 0.3);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0.5, -0.5, 0, 0.3);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0.5, 0.5, 0, 0.3);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, -0.5, 0.5, 0, 0.3);

    }

    @SubscribeEvent
    public static void entityHurt(LivingHurtEvent e) {
        if (e.getSource().is(DamageTypes.ON_FIRE) && e.getEntity().hasEffect((MobEffect)ERUPTION_EFFECT.get())) {
            e.setAmount(e.getAmount() * 3.0F);
        }
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
