package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public class LimitlessEffect extends MobEffect {

    public LimitlessEffect() {
        super(MobEffectCategory.NEUTRAL, 2039587);
    }

    public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
        return true;
    }

}
