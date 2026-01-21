package adamsmods.adamsarsplus.registry;

import adamsmods.adamsarsplus.common.mob_effects.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static adamsmods.adamsarsplus.AdamsArsPlus.MODID;
import static adamsmods.adamsarsplus.common.lib.AdamsLibPotions.*;

public class ModPotions {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, MODID);

    public static final DeferredHolder<MobEffect, MobEffect> LIMITLESS_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> DOMAIN_BURNOUT_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> MANA_EXHAUST_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> SIMPLE_DOMAIN_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> ERUPTION_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> FRACTURE_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> SIX_EYES_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> MANA_HEALTH_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> ICEBURST_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> TENSHADOWS_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> DISRUPTION_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> SOUL_RIME_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> CLOUD_STEPS_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> LEAP_FATIGUE_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> FLAME_DEITY_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> MARKED_CREMATION_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> WALKING_BLIZZARD_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> EARTHEN_HEART_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> LIGHTNING_STEPS_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> HOLY_LEGION_EFFECT;
    public static final DeferredHolder<MobEffect, MobEffect> ABYSSAL_DOMINATION_EFFECT;

    
    @SubscribeEvent
    private static void addBrewingRecipes(final RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        
    }

    static {
         LIMITLESS_EFFECT = EFFECTS.register(LIMITLESS, LimitlessEffect::new);
         DOMAIN_BURNOUT_EFFECT = EFFECTS.register(DOMAIN_BURNOUT, DEburnoutEffect::new);
         MANA_EXHAUST_EFFECT = EFFECTS.register(MANA_EXHAUST, ManaExhaustEffect::new);
         SIMPLE_DOMAIN_EFFECT = EFFECTS.register(SIMPLE_DOMAIN, simpleDomainEffect::new);
         ERUPTION_EFFECT = EFFECTS.register(ERUPTION, eruptionEffect::new);
         FRACTURE_EFFECT = EFFECTS.register(FRACTURE, FractureEffect::new);
         SIX_EYES_EFFECT = EFFECTS.register(SIX_EYES, SixEyesEffect::new);
         MANA_HEALTH_EFFECT = EFFECTS.register(MANA_HEALTH, ManaHealthEffect::new);
         ICEBURST_EFFECT = EFFECTS.register(ICEBURST, IceBurstEffect::new);
         TENSHADOWS_EFFECT = EFFECTS.register(TENSHADOWS, TenShadowsEffect::new);
         DISRUPTION_EFFECT = EFFECTS.register(DISRUPTION, DisruptionEffect::new);
         SOUL_RIME_EFFECT = EFFECTS.register(SOUL_RIME, SoulRimeEffect::new);
         CLOUD_STEPS_EFFECT = EFFECTS.register(CLOUD_STEPS, CloudStepsEffect::new);
         LEAP_FATIGUE_EFFECT = EFFECTS.register(LEAP_FATIGUE, LeapFatigueEffect::new);
         FLAME_DEITY_EFFECT = EFFECTS.register(FLAME_DEITY, FlameDeityAuraEffect::new);
         MARKED_CREMATION_EFFECT = EFFECTS.register(MARKED_CREMATION, MarkedForCremationEffect::new);
         WALKING_BLIZZARD_EFFECT = EFFECTS.register(WALKING_BLIZZARD, WalkingBlizzardEffect::new);
         EARTHEN_HEART_EFFECT = EFFECTS.register(EARTHEN_HEART, EarthSetbonusEffect::new);
         LIGHTNING_STEPS_EFFECT = EFFECTS.register(LIGHTNING_STEPS, LightningStepsEffect::new);
         HOLY_LEGION_EFFECT = EFFECTS.register(HOLY_LEGION, HolyLegionEffect::new);
         ABYSSAL_DOMINATION_EFFECT = EFFECTS.register(ABYSSAL_DOMINATION, AbyssalDominationEffect::new);
    }

}
