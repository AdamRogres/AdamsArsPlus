package adamsmods.adamsarsplus.common.mob_effects.example;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class FrozenEffect extends MobEffect {
    public FrozenEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

//    @Override
//    public List<ItemStack> getCurativeItems() {
//        return List.of();
//    }
}
