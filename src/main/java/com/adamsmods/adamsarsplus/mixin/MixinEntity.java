package com.adamsmods.adamsarsplus.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.MARKED_CREMATION_EFFECT;

@Mixin(Entity.class)
public class MixinEntity {
    @Inject(at = @At(value = "HEAD"), cancellable = true, method = "fireImmune")
    public void fireImmune0(CallbackInfoReturnable<Boolean> cir){
        if((Object) this instanceof LivingEntity living){
            if(living.hasEffect(MARKED_CREMATION_EFFECT.get())){
                cir.setReturnValue(false);
            }
        }
    }
}