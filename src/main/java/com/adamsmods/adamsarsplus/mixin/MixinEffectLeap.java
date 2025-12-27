package com.adamsmods.adamsarsplus.mixin;

import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLeap;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.LEAP_FATIGUE_EFFECT;
import static com.adamsmods.adamsarsplus.Config.Common.DO_LEAP_FATIGUE;

@Mixin(EffectLeap.class)
public class MixinEffectLeap {
    @Inject(at = @At(value = "HEAD"), cancellable = true, method = "onResolveEntity", remap = false)
    private void onResolveEntity0(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver, CallbackInfo ci){
        if(DO_LEAP_FATIGUE.get() && !(shooter instanceof Player player && player.isCreative())){
            if(shooter.hasEffect(LEAP_FATIGUE_EFFECT.get())){
                ci.cancel();
            }  else {
                shooter.addEffect(new MobEffectInstance(LEAP_FATIGUE_EFFECT.get(), 160, 0, false, false, true));
            }
        }
    }
}
