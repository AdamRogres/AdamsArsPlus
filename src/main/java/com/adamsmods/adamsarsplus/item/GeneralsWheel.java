package com.adamsmods.adamsarsplus.item;

import com.adamsmods.adamsarsplus.registry.AdamCapabilityRegistry;
import com.hollingsworth.arsnouveau.api.item.ArsNouveauCurio;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.mana.IManaDiscountEquipment;
import com.hollingsworth.arsnouveau.api.nbt.ItemstackData;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.items.curios.AbstractManaCurio;
import com.hollingsworth.arsnouveau.common.potions.SummoningSicknessEffect;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.core.animation.AnimatableManager;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.Attributes;

public class GeneralsWheel extends ArsNouveauCurio {
    public GeneralsWheel(Item.Properties properties) {
        super(properties);
    }

    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if(wearer instanceof Player player && player.hasEffect((MobEffect) ModPotions.SUMMONING_SICKNESS_EFFECT.get())){
            Level var6 = slotContext.entity().level();
            if (var6 instanceof ServerLevel) {
                ServerLevel world = (ServerLevel)var6;
                if (world.getGameTime() % 20L == 0L) {
                    int time = player.getEffect(ModPotions.SUMMONING_SICKNESS_EFFECT.get()).getDuration();
                    switch (tenShadowsRank(player)){
                        case 4 -> {
                            player.removeEffect(ModPotions.SUMMONING_SICKNESS_EFFECT.get());
                            player.addEffect(new MobEffectInstance((MobEffect)ModPotions.SUMMONING_SICKNESS_EFFECT.get(), Math.max(0, time - 20)));
                        }
                        case 3 -> {
                            player.removeEffect(ModPotions.SUMMONING_SICKNESS_EFFECT.get());
                            player.addEffect(new MobEffectInstance((MobEffect)ModPotions.SUMMONING_SICKNESS_EFFECT.get(), Math.max(0, time - 16)));
                        }
                        case 2 -> {
                            player.removeEffect(ModPotions.SUMMONING_SICKNESS_EFFECT.get());
                            player.addEffect(new MobEffectInstance((MobEffect)ModPotions.SUMMONING_SICKNESS_EFFECT.get(), Math.max(0, time - 12)));
                        }
                        case 1 -> {
                            player.removeEffect(ModPotions.SUMMONING_SICKNESS_EFFECT.get());
                            player.addEffect(new MobEffectInstance((MobEffect)ModPotions.SUMMONING_SICKNESS_EFFECT.get(), Math.max(0, time - 8)));
                        }
                        default -> {
                            player.removeEffect(ModPotions.SUMMONING_SICKNESS_EFFECT.get());
                            player.addEffect(new MobEffectInstance((MobEffect)ModPotions.SUMMONING_SICKNESS_EFFECT.get(), Math.max(0, time - 4)));
                        }
                    }
                }
            }
        }
    }

    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip2, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip2, flagIn);
    }

    public int tenShadowsRank(Player entity){
        AtomicInteger Rank = new AtomicInteger();

        AdamCapabilityRegistry.getTsTier(entity).ifPresent((pRank) -> {
            Rank.set(pRank.getTsTier());
        });

        return Rank.get();
    }

    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        return builder;
    }

}
