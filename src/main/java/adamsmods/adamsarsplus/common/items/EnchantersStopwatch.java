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
import com.hollingsworth.arsnouveau.api.spell.SpellCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.PlayerCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.client.gui.SpellTooltip;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.config.Config;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class EnchantersStopwatch extends ArsNouveauCurio implements ICasterTool, ISpellModifierItem, IManaDiscountEquipment {
    public EnchantersStopwatch(Properties properties) {
        super(properties.component(DataComponentRegistry.SPELL_CASTER, new SpellCaster())
                .component(ModDataComponents.CONFIG_INTERVAL.get(), 0));
    }

    public EnchantersStopwatch() {
        this(new Properties().stacksTo(1));
    }

    @Override
    public SpellCaster getSpellCaster(ItemStack stack) {
        return stack.getOrDefault(DataComponentRegistry.SPELL_CASTER, new SpellCaster());
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
        int newTime = (int) Math.clamp((long) curTime + increment, 0L, Integer.MAX_VALUE);

        stack.set(ModDataComponents.CONFIG_INTERVAL, newTime);

        String message = "Cast Time: " + Integer.toString(newTime);

        if (newTime > 0) {
            PortUtil.sendMessage(playerEntity, Component.literal(message));
        } else {
            PortUtil.sendMessage(playerEntity, Component.translatable("ars_nouveau.off"));
        }

    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (slotContext.cosmetic() || !(wearer.level() instanceof ServerLevel world)) return;
        long interval = (long) stack.getOrDefault(ModDataComponents.CONFIG_INTERVAL.get(), 0) * 20L;
        if (interval <= 0 || world.getGameTime() % interval != 0) return;
        var caster = getSpellCaster(stack);
        Spell spell = caster.getSpell();
        if (spell.isEmpty() || spell.getCastMethod() == null) return;

        // The caster tool is the equipped curio, not whatever happens to be in the wearer's hand.
        var wrapped = wearer instanceof Player player ? new PlayerCaster(player) : new LivingCaster(wearer);
        var context = new SpellContext(world, spell, wearer, wrapped, stack);
        var resolver = caster.getSpellResolver(context, world, wearer, InteractionHand.MAIN_HAND);
        double reach = wearer instanceof Player player
                ? player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) + 0.5 : 4.5;
        HitResult hit = SpellUtil.rayTrace(wearer, reach, 0, false);
        if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity) {
            resolver.onCastOnEntity(stack, entityHit.getEntity(), InteractionHand.MAIN_HAND);
        } else if (hit instanceof BlockHitResult blockHit && hit.getType() == HitResult.Type.BLOCK) {
            resolver.onCastOnBlock(blockHit);
        } else {
            resolver.onCast(stack, world);
        }
    }

    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    public int getManaDiscount(ItemStack i, Spell spell) {
        return (int)((double)spell.getCost() * (double)-0.2F);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        var caster = getSpellCaster(stack);
        return !Screen.hasShiftDown() && Config.GLYPH_TOOLTIPS.get()
                && !caster.isSpellHidden() && !caster.getSpell().isEmpty()
                ? Optional.of(new SpellTooltip(caster)) : Optional.empty();
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
