package adamsmods.adamsarsplus.event;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.common.command.TSrankCommand;
import adamsmods.adamsarsplus.common.glyphs.effect_glyph.SpellEfficiency;
import com.hollingsworth.arsnouveau.api.event.ManaRegenCalcEvent;
import com.hollingsworth.arsnouveau.api.event.MaxManaCalcEvent;
import com.hollingsworth.arsnouveau.api.event.SpellCostCalcEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import static adamsmods.adamsarsplus.registry.ModPotions.*;

@EventBusSubscriber(modid = AdamsArsPlus.MODID)
public class AdamsEvents {

    @SubscribeEvent
    public static void newCostCalc(SpellCostCalcEvent e) {
        double R = 1;
        double newCost = e.currentCost;
        int X = e.context.getSpell().getInstanceCount(SpellEfficiency.INSTANCE);
        if(e.context.getUnwrappedCaster().hasEffect(SIX_EYES_EFFECT)){
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
       if(!(e.getEntity().getEffect(MANA_EXHAUST_EFFECT) == null)){
        e.setRegen(0);
       }
    }

    @SubscribeEvent
    public static void commandRegister(RegisterCommandsEvent event) {
        TSrankCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void newMaxManaCalc(MaxManaCalcEvent event) {
        if(!(event.getEntity().getEffect(EARTHEN_HEART_EFFECT) == null)){
            event.setMax(event.getMax() / 2);
        }
    }

    @SubscribeEvent
    public static void weaponAttackDisruption(AttackEntityEvent event){
        if(event.getEntity().hasEffect(ABYSSAL_DOMINATION_EFFECT)){
            if(event.getTarget() instanceof LivingEntity living){
                living.addEffect(new MobEffectInstance(DISRUPTION_EFFECT, 100, 0, true, true));
            }
        }
    }

}
