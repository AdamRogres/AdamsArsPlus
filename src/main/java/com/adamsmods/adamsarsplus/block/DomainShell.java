package com.adamsmods.adamsarsplus.block;

import com.adamsmods.adamsarsplus.block.tile.DomainShellTile;
import com.hollingsworth.arsnouveau.common.block.TickableModBlock;
import com.hollingsworth.arsnouveau.common.block.tile.MageBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public class DomainShell extends TickableModBlock {
    public static final BooleanProperty TEMPORARY = BooleanProperty.create("temporary");

    public DomainShell(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(TEMPORARY, false));
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DomainShellTile(pos, state);
    }

    public boolean canDropFromExplosion(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
        return false;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{TEMPORARY});
    }

    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return state.hasProperty(TEMPORARY) && (Boolean)state.getValue(TEMPORARY) ? super.getTicker(level, state, type) : null;
    }
}
