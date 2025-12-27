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

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.CLOUD_STEPS_EFFECT;
import static com.hollingsworth.arsnouveau.setup.registry.ModPotions.FLIGHT_EFFECT;

public class CloudStepsPerk extends Perk implements ITickablePerk {
    public CloudStepsPerk(ResourceLocation key) {
        super(key);
    }

    public static final CloudStepsPerk INSTANCE = new CloudStepsPerk(new ResourceLocation(AdamsArsPlus.MOD_ID, "thread_cloudsteps"));

    public String getLangDescription() {
        return "Grants the user flight.";
    }

    public PerkSlot minimumSlot() {
        return APerkSlot.FIVE;
    }

    public String getLangName() {
        return "Cloud Steps";
    }

    public String getName() {
        return Component.translatable("item.adamsarsplus.thread_cloudsteps").getString();
    }

    @Override
    public void tick(ItemStack itemStack, Level level, Player player, PerkInstance perkInstance) {
        if (!player.hasEffect(FLIGHT_EFFECT.get())) {
            player.addEffect(new MobEffectInstance(FLIGHT_EFFECT.get(), 9999999, 0, false, false));
        }
        player.addEffect(new MobEffectInstance(CLOUD_STEPS_EFFECT.get(), 40, 0, false, false, false));
    }
}