package adamsmods.adamsarsplus.common.blocks;

import adamsmods.adamsarsplus.registry.ModBlocks;
import com.hollingsworth.arsnouveau.api.entity.IDispellable;
import com.hollingsworth.arsnouveau.api.registry.ParticleColorRegistry;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.block.ITickable;
import com.hollingsworth.arsnouveau.common.block.tile.ModdedTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
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
        super(ModBlocks.DOMAIN_SHELL_BLOCK_TILE.get(), pos, state);
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

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider pRegistries) {
        super.loadAdditional(compound, pRegistries);
        this.age = compound.getDouble("age");
        this.color = ParticleColorRegistry.from(compound.getCompound("lightColor"));
        this.isPermanent = compound.getBoolean("permanent");
        this.lengthModifier = compound.getDouble("modifier");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(tag, pRegistries);
        tag.putDouble("age", this.age);
        tag.put("lightColor", this.color.serialize());
        tag.putBoolean("permanent", this.isPermanent);
        tag.putDouble("modifier", this.lengthModifier);
    }

    public boolean onDispel(@NotNull LivingEntity caster) {
        this.level.destroyBlock(this.getBlockPos(), false);
        this.level.removeBlockEntity(this.getBlockPos());
        return true;
    }
}
