package com.adamsmods.adamsarsplus.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.FRACTURE_EFFECT;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {


    @Inject(at = @At(value = "TAIL"), method = "travel")
    public void travel0(Vec3 pTravelVector, CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;

        if(living.hasEffect(FRACTURE_EFFECT.get())){
            double d0 = 0.08;

            Vec3 vec3 = living.getDeltaMovement();

            double d3 = vec3.horizontalDistance();

            living.setDeltaMovement(vec3.multiply((double)0.99F, (double)0.98F, (double)0.99F));
            living.move(MoverType.SELF, living.getDeltaMovement());

            if (living.horizontalCollision && !living.level().isClientSide) {
                double d11 = living.getDeltaMovement().horizontalDistance();
                double d7 = d3 - d11;
                float f1 = (float)(d7 * (double)10.0F - (double)1.0F);
                if (f1 > 0.0F) {
                    living.playSound(SoundEvents.GENERIC_BIG_FALL, 1.3F, 1.0F);
                    living.invulnerableTime = 0;
                    living.hurt(living.damageSources().flyIntoWall(), f1 + 2);
                }
            }
        }

    }

}
