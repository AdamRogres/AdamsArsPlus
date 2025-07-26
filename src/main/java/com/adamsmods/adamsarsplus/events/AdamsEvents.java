package com.adamsmods.adamsarsplus.events;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.adamsarsplus.command.TSrankCommand;
import com.adamsmods.adamsarsplus.glyphs.effect_glyph.SpellEfficiency;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.event.*;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.PlayerCaster;
import com.hollingsworth.arsnouveau.api.util.ManaUtil;
import com.hollingsworth.arsnouveau.common.block.tile.GhostWeaveTile;
import com.hollingsworth.arsnouveau.common.block.tile.SpellSensorTile;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectInvisibility;
import com.hollingsworth.arsnouveau.setup.config.ServerConfig;
import com.hollingsworth.arsnouveau.setup.registry.EnchantmentRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

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

}
