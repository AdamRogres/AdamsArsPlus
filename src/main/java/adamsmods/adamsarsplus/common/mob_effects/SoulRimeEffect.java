package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import com.ibm.icu.impl.coll.BOCSU;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public class SoulRimeEffect extends MobEffect {

    public SoulRimeEffect() {
        super(MobEffectCategory.HARMFUL, 11184895);
    }

    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level world;

        world = pLivingEntity.level();

        if (world instanceof ServerLevel level && pLivingEntity.tickCount % 20 == 0) {
            playRingParticles(pLivingEntity, level);
        }

        return true;
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    public void playRingParticles(LivingEntity living, ServerLevel level) {
        Vec3 livingEyes = living.getEyePosition();
        double x = livingEyes.x;
        double y = livingEyes.y;
        double z = livingEyes.z;

        double xOff;
        double yOff;
        double zOff;

        for(int i = 0; i < 8; i++){
            xOff = living.getBbWidth() * 1.2 * (level.random.nextFloat() - 0.5);
            yOff = living.getBbHeight() * 1.2 * (level.random.nextFloat() - 0.5);
            zOff = living.getBbWidth() * 1.2 * (level.random.nextFloat() - 0.5);

            level.sendParticles(ParticleTypes.SNOWFLAKE, x + xOff, y + yOff, z + zOff, 1, 0, -0.1, 0, 0.1);
        }
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
