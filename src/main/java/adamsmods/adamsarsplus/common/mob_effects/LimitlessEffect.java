package adamsmods.adamsarsplus.common.mob_effects;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class LimitlessEffect extends MobEffect {

    public LimitlessEffect() {
        super(MobEffectCategory.NEUTRAL, 2039587);
    }

    @Override
    public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
        return true;
    }

}
