package adamsmods.adamsarsplus.common.perk;

import adamsmods.adamsarsplus.AdamsArsPlus;
import adamsmods.adamsarsplus.util.APerkSlot;
import com.hollingsworth.arsnouveau.api.perk.*;
import com.hollingsworth.arsnouveau.api.util.PerkUtil;
import com.hollingsworth.arsnouveau.common.items.data.ArmorPerkHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static adamsmods.adamsarsplus.registry.ModPotions.SIX_EYES_EFFECT;
import static com.hollingsworth.arsnouveau.setup.registry.ModPotions.MAGIC_FIND_EFFECT;
import static net.minecraft.world.effect.MobEffects.NIGHT_VISION;

public class SixeyesPerk extends Perk implements ITickablePerk {
    public SixeyesPerk(ResourceLocation key) { super(key); }
    public static final SixeyesPerk INSTANCE = new SixeyesPerk(AdamsArsPlus.prefix("thread_sixeyes"));

    public String getLangDescription() {
        return "Grants the user the Six Eyes. Granting superior vision and legendary mana efficiency.";
    }

    public PerkSlot minimumSlot() {
        return APerkSlot.SIX;
    }

    public String getLangName() {
        return "Six Eyes";
    }

    public String getName() {
        return Component.translatable("item.adamsarsplus.thread_sixeyes").getString();
    }

    @Override
    public void tick(ItemStack itemStack, Level level, LivingEntity player, PerkInstance perkInstance) {
        ArmorPerkHolder perkHolder = PerkUtil.getPerkHolder(player.getItemBySlot(EquipmentSlot.HEAD));

        if(perkHolder != null && perkHolder.getPerkInstances(itemStack).contains(perkInstance)){
            if(!player.hasEffect(SIX_EYES_EFFECT)){
                player.addEffect(new MobEffectInstance(SIX_EYES_EFFECT, 100, 0, false, false));
                player.addEffect(new MobEffectInstance(NIGHT_VISION, 600, 0, false, false));
                player.addEffect(new MobEffectInstance(MAGIC_FIND_EFFECT, 300, 0, false, false));
            }
        } else {
            if(!player.hasEffect(SIX_EYES_EFFECT)){
                player.addEffect(new MobEffectInstance(SIX_EYES_EFFECT, 100, 0, false, false));
            }
        }
    }
}
