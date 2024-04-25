package com.adamsmods.adamsarsplus.lib;

public class AdamsLibPotions {

    public static final String LIMITLESS = "limitless";

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
