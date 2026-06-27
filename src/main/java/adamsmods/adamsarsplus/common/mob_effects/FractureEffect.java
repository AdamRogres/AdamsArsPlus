package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public class FractureEffect extends MobEffect {

    public FractureEffect() {
        super(MobEffectCategory.HARMFUL, 16122102);
    }

    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level world;

        world = pLivingEntity.level();

        if (world instanceof ServerLevel level && pLivingEntity.tickCount % 20 == 0) {
            playRingParticles(pLivingEntity, level);
        }

        return true;
    }

    public void playRingParticles(LivingEntity living, ServerLevel level) {
        Vec3 livingEyes = living.getEyePosition();
        double x = livingEyes.x;
        double y = livingEyes.y;
        double z = livingEyes.z;

        level.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0, 0, 0, 0.3);
        level.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0, 0, 0, 0.3);
        level.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, -0.5, -0.5, 0, 0.3);
        level.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0.5, -0.5, 0, 0.3);
        level.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0.5, 0.5, 0, 0.3);
        level.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, -0.5, 0.5, 0, 0.3);

    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
