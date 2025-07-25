package com.adamsmods.adamsarsplus.capability;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.registry.AdamCapabilityRegistry;
import com.hollingsworth.arsnouveau.api.mana.IManaCap;
import com.hollingsworth.arsnouveau.common.capability.ManaCap;
import com.hollingsworth.arsnouveau.common.capability.ManaCapAttacher;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TSrankCapAttacher {
    public TSrankCapAttacher() {
    }

    public static void attach(AttachCapabilitiesEvent<Entity> event) {
        TSrankCapAttacher.TSrankCapProvider provider = new TSrankCapAttacher.TSrankCapProvider();
        event.addCapability(TSrankCapAttacher.TSrankCapProvider.IDENTIFIER, provider);
    }

    private static class TSrankCapProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        public static final ResourceLocation IDENTIFIER = new ResourceLocation(AdamsArsPlus.MOD_ID, "ts_rank");
        private final ITSrankCap backend = new TSrankCap((LivingEntity)null);
        private final LazyOptional<ITSrankCap> optionalData = LazyOptional.of(() -> this.backend);

        private TSrankCapProvider() {
        }

        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return AdamCapabilityRegistry.TSRANK_CAPABILITY.orEmpty(cap, this.optionalData);
        }

        void invalidate() {
            this.optionalData.invalidate();
        }

        public CompoundTag serializeNBT() {
            return (CompoundTag)this.backend.serializeNBT();
        }

        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }
}
