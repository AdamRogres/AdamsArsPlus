package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;

public class IceBurstEffect extends MobEffect {

    public IceBurstEffect() {
        super(MobEffectCategory.HARMFUL, 2039587);
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
