package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.ArrayList;
import java.util.List;

import static adamsmods.adamsarsplus.registry.ModPotions.ERUPTION_EFFECT;

public class SixEyesEffect extends MobEffect {

    public SixEyesEffect() {
        super(MobEffectCategory.BENEFICIAL, 13565951);
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
