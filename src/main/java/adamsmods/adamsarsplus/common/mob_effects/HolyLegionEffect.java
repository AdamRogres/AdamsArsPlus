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
public class HolyLegionEffect extends MobEffect {

    public HolyLegionEffect() {
        super(MobEffectCategory.BENEFICIAL, 16776960);
    }

    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

        return true;
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }


    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
