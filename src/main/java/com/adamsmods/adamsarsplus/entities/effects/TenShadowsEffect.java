package com.adamsmods.adamsarsplus.entities.effects;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.TENSHADOWS_EFFECT;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class TenShadowsEffect extends MobEffect {

    public TenShadowsEffect() {
        super(MobEffectCategory.HARMFUL, 16122102);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity.hasEffect(ModPotions.SUMMONING_SICKNESS_EFFECT.get())){
            pLivingEntity.removeEffect(TENSHADOWS_EFFECT.get());
        }
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
