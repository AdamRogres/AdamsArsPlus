package adamsmods.adamsarsplus.common.capability;

import adamsmods.adamsarsplus.registry.AdamCapabilityRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class TSrankCap implements INBTSerializable<CompoundTag> {
    public int tsTier;

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var cTag = new CompoundTag();
        cTag.putInt("ts_tier", this.tsTier);
        return cTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag cTag) {
        this.tsTier = cTag.getInt("ts_tier");
    }

    public TSrankCap() {
        this.tsTier = 0;
    }

    public static TSrankCap getTsTier(Player p) {
        return p.getData(AdamCapabilityRegistry.TSRANK_CAP_ID);
    }

    public void setTsTier(int tsTier) {
        this.tsTier = tsTier;
    }

}