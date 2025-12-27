package com.adamsmods.adamsarsplus.mixin;

import com.hollingsworth.arsnouveau.common.items.WarpScroll;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.SOUL_RIME_EFFECT;

@Mixin(WarpScroll.class)
public class MixinWarpScroll {
    @Inject(at = @At(value = "HEAD"), cancellable = true, method = "use", remap = false)
    private void use0(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        ItemStack stack = player.getItemInHand(hand);
        if(player.hasEffect(SOUL_RIME_EFFECT.get())){
            cir.setReturnValue(InteractionResultHolder.fail(stack));
        }
    }
}
