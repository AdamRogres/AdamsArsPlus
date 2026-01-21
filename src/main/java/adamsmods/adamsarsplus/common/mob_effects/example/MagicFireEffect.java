package adamsmods.adamsarsplus.common.mob_effects.example;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.common.extensions.IMobEffectExtension;

public class MagicFireEffect extends MobEffect implements IMobEffectExtension {

    public MagicFireEffect() {
        super(MobEffectCategory.NEUTRAL, 14981690);
    }

}
