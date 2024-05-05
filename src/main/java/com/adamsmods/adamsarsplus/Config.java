package com.adamsmods.adamsarsplus;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = AdamsArsPlus.MOD_ID)
public class Config {

    public static ForgeConfigSpec SERVER_CONFIG;
    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        SERVER_BUILDER.comment("SERVER CONFIGURATION");

        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
