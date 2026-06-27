package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public class DisruptionEffect extends MobEffect {

    public DisruptionEffect() {
        super(MobEffectCategory.HARMFUL, 8991416);
    }

    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        for(int i = 0; i < pLivingEntity.getActiveEffects().size(); i++){
            if(pLivingEntity.getActiveEffects().stream().toList().get(i).getEffect().value().isBeneficial()){
                pLivingEntity.removeEffect(pLivingEntity.getActiveEffects().stream().toList().get(i).getEffect());
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
