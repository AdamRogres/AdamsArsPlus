package com.adamsmods.adamsarsplus.events;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.command.TSrankCommand;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.SpellEfficiency;
import com.hollingsworth.arsnouveau.api.event.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.*;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class AdamsEvents {

    @SubscribeEvent
    public static void newCostCalc(SpellCostCalcEvent e) {
        double R = 1;
        double newCost = e.currentCost;
        int X = e.context.getSpell().getInstanceCount(SpellEfficiency.INSTANCE);
        if(e.context.getUnwrappedCaster().hasEffect(SIX_EYES_EFFECT.get())){
            X += 2;
        }

        while(X > 0){
            R = R * 0.7;
            X--;
        }
        e.currentCost = (int)(R * newCost);
    }

    @SubscribeEvent
    public static void newRegenCalc(ManaRegenCalcEvent e) {
       if(!(e.getEntity().getEffect(MANA_EXHAUST_EFFECT.get()) == null)){
        e.setRegen(0);
       }
    }

    @SubscribeEvent
    public static void commandRegister(RegisterCommandsEvent event) {
        TSrankCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void newMaxManaCalc(MaxManaCalcEvent event) {
        if(!(event.getEntity().getEffect(EARTHEN_HEART_EFFECT.get()) == null)){
            event.setMax(event.getMax() / 2);
        }
    }

    @SubscribeEvent
    public static void weaponAttackDisruption(AttackEntityEvent event){
        if(event.getEntity().hasEffect(ABYSSAL_DOMINATION_EFFECT.get())){
            if(event.getTarget() instanceof LivingEntity living){
                living.addEffect(new MobEffectInstance(DISRUPTION_EFFECT.get(), 100, 0, true, true));
            }
        }
    }

}
