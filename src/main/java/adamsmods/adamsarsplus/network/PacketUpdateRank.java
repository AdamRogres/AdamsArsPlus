package adamsmods.adamsarsplus.network;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.capability.TSrankCap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static adamsmods.adamsarsplus.common.capability.TSrankCap.getTsTier;

public record PacketUpdateRank(int tier, float reserved) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PacketUpdateRank> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AdamsArsPlus.MODID, "update_rank"));

    public static final StreamCodec<FriendlyByteBuf, PacketUpdateRank> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeInt(packet.tier());
                buf.writeFloat(packet.reserved());
            },
            buf -> new PacketUpdateRank(buf.readInt(), buf.readFloat())
    );

    // Convenience constructors via static factories
    public static PacketUpdateRank of(int tier) {
        return new PacketUpdateRank(tier, -1.0F);
    }

    @Override
    public CustomPacketPayload.Type<PacketUpdateRank> type() {
        return TYPE;
    }

    // Called on the client thread automatically by NeoForge
    public static void handle(PacketUpdateRank packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var player = Minecraft.getInstance().player;
            if (player != null) {
                TSrankCap rank = getTsTier(player);
                rank.setTsTier(packet.tier());
            }
        });
    }
}