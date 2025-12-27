package com.adamsmods.adamsarsplus.mixin;

import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAmplifyThree;
import com.adamsmods.adamsarsplus.glyphs.augment_glyph.AugmentAmplifyTwo;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.adamsmods.adamsarsplus.datagen.AdamsBlockTagsProvider.BREAK_A;
import static com.adamsmods.adamsarsplus.datagen.AdamsBlockTagsProvider.BREAK_B;

@Mixin(BlockUtil.class)
public class MixinBlockUtil {
    @Inject(at = @At(value = "HEAD"), cancellable = true, method = "canBlockBeHarvested", remap = false)
    private static void canBlockBeHarvested0(SpellStats stats, Level world, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        BlockState state = world.getBlockState(pos);
        if(state.is(BREAK_A) && !(stats.hasBuff(AugmentAmplifyTwo.INSTANCE) || stats.hasBuff(AugmentAmplifyThree.INSTANCE))){
            cir.setReturnValue(false);
        }
        if(state.is(BREAK_B) && (!stats.hasBuff(AugmentAmplifyThree.INSTANCE))){
            cir.setReturnValue(false);
        }
    }
}
