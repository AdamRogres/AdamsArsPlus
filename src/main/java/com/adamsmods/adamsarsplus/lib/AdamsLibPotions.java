package com.adamsmods.adamsarsplus.lib;

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
    public static final String TENSHADOWS = "ten_shadows";

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
