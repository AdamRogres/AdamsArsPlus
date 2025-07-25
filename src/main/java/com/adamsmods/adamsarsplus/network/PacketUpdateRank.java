package com.adamsmods.adamsarsplus.network;

import com.adamsmods.adamsarsplus.registry.AdamCapabilityRegistry;
import com.hollingsworth.arsnouveau.ArsNouveau;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateRank {
    public int tier;
    public float reserved;

    public PacketUpdateRank(FriendlyByteBuf buf) {
        this.tier = buf.readInt();
        this.reserved = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.tier);
        buf.writeFloat(this.reserved);
    }

    public PacketUpdateRank(int tier) {
        this.tier = tier;
        this.reserved = -1.0F;
    }

    public PacketUpdateRank(int tier, float reserved) {
        this.tier = tier;
        this.reserved = reserved;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            if (ArsNouveau.proxy.getPlayer() != null) {
                AdamCapabilityRegistry.getTsTier(ArsNouveau.proxy.getPlayer()).ifPresent((rank) -> {
                    rank.setTsTier(this.tier);
                });


            }
        });
        ((NetworkEvent.Context)ctx.get()).setPacketHandled(true);
    }
}

