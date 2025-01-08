package com.adamsmods.adamsarsplus.entities.effects;

import com.adamsmods.adamsarsplus.AdamsArsPlus;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = AdamsArsPlus.MOD_ID)
public class ManaExhaustEffect extends MobEffect {

    public ManaExhaustEffect() {
        super(MobEffectCategory.HARMFUL, 2039587);
        //this.addAttributeModifier(PerkAttributes.MANA_REGEN_BONUS.get(), "0dee8a21-f182-42c8-8361-1ad6126cac39", -2000.0, AttributeModifier.Operation.ADDITION);
    }


    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
