package adamsmods.adamsarsplus.common.mob_effects;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class TenShadowsEffect extends MobEffect {

    public TenShadowsEffect() {
        super(MobEffectCategory.BENEFICIAL, 16122102);
    }

    /*
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity.hasEffect(ModPotions.SUMMONING_SICKNESS_EFFECT.get())){
            pLivingEntity.removeEffect(TENSHADOWS_EFFECT.get());
        }
    }
    */


    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
