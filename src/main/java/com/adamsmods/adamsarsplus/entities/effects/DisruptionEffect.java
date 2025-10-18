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

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class DisruptionEffect extends MobEffect {

    public DisruptionEffect() {
        super(MobEffectCategory.HARMFUL, 8991416);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        for(int i = 0; i < pLivingEntity.getActiveEffects().size(); i++){
            if(pLivingEntity.getActiveEffects().stream().toList().get(i).getEffect().isBeneficial()){
                pLivingEntity.removeEffect(pLivingEntity.getActiveEffects().stream().toList().get(i).getEffect());
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
