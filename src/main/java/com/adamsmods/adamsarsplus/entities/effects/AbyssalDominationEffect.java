package com.adamsmods.adamsarsplus.entities.effects;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class AbyssalDominationEffect extends MobEffect {

    public AbyssalDominationEffect() {
        super(MobEffectCategory.BENEFICIAL, 2621480);
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
