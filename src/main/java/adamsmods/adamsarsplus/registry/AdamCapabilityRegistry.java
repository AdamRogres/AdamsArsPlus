package adamsmods.adamsarsplus.registry;


import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.capability.TSrankCap;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AdamCapabilityRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES;
    public static final Supplier<AttachmentType<TSrankCap>> TSRANK_CAP_ID;

    static {
        ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, AdamsArsPlus.MODID);
        TSRANK_CAP_ID = ATTACHMENT_TYPES.register("tsrank_cap", () -> AttachmentType.serializable(TSrankCap::new).copyOnDeath().build());

    }

/*
    public static Optional<ITSrankCap> getTsTier(LivingEntity entity) {
        if (entity == null) return Optional.empty();
        return Optional.ofNullable(entity.getData(TSrankCapAttacher.TSRANK_CAPABILITY));
    }

    @EventBusSubscriber(modid = AdamsArsPlus.MODID)
    public static class EventHandler {

        @SubscribeEvent
        public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                // In 1.21 NeoForge, attachments are added directly
                event.addCapability(TSRANK_CAP_ID, new TSrankCapAttacher());
            }
        }

        @SubscribeEvent
        public static void playerClone(PlayerEvent.Clone event) {
            Player oldPlayer = event.getOriginal();
            Player newPlayer = event.getEntity();

            // In 1.21, no revive()/invalidateCaps() needed
            // Data is accessed directly via getData()
            ITSrankCap oldRank = oldPlayer.getData(TSrankCapAttacher.TSRANK_CAPABILITY);
            ITSrankCap newRank = newPlayer.getData(TSrankCapAttacher.TSRANK_CAPABILITY);
            newRank.setTsTier(oldRank.getTsTier());
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
            // Same sync logic, just updated cap access
            ITSrankCap cap = player.getData(TSrankCapAttacher.TSRANK_CAPABILITY);
            CompoundTag tag = (CompoundTag) cap.serializeNBT(player.level().registryAccess());
            if (player instanceof ServerPlayer serverPlayer) {
                Networking.sendToPlayerClient(new PacketSyncPlayerCap(tag), serverPlayer);
            }
        }
    }

 */
}