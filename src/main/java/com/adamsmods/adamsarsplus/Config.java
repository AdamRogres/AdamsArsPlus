package com.adamsmods.adamsarsplus;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = AdamsArsPlus.MOD_ID)
public class Config {


    public static ForgeConfigSpec.IntValue      MAX_DISCOUNTS;
    public static ForgeConfigSpec.IntValue      MAX_DOMAIN_ENTITIES;
    public static ForgeConfigSpec.BooleanValue  DOMAIN_BURNOUT;
    public static ForgeConfigSpec.BooleanValue  DISCOUNT_BACKLASH;

    public static ForgeConfigSpec SERVER_CONFIG;
    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        SERVER_BUILDER.comment("SERVER CONFIGURATION");
        DOMAIN_BURNOUT      = SERVER_BUILDER.comment("does the domain glyph have a cooldown burnout?").define("doDomainBurnout", true);
        DISCOUNT_BACKLASH   = SERVER_BUILDER.comment("does the spell efficiency glyph inflict mana exhaustion?").define("doDiscountBacklash", true);
        MAX_DISCOUNTS       = SERVER_BUILDER.comment("maximum number of times spell efficiency can appear").defineInRange("maxDiscount",5,0,10);
        MAX_DOMAIN_ENTITIES = SERVER_BUILDER.comment("maximum number of entities a domain can target every spell cast").defineInRange("maxDomainEntities",10,1,100);

        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
