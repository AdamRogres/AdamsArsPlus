package com.adamsmods.adamsarsplus.events;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.network.AdamNetworking;
import com.adamsmods.adamsarsplus.network.PacketUpdateRank;
import com.adamsmods.adamsarsplus.registry.AdamCapabilityRegistry;
import com.hollingsworth.arsnouveau.common.network.Networking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(
        modid = AdamsArsPlus.MOD_ID
)
public class TSrankCapEvents {
    public TSrankCapEvents() {
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent e) {
        syncPlayerEvent(e.getEntity());
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone e) {
        if (!e.getOriginal().level().isClientSide) {
            AdamCapabilityRegistry.getTsTier(e.getEntity()).ifPresent((newRank) -> AdamCapabilityRegistry.getTsTier(e.getOriginal()).ifPresent((origRank) -> {
                newRank.setTsTier(origRank.getTsTier());

                AdamNetworking.ADAMINSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)e.getEntity()), new PacketUpdateRank(newRank.getTsTier()));
            }));
        }
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.StartTracking e) {
        syncPlayerEvent(e.getEntity());
    }

    @SubscribeEvent
    public static void playerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
        syncPlayerEvent(e.getEntity());
    }

    public static void syncPlayerEvent(Player playerEntity) {
        if (playerEntity instanceof ServerPlayer) {
            AdamCapabilityRegistry.getTsTier(playerEntity).ifPresent((rank) -> {
                rank.setTsTier(rank.getTsTier());
                AdamNetworking.ADAMINSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)playerEntity), new PacketUpdateRank(rank.getTsTier()));
            });
        }

    }

}
