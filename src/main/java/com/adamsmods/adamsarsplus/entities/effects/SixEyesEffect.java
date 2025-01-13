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

import static java.lang.Math.*;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class SixEyesEffect extends MobEffect {

    public SixEyesEffect() {
        super(MobEffectCategory.BENEFICIAL, 13565951);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }


    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
