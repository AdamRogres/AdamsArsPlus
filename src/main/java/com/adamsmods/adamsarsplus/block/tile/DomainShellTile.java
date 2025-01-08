package com.adamsmods.adamsarsplus.block.tile;

import com.adamsmods.adamsarsplus.block.ModBlocks;
import com.adamsmods.adamsarsplus.entities.EntityDomainSpell;
import com.hollingsworth.arsnouveau.api.entity.IDispellable;
import com.hollingsworth.arsnouveau.api.particle.ParticleColorRegistry;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.block.ITickable;
import com.hollingsworth.arsnouveau.common.block.tile.ModdedTile;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class DomainShellTile extends ModdedTile implements ITickable, IDispellable {
    public double age;
    public boolean isPermanent;
    public double lengthModifier;
    public ParticleColor color = ParticleColor.defaultParticleColor();
    public double refinement;

    public DomainShellTile(BlockPos pos, BlockState state) {
        super((BlockEntityType) ModBlocks.DOMAIN_SHELL_BLOCK_TILE.get(), pos, state);
    }

    public void tick() {
        if (!this.isPermanent) {
            if (!this.level.isClientSide) {
                ++this.age;
                if (this.age > (double)120.0F + (double)20.0F * this.lengthModifier) {
                    this.level.destroyBlock(this.getBlockPos(), false);
                    this.level.removeBlockEntity(this.getBlockPos());
                }
            }

        }
    }

    public void load(CompoundTag compound) {
        super.load(compound);
        this.age = compound.getDouble("age");
        this.color = ParticleColorRegistry.from(compound.getCompound("lightColor"));
        this.isPermanent = compound.getBoolean("permanent");
        this.lengthModifier = compound.getDouble("modifier");
    }

    public void saveAdditional(CompoundTag tag) {
        tag.putDouble("age", this.age);
        tag.put("lightColor", this.color.serialize());
        tag.putBoolean("permanent", this.isPermanent);
        tag.putDouble("modifier", this.lengthModifier);
    }

    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public boolean onDispel(@NotNull LivingEntity caster) {
        this.level.destroyBlock(this.getBlockPos(), false);
        this.level.removeBlockEntity(this.getBlockPos());
        return true;
    }
}
