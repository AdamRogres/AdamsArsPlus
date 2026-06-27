package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public class TenShadowsEffect extends MobEffect {

    public TenShadowsEffect() {
        super(MobEffectCategory.BENEFICIAL, 16122102);
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
