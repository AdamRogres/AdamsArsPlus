package com.adamsmods.adamsarsplus.perk;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.api.APerkSlot;
import com.hollingsworth.arsnouveau.api.perk.ITickablePerk;
import com.hollingsworth.arsnouveau.api.perk.Perk;
import com.hollingsworth.arsnouveau.api.perk.PerkInstance;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.hollingsworth.arsnouveau.setup.registry.ModPotions.MANA_REGEN_EFFECT;

public class AdrenalinePerk extends Perk implements ITickablePerk {
    public AdrenalinePerk(ResourceLocation key) { super(key); }
    public static final AdrenalinePerk INSTANCE = new AdrenalinePerk(new ResourceLocation(AdamsArsPlus.MOD_ID, "thread_adrenaline"));

    public String getLangDescription() {
        return "Grants a boost of mana regeneration after taking damage. This boost scales based on the amount of health missing.";
    }

    public PerkSlot minimumSlot() {
        return APerkSlot.FOUR;
    }

    public String getLangName() {
        return "Mana Adrenaline";
    }

    public String getName() {
        return Component.translatable("item.adamsarsplus.thread_adrenaline").getString();
    }

    @Override
    public void tick(ItemStack itemStack, Level level, Player player, PerkInstance perkInstance) {
        if(player.hurtTime > 0){
            player.addEffect(new MobEffectInstance(MANA_REGEN_EFFECT.get(), (perkInstance.getSlot().value - 3) * 150, Math.min((int)(player.getMaxHealth()/(Math.max(player.getHealth(),1))), 10), true, false));
        }
    }
}

