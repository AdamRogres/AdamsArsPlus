package adamsmods.adamsarsplus.util;

import adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.ConcurrentHashMap;

public class APerkSlot {
    public static ConcurrentHashMap<ResourceLocation, PerkSlot> APERK_SLOTS = new ConcurrentHashMap<>();

    public static final PerkSlot FOUR = new PerkSlot(AdamsArsPlus.prefix("four"), 4);
    public static final PerkSlot FIVE = new PerkSlot(AdamsArsPlus.prefix("five"), 5);
    public static final PerkSlot SIX = new PerkSlot(AdamsArsPlus.prefix("six"), 6);

    static {
        APERK_SLOTS.put(FOUR.id(), FOUR);
        APERK_SLOTS.put(FIVE.id(), FIVE);
        APERK_SLOTS.put(SIX.id(), SIX);
    }
}
