package adamsmods.adamsarsplus.common.items;

import adamsmods.adamsarsplus.common.components.ModDataComponents;
import com.hollingsworth.arsnouveau.api.item.ArsNouveauCurio;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.mana.IManaDiscountEquipment;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.config.Config;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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

public class EnchantersStopwatch extends ArsNouveauCurio implements ICasterTool, ISpellModifierItem, IManaDiscountEquipment {
    public EnchantersStopwatch(Properties properties) {
        super(properties);
    }

    public EnchantersStopwatch() {
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand handIn) {
        if (worldIn.isClientSide) {
            return super.use(worldIn, player, handIn);
        } else {
            ItemStack stack = player.getItemInHand(handIn);

            if (handIn == InteractionHand.MAIN_HAND) {
                if (player.isShiftKeyDown()) {
                    this.setWatchTime(player, stack, -1);

                } else {
                    this.setWatchTime(player, stack, 1);

                }
                return InteractionResultHolder.consume(stack);
            }
            if (handIn == InteractionHand.OFF_HAND) {
                if (player.isShiftKeyDown()) {
                    this.setWatchTime(player, stack, -10);

                } else {
                    this.setWatchTime(player, stack, 10);

                }
                return InteractionResultHolder.consume(stack);
            }

            return InteractionResultHolder.success(stack);
        }
    }

    public void setWatchTime(Player playerEntity, ItemStack stack, int increment) {

        int curTime = (stack.has(ModDataComponents.CONFIG_INTERVAL)) ? stack.get(ModDataComponents.CONFIG_INTERVAL) : 0;
        int newTime = Math.max(0, curTime + increment);

        stack.set(ModDataComponents.CONFIG_INTERVAL, newTime);

        String message = "Cast Time: " + Integer.toString(newTime);

        if (newTime > 0) {
            PortUtil.sendMessage(playerEntity, Component.literal(message));
        } else {
            PortUtil.sendMessage(playerEntity, Component.translatable("ars_nouveau.off"));
        }

    }

    public void curioTick(SlotContext slotContext, ItemStack stack) {
        int data = (stack.has(ModDataComponents.CONFIG_INTERVAL)) ? stack.get(ModDataComponents.CONFIG_INTERVAL) : 0;
        int time = data * 20;

        LivingEntity wearer = slotContext.entity();
        var caster = this.getSpellCaster(stack);
        if (wearer != null && time > 0) {
            Level var6 = slotContext.entity().level();
            if (var6 instanceof ServerLevel) {
                ServerLevel world = (ServerLevel)var6;
                if (world.getGameTime() % time == 0L) {
                    caster.castSpell(var6, wearer, InteractionHand.MAIN_HAND, Component.translatable("adamsarsplus.watch.invalid"), caster.getSpell());
                }
            }
        }
    }

    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    public int getManaDiscount(ItemStack i, Spell spell) {
        return (int)((double)spell.getCost() * (double)-0.2F);
    }

    public void sendInvalidMessage(Player player) {
        PortUtil.sendMessageNoSpam(player, Component.translatable("adamsarsplus.watch.invalid"));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip2, @NotNull TooltipFlag flagIn) {
        if (Screen.hasShiftDown() || !Config.GLYPH_TOOLTIPS.get())
            getInformation(stack, context, tooltip2, flagIn);

        int data = (stack.has(ModDataComponents.CONFIG_INTERVAL)) ? stack.get(ModDataComponents.CONFIG_INTERVAL) : 0;
        if (data > 0) {
            String message = "Cast Time: " + Integer.toString(data);
            tooltip2.add(Component.literal(message));
        } else {
            tooltip2.add(Component.translatable("ars_nouveau.off"));
        }

        super.appendHoverText(stack, context, tooltip2, flagIn);
    }

    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        return builder;
    }

}
