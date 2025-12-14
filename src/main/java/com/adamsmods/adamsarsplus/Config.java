package com.adamsmods.adamsarsplus;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = AdamsArsPlus.MOD_ID)
public class Config {


    public static ForgeConfigSpec.IntValue      MAX_DISCOUNTS;
    public static ForgeConfigSpec.IntValue      MAX_DOMAIN_ENTITIES;
    public static ForgeConfigSpec.BooleanValue  DOMAIN_BURNOUT;
    public static ForgeConfigSpec.BooleanValue  DISCOUNT_BACKLASH;
    public static ForgeConfigSpec.BooleanValue  COM_MAGES;
    public static ForgeConfigSpec.BooleanValue  MAGES_GRIEF;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> MAGE_DIMENSION_BLACKLIST;

    public static ForgeConfigSpec SERVER_CONFIG;
    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        SERVER_BUILDER.comment("SERVER CONFIGURATION");
        DOMAIN_BURNOUT      = SERVER_BUILDER.comment("does the domain glyph have a cooldown burnout?").define("doDomainBurnout", true);
        DISCOUNT_BACKLASH   = SERVER_BUILDER.comment("does the spell efficiency glyph inflict mana exhaustion?").define("doDiscountBacklash", true);
        MAX_DISCOUNTS       = SERVER_BUILDER.comment("maximum number of times spell efficiency can appear").defineInRange("maxDiscount",5,0,10);
        MAX_DOMAIN_ENTITIES = SERVER_BUILDER.comment("maximum number of entities a domain can target every spell cast").defineInRange("maxDomainEntities",10,1,100);
        COM_MAGES           = SERVER_BUILDER.comment("include community spells for the mysterious mage entity?").define("doComMages", true);
        MAGES_GRIEF         = SERVER_BUILDER.comment("Allow overworld mages that can mobgrief?").define("doMageGrief", true);
        MAGE_DIMENSION_BLACKLIST = SERVER_BUILDER.comment("Dimensions where mages will not spawn. Ex: [\"minecraft:overworld\", \"undergarden:undergarden\"]. . Run /forge dimensions for a list.").defineList("dimensionBlacklist", new ArrayList(), (o) -> true);

        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
