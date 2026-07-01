package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;

import static adamsmods.adamsarsplus.registry.ModPotions.MARKED_CREMATION_EFFECT;

//@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public class FlameDeityAuraEffect extends MobEffect {

    public FlameDeityAuraEffect() {
        super(MobEffectCategory.BENEFICIAL, 16724530);
    }

    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        int range = 18 * (1 + pAmplifier);
        BlockPos p = pLivingEntity.blockPosition();

        for(LivingEntity e : pLivingEntity.level().getEntitiesOfClass(LivingEntity.class, new AABB(p.getCenter().add(range, range, range), p.getCenter().subtract(range, range, range)))) {
            if (!e.equals(pLivingEntity) && !e.hasEffect(MARKED_CREMATION_EFFECT)) {
                e.forceAddEffect(new MobEffectInstance(MARKED_CREMATION_EFFECT, 80, 0, false, false), pLivingEntity);
            }
        }
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int p_295629_, int p_295734_) {
        int i = 2 >> p_295734_;
        return i > 0 ? p_295629_ % i == 0 : true;
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}