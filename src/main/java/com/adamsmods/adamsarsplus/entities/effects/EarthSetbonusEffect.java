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
import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.MANA_HEALTH_EFFECT;
import static com.adamsmods.adamsarsplus.Config.Common.DISCOUNT_BACKLASH;
import static net.minecraft.world.effect.MobEffects.HEALTH_BOOST;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class EarthSetbonusEffect extends MobEffect {

    public EarthSetbonusEffect() {
        super(MobEffectCategory.NEUTRAL, 2039587);
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        AtomicInteger health = new AtomicInteger();

        if(pLivingEntity instanceof Player player){
            CapabilityRegistry.getMana(player).ifPresent((mana) -> {
                health.set(mana.getMaxMana() / 48);
            });
        }

        if(pLivingEntity.hasEffect(EARTHEN_HEART_EFFECT.get())){
            if(pLivingEntity.getEffect(EARTHEN_HEART_EFFECT.get()).getDuration() > 40){
                pLivingEntity.addEffect(new MobEffectInstance(HEALTH_BOOST, -1, health.get(), false, false));
            } else {
                pLivingEntity.removeEffect(HEALTH_BOOST);
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
