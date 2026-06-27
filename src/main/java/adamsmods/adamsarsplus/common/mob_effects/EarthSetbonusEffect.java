package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static adamsmods.adamsarsplus.registry.ModPotions.EARTHEN_HEART_EFFECT;
import static net.minecraft.world.effect.MobEffects.HEALTH_BOOST;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public class EarthSetbonusEffect extends MobEffect {

    public EarthSetbonusEffect() {
        super(MobEffectCategory.NEUTRAL, 2039587);
    }

    public boolean applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        AtomicInteger health = new AtomicInteger();

        if(pLivingEntity instanceof Player player){
            health.set(CapabilityRegistry.getMana(player).getMaxMana());
            CapabilityRegistry.getMana(player).setMana((double) health.get() / 48);
        }

        if(pLivingEntity.hasEffect(EARTHEN_HEART_EFFECT)){
            if(pLivingEntity.getEffect(EARTHEN_HEART_EFFECT).getDuration() > 40){
                pLivingEntity.addEffect(new MobEffectInstance(HEALTH_BOOST, -1, health.get(), false, false));
            } else {
                pLivingEntity.removeEffect(HEALTH_BOOST);
            }
        }

        return true;
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
