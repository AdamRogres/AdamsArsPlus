package com.adamsmods.api;

import java.util.concurrent.ConcurrentHashMap;

import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import net.minecraft.resources.ResourceLocation;

public class APerkSlot {
    public static ConcurrentHashMap<ResourceLocation, PerkSlot> APERK_SLOTS = new ConcurrentHashMap();
    public static final PerkSlot FOUR = new PerkSlot(new ResourceLocation("adamsarsplus", "four"), 4);
    public static final PerkSlot FIVE = new PerkSlot(new ResourceLocation("adamsarsplus", "five"), 5);
    public static final PerkSlot SIX = new PerkSlot(new ResourceLocation("adamsarsplus", "six"), 6);

    static {
        APERK_SLOTS.put(FOUR.id, FOUR);
        APERK_SLOTS.put(FIVE.id, FIVE);
        APERK_SLOTS.put(SIX.id, SIX);
    }
}
