package com.adamsmods.adamsarsplus.entities.effects;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.ERUPTION_EFFECT;
import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.WALKING_BLIZZARD_EFFECT;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class WalkingBlizzardEffect extends MobEffect {

    public WalkingBlizzardEffect() {
        super(MobEffectCategory.BENEFICIAL, 11184895);
    }

    public static void playRingParticles(LivingEntity living, ServerLevel level) {
        Vec3 livingEyes = living.getEyePosition();
        double x = livingEyes.x;
        double y = livingEyes.y;
        double z = livingEyes.z;

        for(int i = 0; i < 10; i++){
            level.sendParticles(ParticleTypes.SNOWFLAKE, x + living.getRandom().nextFloat() - 0.5, y + living.getRandom().nextFloat() - 0.5, z + living.getRandom().nextFloat() - 0.5, 1, 0, 0, 0, 0.1);
        }
    }

    @SubscribeEvent
    public static void entityHurt(LivingHurtEvent e) {
        if (e.getEntity().hasEffect((MobEffect)WALKING_BLIZZARD_EFFECT.get())) {
            if(e.getEntity().getEffect(WALKING_BLIZZARD_EFFECT.get()).getAmplifier() == 1){
                e.setAmount(0);
                e.getEntity().invulnerableTime = 40;
                e.getEntity().removeEffect(WALKING_BLIZZARD_EFFECT.get());
                e.getEntity().addEffect(new MobEffectInstance(WALKING_BLIZZARD_EFFECT.get(), 600, 0, false, false, true));

                Level world;
                world = e.getEntity().level();

                if (world instanceof ServerLevel level) {
                    playRingParticles(e.getEntity(), level);
                }
            }
        }
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
