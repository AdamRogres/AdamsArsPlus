package com.adamsmods.adamsarsplus.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class TSrankCap implements ITSrankCap {
    private final LivingEntity livingEntity;
    private int tsTier;

    public TSrankCap(@Nullable LivingEntity entity) {
        this.livingEntity = entity;
    }

    public int getTsTier() {
        return this.tsTier;
    }

    public void setTsTier(int tsTier) {
        this.tsTier = tsTier;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putInt("ts_tier", this.getTsTier());
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.setTsTier(tag.getInt("ts_tier"));
    }
}
