package adamsmods.adamsarsplus;

import adamsmods.adamsarsplus.client.example.ClientEvents;
import adamsmods.adamsarsplus.registry.example.ModAdvTriggers;
import adamsmods.adamsarsplus.registry.ModPotions;
import adamsmods.adamsarsplus.registry.ModRegistry;
import adamsmods.adamsarsplus.util.example.CompatUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(AdamsArsPlus.MODID)
public class AdamsArsPlus {

    public static final String MODID = "adamsarsplus";

    public AdamsArsPlus(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.STARTUP, ConfigHandler.STARTUP_SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);

        ModRegistry.registerRegistries(modEventBus);
        ArsNouveauRegistry.init();

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::loadComplete);

        NeoForge.EVENT_BUS.register(ModPotions.class);
        if (FMLEnvironment.dist.isClient()) {
            NeoForge.EVENT_BUS.addListener(new ClientEvents()::openBackpackGui);
            modEventBus.addListener(this::doClientStuff);
        }
        ModAdvTriggers.init();
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ArsNouveauRegistry.postInit();
            CompatUtils.checkCompats();
        });
    }

    @OnlyIn(Dist.CLIENT)
    private void doClientStuff(final FMLClientSetupEvent event) {

    }

    public void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {

        });
    }

    // Credit to Ars Trinkets
    public static void setInterval(Runnable method, int tickInterval, int timeToLive, Supplier<Boolean> end) {
        NeoForge.EVENT_BUS.register(new SetInterval(method, tickInterval, timeToLive, end));
    }

    public static class SetInterval {
        int ticks = 0;
        Runnable method;
        int tickInterval = 0; //How many ticks have to pass before the method is called again
        int timeToLive = 0;
        Supplier<Boolean> end;

        public SetInterval(Runnable method, int tickInterval, int timeToLive, Supplier<Boolean> end) {
            //function, tick rate, time to live
            this.method = method;
            this.tickInterval = tickInterval;
            this.timeToLive = timeToLive;
            this.end = end;
        }

        @SubscribeEvent
        public void onTick(ServerTickEvent.Pre event) {
            //subtract 1 tickInterval from the time to live to account for the extra tick that runs when
            //unregistering the listener
            if (ticks >= (timeToLive - tickInterval) || end.get()) {
                //System.out.println("Time to unregister this listener, i guess ;-;");
                NeoForge.EVENT_BUS.unregister(this);
            }

            if (ticks % tickInterval == 0) {
                //System.out.println("On tick event called :)");
                this.method.run();
            }
            ticks++;
        }
    }
}
