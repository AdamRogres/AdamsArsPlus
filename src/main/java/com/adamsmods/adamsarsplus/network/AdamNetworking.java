package com.adamsmods.adamsarsplus.network;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;


public class AdamNetworking {
    public static SimpleChannel ADAMINSTANCE;
    private static int ID = 0;

    public AdamNetworking() {
    }

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        ADAMINSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(AdamsArsPlus.MOD_ID, "network"), () -> "1.0", (s) -> true, (s) -> true);
        ADAMINSTANCE.registerMessage(nextID(), PacketUpdateRank.class, PacketUpdateRank::toBytes, PacketUpdateRank::new, PacketUpdateRank::handle);
    }

    public static void sendToNearby(Level world, BlockPos pos, Object toSend) {
        if (world instanceof ServerLevel ws) {
            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).stream().filter((p) -> p.distanceToSqr((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) < (double)4096.0F).forEach((p) -> ADAMINSTANCE.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }

    }

    public static void sendToNearby(Level world, Entity e, Object toSend) {
        sendToNearby(world, e.blockPosition(), toSend);
    }

    public static void sendToPlayerClient(Object msg, ServerPlayer player) {
        ADAMINSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void sendToServer(Object msg) {
        ADAMINSTANCE.sendToServer(msg);
    }
}
