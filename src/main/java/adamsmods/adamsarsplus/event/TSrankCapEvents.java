package adamsmods.adamsarsplus.event;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.capability.TSrankCap;
import adamsmods.adamsarsplus.network.PacketUpdateRank;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public class TSrankCapEvents {

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent e) {
        syncPlayerEvent(e.getEntity());
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone e) {
        if (!e.getOriginal().level().isClientSide) {
            TSrankCap newRank = TSrankCap.getTsTier(e.getEntity());
            TSrankCap origRank = TSrankCap.getTsTier(e.getOriginal());

            newRank.setTsTier(origRank.getTsTier(e.getOriginal()).tsTier);

            PacketDistributor.sendToPlayer(
                    (ServerPlayer) e.getEntity(),
                    PacketUpdateRank.of(newRank.getTsTier(e.getEntity()).tsTier)
            );
        }
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
        syncPlayerEvent(e.getEntity());
    }

    @SubscribeEvent
    public static void playerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
        syncPlayerEvent(e.getEntity());
    }

    public static void syncPlayerEvent(Player playerEntity) {
        if (playerEntity instanceof ServerPlayer serverPlayer) {
            TSrankCap Rank = TSrankCap.getTsTier(playerEntity);

            PacketDistributor.sendToPlayer(
                    serverPlayer,
                    PacketUpdateRank.of(Rank.tsTier)
            );
        }
    }
}