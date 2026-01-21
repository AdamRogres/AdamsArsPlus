package adamsmods.adamsarsplus.common.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class AbyssalDominationEffect extends MobEffect {

    public AbyssalDominationEffect() {
        super(MobEffectCategory.BENEFICIAL, 2621480);
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

}
