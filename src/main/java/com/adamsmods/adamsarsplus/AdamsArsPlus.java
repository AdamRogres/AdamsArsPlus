package com.adamsmods.adamsarsplus;


import com.adamsmods.adamsarsplus.datagen.CommunityMages;
import com.adamsmods.adamsarsplus.entities.AdamsModEntities;
import com.adamsmods.adamsarsplus.entities.client.*;
import com.adamsmods.adamsarsplus.events.AdamsEvents;
import com.adamsmods.adamsarsplus.loot.ModLootModifiers;
import com.adamsmods.adamsarsplus.network.AdamNetworking;
import com.adamsmods.adamsarsplus.registry.AdamCapabilityRegistry;
import com.adamsmods.adamsarsplus.registry.AdamClientHandler;
import com.adamsmods.adamsarsplus.registry.ModRegistry;
import com.adamsmods.adamsarsplus.util.SetInterval;
import com.hollingsworth.arsnouveau.client.registry.ClientHandler;
import com.hollingsworth.arsnouveau.setup.registry.CreativeTabRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AdamsArsPlus.MOD_ID)
public class AdamsArsPlus {
    public static final String MOD_ID = "adamsarsplus";

    private static final Logger LOGGER = LogManager.getLogger();

    public AdamsArsPlus() {
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC, "adamsarsplus-common.toml");

        ModRegistry.registerRegistries(modbus);
        ArsNouveauRegistry.registerGlyphs();

        modbus.addListener(this::setup);
        modbus.addListener(this::doClientStuff);
        modbus.addListener(this::doTabThings);

        ModLootModifiers.register(modbus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    private void setup(final FMLCommonSetupEvent event) {
        ArsNouveauRegistry.registerSounds();
        ArsNouveauRegistry.addAugments();
        ArsNouveauRegistry.registerPerks();

        AdamNetworking.registerMessages();


        event.enqueueWork(AdamsModEntities::registerPlacements);

        CommunityMages.initAlt();
        CommunityMages.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(AdamClientHandler::registerRenderers);

        // Mages
        EntityRenderers.register(AdamsModEntities.RYAN_ENTITY.get(), RyanRenderer::new);
        EntityRenderers.register(AdamsModEntities.CADE_ENTITY.get(), CadeRenderer::new);
        EntityRenderers.register(AdamsModEntities.NICK_ENTITY.get(), NickRenderer::new);
        EntityRenderers.register(AdamsModEntities.CAM_ENTITY.get(), CamRenderer::new);
        EntityRenderers.register(AdamsModEntities.MATT_ENTITY.get(), MattRenderer::new);
        EntityRenderers.register(AdamsModEntities.ADAM_ENTITY.get(), AdamRenderer::new);

        EntityRenderers.register(AdamsModEntities.MAGE_ENTITY.get(), MageRenderer::new);
        EntityRenderers.register(AdamsModEntities.FLAME_MAGE_ENTITY.get(), MageRenderer::new);
        EntityRenderers.register(AdamsModEntities.FROST_MAGE_ENTITY.get(), MageRenderer::new);
        EntityRenderers.register(AdamsModEntities.EARTH_MAGE_ENTITY.get(), MageRenderer::new);
        EntityRenderers.register(AdamsModEntities.LIGHTNING_MAGE_ENTITY.get(), MageRenderer::new);
        EntityRenderers.register(AdamsModEntities.HOLY_MAGE_ENTITY.get(), MageRenderer::new);
        EntityRenderers.register(AdamsModEntities.VOID_MAGE_ENTITY.get(), MageRenderer::new);

        EntityRenderers.register(AdamsModEntities.MAGE_KNIGHT.get(), MageKnightRenderer::new);
        EntityRenderers.register(AdamsModEntities.FLAME_KNIGHT.get(), MageKnightRenderer::new);
        EntityRenderers.register(AdamsModEntities.FROST_KNIGHT.get(), MageKnightRenderer::new);
        EntityRenderers.register(AdamsModEntities.EARTH_KNIGHT.get(), MageKnightRenderer::new);
        EntityRenderers.register(AdamsModEntities.LIGHTNING_KNIGHT.get(), MageKnightRenderer::new);
        EntityRenderers.register(AdamsModEntities.HOLY_KNIGHT.get(), MageKnightRenderer::new);
        EntityRenderers.register(AdamsModEntities.VOID_KNIGHT.get(), MageKnightRenderer::new);

        // Misc
        EntityRenderers.register(AdamsModEntities.FIRE_ENTITY.get(), FireRenderer::new);
        EntityRenderers.register(AdamsModEntities.SUMMON_SKELETON_M.get(), RenderSummonedSkeletonM::new);
        EntityRenderers.register(AdamsModEntities.TERRA_ENTITY.get(), TerraprismaRenderer::new);

        // Ten Shadows
        EntityRenderers.register(AdamsModEntities.DIVINE_DOG.get(), DDogRenderer::new);
        EntityRenderers.register(AdamsModEntities.NUE.get(), NueRenderer::new);
        EntityRenderers.register(AdamsModEntities.RABBIT_ESCAPE.get(), RabbitERenderer::new);
        EntityRenderers.register(AdamsModEntities.ROUND_DEER.get(), RDeerRenderer::new);
        EntityRenderers.register(AdamsModEntities.MAHORAGA.get(), MahoragaRenderer::new);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void doTabThings(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == CreativeTabRegistry.BLOCKS.get()) {
            for (var item : ModRegistry.ITEMS.getEntries()) {
                event.accept(item::get);
            }
        }
    }
    @SubscribeEvent
    public void doCapabilities(RegisterCapabilitiesEvent event){

    }
    public static void setInterval(Runnable method, int tickInterval, int timeToLive, Supplier<Boolean> end){
        MinecraftForge.EVENT_BUS.register(new SetInterval(method, tickInterval, timeToLive, end));
    }
}

