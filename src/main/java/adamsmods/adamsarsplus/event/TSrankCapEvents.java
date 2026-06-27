package adamsmods.adamsarsplus.event;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.capability.TSrankCap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(
        modid = AdamsArsPlus.MODID
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
            TSrankCap.getTsTier(e.getEntity()).ifPresent((newRank) -> TSrankCap.getTsTier(e.getOriginal()).ifPresent((origRank) -> {
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
            TSrankCap.getTsTier(playerEntity).ifPresent((rank) -> {
                rank.setTsTier(rank.getTsTier());
                AdamNetworking.ADAMINSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)playerEntity), new PacketUpdateRank(rank.getTsTier()));
            });
        }

    }

}
