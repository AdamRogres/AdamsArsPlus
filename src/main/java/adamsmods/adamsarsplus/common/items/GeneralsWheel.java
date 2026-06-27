package adamsmods.adamsarsplus.common.items;

import adamsmods.adamsarsplus.common.components.ModDataComponents;
import com.hollingsworth.arsnouveau.api.item.ArsNouveauCurio;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.setup.config.Config;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static adamsmods.adamsarsplus.common.capability.TSrankCap.getTsTier;
import static com.hollingsworth.arsnouveau.setup.registry.ModPotions.SUMMONING_SICKNESS_EFFECT;

public class GeneralsWheel extends ArsNouveauCurio {
    public GeneralsWheel(Properties properties) {
        super(properties);
    }

    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if(wearer instanceof Player player && player.hasEffect(SUMMONING_SICKNESS_EFFECT)){
            Level var6 = slotContext.entity().level();
            if (var6 instanceof ServerLevel) {
                ServerLevel world = (ServerLevel)var6;
                if (world.getGameTime() % 20L == 0L) {
                    int time = player.getEffect(SUMMONING_SICKNESS_EFFECT).getDuration();
                    switch (tenShadowsRank(player)){
                        case 4 -> {
                            player.removeEffect(SUMMONING_SICKNESS_EFFECT);
                            player.addEffect(new MobEffectInstance(SUMMONING_SICKNESS_EFFECT, Math.max(0, time - 20)));
                        }
                        case 3 -> {
                            player.removeEffect(SUMMONING_SICKNESS_EFFECT);
                            player.addEffect(new MobEffectInstance(SUMMONING_SICKNESS_EFFECT, Math.max(0, time - 16)));
                        }
                        case 2 -> {
                            player.removeEffect(SUMMONING_SICKNESS_EFFECT);
                            player.addEffect(new MobEffectInstance(SUMMONING_SICKNESS_EFFECT, Math.max(0, time - 12)));
                        }
                        case 1 -> {
                            player.removeEffect(SUMMONING_SICKNESS_EFFECT);
                            player.addEffect(new MobEffectInstance(SUMMONING_SICKNESS_EFFECT, Math.max(0, time - 8)));
                        }
                        default -> {
                            player.removeEffect(SUMMONING_SICKNESS_EFFECT);
                            player.addEffect(new MobEffectInstance(SUMMONING_SICKNESS_EFFECT, Math.max(0, time - 4)));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip2, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip2, flagIn);
    }

    public int tenShadowsRank(Player player){
        AtomicInteger Rank = new AtomicInteger();

        Rank.set(getTsTier(player).tsTier);

        return Rank.get();
    }

    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        return builder;
    }

}
