package com.adamsmods.adamsarsplus.item;

import com.hollingsworth.arsnouveau.api.item.ArsNouveauCurio;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.mana.IManaDiscountEquipment;
import com.hollingsworth.arsnouveau.api.nbt.ItemstackData;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import software.bernie.geckolib.core.animation.AnimatableManager;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EnchantersStopwatch extends ArsNouveauCurio implements ICasterTool, ISpellModifierItem, IManaDiscountEquipment {
    public EnchantersStopwatch(Item.Properties properties) {
        super(properties);
    }

    public EnchantersStopwatch() {
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
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
        EnchantersStopwatch.StopWatchData stopWatchData = new EnchantersStopwatch.StopWatchData(stack);
        stopWatchData.setTime(Math.max(0, stopWatchData.getTime() + increment));

        String message = "Cast Time: " + Integer.toString(stopWatchData.getTime());

        if (stopWatchData.getTime() > 0) {
            PortUtil.sendMessage(playerEntity, Component.literal(message));
        } else {
            PortUtil.sendMessage(playerEntity, Component.translatable("ars_nouveau.off"));
        }

    }

    public void curioTick(SlotContext slotContext, ItemStack stack) {
        EnchantersStopwatch.StopWatchData data = new EnchantersStopwatch.StopWatchData(stack);
        int time = data.getTime() * 20;

        LivingEntity wearer = slotContext.entity();
        ISpellCaster caster = this.getSpellCaster(stack);
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

    public boolean setSpell(ISpellCaster caster, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
        ArrayList<AbstractSpellPart> recipe = new ArrayList();
        recipe.addAll(spell.recipe);
        spell.recipe = recipe;
        return ICasterTool.super.setSpell(caster, player, hand, stack, spell);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip2, TooltipFlag flagIn) {
        this.getInformation(stack, worldIn, tooltip2, flagIn);
        if (stack.hasTag()) {
            EnchantersStopwatch.StopWatchData data = new EnchantersStopwatch.StopWatchData(stack);
            if (data.getTime() > 0) {
                String message = "Cast Time: " + Integer.toString(data.getTime());
                tooltip2.add(Component.literal(message));
            } else {
                tooltip2.add(Component.translatable("ars_nouveau.off"));
            }
        }
        super.appendHoverText(stack, worldIn, tooltip2, flagIn);
    }

    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        return builder;
    }

    public static class StopWatchData extends ItemstackData {
        @Nullable
        private int castTime;

        public StopWatchData(ItemStack stack) {
            super(stack);
            CompoundTag tag1 = this.getItemTag(stack);
            if (tag1 != null && !tag1.isEmpty()) {
                this.castTime = tag1.getInt("castTime");
            }
        }

        public void writeToNBT(CompoundTag tag) {
            tag.putInt("castTime", this.castTime);
        }

        public String getTagString() {
            return "adamsarsplus_stopwatch";
        }

        @Nullable
        public int getTime() {
            return this.castTime;
        }

        public void setTime(@Nullable int time) {
            this.castTime = time;
            this.writeItem();
        }

        public void copyFrom(EnchantersStopwatch.StopWatchData stopWatchDataData) {
            this.castTime = stopWatchDataData.castTime;
            this.writeItem();
        }
    }

}
