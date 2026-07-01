package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;

//@EventBusSubscriber(modid = AdamsArsPlus.MODID)
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

    public boolean shouldApplyEffectTickThisTick(int p_295629_, int p_295734_) {
        int i = 5 >> p_295734_;
        return i > 0 ? p_295629_ % i == 0 : true;
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
