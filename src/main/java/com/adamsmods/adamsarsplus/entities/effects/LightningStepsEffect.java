package com.adamsmods.adamsarsplus.entities.effects;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.*;
import static net.minecraft.world.effect.MobEffects.HEALTH_BOOST;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class LightningStepsEffect extends MobEffect {

    public LightningStepsEffect() {
        super(MobEffectCategory.BENEFICIAL, 16776960);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity.hasEffect(LEAP_FATIGUE_EFFECT.get()) && pLivingEntity.hasEffect(LIGHTNING_STEPS_EFFECT.get())){
            int amp = pLivingEntity.getEffect(LIGHTNING_STEPS_EFFECT.get()).getAmplifier();
            pLivingEntity.removeEffect(LIGHTNING_STEPS_EFFECT.get());
            pLivingEntity.removeEffect(LEAP_FATIGUE_EFFECT.get());
            if(amp != 0){
                pLivingEntity.addEffect(new MobEffectInstance(LIGHTNING_STEPS_EFFECT.get(), 180,amp - 1, false, false));
            }
        }
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
