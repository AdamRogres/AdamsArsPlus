package com.adamsmods.adamsarsplus.perk;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.adamsmods.api.APerkSlot;
import com.hollingsworth.arsnouveau.api.perk.ITickablePerk;
import com.hollingsworth.arsnouveau.api.perk.Perk;
import com.hollingsworth.arsnouveau.api.perk.PerkInstance;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.MANA_EXHAUST_EFFECT;
import static com.adamsmods.adamsarsplus.ArsNouveauRegistry.MANA_HEALTH_EFFECT;
import static com.adamsmods.adamsarsplus.Config.Common.DISCOUNT_BACKLASH;

public class ImmortalPerk extends Perk implements ITickablePerk {
    public ImmortalPerk(ResourceLocation key) { super(key); }
    public static final ImmortalPerk INSTANCE = new ImmortalPerk(new ResourceLocation(AdamsArsPlus.MOD_ID, "thread_immortal"));

    public String getLangDescription() {
        return "Grants the user a set of regenerating absorption hearts that draws from your mana.";
    }

    public PerkSlot minimumSlot() {
        return APerkSlot.FIVE;
    }

    public String getLangName() {
        return "Immortal";
    }

    public String getName() {
        return Component.translatable("item.adamsarsplus.thread_immortal").getString();
    }

    @Override
    public void tick(ItemStack itemStack, Level level, Player player, PerkInstance perkInstance) {
        MobEffectInstance effectInstance = ((LivingEntity)player).getEffect((MobEffect) MANA_HEALTH_EFFECT.get());

        int amp = Math.min(effectInstance != null ? effectInstance.getAmplifier() : -1,(int)Math.floor((player.getAbsorptionAmount() / 2)));

        if (player.level().getGameTime() % 40L == 0L) {

            CapabilityRegistry.getMana(player).ifPresent((mana) -> {
                if (mana.getCurrentMana() > (double)250.0F) {
                    if(amp != 10) {
                        mana.removeMana((double) 250.0F);
                    }
                    ((LivingEntity) player).removeEffect((MobEffect) MANA_HEALTH_EFFECT.get());
                    ((LivingEntity) player).addEffect(new MobEffectInstance((MobEffect) MANA_HEALTH_EFFECT.get(), 300, Math.min(10, amp + 1), false, false));
                }
            });
        }
    }
}