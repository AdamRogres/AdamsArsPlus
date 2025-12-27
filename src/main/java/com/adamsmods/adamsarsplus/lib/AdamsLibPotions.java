package com.adamsmods.adamsarsplus.lib;

import com.adamsmods.adamsarsplus.entities.effects.FlameDeityAuraEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.RegistryObject;

public class AdamsLibPotions {

    public static final String LIMITLESS = "limitless";
    public static final String DOMAIN_BURNOUT = "domain_burnout";
    public static final String MANA_EXHAUST = "mana_exhaust";
    public static final String SIMPLE_DOMAIN = "simple_domain";
    public static final String ERUPTION = "eruption";
    public static final String FRACTURE = "fracture";
    public static final String SIX_EYES = "six_eyes";
    public static final String MANA_HEALTH = "mana_health";
    public static final String ICEBURST = "ice_burst";
    public static final String DISRUPTION = "disruption";
    public static final String TENSHADOWS = "ten_shadows";
    public static final String SOUL_RIME = "soul_rime";
    public static final String CLOUD_STEPS = "cloud_steps";
    public static final String LEAP_FATIGUE = "leap_fatigue";
    public static final String FLAME_DEITY = "flame_deity_aura";
    public static final String MARKED_CREMATION = "marked_for_cremation";
    public static final String WALKING_BLIZZARD = "walking_blizzard";
    public static final String EARTHEN_HEART = "earthen_heart";
    public static final String LIGHTNING_STEPS = "lightning_steps";
    public static final String HOLY_LEGION = "holy_legion";
    public static final String ABYSSAL_DOMINATION = "abyssal_domination";

    public static String potion(String base) {
        return base + "_potion";
    }

    public static String longPotion(String base) {
        return potion(base) + "_long";
    }

    public static String strongPotion(String base) {
        return potion(base) + "_strong";
    }


}
