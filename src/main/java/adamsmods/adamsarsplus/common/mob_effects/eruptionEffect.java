package adamsmods.adamsarsplus.common.mob_effects;

import adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.ArrayList;
import java.util.List;

import static adamsmods.adamsarsplus.registry.ModPotions.ERUPTION_EFFECT;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public class eruptionEffect extends MobEffect {

    public eruptionEffect() {
        super(MobEffectCategory.HARMFUL, 15931670);
    }

    public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level world;

        world = pLivingEntity.level();

        if (world instanceof ServerLevel level && pLivingEntity.tickCount % 20 == 0) {
            playRingParticles(pLivingEntity, level);
        }

        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int p_295629_, int p_295734_) {
        int i = 5 >> p_295734_;
        return i > 0 ? p_295629_ % i == 0 : true;
    }

    public void playRingParticles(LivingEntity living, ServerLevel level) {
        Vec3 livingEyes = living.getEyePosition();
        double x = livingEyes.x;
        double y = livingEyes.y;
        double z = livingEyes.z;

        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0, 0, 0, 0.3);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0, 0, 0, 0.3);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, -0.5, -0.5, 0, 0.3);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0.5, -0.5, 0, 0.3);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0.5, 0.5, 0, 0.3);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, -0.5, 0.5, 0, 0.3);

    }

    @SubscribeEvent
    public static void entityHurt(LivingDamageEvent.Pre e) {
        var container = e.getContainer();
        var source = container.getSource();
        var amount = container.getNewDamage();
        if (source.is(DamageTypes.ON_FIRE) && e.getEntity().hasEffect(ERUPTION_EFFECT)) {
            container.setNewDamage(amount * 2.0f);
        }
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
