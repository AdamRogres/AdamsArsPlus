package com.adamsmods.adamsarsplus.mixin;

import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBlink;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.SOUL_RIME_EFFECT;

@Mixin(EffectBlink.class)
public class MixinEffectBlink {
    @Inject(at = @At(value = "HEAD"), cancellable = true, method = "onResolveEntity", remap = false)
    private void onResolveEntity0(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver, CallbackInfo ci){
        if (rayTraceResult.getEntity() instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) rayTraceResult.getEntity();
            if (living.hasEffect(SOUL_RIME_EFFECT.get())) {
                ci.cancel();
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), cancellable = true, method = "onResolveBlock", remap = false)
    private void onResolveBlock0(BlockHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver, CallbackInfo ci){
        if(shooter.hasEffect(SOUL_RIME_EFFECT.get())){
            ci.cancel();
        }
    }
}
