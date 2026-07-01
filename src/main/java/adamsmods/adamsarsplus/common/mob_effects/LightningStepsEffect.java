package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;

import static adamsmods.adamsarsplus.registry.ModPotions.LEAP_FATIGUE_EFFECT;
import static adamsmods.adamsarsplus.registry.ModPotions.LIGHTNING_STEPS_EFFECT;

//@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public class LightningStepsEffect extends MobEffect {

    public LightningStepsEffect() {
        super(MobEffectCategory.BENEFICIAL, 16776960);
    }

    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity.hasEffect(LEAP_FATIGUE_EFFECT) && pLivingEntity.hasEffect(LIGHTNING_STEPS_EFFECT)){
            int amp = pLivingEntity.getEffect(LIGHTNING_STEPS_EFFECT).getAmplifier();
            pLivingEntity.removeEffect(LIGHTNING_STEPS_EFFECT);
            pLivingEntity.removeEffect(LEAP_FATIGUE_EFFECT);
            if(amp != 0){
                pLivingEntity.addEffect(new MobEffectInstance(LIGHTNING_STEPS_EFFECT, 180,amp - 1, false, false));
            }
        }
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int p_295629_, int p_295734_) {
        int i = 5 >> p_295734_;
        return i > 0 ? p_295629_ % i == 0 : true;
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
