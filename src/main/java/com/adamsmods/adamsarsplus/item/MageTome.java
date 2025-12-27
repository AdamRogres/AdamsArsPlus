package com.adamsmods.adamsarsplus.item;

import com.adamsmods.adamsarsplus.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.common.items.ModItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

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
        public int getLevel() {
            return 0;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemLike[]{ModRegistry.MAGE_CLOTH.get()});
        }
    };

    public MageTome(Tier pTier, Item.Properties pProperties) {
        super(pProperties.defaultDurability(pTier.getUses()));
        this.tier = pTier;
    }

    public boolean onScribe(Level world, BlockPos pos, Player player, InteractionHand handIn, ItemStack tableStack) {
        return player.isCreative() && ICasterTool.super.onScribe(world, pos, player, handIn, tableStack);

    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);

        stack.hurtAndBreak(1, playerIn, (p_43296_) -> p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND));

        ISpellCaster caster = this.getSpellCaster(stack);
        Spell spell = caster.getSpell();
        return caster.castSpell(worldIn, playerIn, handIn, Component.empty(), spell);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip2, TooltipFlag flagIn) {
        if (worldIn != null) {
            ISpellCaster caster = this.getSpellCaster(stack);
            Spell spell = caster.getSpell();
            this.getInformation(stack, worldIn, tooltip2, flagIn);
            tooltip2.add(Component.translatable("tooltip.ars_nouveau.caster_tome"));
            super.appendHoverText(stack, worldIn, tooltip2, flagIn);
        }
    }

    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return this.tier.getRepairIngredient().test(pRepair) || super.isValidRepairItem(pToRepair, pRepair);
    }
}