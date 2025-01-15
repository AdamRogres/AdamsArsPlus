package com.adamsmods.adamsarsplus.perk;

import com.adamsmods.api.APerkSlot;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.perk.Perk;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

import static com.adamsmods.adamsarsplus.AdamsArsPlus.MOD_ID;

public class InvinciblePerk extends Perk {
    public static final InvinciblePerk INSTANCE = new InvinciblePerk(new ResourceLocation(MOD_ID, "thread_invincible"));

    public InvinciblePerk(ResourceLocation key) {
        super(key);
    }

    public static final UUID PERK_UUID = UUID.fromString("e2a7e5bc-ab34-4ea5-b3b6-ef23d352fa49");

    public Multimap<Attribute, AttributeModifier> getModifiers(EquipmentSlot pEquipmentSlot, ItemStack stack, int slotValue) {
        return this.attributeBuilder().put((Attribute) Attributes.ARMOR, new AttributeModifier(PERK_UUID, "ArmorPerk", (double)(2 * (slotValue - 3) + 2), AttributeModifier.Operation.ADDITION)).build();
    }

    public String getLangName() {
        return "Invincible";
    }

    public String getLangDescription() {
        return "Grants an increasing amount of Armor each level.";
    }

    public PerkSlot minimumSlot() {
        return APerkSlot.FOUR;
    }

    public String getName() {
        return Component.translatable("item.adamsarsplus.thread_invincible").getString();
    }

}
