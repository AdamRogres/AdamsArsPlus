package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;

import static adamsmods.adamsarsplus.registry.ModPotions.CLOUD_STEPS_EFFECT;
import static com.hollingsworth.arsnouveau.setup.registry.ModPotions.FLIGHT_EFFECT;


@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public class CloudStepsEffect extends MobEffect {

    public CloudStepsEffect() {
        super(MobEffectCategory.HARMFUL, 2039587);
    }

    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity.hasEffect(CLOUD_STEPS_EFFECT) && pLivingEntity.hasEffect(FLIGHT_EFFECT)){
            if(pLivingEntity.getEffect(CLOUD_STEPS_EFFECT).getDuration() < 10){
                pLivingEntity.removeEffect(FLIGHT_EFFECT);
            }
        }
        return true;
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
