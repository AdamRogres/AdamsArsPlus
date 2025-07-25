package com.adamsmods.adamsarsplus.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface ITSrankCap extends INBTSerializable<CompoundTag> {

    default int getTsTier() {
        return 0;
    }

    default void setTsTier(int tier) {
    }
}
