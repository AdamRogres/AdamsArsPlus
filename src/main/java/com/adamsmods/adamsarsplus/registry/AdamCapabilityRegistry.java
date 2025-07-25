package com.adamsmods.adamsarsplus.registry;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.capability.ITSrankCap;
import com.adamsmods.adamsarsplus.capability.TSrankCapAttacher;
import com.hollingsworth.arsnouveau.common.capability.ANPlayerDataCap;
import com.hollingsworth.arsnouveau.common.capability.IPlayerCap;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketSyncPlayerCap;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class AdamCapabilityRegistry {
    public static final Capability<ITSrankCap> TSRANK_CAPABILITY = CapabilityManager.get(new CapabilityToken<ITSrankCap>() {
    });
    public static final Direction DEFAULT_FACING = null;

    public AdamCapabilityRegistry() {
    }

    public static LazyOptional<ITSrankCap> getTsTier(LivingEntity entity) {
        return entity == null ? LazyOptional.empty() : entity.getCapability(TSRANK_CAPABILITY);
    }

    @Mod.EventBusSubscriber(
            modid = AdamsArsPlus.MOD_ID
    )
    public static class EventHandler {
        public EventHandler() {
        }

        @SubscribeEvent
        public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                TSrankCapAttacher.attach(event);
            }

        }

        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.register(ITSrankCap.class);
        }

        @SubscribeEvent
        public static void playerClone(PlayerEvent.Clone event) {
            Player oldPlayer = event.getOriginal();
            oldPlayer.revive();
            AdamCapabilityRegistry.getTsTier(oldPlayer).ifPresent((oldRank) -> AdamCapabilityRegistry.getTsTier(event.getEntity()).ifPresent((newRank) -> {
                newRank.setTsTier(oldRank.getTsTier());
            }));

            event.getOriginal().invalidateCaps();
        }

        @SubscribeEvent
        public static void onPlayerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer) {
                syncPlayerCap(event.getEntity());
            }

        }

        @SubscribeEvent
        public static void respawnEvent(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity() instanceof ServerPlayer) {
                syncPlayerCap(event.getEntity());
            }

        }

        @SubscribeEvent
        public static void onPlayerStartTrackingEvent(PlayerEvent.StartTracking event) {
            if (event.getTarget() instanceof Player && event.getEntity() instanceof ServerPlayer) {
                syncPlayerCap(event.getEntity());
            }

        }

        @SubscribeEvent
        public static void onPlayerDimChangedEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (event.getEntity() instanceof ServerPlayer) {
                syncPlayerCap(event.getEntity());
            }

        }

        public static void syncPlayerCap(Player player) {
            IPlayerCap cap = (IPlayerCap)CapabilityRegistry.getPlayerDataCap(player).orElse(new ANPlayerDataCap());
            CompoundTag tag = (CompoundTag)cap.serializeNBT();
            if (player instanceof ServerPlayer serverPlayer) {
                Networking.sendToPlayerClient(new PacketSyncPlayerCap(tag), serverPlayer);
            }

        }
    }
}
