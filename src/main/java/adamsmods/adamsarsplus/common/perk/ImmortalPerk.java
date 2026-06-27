package adamsmods.adamsarsplus.common.perk;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.util.APerkSlot;
import com.hollingsworth.arsnouveau.api.perk.ITickablePerk;
import com.hollingsworth.arsnouveau.api.perk.Perk;
import com.hollingsworth.arsnouveau.api.perk.PerkInstance;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static adamsmods.adamsarsplus.registry.ModPotions.MANA_HEALTH_EFFECT;


public class ImmortalPerk extends Perk implements ITickablePerk {
    public ImmortalPerk(ResourceLocation key) { super(key); }
    public static final ImmortalPerk INSTANCE = new ImmortalPerk(AdamsArsPlus.prefix("thread_immortal"));

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
    public void tick(ItemStack itemStack, Level level, LivingEntity player, PerkInstance perkInstance) {
        MobEffectInstance effectInstance = (player).getEffect(MANA_HEALTH_EFFECT);

        int amp = Math.min(effectInstance != null ? effectInstance.getAmplifier() : -1,(int)Math.floor((player.getAbsorptionAmount() / 2)));

        if (player.level().getGameTime() % 40L == 0L) {

            if (CapabilityRegistry.getMana(player).getCurrentMana() > (double)250.0F) {
                if(amp != 10) {
                    CapabilityRegistry.getMana(player).removeMana((double) 250.0F);
                }
                (player).removeEffect(MANA_HEALTH_EFFECT);
                (player).addEffect(new MobEffectInstance(MANA_HEALTH_EFFECT, 300, Math.min(10, amp + 1), false, false));
            }
        }
    }
}