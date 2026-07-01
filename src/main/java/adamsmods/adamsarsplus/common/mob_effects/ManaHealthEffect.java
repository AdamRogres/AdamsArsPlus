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
public class ManaHealthEffect extends MobEffect {

    public ManaHealthEffect() {
        super(MobEffectCategory.BENEFICIAL, 13565951);
    }
    
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return entity.getAbsorptionAmount() > 0.0F || entity.level().isClientSide;
    }

    public void onEffectStarted(LivingEntity entity, int amplifier) {
        super.onEffectStarted(entity, amplifier);
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), (float)(2 * (amplifier))));
    }

    public boolean shouldApplyEffectTickThisTick(int p_295629_, int p_295734_) {
        int i = 5 >> p_295734_;
        return i > 0 ? p_295629_ % i == 0 : true;
    }
    
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
