package com.adamsmods.adamsarsplus.entities.effects;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.CLOUD_STEPS_EFFECT;
import static com.hollingsworth.arsnouveau.setup.registry.ModPotions.FLIGHT_EFFECT;


@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class CloudStepsEffect extends MobEffect {

    public CloudStepsEffect() {
        super(MobEffectCategory.HARMFUL, 2039587);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity.hasEffect(CLOUD_STEPS_EFFECT.get()) && pLivingEntity.hasEffect(FLIGHT_EFFECT.get())){
            if(pLivingEntity.getEffect(CLOUD_STEPS_EFFECT.get()).getDuration() < 10){
                pLivingEntity.removeEffect(FLIGHT_EFFECT.get());
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
