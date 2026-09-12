package adamsmods.adamsarsplus.common.items;

import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.AbstractCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellCaster;
import com.hollingsworth.arsnouveau.client.gui.SpellTooltip;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import com.hollingsworth.arsnouveau.common.items.ModItem;
import com.hollingsworth.arsnouveau.setup.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static adamsmods.adamsarsplus.registry.ModItems.MAGE_CLOTH;

public class MageTome extends ModItem implements ICasterTool {
    private final Tier tier;
    static public Tier tomeTier = new Tier() {
        @Override
        public int getUses() {
            return 10;
        }

        @Override
        public float getSpeed() {
            return 0;
        }

        @Override
        public float getAttackDamageBonus() {
            return 0;
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return null;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemLike[]{MAGE_CLOTH.get()});
        }
    };

    public MageTome(Tier pTier, Properties pProperties) {
        super(pProperties.durability(pTier.getUses()).component(DataComponentRegistry.SPELL_CASTER, new SpellCaster()));
        this.tier = pTier;
    }

    @Override
    public SpellCaster getSpellCaster(ItemStack stack) {
        return stack.getOrDefault(DataComponentRegistry.SPELL_CASTER, new SpellCaster());
    }

    public boolean onScribe(Level world, BlockPos pos, Player player, InteractionHand handIn, ItemStack tableStack) {
        return player.isCreative() && ICasterTool.super.onScribe(world, pos, player, handIn, tableStack);

    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);

        AbstractCaster<?> caster = getSpellCaster(stack);
        Spell spell = caster.getSpell();
        if (spell.isEmpty()) return InteractionResultHolder.pass(stack);
        var result = caster.castSpell(worldIn, playerIn, handIn, Component.empty(), spell);
        if (!worldIn.isClientSide() && result.getResult().consumesAction()) {
            stack.hurtAndBreak(1, playerIn, handIn == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        }
        return result;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip2, @NotNull TooltipFlag flagIn) {
        AbstractCaster<?> caster = getSpellCaster(stack);

        if (caster != null) {

            // If the caster is hidden, show the hidden recipe

            if (caster.isSpellHidden()) {
                tooltip2.add(Component.literal(caster.getHiddenRecipe()).withStyle(Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath("minecraft", "alt")).withColor(ChatFormatting.GOLD)));
            } else if (Screen.hasShiftDown() || !Config.GLYPH_TOOLTIPS.get()) {
                getInformation(stack, context, tooltip2, flagIn);
            }

            if (!Screen.hasShiftDown() && !caster.getFlavorText().isEmpty())
                tooltip2.add(Component.literal(caster.getFlavorText()).withStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.BLUE)));

            tooltip2.add(Component.translatable("tooltip.ars_nouveau.caster_tome"));

        }
        super.appendHoverText(stack, context, tooltip2, flagIn);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        var caster = getSpellCaster(stack);
        if (!Screen.hasShiftDown() && Config.GLYPH_TOOLTIPS.get()
                && !caster.isSpellHidden() && !caster.getSpell().isEmpty()) {
            return Optional.of(new SpellTooltip(caster));
        }
        return Optional.empty();
    }

    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return this.tier.getRepairIngredient().test(pRepair) || super.isValidRepairItem(pToRepair, pRepair);
    }
}
